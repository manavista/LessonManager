package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.Timetable;
import jp.manavista.lessonmanager.service.TimetableService;
import jp.manavista.lessonmanager.view.adapter.TimetableAdapter;
import jp.manavista.lessonmanager.view.decoration.TimetableItemDecoration;
import jp.manavista.lessonmanager.view.dialog.NumberPickerDialogFragment;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.TimetableOperation;
import lombok.val;

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
    private ItemTouchHelperExtension itemTouchHelper;

    /** Timetable categoriesList disposable */
    private Disposable timetableDisposable;

    @Inject
    TimetableService timetableService;


    /** constructor */
    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     *
     * New Instance
     *
     * <p>
     * Overview:
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * </p>
     *
     * @return A new instance of fragment TimetableFragment.
     */
    public static TimetableFragment newInstance() {
        return new TimetableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timetableDisposable = Disposables.empty();
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
        final RecyclerView view = contents.findViewById(R.id.rv);

        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new TimetableItemDecoration(contents));

        adapter = TimetableAdapter.newInstance(contents, timetableOperation);
        view.setAdapter(adapter);

        ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        final List<TimetableDto> list = new ArrayList<>();

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

        timetableDisposable = timetableService.addDtoList().subscribe(new Consumer<TimetableDto>() {
            @Override
            public void accept(TimetableDto dto) throws Exception {
                list.add(dto);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException(throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private final TimetableOperation timetableOperation = new TimetableOperation() {
        @Override
        public void delete(int id) {
            itemTouchHelper.closeOpened();
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

            itemTouchHelper.closeOpened();
            final List<TimetableDto> list = new ArrayList<>();
            timetableDisposable = timetableService.updateDtoList(timetable).subscribe(new Consumer<TimetableDto>() {
                @Override
                public void accept(@NonNull TimetableDto dto) throws Exception {
                    list.add(dto);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    throw new RuntimeException(throwable);
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
        public void inputLessonNo(final View view, final int position) {

            final TextView textView = (TextView) view;
            final int lessonNo = Integer.valueOf(textView.getText().toString());

            final val dialog = NumberPickerDialogFragment.newInstance(new NumberPickerDialogFragment.OnSetListener() {
                @Override
                public void onNumberSet(int value) {
                    final TimetableDto row = adapter.getList().get(position);

                    List<Integer> lessonNoList = new ArrayList<>();
                    for( val dto : adapter.getList() ) {
                        lessonNoList.add(dto.getLessonNo());
                    }

                    if( lessonNoList.contains(value) ) {
                        Crouton.makeText(getActivity(),
                                R.string.message_timetable_error_duplicate_lesson_no,
                                Style.ALERT).show();
                        return;
                    }

                    row.setLessonNo(value);
                    Log.d(TAG, "change Timetable row position: " + position + " row: " + row);
                    update(Timetable.convert(row));
                }
            }, 1, 20, lessonNo, getString(R.string.label_timetable_lesson_no_dialog_title));
            dialog.show(getFragmentManager(), "LESSON_NO_DIALOG");

        }
    };

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
