package jp.manavista.developbase.util;

import android.util.Pair;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

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
