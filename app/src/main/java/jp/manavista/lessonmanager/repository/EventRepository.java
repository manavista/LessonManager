package jp.manavista.lessonmanager.repository;

import jp.manavista.lessonmanager.model.entity.Event_Deleter;
import jp.manavista.lessonmanager.model.entity.Event_Relation;
import jp.manavista.lessonmanager.model.entity.Event_Schema;
import jp.manavista.lessonmanager.model.entity.Event_Selector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface EventRepository {

    /**
     *
     * Get schema
     *
     * <p>
     * Overview:<br>
     * Get Event schema
     * </p>
     *
     * @return {@code Event} Schema
     */
    Event_Schema getSchema();

    /**
     *
     * Get selector
     *
     * <p>
     * Overview:<br>
     * Get Event selector
     * </p>
     *
     * @return {@code Event} Selector
     */
    Event_Selector getSelector();

    /**
     *
     * Get relation
     *
     * <p>
     * Overview:<br>
     * Get Event relation object.
     * </p>
     *
     * @return {@code Event} relation
     */
    Event_Relation getRelation();

    /**
     *
     * Get deleter
     *
     * <p>
     * Overview:<br>
     * Get Event deleter object.
     * </p>
     *
     * @return {@code Event} deleter
     */
    Event_Deleter getDeleter();
}
