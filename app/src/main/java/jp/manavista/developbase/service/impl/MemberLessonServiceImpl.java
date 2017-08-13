package jp.manavista.developbase.service.impl;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.developbase.model.entity.MemberLesson;
import jp.manavista.developbase.repository.MemberLessonRepository;
import jp.manavista.developbase.service.MemberLessonService;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class MemberLessonServiceImpl implements MemberLessonService {

    /** Member Repository */
    private final MemberLessonRepository repository;

    /** Constructor */
    public MemberLessonServiceImpl(MemberLessonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<MemberLesson> save(MemberLesson memberLesson) {
        return repository.getRelation()
                .upsertAsSingle(memberLesson)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
