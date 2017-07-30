package jp.manavista.developbase.util;

import android.util.Pair;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class DateTimeUtil {

    public static String COLON = ":";
    public static String SLASH = "/";

    public static SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_MMDD = new SimpleDateFormat("MM/dd", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_MMDDE = new SimpleDateFormat("MM/dd E", Locale.getDefault());
    public static SimpleDateFormat DATE_FORMAT_E = new SimpleDateFormat("E", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_HHMM = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_hhMM = new SimpleDateFormat("hh:mm", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_H = new SimpleDateFormat("H", Locale.getDefault());
    public static SimpleDateFormat TIME_FORMAT_m = new SimpleDateFormat("m", Locale.getDefault());

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
