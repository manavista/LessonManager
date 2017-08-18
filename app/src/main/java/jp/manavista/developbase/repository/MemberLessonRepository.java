package jp.manavista.developbase.repository;

import jp.manavista.developbase.model.entity.MemberLesson_Deleter;
import jp.manavista.developbase.model.entity.MemberLesson_Relation;
import jp.manavista.developbase.model.entity.MemberLesson_Selector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface MemberLessonRepository {

    MemberLesson_Selector getSelector();

    MemberLesson_Relation getRelation();

    MemberLesson_Deleter getDeleter();
}
