package jp.manavista.lessonmanager.facade.impl;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import jp.manavista.lessonmanager.facade.MemberLessonFacade;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.model.entity.MemberLesson;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;

/**
 *
 * MemberLesson Facade Implementation
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonFacadeImpl implements MemberLessonFacade {

    private final MemberService memberService;
    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;

    /** Constructor */
    public MemberLessonFacadeImpl(
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService) {
        this.memberService = memberService;
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
    }

    @Override
    public Single<Long> save(final long memberId, final MemberLesson entity, final boolean addSchedule) {
        return memberService.getById(memberId)
                .flatMap(new Function<Member, SingleSource<MemberLesson>>() {
                    @Override
                    public SingleSource<MemberLesson> apply(@NonNull Member member) throws Exception {
                        entity.member = member;
                        return memberLessonService.save(entity);
                    }
                })
                .flatMap(new Function<MemberLesson, SingleSource<Long>>() {
                    @Override
                    public SingleSource<Long> apply(@NonNull MemberLesson lesson) throws Exception {

                        return addSchedule
                                ? memberLessonScheduleService.createByLesson(lesson.member, lesson)
                                : Single.just(0L);
                    }
                });
    }

    @Override
    public Single<Boolean> isEmptySchedule(long lessonId) {
        return memberLessonScheduleService.getListByLessonId(lessonId)
                .isEmpty();
    }
}
