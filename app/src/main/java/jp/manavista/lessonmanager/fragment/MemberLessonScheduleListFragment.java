package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

import javax.inject.Inject;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.activity.MemberLessonActivity;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberLessonScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberLessonScheduleListFragment extends Fragment {

    private static final String TAG = MemberLessonScheduleListFragment.class.getSimpleName();

    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";

    private long memberId;

    /** Activity Contents */
    private Activity contents;

    /** MemberLesson RecyclerView Adapter */
    private SectionedRecyclerViewAdapter sectionAdapter;
    /** Adapter MemberLesson Section */
    private MemberLessonSection memberLessonSection;
    /** Adapter MemberLessonSchedule Section */
    private MemberLessonScheduleSection memberLessonScheduleSection;
    /** MemberLessonSchedule RecyclerView Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;

    @Inject
    MemberLessonService memberLessonService;
    @Inject
    MemberLessonScheduleService memberLessonScheduleService;
    @Inject
    MemberLessonScheduleListFacade facade;

    /** MemberLesson disposable */
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

        final RecyclerView view = contents.findViewById(R.id.rv);
        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new ItemDecoration(contents));

        sectionAdapter = new SectionedRecyclerViewAdapter();
        memberLessonSection = MemberLessonSection.newInstance(contents, memberLessonOperation);
        memberLessonSection.setTitle("Lesson");
        sectionAdapter.addSection(memberLessonSection);

        memberLessonScheduleSection = MemberLessonScheduleSection.newInstance(contents, memberLessonScheduleOperation);
        memberLessonScheduleSection.setTitle("Schedule");
        sectionAdapter.addSection(memberLessonScheduleSection);

        view.setAdapter(sectionAdapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {

        super.onResume();

        disposable = memberLessonService.getSingleVoListByMemberId(memberId).subscribe(new Consumer<List<MemberLessonVo>>() {
            @Override
            public void accept(List<MemberLessonVo> voList) throws Exception {
                memberLessonSection.setList(voList);
                sectionAdapter.notifyDataSetChanged();

                disposable = memberLessonScheduleService.getSingleVoListByMemberId(memberId).subscribe(new Consumer<List<MemberLessonScheduleVo>>() {
                    @Override
                    public void accept(List<MemberLessonScheduleVo> memberLessonScheduleVos) throws Exception {
                        memberLessonScheduleSection.setList(memberLessonScheduleVos);
                        sectionAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    final private MemberLessonScheduleOperation memberLessonScheduleOperation = new MemberLessonScheduleOperation() {
        @Override
        public void edit(long id, int position) {

        }

        @Override
        public void delete(long id, int position) {

        }
    };

    final private MemberLessonOperation memberLessonOperation = new MemberLessonOperation() {
        @Override
        public void edit(MemberLessonVo dto, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonActivity.class);
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_ID, dto.getMemberId());
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_LESSON_ID, dto.getId());
            contents.startActivity(intent);
        }

        @Override
        public void delete(long id, final int position) {
            itemTouchHelper.closeOpened();
            disposable = memberLessonService.deleteById(id).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    memberLessonSection.getList().remove(position);
                    sectionAdapter.notifyItemRemovedFromSection(memberLessonSection, position);
                }
            });
        }

        @Override
        public void scheduleList(long id) {

        }
    };
}
