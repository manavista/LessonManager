package jp.manavista.lessonmanager.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.injector.DependencyInjector;
import jp.manavista.lessonmanager.model.dto.MemberFragmentDto;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.service.MemberService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

/**
 *
 * Member Fragment
 *
 * <p>
 * Overview:<br>
 * Member control fragment.<br>
 * Handling of Member insert and update (database control) is defined in this class.
 * </p>
 */
public final class MemberFragment extends Fragment implements Validator.ValidationListener {

    /** Logger tag string */
    public static final String TAG = MemberFragment.class.getSimpleName();
    /** bundle key: member id */
    public static final String KEY_MEMBER_ID = "MEMBER_ID";

    /** Root view(R.layout.fragment_member) */
    private View rootView;
    /** Activity Contents */
    private Activity contents;
    /** DTO */
    private MemberFragmentDto dto;
    /** member id */
    private long memberId;
    /** input validator */
    private Validator validator;
    /** Member disposable */
    private Disposable disposable;

    @Inject
    MemberService memberService;


    /** Constructor */
    public MemberFragment() {

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
     * @return A new instance of fragment MemberFragment.
     */
    public static MemberFragment newInstance(final long id) {

        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_MEMBER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        memberId = args.getLong(KEY_MEMBER_ID);

        this.disposable = Disposables.empty();
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
                .birthdayIconImage((ImageView) rootView.findViewById(R.id.birthdayCalenderIcon))
                .gender((Spinner) rootView.findViewById(R.id.genderSpinner))

                .phoneTypeValues(getResources().getIntArray(R.array.values_member_phone_type))
                .emailTypeValues(getResources().getIntArray(R.array.values_member_email_type))
                .genderTypeValues(getResources().getIntArray(R.array.values_member_gender_type))

                .dateFormat(DateTimeUtil.DATE_PATTERN_YYYYMMDD)

                .build();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        this.contents = getActivity();

        DependencyInjector.appComponent().inject(this);

        validator = new Validator(dto);
        validator.setValidationListener(this);

        dto.getBirthdayIconImage().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int year = 1980;
                int month = Calendar.JANUARY;
                int day = 1;

                if( dto.getBirthdayCalendar() != null ) {
                    year = dto.getBirthdayCalendar().get(Calendar.YEAR);
                    month = dto.getBirthdayCalendar().get(Calendar.MONTH);
                    day = dto.getBirthdayCalendar().get(Calendar.DAY_OF_MONTH);
                }
                new DatePickerDialog(contents, dto.birthdaySetListener, year, month, day).show();
            }
        });

        if( memberId > 0 ) {
            storeEntityToDto(memberId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.disposable.dispose();
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

        final String birthday = dto.getBirthday().getText().toString();

        if( StringUtils.isNotEmpty(birthday) ) {
            if( !DateTimeUtil.parseDateStrictly(birthday, DateTimeUtil.DATE_PATTERN_YYYYMMDD) ) {
                dto.getBirthday().setError(getString(R.string.message_member_birthday_input_invalid_date));
                return;
            }
        }

        disposable = memberService.save(dto.convert()).subscribe(new Consumer<Member>() {
            @Override
            public void accept(Member member) throws Exception {
                Log.d(TAG, member.toString());
                contents.finish();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "can not save member", throwable);
            }
        });
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

    /**
     *
     * Store
     *
     * <p>
     * Get {@link Member} entity by default id,
     * then store categoriesList to fragment categoriesList transfer object.
     * </p>
     *
     * @param memberId target member id
     */
    private void storeEntityToDto(final long memberId) {

        disposable = memberService.getById(memberId).subscribe(new Consumer<Member>() {
            @Override
            public void accept(Member member) throws Exception {
                dto.store(member);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throw new RuntimeException("can not select member by " + memberId, throwable);
            }
        });
    }

}