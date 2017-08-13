package jp.manavista.developbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.developbase.R;
import jp.manavista.developbase.model.dto.MemberLessonFragmentDto;

/**
 *
 * MemberLesson Fragment
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public final class MemberLessonFragment extends Fragment {

    /** Logger tag string */
    public static final String TAG = MemberLessonFragment.class.getSimpleName();
    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";

    /** member id */
    private int memberId;

    private MemberLessonFragmentDto dto;

    /** Member disposable */
    private Disposable disposable;

    /** Constructor */
    public MemberLessonFragment() {
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
     * @param id display member id
     * @return A new instance of fragment MemberLessonFragment.
     */
    public static MemberLessonFragment newInstance(final int id) {
        MemberLessonFragment fragment = new MemberLessonFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_MEMBER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            memberId = args.getInt(KEY_MEMBER_ID);
        }

        this.disposable = Disposables.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_lesson, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.disposable.dispose();
    }
}
