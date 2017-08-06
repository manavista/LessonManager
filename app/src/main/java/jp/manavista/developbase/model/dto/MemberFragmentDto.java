package jp.manavista.developbase.model.dto;

import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;

import java.io.Serializable;

import jp.manavista.developbase.model.entity.Member;
import lombok.Data;

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
public final class MemberFragmentDto implements Serializable {

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
    private EditText birthday;

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

        member.phoneType = 0;
        member.phoneNumber = this.phoneNumber.getText().toString();
        member.emailType = 0;
        member.email = this.email.getText().toString();

        member.birthday = null;

        return member;
    }
}
