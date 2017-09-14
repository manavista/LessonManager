package jp.manavista.lessonmanager.model.entity;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import lombok.ToString;

/**
 *
 * Event
 *
 * <p>
 * Overview:<br>
 * Define the event data.
 * </p>
 */
@Table
@ToString
public class Event {

    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String name;
    @Column(indexed = true)
    public String date;
    @Column
    public int textColor;
    @Column
    public int backgroundColor;
    @Column
    @Nullable
    public String memo;
}
