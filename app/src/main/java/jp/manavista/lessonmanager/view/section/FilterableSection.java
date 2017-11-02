/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.section;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public interface FilterableSection {

    /**
     *
     * Filter
     *
     * <p>
     * Overview:<br>
     * Filter by the query string specified as argument.
     * </p>
     *
     * @param query filter string
     */
    void filter(String query);
}
