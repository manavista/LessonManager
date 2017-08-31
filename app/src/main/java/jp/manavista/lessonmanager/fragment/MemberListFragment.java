package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import jp.manavista.lessonmanager.activity.MemberActivity;
import jp.manavista.lessonmanager.activity.MemberLessonScheduleListActivity;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.MemberVo;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.MemberOperation;
import jp.manavista.lessonmanager.view.section.MemberSection;
import lombok.Getter;

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
    MemberService memberService;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
        this.contents = getActivity();

        final RecyclerView view = contents.findViewById(R.id.rv);
        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new ItemDecoration(contents));

        sectionAdapter = new SectionedRecyclerViewAdapter();
        memberSection = MemberSection.newInstance(contents, memberOperation);
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

        disposable = memberService.getListAll().subscribe(new Consumer<Member>() {
            @Override
            public void accept(Member member) throws Exception {
                list.add(MemberVo.copy(member));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("Can not get member categoriesList all.", throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                memberSection.setList(list);
                sectionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    private MemberOperation memberOperation = new MemberOperation() {

        @Override
        public void lessonList(MemberVo dto, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberLessonScheduleListActivity.class);
            intent.putExtra(MemberLessonScheduleListActivity.EXTRA_MEMBER_ID, dto.getId());
            intent.putExtra(MemberLessonScheduleListActivity.EXTRA_MEMBER_NAME, dto.getDisplayName());
            contents.startActivity(intent);
        }

        @Override
        public void edit(final long id, final int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, MemberActivity.class);
            intent.putExtra(MemberActivity.EXTRA_MEMBER_ID, id);
            contents.startActivity(intent);
        }

        @Override
        public void delete(final long id, final int position) {

            itemTouchHelper.closeOpened();

            disposable = memberService.deleteById(id).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    memberSection.getList().remove(position);
                    sectionAdapter.notifyItemRemoved(position);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throw new RuntimeException("Can not delete a member.", throwable);
                }
            });
        }
    };
}
