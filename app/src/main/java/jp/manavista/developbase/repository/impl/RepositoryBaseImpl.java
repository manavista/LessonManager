package jp.manavista.developbase.repository.impl;

import jp.manavista.developbase.model.entity.OrmaDatabase;

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
