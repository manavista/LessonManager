package jp.manavista.lessonmanager.model.dto;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.util.DateTimeUtil;
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
    private int[] phoneTypeValues;
    /** email type Spinner value */
    private int[] emailTypeValues;
    /** gender type Spinner value */
    private int[] genderTypeValues;
    /** birthday string format */
    private String dateFormat;

    /** entity id */
    private int id;

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
    private ImageView birthdayIconImage;

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

        member.id = id;
        member.givenName = this.givenName.getText().toString();
        member.additionalName = this.additionalName.getText().toString();
        member.familyName = this.familyName.getText().toString();
        member.nickName = this.nickName.getText().toString();

        member.phoneType = getPhoneTypeValues()[phoneType.getSelectedItemPosition()];
        member.phoneNumber = this.phoneNumber.getText().toString();
        member.emailType = getEmailTypeValues()[emailType.getSelectedItemPosition()];
        member.email = this.email.getText().toString();

        member.birthday = this.birthday.getText().toString();
        member.gender = getGenderTypeValues()[gender.getSelectedItemPosition()];

        return member;
    }

    public void store(@NonNull Member entity) {

        id = entity.id;

        givenName.setText(entity.givenName);
        additionalName.setText(entity.additionalName);
        familyName.setText(entity.familyName);
        nickName.setText(entity.nickName);

        phoneType.setSelection(ArrayUtils.indexOf(phoneTypeValues, entity.phoneType != null ? entity.phoneType : 1));
        phoneNumber.setText(entity.phoneNumber);
        emailType.setSelection(ArrayUtils.indexOf(emailTypeValues, entity.emailType != null ? entity.emailType : 1));
        email.setText(entity.email);

        // TODO: raw to format data YYYY/MM/DD
        birthday.setText(entity.birthday);
        gender.setSelection(ArrayUtils.indexOf(genderTypeValues, entity.gender != null ? entity.gender : 1));
    }

    /**
     *
     * Birthday Calendar
     *
     * <p>
     * Overview:<br>
     * Get a Calendar object of a birthday date.
     * </p>
     *
     * @return Birthday Calendar object
     */
    public Calendar getBirthdayCalendar() {
        final String date = birthday.getText().toString();
        return StringUtils.isNotEmpty(date)
                ? DateTimeUtil.parserCalendar(date, dateFormat)
                : null;
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
