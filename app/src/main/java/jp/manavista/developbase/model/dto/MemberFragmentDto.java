package jp.manavista.developbase.model.dto;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Past;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.io.Serializable;
import java.util.Locale;

import jp.manavista.developbase.model.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * MemberFragment Data Transfer Object
 *
 * <p>
 * Overview:<br>
 * Definition of objects used to input and output data on the screen.
 * </p>
 */
@Data
@Builder
public final class MemberFragmentDto implements Serializable {

    /** Logger tag string */
    public static final String TAG = MemberFragmentDto.class.getSimpleName();

    /** phone type Spinner value */
    private int[] phoneTypeValue;
    /** email type Spinner value */
    private int[] emailTypeValue;
    /** gender type Spinner value */
    private int[] genderTypeValue;


    /** Given Name(FirstName) */
//    @BindView(R.id.givenNameEditText)
    @NotEmpty
    @Length(max = 50)
    private EditText givenName;

    /** Additional Name(MiddleName) */
//    @BindView(R.id.additionalNameEditText)
    @Optional
    @Length(max = 50)
    private EditText additionalName;

    /** Family Name(LastName) */
//    @BindView(R.id.familyNameEditText)
    @NotEmpty
    @Length(max = 50)
    private EditText familyName;

    /** Nick Name */
//    @BindView(R.id.nickNameEditText)
    @Optional
    @Length(max = 50)
    private EditText nickName;

    /** Phone Type(mobile, home, work, other) */
//    @BindView(R.id.phoneNumberTypeSpinner)
    private Spinner phoneType;

    /** Phone Number */
//    @BindView(R.id.phoneNumberEditText)
    @Optional
    @Length(max = 20)
    private EditText phoneNumber;

    /** Email Type(mobile, home, work, other) */
//    @BindView(R.id.emailTypeSpinner)
    private Spinner emailType;

    /** Email Address */
//    @BindView(R.id.emailEditText)
    @Optional
    @Email
    private EditText email;

    /** Birthday */
//    @BindView(R.id.birthdayEditText)
    @Optional
    @Pattern(regex = "^\\d{4}/\\d{2}/\\d{2}$")
    @Past(dateFormat = "yyyy/MM/dd")
    private EditText birthday;

    /** Image icon for input Birthday date */
    private ImageView birthdayCalendar;

    /** Gender */
    @Optional
    private Spinner gender;


    /**
     *
     * Convert Object
     *
     * <p>
     * Overview:<br>
     * Convert DTO to entity.
     * </p>
     *
     * @return Member entity
     */
    public Member convert() {

        Member member = new Member();

        member.givenName = this.givenName.getText().toString();
        member.additionalName = this.additionalName.getText().toString();
        member.familyName = this.familyName.getText().toString();
        member.nickName = this.nickName.getText().toString();

        member.phoneType = getPhoneTypeValue()[phoneType.getSelectedItemPosition()];
        member.phoneNumber = this.phoneNumber.getText().toString();
        member.emailType = getEmailTypeValue()[emailType.getSelectedItemPosition()];
        member.email = this.email.getText().toString();

        member.birthday = this.birthday.getText().toString();
        member.gender = getGenderTypeValue()[gender.getSelectedItemPosition()];

        return member;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    public final DatePickerDialog.OnDateSetListener birthdaySetListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {

            /* monthYear is 0-11 */
            monthYear += 1;

            final String format = "%04d/%02d/%02d";
            Log.d(TAG, String.format(format, year, monthYear, dayOfMonth));
            birthday.setText(String.format(Locale.getDefault(), format, year, monthYear, dayOfMonth));
        }
    };
}
