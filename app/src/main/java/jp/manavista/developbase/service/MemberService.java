package jp.manavista.developbase.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.developbase.model.entity.Member;

/**
 *
 * Member Service interface
 *
 * <p>
 * Overview:<br>
 * For the data of {@link Member}, define actions related to acquisition and processing.
 * </p>
 */
public interface MemberService {

    /**
     *
     * Get
     *
     * <p>
     * Overview:<br>
     * Get a Member entity by id.
     * </p>
     *
     * @param id Member id
     * @return {@link Member} entity
     */
    Single<Member> getById(final int id);

    /**
     *
     * Get All List
     *
     * <p>
     * Overview:<br>
     * Get All Member entity of Observable object.
     * </p>
     *
     * @return All Member entity of Observable
     */
    Observable<Member> getListAll();

    /**
     *
     * Save
     *
     * <p>
     * Overview:<br>
     * Save member entity.
     * </p>
     *
     * @param member target {@link Member} entity
     * @return {@link Single} observable object
     */
    Single<Member> save(Member member);

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
    Single<Integer> deleteById(int id);
}
