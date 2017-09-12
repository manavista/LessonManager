package jp.manavista.lessonmanager.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import lombok.val;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * DateTimeUtilTest
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class DateTimeUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void parseCalendarDate() throws Exception {

        final String date  = "2013/04/30";
        final String format = DateTimeUtil.DATE_PATTERN_YYYYMMDD;

        Calendar expected = Calendar.getInstance();
        expected.set(2013, Calendar.APRIL, 30);

        Calendar actual = DateTimeUtil.parserCalendar(date, format);
        assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
        assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
        assertEquals(expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void parseCalendarDateTime() throws Exception {

        final String date  = "2013/04/30";
        final String format = DateTimeUtil.DATE_PATTERN_YYYYMMDD;
        final Time time = DateTimeUtil.parseTime(17, 40);

        Calendar actual = DateTimeUtil.parseCalendar(date, format, time);

        Calendar expected = Calendar.getInstance();
        expected.set(2013, Calendar.APRIL, 30);
        expected.set(Calendar.HOUR_OF_DAY, 17);
        expected.set(Calendar.MINUTE, 40);

        val sdf = DateTimeUtil.DATETIME_FORMAT_YYYYMMDD_HHMMDD;

        assertThat(sdf.format(actual.getTime()), is(sdf.format(expected.getTime())));
    }

    @Test
    public void parseTime() throws Exception {

        final SimpleDateFormat format = DateTimeUtil.TIME_FORMAT_HHMM;
        final String time = "13:00";

        final String expected = "13:00:00";
        final Time actual = DateTimeUtil.parseTime(format, time);

        assertThat(actual.toString(), is(expected));
    }

    @Test
    @Ignore
    public void today() throws Exception {

        final String expected = "2017/08/17";
        final String actual = DateTimeUtil.today(DateTimeUtil.DATE_FORMAT_YYYYMMDD);

        assertThat(actual, is(expected));
    }

    @Test
    public void extractDate() {

        final String startDate = "2017/08/28";
        final String endDate = "2017/10/30";
        final String[] dayOfWeek = {"2"}; // Monday

        final List<String> actual = DateTimeUtil.extractTargetDates(
                startDate, endDate, DateTimeUtil.DATE_PATTERN_YYYYMMDD, dayOfWeek);

        assertThat(actual.size(), is(10));
    }

    @Test
    public void extractDateNoArgument() throws Exception {

        final String startDate = "2017/08/26";
        final String endDate = "2017/10/30";
        final String[] dayOfWeek = {"2"}; // Monday

        expectedException.expect(IllegalArgumentException.class);

        final List<String> actual = DateTimeUtil.extractTargetDates(
                null, null, DateTimeUtil.DATE_PATTERN_YYYYMMDD, dayOfWeek);

    }

    @Test
    public void compareCalendar() throws Exception {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal2.set(Calendar.YEAR, 2019);
        cal2.set(Calendar.MONTH, 11);

        boolean expected = true;
        boolean actual = cal1.before(cal2);

        assertThat(actual, is(expected));

        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DAY_OF_MONTH, 1);

        // cal1 > cal3 = 1, ca1 == cal2 = 0, cal1 < cal3 = -1
        int actual2 = DateUtils.truncatedCompareTo(cal1, cal3, Calendar.DAY_OF_MONTH);
        assertThat(actual2, is(-1));

        int count = 0;
        while (  cal1.before(cal2) ) {

            if( cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
                count++;
            }
            cal1.add(Calendar.DAY_OF_MONTH, 1);
        }

        assertThat(count, is(not(0)));
    }

    @Test
    public void nextMonth() throws Exception {

        int year = 2017;
        int month = 12;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        calendar.add(Calendar.MONTH, 1);

        assertThat(calendar.get(Calendar.YEAR), is(2018));
        assertThat(calendar.get(Calendar.MONTH), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(1));
    }

    @Test
    public void addTimeMinutes() throws Exception {

        Time time = DateTimeUtil.parseTime(12,40);
        Time time2 = DateTimeUtil.addMinutes(time, 30);

        String expected = "13:10";
        String actual = DateTimeUtil.TIME_FORMAT_HHMM.format(time2);

        assertThat(expected, is(actual));
    }

    @Test
    public void calculateTimeSpan() throws Exception {

        Time start = DateTimeUtil.parseTime(12, 40);
        Time end = DateTimeUtil.parseTime(14, 0);

        int expected = 80;
        int actual = DateTimeUtil.calculateMinuteSpan(start,end);

        assertThat(actual, is(expected));
    }
}
