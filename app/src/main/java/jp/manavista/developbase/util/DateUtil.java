package jp.manavista.developbase.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * 対象日付を基準に月の週開始、週開始終了の日付を取得する。 ※.月を跨る場合は月末または月初
     * http://www.uuniy.com/entry/2016/10/25/125149
     *
     * @param targetDate 対象日付
     * @param firstDayOfWeek 週の開始曜日(Calendar.MONDAY等)
     * @param lastDayOfWeek 週の終了曜日(Calendar.SUNDAY等)
     * @return 返却
     */
    public static Calendar[] getWeekRangeOfMonth(Date targetDate, int firstDayOfWeek, int lastDayOfWeek) {

        // 月初取得
        Calendar calFirstDayOfMonth = getFirstDayOfMonth(targetDate);
        // 月末取得
        Calendar calLastDayOfMonth = getLastDayOfMonth(targetDate);
        // 週初の取得
        Calendar calFirstDayOfWeek = getDayOfWeek(targetDate, firstDayOfWeek, firstDayOfWeek);
        // 週末の取得
        Calendar calLastDayOfWeek = getDayOfWeek(targetDate, firstDayOfWeek, lastDayOfWeek);

        // ***************************
        // * 大小比較用変数
        // ***************************
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        int intFirstDayOfMonth = Integer.parseInt(sdf.format(calFirstDayOfMonth.getTime()));
        int intLastDayOfMonth = Integer.parseInt(sdf.format(calLastDayOfMonth.getTime()));
        int intFirstDayOfWeek = Integer.parseInt(sdf.format(calFirstDayOfWeek.getTime()));
        int intLastDayOfWeek = Integer.parseInt(sdf.format(calLastDayOfWeek.getTime()));

        // ***************************
        // * 範囲判定と結果作成
        // ***************************
        Calendar[] resultCalArr = new Calendar[2];
        // if-- 週初と月初の日付の大小比較
        if (intFirstDayOfWeek > intFirstDayOfMonth) {
            // then-- 週初の方が大きい場合
            // 週初を開始日付に設定
            resultCalArr[0] = calFirstDayOfWeek;
        } else {
            // then-- 月初の方が大きい場合
            // 月初を開始日付に設定
            resultCalArr[0] = calFirstDayOfMonth;
        }
        // if-- 週末と月末の日付の大小比較
        if (intLastDayOfWeek < intLastDayOfMonth) {
            // then-- 週末の方が小さい場合
            // 週末を終了日付に設定
            resultCalArr[1] = calLastDayOfWeek;
        } else {
            // then-- 月末の方が小さい場合
            // 月末を終了日付に設定
            resultCalArr[1] = calLastDayOfMonth;
        }

        return resultCalArr;
    }

    /**
     * 月初取得
     *
     * @param targetDate
     *            対象日付
     * @return 月初日付
     */
    private static Calendar getFirstDayOfMonth(Date targetDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return cal;
    }

    /**
     * 月末取得
     *
     * @param targetDate
     *            対象日付
     * @return 月末日付
     */
    private static Calendar getLastDayOfMonth(Date targetDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal;
    }

    /**
     * 対象日付の同一週の指定曜日の日付を取得する。
     *
     * @param targetDate
     *            対象日付
     * @param firstDayOfWeek
     *            週の開始曜日
     * @param dayOfWeek
     *            取得指定曜日
     * @return 指定曜日の日付
     */
    private static Calendar getDayOfWeek(Date targetDate, int firstDayOfWeek, int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return cal;
    }

}
