package jp.manavista.lessonmanager.model.vo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.Serializable;

import jp.manavista.lessonmanager.model.entity.Member;
import lombok.Data;
import lombok.ToString;

/**
 *
 * Member Data Transfer Object
 *
 * <p>
 * Overview:<br>
 * Definition of objects used to input and output data on the screen.
 * </p>
 */
@Data
@ToString
public final class MemberVo implements Serializable {

    private long id;
    private String givenName;
    private String additionalName;
    private String familyName;
    private String nickName;
    private String birthday;
    private Integer phoneType;
    private String phoneNumber;
    private Integer emailType;
    private String email;
    private Bitmap photo;

    private String displayName;

    /**
     *
     * Copy Object
     *
     * <p>
     * Overview:<br>
     * Copy the entity object according to the dto object.
     * </p>
     *
     * @param entity Member entity object
     * @return Member data transfer object
     */
    public static MemberVo copy(@NonNull Member entity) {

        MemberVo vo = new MemberVo();

        vo.setId(entity.id);
        vo.setGivenName(entity.givenName);
        vo.setAdditionalName(entity.additionalName);
        vo.setFamilyName(entity.familyName);
        vo.setNickName(entity.nickName);
        vo.setBirthday(entity.birthday);
        vo.setPhoneType(entity.phoneType);
        vo.setPhoneNumber(entity.phoneNumber);
        vo.setEmailType(entity.emailType);
        vo.setEmail(entity.email);

        if( entity.photo != null && entity.photo.length > 0 ) {
            vo.photo = BitmapFactory.decodeByteArray(entity.photo, 0, entity.photo.length);
        }

        return vo;
    }
}
