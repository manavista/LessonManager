package jp.manavista.developbase.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.developbase.R;
import jp.manavista.developbase.activity.MemberActivity;
import jp.manavista.developbase.activity.MemberLessonListActivity;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.model.dto.MemberDto;
import jp.manavista.developbase.model.entity.Member;
import jp.manavista.developbase.service.MemberService;
import jp.manavista.developbase.view.adapter.MemberAdapter;
import jp.manavista.developbase.view.decoration.ItemDecoration;
import jp.manavista.developbase.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.developbase.view.operation.MemberOperation;

/**
 *
 * Member List Fragment
 *
 * <p>
 * Overview:<br>
 * Display a list of members. <br>
 * Provide interface for editing and creating new.
 * </p>
 */
public final class MemberListFragment extends Fragment {

    /** Logger tag string */
    private static final String TAG = MemberListFragment.class.getSimpleName();

    /** Activity Contents */
    private Activity contents;
    /** Member Adapter */
    private MemberAdapter adapter;
    /** Item Touch Helper */
    ItemTouchHelperExtension itemTouchHelper;
    /** Member list disposable */
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

        adapter = MemberAdapter.newInstance(contents, memberOperation);
        view.setAdapter(adapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.setClickToRecoverAnimation(false);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {

        super.onResume();

        final List<MemberDto> list = new ArrayList<>();

        disposable = memberService.getListAll().subscribe(new Consumer<Member>() {
            @Override
            public void accept(Member member) throws Exception {
                list.add(MemberDto.copy(member));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("Can not get member list all.", throwable);
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

    private MemberOperation memberOperation = new MemberOperation() {

        @Override
        public void edit(final int id, final int position) {

            final String[] items = {getString(R.string.label_member_list_dialog_edit),
                    getString(R.string.label_member_list_dialog_lesson),
                    getString(R.string.label_member_list_dialog_schedule)};

            AlertDialog.Builder builder = new AlertDialog.Builder(contents);
            builder.setTitle(getString(R.string.label_member_list_dialog_title))
                    .setIcon(R.drawable.ic_person_black)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int which) {

                            Log.d(TAG, "which: " + which);
                            Intent intent;

                            switch (which) {
                                case 0:
                                    intent = new Intent(contents, MemberActivity.class);
                                    intent.putExtra(MemberActivity.EXTRA_MEMBER_ID, id);
                                    contents.startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(contents, MemberLessonListActivity.class);
                                    contents.startActivity(intent);
                                    break;
                                case 2:
                                    break;
                            }
                        }
                    })
                    .show();
        }

        @Override
        public void delete(final int id, final int position) {

            itemTouchHelper.closeOpened();

            disposable = memberService.deleteById(id).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    adapter.getList().remove(position);
                    adapter.notifyItemRemoved(position);
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
