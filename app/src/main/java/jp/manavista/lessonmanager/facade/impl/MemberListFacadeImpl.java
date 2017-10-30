/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade.impl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import jp.manavista.lessonmanager.facade.MemberListFacade;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;
import jp.manavista.lessonmanager.service.MemberService;

/**
 *
 * MemberList Facade Implementation
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class MemberListFacadeImpl implements MemberListFacade {

    private final MemberService memberService;
    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;

    /** Constructor */
    public MemberListFacadeImpl(
            MemberService memberService,
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService) {
        this.memberService = memberService;
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
    }

    @Override
    public Single<Integer> delete(long memberId) {

        List<Observable<Integer>> targetList = new ArrayList<>();

        targetList.add(memberLessonScheduleService.deleteByMemberId(memberId).toObservable());
        targetList.add(memberLessonService.deleteByMemberId(memberId).toObservable());
        targetList.add(memberService.deleteById(memberId).toObservable());

        return Observable.concat(targetList)
                .reduce(0, (sum, rows) -> sum + rows);
    }
}
