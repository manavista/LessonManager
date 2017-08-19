package jp.manavista.lessonmanager.injector.component;

import jp.manavista.lessonmanager.fragment.BaseFragment;
import jp.manavista.lessonmanager.fragment.LessonViewFragment;
import jp.manavista.lessonmanager.fragment.MemberFragment;
import jp.manavista.lessonmanager.fragment.MemberLessonFragment;
import jp.manavista.lessonmanager.fragment.MemberLessonListFragment;
import jp.manavista.lessonmanager.fragment.MemberListFragment;
import jp.manavista.lessonmanager.fragment.TimetableFragment;

/**
 *
 * Fragment Component
 *
 * <p>
 * Overview:<br>
 * Combine objects and modules related to Fragment.<br>
 * This interface is inherited by AppComponent.
 * </p>
 *
 * @see jp.manavista.lessonmanager.AppComponent Application Component interface
 */
public interface FragmentComponent {
    void inject(BaseFragment baseFragment);
    void inject(TimetableFragment timetableFragment);
    void inject(MemberFragment memberFragment);
    void inject(MemberListFragment memberListFragment);
    void inject(MemberLessonFragment memberLessonFragment);
    void inject(MemberLessonListFragment memberLessonListFragment);
    void inject(LessonViewFragment lessonViewFragment);
}
