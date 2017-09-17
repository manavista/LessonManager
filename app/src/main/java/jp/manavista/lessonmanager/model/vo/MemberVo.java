package jp.manavista.lessonmanager.model.vo;

import org.apache.commons.lang3.StringUtils;

import jp.manavista.lessonmanager.model.entity.Member;
import lombok.Data;

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
public class MemberVo {

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


    /**
     *
     * Get Display Name
     *
     * <p>
     * Overview:<br>
     * Create and get concatenate names string to be displayed in the list.<br>
     * </p>
     * <pre>
     * givenName + " " + (additionalName + " ") + familyName
     * </pre>
     *
     * @return concatenate names string
     */
    public String getDisplayName() {

        StringBuilder builder = new StringBuilder();
        builder.append(givenName)
                .append(StringUtils.SPACE)
                .append(StringUtils.isEmpty(additionalName) ? StringUtils.EMPTY : this.additionalName + StringUtils.SPACE)
                .append(familyName);

        if( StringUtils.isNotEmpty(nickName) ) {
            builder.append(StringUtils.SPACE)
                    .append("(")
                    .append(nickName)
                    .append(")");
        }

        return builder.toString();
    }

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
    public static MemberVo copy(Member entity) {

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

        return vo;
    }
}
