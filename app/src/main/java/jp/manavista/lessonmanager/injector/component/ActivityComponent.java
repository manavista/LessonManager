package jp.manavista.lessonmanager.injector.component;

import jp.manavista.lessonmanager.activity.LessonViewActivity;
import jp.manavista.lessonmanager.activity.MainActivity;

/**
 *
 * Activity Component
 *
 * <p>
 * Overview:<br>
 * Combine objects and modules related to Activity.<br>
 * This interface is inherited by AppComponent.
 * </p>
 *
 * @see jp.manavista.lessonmanager.AppComponent Application Component interface
 */
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LessonViewActivity lessonViewActivity);
}
