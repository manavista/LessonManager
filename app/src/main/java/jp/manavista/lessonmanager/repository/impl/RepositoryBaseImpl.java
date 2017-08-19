package jp.manavista.lessonmanager.repository.impl;

import jp.manavista.lessonmanager.model.entity.OrmaDatabase;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public abstract class RepositoryBaseImpl {

    final OrmaDatabase database;

    public RepositoryBaseImpl(OrmaDatabase database) {
        this.database = database;
    }
}
