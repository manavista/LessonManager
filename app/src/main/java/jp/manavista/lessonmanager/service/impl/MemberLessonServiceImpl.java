package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.repository.MemberLessonRepository;
import jp.manavista.lessonmanager.service.MemberLessonService;

/**
 *
 * Member Lesson Service Implement
 *
 * <p>
 * Overview:<br>
 *
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
    public Observable<MemberLesson> getListAll() {
        return repository.getSelector()
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MemberLesson> getListByMemberId(long memberId) {
        return repository.getRelation()
                .selector()
                .memberIdEq(memberId)
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MemberLesson> save(MemberLesson memberLesson) {
        return repository.getRelation()
                .upsertAsSingle(memberLesson)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteAll() {
        return repository.getDeleter()
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
