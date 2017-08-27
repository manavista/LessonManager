package jp.manavista.lessonmanager.repository;

import jp.manavista.lessonmanager.model.entity.MemberLesson_Deleter;
import jp.manavista.lessonmanager.model.entity.MemberLesson_Relation;
import jp.manavista.lessonmanager.model.entity.MemberLesson_Selector;

/**
 *
 * MemberLesson Repository
 *
 * <p>
 * Overview:<br>
 * Manipulate repository on {@link jp.manavista.lessonmanager.model.entity.MemberLesson} data.
 * </p>
 */
public interface MemberLessonRepository {


    /**
     *
     * Get selector
     *
     * <p>
     * Overview:<br>
     * Get MemberLesson selector
     * </p>
     *
     * @return MemberLesson Selector
     */
    MemberLesson_Selector getSelector();

    /**
     *
     * Get relation
     *
     * <p>
     * Overview:<br>
     * Get MemberLesson relation object.
     * </p>
     *
     * @return MemberLesson relation
     */
    MemberLesson_Relation getRelation();

    /**
     *
     * Get deleter
     *
     * <p>
     * Overview:<br>
     * Get MemberLesson deleter object.
     * </p>
     *
     * @return MemberLesson deleter
     */
    MemberLesson_Deleter getDeleter();
}
