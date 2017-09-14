package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.dto.EventDto;
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
     * Convert DTO to entity object. Then, save Event entity.
     * </p>
     *
     * @param dto target {@link EventDto}
     * @return {@link Single} observable object
     */
    Single<Event> save(EventDto dto);

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
