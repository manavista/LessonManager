package jp.manavista.developbase.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 *
 * Preference Setting Dao
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 *
 */
@Builder
@Data
public class PreferenceSettingDao implements Serializable {

    /** view mode */
    private String viewMode;
    /** display days of week */
    private Set<String> displayDaySet;
    /** start view day of week */
    private int displayStartDay;


    public int getStartDisplayDay() {

        if( this.displayDaySet == null || this.displayDaySet.isEmpty() ) {
            return this.displayStartDay;
        }

        final String day = Collections.min(this.displayDaySet);
        return Integer.valueOf(day);
    }

    public int getEndDisplayDay() {

        if( this.displayDaySet == null || this.displayDaySet.isEmpty() ) {
            return this.displayStartDay == Calendar.SUNDAY
                    ? Calendar.SATURDAY
                    : this.displayStartDay - 1;
        }

        final String day = Collections.max(this.displayDaySet);
        return Integer.valueOf(day);
    }
}
