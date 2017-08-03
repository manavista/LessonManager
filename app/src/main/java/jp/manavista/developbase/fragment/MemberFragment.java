package jp.manavista.developbase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.sql.Date;

import jp.manavista.developbase.R;
import jp.manavista.developbase.injector.DependencyInjector;

/**
 *
 * Member Fragment
 *
 * <p>
 * Overview:<br>
 * Timetable control fragment.<br>
 * Handling of Member insert and update (database control) is defined in this class.
 * </p>
 */
public final class MemberFragment extends Fragment {

    private EditText givenName;
    private EditText additionalName;
    private EditText familyName;
    private EditText nickName;
    private int phoneType;
    private String phoneNumber;
    private int emailType;
    private String email;
    private Date birthday;

    /** Constructor */
    public MemberFragment() {
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
     * @return A new instance of fragment MemberFragment.
     */
    public static MemberFragment newInstance() {

        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
        final Activity contents = getActivity();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     *
     * Save Member
     *
     * <p>
     * Overview:<br>
     * Save the information on the member you entered on the screen.
     * </p>
     */
    public void saveMember() {

        // TODO: input data validation.
    }
}
