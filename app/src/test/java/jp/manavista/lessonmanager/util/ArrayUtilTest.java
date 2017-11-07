package jp.manavista.lessonmanager.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 *
 * ArrayUtilTest
 *
 * <p>
 * Overview:<br>
 * Test define for Utility class of Array.
 * </p>
 */
public class ArrayUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void join() throws Exception {

        final String[] array = {"a", "b", "c"};
        final String expected = "a,b,c";
        final String actual = StringUtils.join(array, ",");

        assertThat(actual, is(expected));
    }

    @Test
    public void concatIndex() throws Exception {

        final String[] array = {"a", "b", "c"};
        final boolean[] index = {true, false, true};

        final String expected = "a,c";
        final String actual = ArrayUtil.concatIndexOfArray(array, index, ",");

        assertThat(actual, is(expected));
    }

    @Test
    public void concatEmptyIndex() throws Exception {

        final String[] array = {};
        final boolean[] index = {};

        final String expected = "";
        final String actual = ArrayUtil.concatIndexOfArray(array, index, ",");

        assertThat(actual, is(expected));
    }

    @Test
    public void concatDifferenceIndex() throws Exception {

        final String[] array = {"a", "b", "c"};
        final boolean[] index = {true, false};

        expectedException.expect(RuntimeException.class);

        ArrayUtil.concatIndexOfArray(array, index, ",");
    }

    @Test
    public void convertIndex() throws Exception {

        final String dayOfWeek = "1,3,5";
        final String[] dayOfWeekValues = {"1","2","3","4","5","6","7"};

        final boolean[] expected = {true, false, true, false, true, false, false};
        final boolean[] actual = ArrayUtil.convertIndexFromArray(dayOfWeek, dayOfWeekValues, ",");

        assertThat(actual, is(expected));
    }

    @Test
    public void convertValuesEmpty() throws Exception {

        final String dayOfWeek = "1,3,5";
        final String[] dayOfWeekValues = {};
        final boolean[] expected = {};
        final boolean[] actual = ArrayUtil.convertIndexFromArray(dayOfWeek, dayOfWeekValues, ",");

        assertThat(actual, is(expected));
    }

    @Test
    public void convertTargetEmpty() throws Exception {

        final String dayOfWeek = "";
        final String[] dayOfWeekValues = {"1","2","3","4","5","6","7"};

        final boolean[] expected = {false, false, false, false, false, false, false};
        final boolean[] actual = ArrayUtil.convertIndexFromArray(dayOfWeek, dayOfWeekValues, ",");

        assertThat(actual, is(expected));
    }
}
