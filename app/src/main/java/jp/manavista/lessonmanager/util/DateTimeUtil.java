package jp.manavista.lessonmanager.util;

import android.support.annotation.NonNull;
import android.util.Pair;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * Date and Time Utility
 *
 * <p>
 * Overview:<br>
 * Define the utility function on date and time manipulation.
 * </p>
 */
public final class DateTimeUtil {

    public static final String COLON = ":";
    public static final String SLASH = "/";

    public static final String DATE_PATTERN_YYYYMMDD = "yyyy/MM/dd";
    public static final String DATE_PATTERN_YYYYMD = "yyyy/M/d";
    public static final String DATE_PATTERN_YYYYMMDD_DIGITS = "yyyyMMdd";
    public static final String DATETIME_PATTERN_YYYYMMDD_HHMMDD = "yyyy/MM/dd HH:mm";

    public static SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD, Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_YYYYMMDD_DIGITS = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD_DIGITS, Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_MMDD = new SimpleDateFormat("MM/dd", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_MMDDE = new SimpleDateFormat("MM/dd E", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_E = new SimpleDateFormat("E", Locale.getDefault());

    public static SimpleDateFormat TIME_FORMAT_HHMM = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_hhMM = new SimpleDateFormat("hh:mm", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_H = new SimpleDateFormat("H", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_m = new SimpleDateFormat("m", Locale.getDefault());

    public static SimpleDateFormat DATETIME_FORMAT_YYYYMMDD_HHMMDD = new SimpleDateFormat(DATETIME_PATTERN_YYYYMMDD_HHMMDD, Locale.getDefault());

    private static final Map<String, Integer> dayOfWeekConvertMap = new HashMap<String, Integer>() {{
        put("1", Calendar.SUNDAY);
        put("2",Calendar.MONDAY);
        put("3",Calendar.TUESDAY);
        put("4",Calendar.WEDNESDAY);
        put("5",Calendar.THURSDAY);
        put("6",Calendar.FRIDAY);
        put("7",Calendar.SATURDAY);
    }};

    /**
     *
     * Get First and Last Days of Week
     *
     * <p>
     * Overview:<br>
     *
     * </p>
     *
     * @param targetDate target date
     * @param firstDayOfWeek first day of week
     * @param lastDayOfWeek last day of week
     * @return pair of calendar object
     */
    public static Pair<Calendar, Calendar> getWeekRange (
            Date targetDate, final int firstDayOfWeek, final int lastDayOfWeek) {

        // First day of Week
        final Calendar first = getDayOfWeek(targetDate, firstDayOfWeek, firstDayOfWeek);
        // Last day of week
        final Calendar last = getDayOfWeek(targetDate, firstDayOfWeek, lastDayOfWeek);

        return Pair.create(first, last);
    }

    public static String today(final SimpleDateFormat format) {
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    public static Calendar today() {
        return Calendar.getInstance();
    }

    public static String format(final String format, final Calendar calendar) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }

    public static Time parseTime(int hourOfDay, int minute) {
        return parseTime(hourOfDay, minute, 0);
    }

    public static Time parseTime(int hourOfDay, int minute, int second) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return new Time(calendar.getTime().getTime());
    }

    public static Time parseTime(@NonNull SimpleDateFormat format, @NonNull String time) {

        try {
            return new Time(format.parse(time).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean parseDateStrictly(final String str, final String... patterns) {

        try {
            DateUtils.parseDateStrictly(str, Locale.getDefault(), patterns);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static Calendar parseCalendar(final String date, final String dateFormat, final Time time) {

        Calendar calendar = parserCalendar(date, dateFormat);

        Calendar timeCalendar = Calendar.getInstance(Locale.getDefault());
        timeCalendar.setTime(time);

        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

        return calendar;
    }

    public static Calendar parserCalendar(final String date, final String format) {

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     *
     * Extract Target Dates
     *
     * <p>
     * Overview:<br>
     * In the start and end period specified in the argument,
     * extract the date character string list corresponding to the target day of the week.
     * </p>
     *
     * @param startDate target start date
     * @param endDate target end date
     * @param format target date string format
     * @param dayOfWeek target day of week argument (1:Sunday, 2:Monday,...)
     * @return extracted date string list
     */
    public static List<String> extractTargetDates(@NonNull final String startDate, @NonNull final String endDate,
            @NonNull final String format, @NonNull final String[] dayOfWeek) {

        if( StringUtils.isAnyEmpty(startDate, endDate, format) ) {
            throw new IllegalArgumentException("The arguments must not be null");
        }

        final List<Integer> dayOfWeekList = new ArrayList<>();
        final List<String> dateList = new ArrayList<>();

        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

        try {

            final Calendar start = DateUtils.toCalendar(dateFormat.parse(startDate));
            final Calendar end = DateUtils.toCalendar(dateFormat.parse(endDate));

            /*
             * truncate
             * 2017/08/27 15:34:56 -> 2017/08/27 00:00:00
             * To become a comparison by only date
             */
            DateUtils.truncate(start, Calendar.DAY_OF_MONTH);
            DateUtils.truncate(end, Calendar.DAY_OF_MONTH);

            for( String day : dayOfWeek ) {
                dayOfWeekList.add(dayOfWeekConvertMap.get(day));
            }

            if( dayOfWeekList.isEmpty() ) {
                return dateList;
            }

            while( start.compareTo(end) <= 0 ) {

                if( dayOfWeekList.contains(start.get(Calendar.DAY_OF_WEEK)) ) {
                    dateList.add(dateFormat.format(start.getTime()));
                }
                start.add(Calendar.DAY_OF_MONTH, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateList;
    }

    /**
     * 対象日付の同一週の指定曜日の日付を取得する。
     *
     * @param targetDate 対象日付
     * @param firstDayOfWeek 週の開始曜日
     * @param dayOfWeek 取得指定曜日
     * @return 指定曜日の日付
     */
    private static Calendar getDayOfWeek (
            Date targetDate, final int firstDayOfWeek, final int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return cal;
    }
}
