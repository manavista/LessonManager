package jp.manavista.developbase.component;

import jp.manavista.developbase.view.fragment.BaseFragment;
import jp.manavista.developbase.view.fragment.TimetableFragment;
import jp.manavista.developbase.view.fragment.WeeklyFragment;

/**
 *
 * Fragment Component
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public interface FragmentComponent {
    void inject(BaseFragment baseFragment);
    void inject(WeeklyFragment weeklyFragment);
    void inject(TimetableFragment timetableFragment);
}
