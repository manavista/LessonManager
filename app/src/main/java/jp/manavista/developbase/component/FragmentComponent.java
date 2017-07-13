package jp.manavista.developbase.component;

import jp.manavista.developbase.view.fragment.BaseFragment;
import jp.manavista.developbase.view.fragment.WeeklyFragment;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public interface FragmentComponent {
    void inject(BaseFragment baseFragment);
    void inject(WeeklyFragment weeklyFragment);
}
