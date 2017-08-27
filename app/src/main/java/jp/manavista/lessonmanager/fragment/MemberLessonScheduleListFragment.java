package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
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

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.view.adapter.MemberLessonScheduleAdapter;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.MemberLessonScheduleOperation;

/**
 *
 * MemberLessonSchedule List Fragment
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberLessonScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberLessonScheduleListFragment extends Fragment {

    /** bundle key: member id */
    public static final String KEY_LESSON_ID = "LESSON_ID";

    private long lessonId;

    /** Activity Contents */
    private Activity contents;

    /** MemberLessonSchedule recycler view adapter */
    private MemberLessonScheduleAdapter adapter;

    @Inject
    MemberLessonScheduleService memberLessonScheduleService;

    /** MemberLesson disposable */
    private Disposable disposable;

    public MemberLessonScheduleListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lessonId MemberLesson Id
     * @return A new instance of fragment MemberLessonScheduleListFragment.
     */
    public static MemberLessonScheduleListFragment newInstance(final long lessonId) {
        final MemberLessonScheduleListFragment fragment = new MemberLessonScheduleListFragment();
        final Bundle args = new Bundle();
        args.putLong(KEY_LESSON_ID, lessonId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lessonId = getArguments().getLong(KEY_LESSON_ID);
        }
        this.disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        adapter = MemberLessonScheduleAdapter.newInstance(contents, operation);
        view.setAdapter(adapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        ItemTouchHelperExtension itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {

        super.onResume();

        final List<MemberLessonScheduleVo> list = new ArrayList<>();

        disposable = memberLessonScheduleService.getListByLessonId(lessonId).subscribe(new Consumer<MemberLessonSchedule>() {
            @Override
            public void accept(MemberLessonSchedule memberLessonSchedule) throws Exception {
                list.add(MemberLessonScheduleVo.copy(memberLessonSchedule));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    private MemberLessonScheduleOperation operation = new MemberLessonScheduleOperation() {
        @Override
        public void edit(long id, int position) {

        }

        @Override
        public void delete(long id, int position) {

        }
    };
}
