/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.constants.analytics;

/**
 *
 * Analytics Event
 *
 * <p>
 * Overview:<br>
 * Define an enumeration of events to log.
 * The event name is a category of each operation.
 * </p>
 */
public enum Event {

    View("operation_view"),
    Add("operation_add"),
    Edit("operation_edit"),
    Delete("operation_delete"),
    Filter("operation_filter"),
    ;

    private final String label;

    Event(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
