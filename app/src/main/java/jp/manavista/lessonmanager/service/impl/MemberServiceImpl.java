/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.service.impl;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.manavista.lessonmanager.constants.MemberDisplayNameType;
import jp.manavista.lessonmanager.model.entity.Member;
import jp.manavista.lessonmanager.model.vo.MemberVo;
import jp.manavista.lessonmanager.repository.MemberRepository;
import jp.manavista.lessonmanager.service.MemberService;

/**
 *
 * Member service implement
 *
 * <p>
 * Overview:<br>
 * Define implements actions related to acquisition and processing
 * for the {@link Member} entity.
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
    public Single<Member> getById(final long id) {
        return repository.getRelation()
                .idEq(id)
                .getAsSingle(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MemberVo> getVoListAll(final int displayNameCode) {

        final StringBuilder builder = new StringBuilder();

        return repository.getSelector()
                .executeAsObservable()
                .map(member -> {
                    final MemberVo vo = MemberVo.copy(member);
                    vo.setDisplayName(getDisplayName(member, displayNameCode, builder));
                    return vo;
                })
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
    public Single<Integer> deleteById(final long id) {
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

    @Override
    public String getDisplayName(final Member entity, final int displayCode, final StringBuilder builder) {

        /* Don't create StringBuilder new instance in loop */
        builder.setLength(0);

        final MemberDisplayNameType type = MemberDisplayNameType.map.get(displayCode);
        switch (type) {

            case GIVEN_FAMILY_NICK:
                builder.append(entity.givenName)
                        .append(StringUtils.SPACE)
                        .append(StringUtils.isEmpty(entity.additionalName) ? StringUtils.EMPTY : entity.additionalName + StringUtils.SPACE)
                        .append(entity.familyName);

                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(StringUtils.SPACE)
                            .append("(")
                            .append(entity.nickName)
                            .append(")");
                }
                break;
            case FAMILY_GIVEN_NICK:
                builder.append(entity.familyName)
                        .append(StringUtils.SPACE)
                        .append(StringUtils.isEmpty(entity.additionalName) ? StringUtils.EMPTY : entity.additionalName + StringUtils.SPACE)
                        .append(entity.givenName);

                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(StringUtils.SPACE)
                            .append("(")
                            .append(entity.nickName)
                            .append(")");
                }
                break;
            case NICK_GIVEN_FAMILY:
                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(entity.nickName)
                            .append(StringUtils.SPACE)
                            .append("(");
                }
                builder.append(entity.givenName)
                        .append(StringUtils.SPACE)
                        .append(StringUtils.isEmpty(entity.additionalName) ? StringUtils.EMPTY : entity.additionalName + StringUtils.SPACE)
                        .append(entity.familyName);
                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(")");
                }
                break;
            case NICK_FAMILY_GIVEN:
                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(entity.nickName)
                            .append(StringUtils.SPACE)
                            .append("(");
                }
                builder.append(entity.familyName)
                        .append(StringUtils.SPACE)
                        .append(StringUtils.isEmpty(entity.additionalName) ? StringUtils.EMPTY : entity.additionalName + StringUtils.SPACE)
                        .append(entity.givenName);
                if( StringUtils.isNotEmpty(entity.nickName) ) {
                    builder.append(")");
                }
                break;
        }

        return builder.toString();
    }
}
