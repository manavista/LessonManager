package jp.manavista.lessonmanager.model.dto;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.ByteArrayOutputStream;
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
public final class MemberDto implements Serializable {

    /** Logger tag string */
    public static final String TAG = MemberDto.class.getSimpleName();

    /** phone type Spinner value */
    private int[] phoneTypeValues;
    /** email type Spinner value */
    private int[] emailTypeValues;
    /** birthday string format */
    private String dateFormat;

    /** entity id */
    private long id;

    /** Given Name(FirstName) */
    @NotEmpty
    @Length(max = 50)
    private EditText givenName;

    /** Additional Name(MiddleName) */
    @Optional
    @Length(max = 50)
    private EditText additionalName;

    /** Family Name(LastName) */
    @NotEmpty
    @Length(max = 50)
    private EditText familyName;

    /** Nick Name */
    @Optional
    @Length(max = 50)
    private EditText nickName;

    /** Phone Type(mobile, home, work, other) */
    private Spinner phoneType;

    /** Phone Number */
    @Optional
    @Length(max = 20)
    private EditText phoneNumber;

    /** Email Type(mobile, home, work, other) */
    private Spinner emailType;

    /** Email Address */
    @Optional
    @Email
    private EditText email;

    /** Birthday */
    @Optional
    @Pattern(regex = "^\\d{4}/\\d{2}/\\d{2}$")
    @Past(dateFormat = "yyyy/MM/dd")
    private EditText birthday;

    /** Image icon for input Birthday date */
    private ImageButton birthdayIconImage;

    private ImageView photo;
    private ImageButton photoIconImage;

    /**
     *
     * Convert Object
     *
     * <p>
     * Overview:<br>
     * Convert DTO to entity.
     * </p>
     *
     * @return {@code Member} entity
     */
    public Member toEntity() {

        final Member entity = new Member();

        entity.id = id;
        entity.givenName = this.givenName.getText().toString();
        entity.additionalName = this.additionalName.getText().toString();
        entity.familyName = this.familyName.getText().toString();
        entity.nickName = this.nickName.getText().toString();

        entity.phoneType = getPhoneTypeValues()[phoneType.getSelectedItemPosition()];
        entity.phoneNumber = this.phoneNumber.getText().toString();
        entity.emailType = getEmailTypeValues()[emailType.getSelectedItemPosition()];
        entity.email = this.email.getText().toString();

        entity.birthday = this.birthday.getText().toString();

        if( photo.getDrawable() != null ) {
            final Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            entity.photo = stream.toByteArray();
        }

        return entity;
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

        birthday.setText(entity.birthday);

        if( entity.photo != null && entity.photo.length > 0 ) {
            final Bitmap bitmap = BitmapFactory.decodeByteArray(entity.photo, 0, entity.photo.length);
            photo.setImageBitmap(bitmap);
        }
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
