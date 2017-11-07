/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.MemberLessonActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleActivity;
import jp.manavista.lessonmanager.constants.analytics.ContentType;
import jp.manavista.lessonmanager.constants.analytics.Event;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleListCriteria;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.holder.MemberLessonHolder;
import jp.manavista.lessonmanager.view.layout.expandable.ExpandableLayout;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import jp.manavista.lessonmanager.view.operation.MemberLessonScheduleOperation;
import jp.manavista.lessonmanager.view.section.MemberLessonScheduleSection;
import jp.manavista.lessonmanager.view.section.MemberLessonSection;
import lombok.val;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE;
import static jp.manavista.lessonmanager.activity.MemberLessonActivity.Extra.MEMBER_ID;
import static jp.manavista.lessonmanager.activity.MemberLessonActivity.Extra.MEMBER_LESSON_ID;
import static jp.manavista.lessonmanager.activity.MemberLessonScheduleActivity.EXTRA_SCHEDULE_ID;

/**
 *
 * MemberLessonSchedule List Fragment
 *
 * <p>
 * Overview:<br>
 * Display a lesson and schedule List of members. <br>
 * Provide interface for editing and creating new.
 * </p>
 */
public final class MemberLessonScheduleListFragment extends Fragment {

    private static final String TAG = MemberLessonScheduleListFragment.class.getSimpleName();

    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";

    private long memberId;

    /** Activity Contents */
    private Activity contents;

    private RecyclerView view;
    private ViewGroup emptyState;

    private Set<String> displayStatusSet;

    /** MemberLesson RecyclerView Adapter */
    private SectionedRecyclerViewAdapter sectionAdapter;
    /** Adapter MemberLesson Section */
    private MemberLessonSection lessonSection;
    /** Adapter MemberLessonSchedule Section */
    private MemberLessonScheduleSection scheduleSection;
    /** MemberLessonSchedule RecyclerView Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;

    @Inject
    MemberLessonScheduleService memberLessonScheduleService;
    @Inject
    MemberLessonScheduleListFacade facade;
    @Inject
    SharedPreferences preferences;

    /** MemberLessonScheduleList disposable */
    private Disposable disposable;

    private FirebaseAnalytics analytics;

    public MemberLessonScheduleListFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:<br>
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @param memberId target MemberId
     * @return A new instance of fragment MemberLessonScheduleListFragment.
     */
    public static MemberLessonScheduleListFragment newInstance(final long memberId) {
        final MemberLessonScheduleListFragment fragment = new MemberLessonScheduleListFragment();
        final Bundle args = new Bundle();
        args.putLong(KEY_MEMBER_ID, memberId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getLong(KEY_MEMBER_ID);
        } else {
            Log.w(TAG, "bundle arguments is null");
        }

        this.disposable = Disposables.empty();
        this.analytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_lesson_schedule_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();
        DependencyInjector.appComponent().inject(this);

        view = contents.findViewById(R.id.rv);
        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new ItemDecoration(contents));

        emptyState = contents.findViewById(R.id.empty_state);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        lessonSection = MemberLessonSection.newInstance(contents, memberLessonOperation);
        lessonSection.setTitle(getString(R.string.title_member_lesson_schedule_list_section_lesson));
        sectionAdapter.addSection(lessonSection);

        scheduleSection = MemberLessonScheduleSection.newInstance(memberLessonScheduleOperation);
        scheduleSection.setTitle(getString(R.string.title_member_lesson_schedule_list_section_schedule));
        sectionAdapter.addSection(scheduleSection);

        view.setAdapter(sectionAdapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
        itemTouchHelper.setClickToRecoverAnimation(false);
    }

    @Override
    public void onResume() {

        super.onResume();

        final int pastKey = R.string.key_preferences_lesson_list_display_past;
        final boolean containPast = preferences.getBoolean(getString(pastKey), false);

        final int defaultKey = R.array.default_values_schedule_list_display_status;
        final int statusKey = R.string.key_preferences_schedule_list_display_status;
        final Set<String> defaultSet = new HashSet<>(Arrays.asList(getResources().getStringArray(defaultKey)));
        displayStatusSet = preferences.getStringSet(getString(statusKey), defaultSet);

        final val criteria = MemberLessonScheduleListCriteria.builder()
                .memberId(memberId)
                .containPastLesson(containPast)
                .scheduleStatusSet(displayStatusSet)
                .lessonSection(lessonSection)
                .scheduleSection(scheduleSection)
                .view(view)
                .emptyState(emptyState)
                .sectionAdapter(sectionAdapter)
                .build();

        disposable = facade.getListData(criteria);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    final private MemberLessonScheduleOperation memberLessonScheduleOperation = new MemberLessonScheduleOperation() {
        @Override
        public void edit(long id, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonScheduleActivity.class);
            intent.putExtra(EXTRA_SCHEDULE_ID, id);
            contents.startActivity(intent);
        }

        @Override
        public void delete(final int adapterPosition) {

            itemTouchHelper.closeOpened();

            final int lessonItems = lessonSection.getContentItemsTotal();
            Log.d(TAG, "lessonItems: " + lessonItems);

            /*
             * Calculate position
             * lessonSchedule position =
             *   adapterPosition - (lessonItemCount + lessonHeader + lessonScheduleHeader)
             */
            final int position = adapterPosition - (lessonItems + 1 + 1);
            Log.d(TAG, "delete target position: " + position);
            if( position > scheduleSection.getList().size() ) {
                throw new ArrayIndexOutOfBoundsException("invalid position in memberLessonSchedule list");
            }
            final MemberLessonScheduleVo vo = scheduleSection.getList().get(position);
            Log.d(TAG, "delete target id: " + vo.getId());

            memberLessonScheduleService.deleteById(vo.getId()).subscribe(integer -> {
                scheduleSection.getList().remove(position);
                sectionAdapter.notifyItemRemovedFromSection(scheduleSection, position);
            });
        }
    };

    final private MemberLessonOperation memberLessonOperation = new MemberLessonOperation() {
        @Override
        public void edit(MemberLessonVo dto, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonActivity.class);
            intent.putExtra(MEMBER_ID, dto.getMemberId());
            intent.putExtra(MEMBER_LESSON_ID, dto.getId());
            contents.startActivityForResult(intent, MemberLessonActivity.RequestCode.EDIT);
        }

        @Override
        public void delete(long id, final int position) {
            itemTouchHelper.closeOpened();
            scheduleSection.getList().clear();
            final List<MemberLessonScheduleVo> scheduleVoList = new ArrayList<>();

            disposable = facade.deleteLessonByLessonId(memberId, id, displayStatusSet).subscribe(scheduleVoList::add, Throwable::printStackTrace, () -> {
                lessonSection.getList().remove(position);
//                    sectionAdapter.notifyItemRemovedFromSection(lessonSection, position);
                scheduleSection.setList(scheduleVoList);
                sectionAdapter.notifyDataSetChanged();

                if( lessonSection.getList().isEmpty() ) {
                    view.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                }

                final Bundle bundle = new Bundle();
                bundle.putString(CONTENT_TYPE, ContentType.Lesson.label());
                analytics.logEvent(Event.Delete.label(), bundle);
            });
        }

        @Override
        public void close() {

            for( int i = 0, size = lessonSection.getContentItemsTotal() ; i < size ; i++ ) {

                /* Offset Section Title row, view.getChildAt( i+1 )  */
                final MemberLessonHolder viewHolder = (MemberLessonHolder) lessonSection.getItemViewHolder(view.getChildAt(i+1));
                if( viewHolder.view instanceof ExpandableLayout
                        && ((ExpandableLayout) viewHolder.view).isExpanded()) {

                    final ExpandableLayout layout = (ExpandableLayout) viewHolder.view;

                    /*
                     * TIPS TO AVOID BUGS
                     *
                     * Temporarily disable line expand/closed animation.
                     * When animation is enabled and closed,
                     * the image of the line to be opened is not displayed.
                     */
                    layout.setExpandDuration(0);
                    layout.close();
                    layout.setExpandDuration(200);
                }
            }
        }

        @Override
        public void filter(long lessonId) {

            /*
             * If another filter is enabled when the filter is operated,
             * the other selection is canceled.
             */
            for( int i = 0, size = lessonSection.getContentItemsTotal() ; i < size ; i++ ) {

                /* Offset Section Title row, view.getChildAt( i+1 )  */
                final MemberLessonHolder viewHolder = (MemberLessonHolder) lessonSection.getItemViewHolder(view.getChildAt(i+1));
                if( !viewHolder.lessonId.getText().toString().equals(String.valueOf(lessonId))
                        && viewHolder.filterImageButton.isSelected() ) {
                    viewHolder.filterImageButton.setSelected(false);
                }
            }

            scheduleSection.filterByLessonId(lessonId);
            sectionAdapter.notifyDataSetChanged();

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Schedule.label());
            analytics.logEvent(Event.Filter.label(), bundle);
        }

        @Override
        public void clearFilter() {
            scheduleSection.clearFilter();
            sectionAdapter.notifyDataSetChanged();
        }
    };
}
