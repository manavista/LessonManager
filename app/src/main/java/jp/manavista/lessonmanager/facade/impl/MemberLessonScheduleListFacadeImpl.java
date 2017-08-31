package jp.manavista.lessonmanager.facade.impl;

import android.util.Pair;

import java.util.List;

import io.reactivex.Single;
import jp.manavista.lessonmanager.facade.MemberLessonScheduleListFacade;
import jp.manavista.lessonmanager.model.vo.MemberLessonScheduleVo;
import jp.manavista.lessonmanager.model.vo.MemberLessonVo;
import jp.manavista.lessonmanager.service.MemberLessonScheduleService;
import jp.manavista.lessonmanager.service.MemberLessonService;

/**
 *
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonScheduleListFacadeImpl implements MemberLessonScheduleListFacade {

    private final MemberLessonService memberLessonService;
    private final MemberLessonScheduleService memberLessonScheduleService;

    /** Constructor */
    public MemberLessonScheduleListFacadeImpl (
            MemberLessonService memberLessonService,
            MemberLessonScheduleService memberLessonScheduleService) {
        this.memberLessonService = memberLessonService;
        this.memberLessonScheduleService = memberLessonScheduleService;
    }

    @Override
    public Single<Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>>> getListPair(final long memberId) {

        // TODO: 2017/08/29 implement
        final Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>> pair = Pair.create(null, null);
        final List<MemberLessonVo> memberLessonVoList;

//        return memberLessonService.getSingleVoListByMemberId(memberId).flatMap(new Function<List<MemberLessonVo>, SingleSource<? extends List<MemberLessonScheduleVo>>>() {
//            @Override
//            public SingleSource<? extends List<MemberLessonScheduleVo>> apply(@NonNull final List<MemberLessonVo> memberLessonVos) throws Exception {
//                memberLessonVoList = memberLessonVos;
//                return memberLessonScheduleService.getSingleVoListByMemberId(memberId);
//            }
//        }).flatMap(new Function<List<MemberLessonScheduleVo>, SingleSource<? extends Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>>>>() {
//            @Override
//            public Single<Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>>> apply(@NonNull List<MemberLessonScheduleVo> memberLessonScheduleVos) throws Exception {
//                return Single.just(Pair.create(memberLessonVoList, memberLessonScheduleVos));
//            }
//        });
//    }

//        return memberLessonService.getSingleVoListByMemberId(memberId).flatMapObservable(new Function<List<MemberLessonVo>, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<List<MemberLessonVo>> apply(@NonNull List<MemberLessonVo> memberLessonVos) throws Exception {
//                return io.reactivex.Observable.just(memberLessonVos);
//            }
//        }).flatMap(new Function<ObservableSource<List<MemberLessonVo>>, SingleSource<List<MemberLessonScheduleVo>>>() {
//            @Override
//            public SingleSource<List<MemberLessonScheduleVo>> apply(@NonNull ObservableSource<List<MemberLessonVo>> listObservableSource) throws Exception {
//                return memberLessonScheduleService.getSingleVoListByMemberId(memberId);
//            }
//        }, new BiFunction<ObservableSource<List<MemberLessonVo>>, SingleSource<List<MemberLessonScheduleVo>>, SingleSource<Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>>>>() {
//
//            @Override
//            public Object apply(@NonNull Object o, @NonNull Object o2) throws Exception {
//                return null;
//            }
//        })
        return null;
    }

    @Override
    public void createListPair(long memberId, Pair<List<MemberLessonVo>, List<MemberLessonScheduleVo>> listPair) {

        List<MemberLessonVo> memberLessonVoList;
        List<MemberLessonScheduleVo> memberLessonScheduleVoList;


//        listPair = Pair.create(memberLessonVoList, memberLessonScheduleVoList);
    }
}
