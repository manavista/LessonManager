package jp.manavista.developbase.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.view.adapter.TimetableAdapter;
import jp.manavista.developbase.view.adapter.TimetableTouchHelperCallback;
import jp.manavista.developbase.view.operation.TimetableOperation;

/**
 *
 * Timetable Fragment
 *
 * <p>
 * Overview:<br>
 * Timetable control fragment.<br>
 * Handling of timetable select, insert and update (database control) is defined in this class.
 * </p>
 */
public final class TimetableFragment extends Fragment {

    /** Logger tag */
    private static final String TAG = TimetableFragment.class.getSimpleName();

    /** RootView object */
    private View rootView;

    /** Timetable recycler view adapter */
    private TimetableAdapter adapter;

    /** Timetable list disposable */
    private Disposable timetableDisposable = Disposables.empty();

    @Inject
    TimetableService timetableService;


    /** constructor */
    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimetableFragment.
     */
    public static TimetableFragment newInstance() {
        TimetableFragment fragment = new TimetableFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
        final Activity contents = getActivity();

        final List<TimetableDto> list = new ArrayList<>();

        final RecyclerView view = contents.findViewById(R.id.rv);
        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);

        adapter = TimetableAdapter.newInstance(contents, timetableOperation);
        view.setAdapter(adapter);

        ItemTouchHelperExtension.Callback callback = new TimetableTouchHelperCallback();
        ItemTouchHelperExtension itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);

        timetableDisposable = timetableService.getListAll().subscribe(new Consumer<Timetable>() {
            @Override
            public void accept(@NonNull Timetable timetable) throws Exception {
                list.add(TimetableDto.copy(timetable));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throw new RuntimeException(throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });

    }

    /**
     *
     * Add Timetable
     *
     * <p>
     * Overview:<br>
     * Add new Timetable row.<br>
     * Default lesson no, start time and end time is automatic formed.
     * </p>
     *
     */
    public void addTimetable() {

        final List<TimetableDto> list = new ArrayList<>();

        timetableDisposable = timetableService.add().subscribe(new Consumer<Timetable>() {
            @Override
            public void accept(@NonNull Timetable timetable) throws Exception {
                list.add(TimetableDto.copy(timetable));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throw new RuntimeException(throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private TimetableOperation timetableOperation = new TimetableOperation() {
        @Override
        public void delete(int id) {
            final List<TimetableDto> list = new ArrayList<>();
            timetableDisposable = timetableService.delete(id).subscribe(new Consumer<Timetable>() {
                @Override
                public void accept(@NonNull Timetable timetable) throws Exception {
                    list.add(TimetableDto.copy(timetable));
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    throw new RuntimeException(throwable.toString());
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
        public void update(Timetable timetable) {
            final List<TimetableDto> list = new ArrayList<>();
            timetableDisposable = timetableService.update(timetable).subscribe(new Consumer<Timetable>() {
                @Override
                public void accept(@NonNull Timetable timetable) throws Exception {
                    list.add(TimetableDto.copy(timetable));
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    throw new RuntimeException(throwable.toString());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timetableDisposable.dispose();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
