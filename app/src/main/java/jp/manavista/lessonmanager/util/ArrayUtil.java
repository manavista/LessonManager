package jp.manavista.lessonmanager.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Array Utility
 *
 * <p>
 * Overview:<br>
 * Classes that summarize operations related to arrays.
 * </p>
 */
public class ArrayUtil {

    /**
     *
     * Concat Index of Array
     *
     * <p>
     * Overview:<br>
     * For a target array specified as an argument, return a string concatenated with
     * the specified separator with values whose index is valid (true).
     * </p>
     *
     * @param target target concat string array
     * @param index judge of concat value array
     * @param separator string separator
     * @return concat string
     */
    public static String concatIndexOfArray(String[] target, boolean[] index, String separator) {

        if( ArrayUtils.isEmpty(target) ) {
            return StringUtils.EMPTY;
        } else if ( target.length == 1 ) {
            return target[0];
        }

        if( target.length != index.length ) {
            throw new RuntimeException("difference to index and target length ");
        }

        List<String> list = new ArrayList<>();

        for( int i = 0 ; target.length > i ; i++ ) {
            if( index[i] ) {
                list.add(target[i]);
            }
        }

        return StringUtils.join(list, separator);
    }

    /**
     *
     * Convert Index Array from Value Array
     *
     * <p>
     * Overview:<br>
     * From the value of the target string specified in the argument,
     * return a boolean array corresponding to the index of the corresponding values array
     * </p>
     *
     * @param target target string (e.g. "1,3,5")
     * @param values values array (e.g. {"1","2","3","4","5","6","7"})
     * @param separator target string separator (e.g. ",")
     * @return target index array (e.g. {true, false, true, false, true, false, false})
     */
    public static boolean[] convertIndexFromArray(@Nullable String target, @NonNull String[] values, final String separator) {

        if( ArrayUtils.isEmpty(values) ) {
            return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        }

        final String[] array = StringUtils.split(target, separator);
        final List<Boolean> list = new ArrayList<>();

        for( String value : values ) {
            list.add(ArrayUtils.contains(array, value));
        }

        return ArrayUtils.toPrimitive(list.toArray(new Boolean[0]));
    }
}
