/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.facade;

import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import jp.manavista.lessonmanager.model.dto.TimetableDto;
import jp.manavista.lessonmanager.view.week.LessonView;
import jp.manavista.lessonmanager.view.week.WeekViewEvent;

/**
 *
 * LessonView Facade
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public interface LessonViewFacade {

    Disposable getViewData(LessonView lessonView, List<TimetableDto> timetableList,
                           List<WeekViewEvent> lessonList, Set<Integer> statusSet);
}
