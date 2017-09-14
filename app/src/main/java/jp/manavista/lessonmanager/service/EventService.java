package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.entity.Event;
import jp.manavista.lessonmanager.model.vo.EventVo;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
public interface EventService {

    /**
     *
     * Get
     *
     * <p>
     * Overview:<br>
     * Get a entity by id.
     * </p>
     *
     * @param id entity id
     * @return {@link Event} entity
     */
    Single<Event> getById(final long id);

    /**
     *
     * Get All Value Object List
     *
     * @return {@link Event} vo list
     */
    Observable<EventVo> getVoListAll();


    /**
     *
     * Save
     *
     * <p>
     * Overview:<br>
     * save Event entity.
     * </p>
     *
     * @param entity target {@link Event}
     * @return {@link Single} observable object
     */
    Single<Event> save(Event entity);

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete by entity row id.
     * </p>
     *
     * @param id row id
     * @return transaction row count
     */
    Single<Integer> deleteById(long id);

    /**
     *
     * Delete All
     *
     * <p>
     * Overview:<br>
     * Delete All rows in {@code Event}
     * </p>
     *
     * @return {@link Single} observable object in deleted rows count
     */
    Single<Integer> deleteAll();
}
