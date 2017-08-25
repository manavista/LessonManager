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
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.MemberLessonDto;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.MemberLessonOperation;
import jp.manavista.lessonmanager.view.section.MemberLessonSection;

/**
 *
 * MemberLesson List Fragment
 *
 * <p>
 * Overview:<br>
 * Member Lesson categoriesList control fragment.<br>
 * Handling of MemberLesson insert and update (database control) is defined in this class.
 * </p>
 */
public final class MemberLessonListFragment extends Fragment {

    /** Logger tag string */
    private static final String TAG = MemberLessonListFragment.class.getSimpleName();

    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";
    /** member id */
    private long memberId;

    /** Activity Contents */
    private Activity contents;

    /** MemberLesson RecyclerView Adapter */
    private SectionedRecyclerViewAdapter sectionAdapter;
    /** MemberLesson Adapter Section */
    private MemberLessonSection memberLessonSection;
    /** MemberLesson RecyclerView Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;

    @Inject
    MemberLessonService memberLessonService;

    /** MemberLesson disposable */
    private Disposable disposable;

    /** Constructor */
    public MemberLessonListFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param memberId target memberId
     * @return A new instance of fragment MemberLessonListFragment.
     */
    public static MemberLessonListFragment newInstance(final long memberId) {
        final MemberLessonListFragment fragment = new MemberLessonListFragment();
        final Bundle args = new Bundle();
        args.putLong(KEY_MEMBER_ID, memberId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            memberId = args.getLong(KEY_MEMBER_ID);
        }
        this.disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_lesson_list, container, false);
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
        memberLessonSection = MemberLessonSection.newInstance(contents, operation);
        sectionAdapter.addSection(memberLessonSection);

        view.setAdapter(sectionAdapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.setClickToRecoverAnimation(false);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        final List<MemberLessonDto> list = new ArrayList<>();

        disposable = memberLessonService.getListByMemberId(memberId).subscribe(new Consumer<MemberLesson>() {
            @Override
            public void accept(MemberLesson memberLesson) throws Exception {
                list.add(MemberLessonDto.copy(memberLesson));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("can not read MemberLesson entity", throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if( !list.isEmpty() ){
                    memberLessonSection.setTitle(list.get(0).getMemberName());
                }
                memberLessonSection.setList(list);
                sectionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    private MemberLessonOperation operation = new MemberLessonOperation() {
        @Override
        public void edit(final MemberLessonDto dto, final int position) {
            Log.d(TAG, "Edit");
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonActivity.class);
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_ID, dto.getMemberId());
            intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_LESSON_ID, dto.getId());
            contents.startActivity(intent);
        }

        @Override
        public void delete(long id, final int position) {
            Log.d(TAG, "Delete");
            itemTouchHelper.closeOpened();
            disposable = memberLessonService.deleteById(id).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    memberLessonSection.getList().remove(position);
                    sectionAdapter.notifyItemRemoved(position);
                }
            });
        }
    };
}
