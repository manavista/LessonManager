package jp.manavista.developbase.model.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Date;

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
public class Member {

    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public String givenName;
    @Column
    public String additionalName;
    @Column
    public String familyName;
    @Column
    public String nickName;
    @Column
    public Date birthday;
    @Column
    public int gender;
    @Column
    public int phoneType;
    @Column
    public String phoneNumber;
    @Column
    public int emailType;
    @Column
    public String email;
}
