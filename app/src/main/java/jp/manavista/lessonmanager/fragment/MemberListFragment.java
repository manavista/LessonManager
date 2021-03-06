/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.MemberActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleListActivity;
import jp.manavista.lessonmanager.constants.analytics.ContentType;
import jp.manavista.lessonmanager.constants.analytics.Event;
import jp.manavista.lessonmanager.constants.analytics.Param;
import jp.manavista.lessonmanager.facade.MemberListFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.MemberVo;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.MemberOperation;
import jp.manavista.lessonmanager.view.section.MemberSection;
import lombok.Getter;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE;
import static jp.manavista.lessonmanager.activity.MemberLessonScheduleListActivity.Extra.MEMBER_ID;
import static jp.manavista.lessonmanager.activity.MemberLessonScheduleListActivity.Extra.MEMBER_NAME;

/**
 *
 * Member List Fragment
 *
 * <p>
 * Overview:<br>
 * Display a categoriesList of members. <br>
 * Provide interface for editing and creating new.
 * </p>
 */
public final class MemberListFragment extends Fragment {

    /** Logger tag string */
    private static final String TAG = MemberListFragment.class.getSimpleName();

    /** Activity Contents */
    private Activity contents;

    private RecyclerView view;
    private ViewGroup emptyState;

    /** Member RecyclerView Adapter */
    @Getter
    private SectionedRecyclerViewAdapter sectionAdapter;
    /** Member Adapter Section */
    private MemberSection memberSection;
    /** Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;
    /** Member categoriesList disposable */
    private Disposable disposable;

    @Inject
    SharedPreferences preferences;
    @Inject
    MemberService memberService;
    @Inject
    MemberListFacade facade;

    private FirebaseAnalytics analytics;

    /** Constructor */
    public MemberListFragment() {
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
     * @return A new instance of fragment MemberListFragment.
     */
    public static MemberListFragment newInstance() {
        MemberListFragment fragment = new MemberListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = Disposables.empty();
        analytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
        this.contents = getActivity();

        view = contents.findViewById(R.id.rv);
        emptyState = contents.findViewById(R.id.empty_state);

        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new ItemDecoration(contents));

        sectionAdapter = new SectionedRecyclerViewAdapter();
        memberSection = MemberSection.newInstance(memberOperation);
        sectionAdapter.addSection(memberSection);
        view.setAdapter(sectionAdapter);

        final ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.setClickToRecoverAnimation(false);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {

        super.onResume();

        final List<MemberVo> list = new ArrayList<>();

        final String key = getString(R.string.key_preference_member_name_display);
        final String defaultValue = getString(R.string.value_preference_member_name_display);
        final int displayNameCode = Integer.valueOf(preferences.getString(key, defaultValue));

        disposable = memberService.getVoListAll(displayNameCode).subscribe(list::add, throwable -> Log.e(TAG, "Can not get member List all.", throwable), () -> {
            memberSection.setList(list);
            sectionAdapter.notifyDataSetChanged();

            if( list.isEmpty() ) {
                view.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    private final MemberOperation memberOperation = new MemberOperation() {

        @Override
        public void lessonList(MemberVo dto, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonScheduleListActivity.class);
            intent.putExtra(MEMBER_ID, dto.getId());
            intent.putExtra(MEMBER_NAME, dto.getDisplayName());
            contents.startActivity(intent);
        }

        @Override
        public void edit(final long id, final int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberActivity.class);
            intent.putExtra(MemberActivity.EXTRA_MEMBER_ID, id);
            contents.startActivityForResult(intent, MemberActivity.RequestCode.EDIT);
        }

        @Override
        public void delete(final long id, final int position) {

            final String key = getString(R.string.key_preference_general_delete_confirm);

            if( preferences.getBoolean(key, true) ) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(contents);
                builder.setTitle(R.string.title_member_list_dialog_delete_confirm)
                    .setIcon(R.drawable.ic_delete_black)
                    .setMessage(R.string.message_member_list_dialog_delete_confirm)
                    .setPositiveButton(android.R.string.ok, onOkListener(id, position))
                    .setNegativeButton(android.R.string.cancel, onCancelListener)
                    .show();

            } else {
                execDelete(id, position);
            }
        }

        private final DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemTouchHelper.closeOpened();
            }
        };

        private DialogInterface.OnClickListener onOkListener(final long id, final int position) {
            return (dialogInterface, i) -> {
                itemTouchHelper.closeOpened();
                execDelete(id, position);
            };
        }

        private void execDelete(final long id, final int position) {
            disposable = facade.delete(id).subscribe(rows -> {
                memberSection.getList().remove(position);
                sectionAdapter.notifyItemRemoved(position);

                if( memberSection.getList().isEmpty() ) {
                    view.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                }

                final String message = getString(R.string.message_member_list_delete_member);
                Toast.makeText(contents, message, Toast.LENGTH_SHORT).show();

                final Bundle bundle = new Bundle();
                bundle.putString(CONTENT_TYPE, ContentType.Member.label());
                bundle.putInt(Param.Rows.label(), rows);
                analytics.logEvent(Event.Delete.label(), bundle);
            }, throwable -> Log.e(TAG, "Can not delete a member!", throwable));
        }
    };
}
