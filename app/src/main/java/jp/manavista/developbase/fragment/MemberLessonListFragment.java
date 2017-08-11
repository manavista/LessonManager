package jp.manavista.developbase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.manavista.developbase.R;

/**
 *
 * MemberLessonListFragment
 *
 *
 *
 */
public class MemberLessonListFragment extends Fragment {

    /** Logger tag string */
    private static final String TAG = MemberLessonListFragment.class.getSimpleName();

    /** Activity Contents */
    private Activity contents;

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
     * @return A new instance of fragment MemberLessonListFragment.
     */
    public static MemberLessonListFragment newInstance() {
        MemberLessonListFragment fragment = new MemberLessonListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_lesson_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.contents = getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
