package jp.manavista.lessonmanager.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.model.entity.Timetable;

/**
 *
 * Timetable Service interface
 *
 * <p>
 * Overview:<br>
 * For the data of Timetable, define actions related to acquisition and processing.
 * </p>
 */
public interface TimetableService {

    /**
     *
     * Get All List
     *
     * <p>
     * Overview:<br>
     * Get All Timetable entity of Observable object.
     * </p>
     *
     * @return All Timetable entity of Observable
     */
    Observable<Timetable> getListAll();

    Observable<TimetableDto> getDtoListAll();

    /**
     *
     * Add
     *
     * <p>
     * Overview:<br>
     * Add new Timetable row, and then get All TimetableDto List of Observable object.
     * </p>
     *
     * @return All Timetable Data Transfer Object of Observable
     */
    Observable<TimetableDto> addDtoList();

    /**
     *
     * Delete
     *
     * <p>
     * Overview:<br>
     * Delete exists Timetable row, and then get All Timetable List of Observable object.
     * </p>
     *
     * @param id target delete Timetable id
     * @return All Timetable entity of Observable
     */
    Observable<Timetable> delete(int id);

    /**
     *
     * Update
     *
     * <p>
     * Overview:<br>
     * Update exists Timetable row, and then get All Timetable List of Observable object.
     * </p>
     *
     * @param timetable target update Timetable entity
     * @return All Timetable entity of Observable
     */
    Observable<Timetable> update(Timetable timetable);

    /**
     *
     * Update
     *
     * <p>
     * Overview:<br>
     * Update exists Timetable row, and then get All TimetableDto List of Observable object.
     * </p>
     *
     * @param timetable target update Timetable entity
     * @return All Timetable Data Transfer Object of Observable
     */
    Observable<TimetableDto> updateDtoList(Timetable timetable);


    Single<Timetable> save(Timetable timetable);

    Single<Integer> deleteById(int id);

    /**
     *
     * Delete All
     *
     * <p>
     * Overview:<br>
     * Delete All rows in {@code Timetable}
     * </p>
     *
     * @return {@link Single} observable object in deleted rows count
     */
    Single<Integer> deleteAll();
}
