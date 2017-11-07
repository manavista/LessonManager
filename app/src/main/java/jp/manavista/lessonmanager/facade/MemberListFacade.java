/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade;

import io.reactivex.Single;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface MemberListFacade {

    Single<Integer> delete(long memberId);
}
