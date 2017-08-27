package jp.manavista.lessonmanager.service.impl;

import android.util.Log;

import com.github.gfx.android.orma.SingleAssociation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.model.entity.MemberLessonSchedule;
import jp.manavista.lessonmanager.repository.MemberLessonScheduleRepository;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.util.DateTimeUtil;

/**
 *
 * MemberLessonSchedule Service Implement
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonScheduleServiceImpl implements MemberLessonScheduleService {

    private static final String TAG = MemberLessonScheduleServiceImpl.class.getSimpleName();

    /** MemberLessonSchedule Repository */
    private final MemberLessonScheduleRepository repository;

    /** Constructor */
    public MemberLessonScheduleServiceImpl(MemberLessonScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<MemberLessonSchedule> getById(long id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MemberLessonSchedule> getListByLessonId(long lessonId) {
        return repository.getRelation()
                .selector()
                .memberLessonEq(lessonId)
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MemberLessonSchedule> getListByMemberId(long memberId) {
        return null;
    }

    @Override
    public Single<MemberLessonSchedule> save(MemberLessonSchedule entity) {
        return repository.getRelation()
                .upsertAsSingle(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Long> createByLesson(final MemberLesson lesson) {

        if( lesson == null ) {
            Log.e(TAG, "Argument entity is null");
            throw new RuntimeException("Argument entity is null");
        }

        final String[] dayOfWeek = lesson.dayOfWeeks.split(",");
        final List<String> dateList = DateTimeUtil.extractTargetDates(
                lesson.periodFrom, lesson.periodTo, DateTimeUtil.DATE_PATTERN_YYYYMMDD, dayOfWeek);

        final List<MemberLessonSchedule> entityList = new ArrayList<>();

        for( String date : dateList ) {
            final MemberLessonSchedule schedule = new MemberLessonSchedule();
            schedule.lessonDate = date;
            schedule.memberLesson = SingleAssociation.just(lesson);
            entityList.add(schedule);
        }

        return repository.getRelation()
                .upsertAsObservable(entityList)
                .count()
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
