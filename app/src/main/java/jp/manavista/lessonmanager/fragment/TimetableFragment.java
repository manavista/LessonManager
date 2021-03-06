/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.constants.analytics.ContentType;
import jp.manavista.lessonmanager.constants.analytics.Event;
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

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE;

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

    private RecyclerView view;
    private ViewGroup emptyState;

    /** Timetable recycler view adapter */
    private TimetableAdapter adapter;
    private ItemTouchHelperExtension itemTouchHelper;

    /** Timetable categoriesList disposable */
    private Disposable timetableDisposable;

    @Inject
    TimetableService timetableService;

    private FirebaseAnalytics analytics;

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
        analytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
        final Activity contents = getActivity();

        view = contents.findViewById(R.id.rv);
        emptyState = contents.findViewById(R.id.empty_state);

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

        timetableDisposable = timetableService.getListAll().subscribe(timetable -> list.add(TimetableDto.copy(timetable)), throwable -> {
            throw new RuntimeException(throwable.toString());
        }, () -> {
            adapter.setList(list);
            adapter.notifyDataSetChanged();

            if( list.isEmpty() ) {
                view.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
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
     */
    public void addTimetable() {

        final List<TimetableDto> list = new ArrayList<>();

        timetableDisposable = timetableService.addDtoList().subscribe(list::add, throwable -> {
            throw new RuntimeException(throwable);
        }, () -> {
            adapter.setList(list);
            adapter.notifyDataSetChanged();

            if( list.isEmpty() ) {
                view.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
            }

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Timetable.label());
            analytics.logEvent(Event.Add.label(), bundle);
        });
    }

    private final TimetableOperation timetableOperation = new TimetableOperation() {
        @Override
        public void delete(int id) {
            itemTouchHelper.closeOpened();
            final List<TimetableDto> list = new ArrayList<>();
            timetableDisposable = timetableService.delete(id).subscribe(timetable -> list.add(TimetableDto.copy(timetable)), throwable -> {
                throw new RuntimeException(throwable.toString());
            }, () -> {
                adapter.setList(list);
                adapter.notifyDataSetChanged();

                if( list.isEmpty() ) {
                    view.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                }

                final Bundle bundle = new Bundle();
                bundle.putString(CONTENT_TYPE, ContentType.Timetable.label());
                analytics.logEvent(Event.Delete.label(), bundle);
            });
        }

        @Override
        public void update(Timetable timetable) {

            itemTouchHelper.closeOpened();
            final List<TimetableDto> list = new ArrayList<>();
            timetableDisposable = timetableService.updateDtoList(timetable).subscribe(list::add, throwable -> {
                throw new RuntimeException(throwable);
            }, () -> {
                adapter.setList(list);
                adapter.notifyDataSetChanged();

                if( list.isEmpty() ) {
                    view.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                }

                final Bundle bundle = new Bundle();
                bundle.putString(CONTENT_TYPE, ContentType.Timetable.label());
                analytics.logEvent(Event.Edit.label(), bundle);
            });
        }

        @Override
        public void inputLessonNo(final View view, final int position) {

            final TextView textView = (TextView) view;
            final int lessonNo = Integer.valueOf(textView.getText().toString());

            final val dialog = NumberPickerDialogFragment.newInstance((NumberPickerDialogFragment.OnSetListener) value -> {
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
            }, 1, 20, lessonNo, getString(R.string.label_timetable_lesson_no_dialog_title));
            dialog.show(getFragmentManager(), "LESSON_NO_DIALOG");

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timetableDisposable.dispose();
    }

}
