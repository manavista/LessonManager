package jp.manavista.developbase.dto;

import android.support.v4.view.ViewPager;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
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
    /** display days of week (SUNDAY:1, ... SATURDAY:7) */
    private Set<String> displayDaySet;
    /** start view day of week */
    private int displayStartDay;

    /**
     *
     * Get start display day
     *
     * <p>
     * Overview:<br>
     * Get start day of week code.
     * </p>
     *
     * @see Calendar calendar static code.
     * @return start day of week code
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
     * Get end day of week code.
     * </p>
     *
     * @return end day of week code
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

    /**
     *
     * Get display days of week array
     *
     * <p>
     * Overview:<br>
     * Get display days of week in array.<br>
     * Guarantee the order of the days of the week to display.
     * </p>
     *
     * @return the days of the week in array
     */
    public String[] getDisplayDaysOfWeek() {

        if( this.displayStartDay == 0 ) {
            setDisplayStartDay(Calendar.SUNDAY);
        }

        List<String> days1 = new ArrayList<>();
        List<String> days2 = new ArrayList<>();

        for( String day : getDisplayDaySet() ) {
            if( Integer.valueOf(day) >= this.displayStartDay ) {
                days1.add(day);
            } else {
                days2.add(day);
            }
        }

        Collections.sort(days1);
        Collections.sort(days2);

        days1.addAll(days2);

        return days1.toArray(new String[0]);
    }
}
