package jp.manavista.developbase.injector.component;

import jp.manavista.developbase.activity.LessonViewActivity;
import jp.manavista.developbase.activity.MainActivity;

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
 * @see jp.manavista.developbase.AppComponent Application Component interface
 */
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LessonViewActivity lessonViewActivity);
}
