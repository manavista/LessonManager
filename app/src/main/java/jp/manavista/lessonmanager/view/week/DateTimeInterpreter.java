/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.week;

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
