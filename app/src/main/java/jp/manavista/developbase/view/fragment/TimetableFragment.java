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

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.TimetableDto;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.view.adapter.TimetableAdapter;
import jp.manavista.developbase.view.adapter.TimetableTouchHelperCallback;

public final class TimetableFragment extends Fragment {

    private static final String TAG = TimetableFragment.class.getSimpleName();

    @Inject
    TimetableService timetableService;

    /** RootView object */
    private View rootView;

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
        Activity contents = getActivity();
        
        int[] lessonNo = {1,2,3,4,5};
        Time[] startTime = {new Time(17,0,0),new Time(17,0,0),new Time(17,0,0),new Time(17,0,0),new Time(17,0,0)};
        Time[] endTime = {new Time(19,0,0),new Time(19,0,0),new Time(19,0,0),new Time(19,0,0),new Time(19,0,0)};

        List<TimetableDto> list = new ArrayList<>();
        for( int i = 0 ; i < lessonNo.length ; i++ ) {
            TimetableDto dto = new TimetableDto();
            dto.setLessonNo(lessonNo[i]);
            dto.setStartTime(startTime[i]);
            dto.setEndTime(endTime[i]);
            list.add(dto);
        }

        RecyclerView view = contents.findViewById(R.id.rv);
        view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(contents);
        view.setLayoutManager(manager);

        TimetableAdapter adapter = TimetableAdapter.newInstance(contents);
        adapter.setList(list);
        view.setAdapter(adapter);

        ItemTouchHelperExtension.Callback callback = new TimetableTouchHelperCallback();
        ItemTouchHelperExtension itemTouchHelper = new ItemTouchHelperExtension(callback);
        itemTouchHelper.attachToRecyclerView(view);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
