package jp.manavista.developbase.view.week;

import java.util.Calendar;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
