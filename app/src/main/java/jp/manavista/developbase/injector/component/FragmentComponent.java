package jp.manavista.developbase.injector.component;

import jp.manavista.developbase.fragment.BaseFragment;
import jp.manavista.developbase.fragment.LessonViewFragment;
import jp.manavista.developbase.fragment.MemberFragment;
import jp.manavista.developbase.fragment.MemberLessonFragment;
import jp.manavista.developbase.fragment.MemberLessonListFragment;
import jp.manavista.developbase.fragment.MemberListFragment;
import jp.manavista.developbase.fragment.TimetableFragment;

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
 * @see jp.manavista.developbase.AppComponent Application Component interface
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
