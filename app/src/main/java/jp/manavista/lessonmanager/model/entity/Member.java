package jp.manavista.lessonmanager.model.entity;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import lombok.ToString;

/**
 *
 * Member entity
 *
 * <p>
 * Overview:<br>
 * Define the object of the person who will take the lesson.
 * And, define some data type conversion methods.
 * </p>
 */
@Table
@ToString
public class Member {

    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public String givenName;
    @Column
    @Nullable
    public String additionalName;
    @Column
    public String familyName;
    @Column
    @Nullable
    public String nickName;
    @Column
    @Nullable
    public String birthday;
    @Column
    @Nullable
    public Integer gender;
    @Column
    @Nullable
    public Integer phoneType;
    @Column
    @Nullable
    public String phoneNumber;
    @Column
    @Nullable
    public Integer emailType;
    @Column
    @Nullable
    public String email;
}
