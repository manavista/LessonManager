/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
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
import jp.manavista.lessonmanager.activity.EventActivity;
import jp.manavista.lessonmanager.constants.DateLabel;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.vo.EventVo;
import jp.manavista.lessonmanager.service.EventService;
import jp.manavista.lessonmanager.view.decoration.ItemDecoration;
import jp.manavista.lessonmanager.view.helper.SwipeDeleteTouchHelperCallback;
import jp.manavista.lessonmanager.view.operation.EventOperation;
import jp.manavista.lessonmanager.view.section.EventSection;
import lombok.Getter;

/**
 *
 * Event List Fragment
 *
 */
public final class EventListFragment extends Fragment {

    /** Date Label Map */
    private SparseArray<String> dateLabelArray;

    /** Activity Contents */
    private Activity contents;

    private RecyclerView view;
    private ViewGroup emptyState;

    /** MemberLesson RecyclerView Adapter */
    @Getter
    private SectionedRecyclerViewAdapter adapter;
    /** Event Adapter Section */
    private EventSection section;
    /** Item Touch Helper */
    private ItemTouchHelperExtension itemTouchHelper;
    /** Member categoriesList disposable */
    private Disposable disposable;

    @Inject
    EventService service;
    /** Shared preferences */
    @Inject
    SharedPreferences preferences;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventListFragment.
     */
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.disposable = Disposables.empty();

        dateLabelArray = new SparseArray<>();
        dateLabelArray.put(DateLabel.TODAY.code(), getString(R.string.label_event_list_date_today));
        dateLabelArray.put(DateLabel.YESTERDAY.code(), getString(R.string.label_event_list_date_yesterday));
        dateLabelArray.put(DateLabel.TOMORROW.code(), getString(R.string.label_event_list_date_tomorrow));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();
        DependencyInjector.appComponent().inject(this);

        view = contents.findViewById(R.id.rv);
        emptyState = contents.findViewById(R.id.empty_state);

        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);
        view.addItemDecoration(new ItemDecoration(contents));

        adapter = new SectionedRecyclerViewAdapter();
        section = EventSection.newInstance(operation);
        adapter.addSection(section);
        view.setAdapter(adapter);

        final ItemTouchHelperExtension.Callback callback = new SwipeDeleteTouchHelperCallback();
        itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.setClickToRecoverAnimation(false);
        itemTouchHelper.attachToRecyclerView(view);
    }

    @Override
    public void onResume() {

        super.onResume();

        final int key = R.string.key_preferences_event_list_display_past;
        final boolean containPast = preferences.getBoolean(getString(key), false);

        final List<EventVo> list = new ArrayList<>();

        disposable = service.getVoListByCriteria(containPast, dateLabelArray).subscribe(new Consumer<EventVo>() {
            @Override
            public void accept(EventVo vo) throws Exception {
                list.add(vo);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                section.setList(list);
                adapter.notifyDataSetChanged();

                if( list.isEmpty() ) {
                    view.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    /**
     *
     * Event Operation
     *
     * <p>
     * Define the implementation
     * that manipulates the items of the EventList within the section.
     * </p>
     */
    private final EventOperation operation = new EventOperation() {
        @Override
        public void edit(long id, int position) {
            itemTouchHelper.closeOpened();
            final Intent intent = new Intent(contents, EventActivity.class);
            intent.putExtra(EventActivity.EXTRA_EVENT_ID, id);
            contents.startActivity(intent);
        }

        @Override
        public void delete(long id, final int position) {
            itemTouchHelper.closeOpened();
            disposable = service.deleteById(id).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    section.getList().remove(position);
                    adapter.notifyItemRemoved(position);

                    if( section.getList().isEmpty() ) {
                        view.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    }
                }
            });
        }
    };
}
