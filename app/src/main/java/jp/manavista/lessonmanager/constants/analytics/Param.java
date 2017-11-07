/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.constants.analytics;

/**
 *
 * Analytics Parameter
 *
 * <p>
 * Overview:<br>
 * Define an enumeration of events parameter to log.
 * The parameter name is a property of each content type in event.
 * </p>
 */
public enum Param {

    Value("value"),
    Rows("rows")
    ;

    private final String label;

    Param(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
