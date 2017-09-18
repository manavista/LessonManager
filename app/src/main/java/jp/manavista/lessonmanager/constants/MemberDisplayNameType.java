package jp.manavista.lessonmanager.constants;

import android.util.SparseArray;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public enum MemberDisplayNameType {

    /** GivenName + " " + (AdditionalName + " ") + FamilyName + " " + (NickName) */
    GIVEN_FAMILY_NICK(1),
    /** FamilyName + " " + (AdditionalName + " ") + GivenName + " " + (NickName) */
    FAMILY_GIVEN_NICK(2),
    /** NickName + " " + (GivenName + " " + (AdditionalName + " ") + FamilyName) */
    NICK_GIVEN_FAMILY(3),
    /** NickName + " " + (FamilyName + " " + (AdditionalName + " ") + GivenName) */
    NICK_FAMILY_GIVEN(4);

    private int code;

    MemberDisplayNameType(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static SparseArray<MemberDisplayNameType> map = new SparseArray<MemberDisplayNameType>() {{
        for (MemberDisplayNameType type : values() ) {
            put(type.code(), type);
        }
    }};
}
