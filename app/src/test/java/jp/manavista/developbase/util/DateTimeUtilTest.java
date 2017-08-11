package jp.manavista.developbase.util;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

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
}
