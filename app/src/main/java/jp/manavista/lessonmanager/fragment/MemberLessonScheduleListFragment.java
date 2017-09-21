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
import android.widget.FrameLayout;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.MemberLessonActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleActivity;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import jp.manavista.lessonmanager.view.operation.MemberLessonScheduleOperation;
import jp.manavista.lessonmanager.view.section.MemberLessonScheduleSection;
import jp.manavista.lessonmanager.view.section.MemberLessonSection;

/**
 *
 * MemberLessonSchedule List Fragment
 *
 *
 */
public final class MemberLessonScheduleListFragment extends Fragment {

    private static final String TAG = MemberLessonScheduleListFragment.class.getSimpleName();

    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";

    private long memberId;

    /** Activity Contents */
    private Activity contents;

    private RecyclerView view;
    private FrameLayout emptyState;

    /** MemberLesson RecyclerView Adapter */
    private SectionedRecyclerViewAdapter sectionAdapter;
    /** Adapter MemberLesson Section */
    private MemberLessonSection memberLessonSection;
    /** Adapter MemberLessonSchedule Section */
    private MemberLessonScheduleSection memberLessonScheduleSection;
    /** MemberLessonSchedule RecyclerView Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;

    private List<MemberLessonVo> lessonVoList;
    private List<MemberLessonScheduleVo> scheduleVoList;

    @Inject
    MemberLessonScheduleService memberLessonScheduleService;
    @Inject
    MemberLessonScheduleListFacade facade;
    @Inject
    SharedPreferences preferences;

    /** MemberLessonScheduleList disposable */
    private Disposable disposable;

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

        lessonVoList = new ArrayList<>();
        scheduleVoList = new ArrayList<>();
        this.disposable = Disposables.empty();
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
        memberLessonSection = MemberLessonSection.newInstance(contents, memberLessonOperation);
        memberLessonSection.setTitle(getString(R.string.title_member_lesson_schedule_list_section_lesson));
        memberLessonSection.setList(lessonVoList);
        sectionAdapter.addSection(memberLessonSection);

        memberLessonScheduleSection = MemberLessonScheduleSection.newInstance(contents, memberLessonScheduleOperation);
        memberLessonScheduleSection.setTitle(getString(R.string.title_member_lesson_schedule_list_section_schedule));
        memberLessonScheduleSection.setList(scheduleVoList);
        sectionAdapter.addSection(memberLessonScheduleSection);

        view.setAdapter(sectionAdapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
        itemTouchHelper.setClickToRecoverAnimation(false);
    }

    @Override
    public void onResume() {

        super.onResume();

        final boolean containPast = preferences.getBoolean(
                getString(R.string.key_preferences_lesson_list_display_past), false);

        disposable = facade.getListData(memberId, containPast,
                memberLessonSection.getList(), memberLessonScheduleSection.getList(),
                view, emptyState, sectionAdapter);
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
            intent.putExtra(MemberLessonScheduleActivity.EXTRA_SCHEDULE_ID, id);
            contents.startActivity(intent);
        }

        @Override
        public void delete(final int adapterPosition) {

            itemTouchHelper.closeOpened();

            final int lessonItems = memberLessonSection.getContentItemsTotal();
            Log.d(TAG, "lessonItems: " + lessonItems);

            /*
             * Calculate position
             * lessonSchedule position =
             *   adapterPosition - (lessonItemCount + lessonHeader + lessonScheduleHeader)
             */
            final int position = adapterPosition - (lessonItems + 1 + 1);
            Log.d(TAG, "delete target position: " + position);
            if( position > memberLessonScheduleSection.getList().size() ) {
                throw new ArrayIndexOutOfBoundsException("invalid position in memberLessonSchedule list");
            }
            final MemberLessonScheduleVo vo = memberLessonScheduleSection.getList().get(position);
            Log.d(TAG, "delete target id: " + vo.getId());

            memberLessonScheduleService.deleteById(vo.getId()).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    memberLessonScheduleSection.getList().remove(position);
                    sectionAdapter.notifyItemRemovedFromSection(memberLessonScheduleSection, position);
                }
            });
        }
    };

    final private MemberLessonOperation memberLessonOperation = new MemberLessonOperation() {
        @Override
        public void edit(MemberLessonVo dto, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonActivity.class);
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_ID, dto.getMemberId());
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_LESSON_ID, dto.getId());
            contents.startActivityForResult(intent, MemberLessonActivity.RequestCode.EDIT);
        }

        @Override
        public void delete(long id, final int position) {
            itemTouchHelper.closeOpened();
            scheduleVoList.clear();
            disposable = facade.deleteLessonByLessonId(memberId, id).subscribe(new Consumer<MemberLessonScheduleVo>() {
                @Override
                public void accept(MemberLessonScheduleVo vo) throws Exception {
                    scheduleVoList.add(vo);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    memberLessonSection.getList().remove(position);
//                    sectionAdapter.notifyItemRemovedFromSection(memberLessonSection, position);
                    memberLessonScheduleSection.setList(scheduleVoList);
                    sectionAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void scheduleList(long id) {

        }
    };
}
