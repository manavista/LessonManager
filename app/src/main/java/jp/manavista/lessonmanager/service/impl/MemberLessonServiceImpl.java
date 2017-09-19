package jp.manavista.lessonmanager.service.impl;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
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

    /** MemberLesson Repository */
    private final MemberLessonRepository repository;

    /** Constructor */
    public MemberLessonServiceImpl(MemberLessonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<MemberLesson> getById(long id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
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
    public Observable<MemberLessonVo> getVoListByMemberId(long memberId) {
        return repository.getRelation()
                .selector()
                .memberIdEq(memberId)
                .executeAsObservable()
                .map(new Function<MemberLesson, MemberLessonVo>() {
                    @Override
                    public MemberLessonVo apply(@NonNull MemberLesson entity) throws Exception {
                        return MemberLessonVo.copy(entity);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<MemberLessonVo>> getSingleVoListByMemberId(long memberId) {
        return repository.getRelation()
                .selector()
                .memberIdEq(memberId)
                .executeAsObservable()
                .map(new Function<MemberLesson, MemberLessonVo>() {
                    @Override
                    public MemberLessonVo apply(@NonNull MemberLesson entity) throws Exception {
                        return MemberLessonVo.copy(entity);
                    }
                })
                .toList()
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
    public Single<Integer> deleteById(long id) {
        return repository.getDeleter()
                .idEq(id)
                .executeAsSingle()
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
