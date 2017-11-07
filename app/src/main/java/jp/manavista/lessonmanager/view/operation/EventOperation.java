/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.view.operation;

/**
 *
 * Event Operation
 *
 * <p>
 * Overview:<br>
 * Operation {@code Event} entity.<br>
 * Interface for executing listeners defined by adapters with fragment.
 * </p>
 */
public interface EventOperation {

    /**
     *
     * Edit
     *
     * <p>
     * Overview:<br>
     * Edit {@code Event} row
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void edit(final long id, final int position);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete {@code Event} row.
     * </p>
     *
     * @param id target row id
     * @param position target adapter position
     */
    void delete(final long id, final int position);
}
