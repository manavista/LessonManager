package jp.manavista.developbase.dto;

import android.support.v4.view.ViewPager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 *
 * Main Activity Dto
 *
 * <p>
 * Overview:<br>
 * Use Main Activity properties and utility methods.
 * </p>
 *
 */
@Builder
@Data
public class MainActivityDto implements Serializable {

    /* view pager */

    /** view pager object */
    private ViewPager viewPager;
    /** view pager position */
    private int viewPosition;

    /* preference */

    /** view mode */
    private String viewMode;
    /** display days of week */
    private Set<String> displayDaySet;
    /** start view day of week */
    private int displayStartDay;

    /**
     *
     * Get start display day
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     * @return
     */
    public int getStartDisplayDay() {

        if( this.displayDaySet == null || this.displayDaySet.isEmpty() ) {
            return this.displayStartDay;
        }

        final String day = Collections.min(this.displayDaySet);
        return Integer.valueOf(day);
    }

    /**
     *
     * Get end display day
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     * @return
     */
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
