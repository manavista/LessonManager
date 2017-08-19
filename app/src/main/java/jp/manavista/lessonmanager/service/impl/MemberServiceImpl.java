package jp.manavista.lessonmanager.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.repository.MemberRepository;
import jp.manavista.lessonmanager.service.MemberService;

/**
 *
 * Member service implement
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberServiceImpl implements MemberService {

    /** Member Repository */
    private final MemberRepository repository;

    /** Constructor */
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.repository = memberRepository;
    }

    @Override
    public Single<Member> getById(int id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Member> getListAll() {
        return repository.getSelector()
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Member> save(Member member) {
        return repository.getRelation()
                .upsertAsSingle(member)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteById(int id) {
        return repository.getDeleter()
                .idEq(id)
                .executeAsSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
