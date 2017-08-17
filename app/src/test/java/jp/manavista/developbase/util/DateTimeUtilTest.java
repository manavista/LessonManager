package jp.manavista.developbase.util;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class DateTimeUtilTest {

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
}
