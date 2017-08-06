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
 * </p>
 */
public interface MemberService {

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
}
