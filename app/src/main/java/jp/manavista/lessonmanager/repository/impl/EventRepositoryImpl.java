package jp.manavista.lessonmanager.repository.impl;

import jp.manavista.lessonmanager.model.entity.Event_Deleter;
import jp.manavista.lessonmanager.model.entity.Event_Relation;
import jp.manavista.lessonmanager.model.entity.Event_Schema;
import jp.manavista.lessonmanager.model.entity.Event_Selector;
import jp.manavista.lessonmanager.model.entity.OrmaDatabase;
import jp.manavista.lessonmanager.repository.EventRepository;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public class EventRepositoryImpl extends RepositoryBaseImpl implements EventRepository {

    /** Constructor */
    public EventRepositoryImpl(OrmaDatabase database) {
        super(database);
    }

    @Override
    public Event_Schema getSchema() {
        return getRelation().getSchema();
    }

    @Override
    public Event_Selector getSelector() {
        return database.selectFromEvent();
    }

    @Override
    public Event_Relation getRelation() {
        return database.relationOfEvent();
    }

    @Override
    public Event_Deleter getDeleter() {
        return database.deleteFromEvent();
    }
}
