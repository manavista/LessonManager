/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.constants.analytics;

/**
 *
 * Analytics Content Type
 *
 * <p>
 * Overview:<br>
 * Define an enumeration of events content type to log.
 * The type name is a target item name of each operation.
 * </p>
 */
public enum ContentType {

    VisibleDays("Visible Days"),
    Calendar("Calendar"),
    Member("Member"),
    Lesson("Lesson"),
    Schedule("Schedule"),
    Event("Event"),
    Timetable("Timetable"),
    ;

    private final String label;

    ContentType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
