package jp.manavista.developbase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import jp.manavista.developbase.R;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.model.dto.MemberFragmentDto;
import jp.manavista.developbase.service.MemberService;
import jp.manavista.developbase.util.DateTimeUtil;

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
public final class MemberFragment extends Fragment implements Validator.ValidationListener {

    /** Logger tag string */
    public static final String TAG = MemberFragment.class.getSimpleName();

    /** Root view(R.layout.fragment_member) */
    private View rootView;
    /** Activity Contents */
    private final Activity contents;
    /** DTO */
    private MemberFragmentDto dto;
    /** input validator */
    private Validator validator;
    /** Member disposable */
    private Disposable memberDisposable;

    @Inject
    MemberService memberService;


    /** Constructor */
    public MemberFragment() {
        this.memberDisposable = Disposables.empty();
        this.contents = getActivity();
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

        rootView = inflater.inflate(R.layout.fragment_member, container, false);

        dto = MemberFragmentDto.builder()
                .givenName((EditText) rootView.findViewById(R.id.givenNameEditText))
                .additionalName((EditText) rootView.findViewById(R.id.additionalNameEditText))
                .familyName((EditText) rootView.findViewById(R.id.familyNameEditText))
                .nickName((EditText) rootView.findViewById(R.id.nickNameEditText))
                .phoneType((Spinner) rootView.findViewById(R.id.phoneNumberTypeSpinner))
                .phoneNumber((EditText) rootView.findViewById(R.id.phoneNumberEditText))
                .emailType((Spinner) rootView.findViewById(R.id.emailTypeSpinner))
                .email((EditText) rootView.findViewById(R.id.emailEditText))
                .birthday((EditText) rootView.findViewById(R.id.birthdayEditText))
                .gender((Spinner) rootView.findViewById(R.id.genderSpinner))

                .phoneTypeValue(getResources().getIntArray(R.array.values_member_phone_type))
                .emailTypeValue(getResources().getIntArray(R.array.values_member_email_type))
                .genderTypeValue(getResources().getIntArray(R.array.values_member_gender_type))

                .build();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.memberDisposable.dispose();
    }

    /**
     *
     * Save Member
     *
     * <p>
     * Overview:<br>
     * Save the information on the member you entered on the screen.<br>
     * First, validate the input value. Perform processing with another
     * function according to the result.
     * </p>
     */
    public void saveMember() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        Log.d(TAG, dto.convert().toString());

        final String birthday = dto.getBirthday().getText().toString();
        if( !DateTimeUtil.parseDateStrictly(birthday, DateTimeUtil.DATE_PATTERN_YYYYMMDD) ) {
            dto.getBirthday().setError(getString(R.string.message_member_birthday_input_invalid_date));
            return;
        }

//        memberDisposable = memberService.save(dto.convert()).subscribe(new Consumer<Member>() {
//            @Override
//            public void accept(Member member) throws Exception {
//                Log.d(TAG, member.toString());
//                contents.finish();
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                Log.e(TAG, "can not save member", throwable);
//            }
//        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for( ValidationError error : errors ) {

            final String message = error.getCollatedErrorMessage(contents);
            final View view = error.getView();

            if( view instanceof EditText ) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(contents, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
