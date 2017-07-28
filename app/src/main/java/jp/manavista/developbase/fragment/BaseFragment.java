package jp.manavista.developbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import jp.manavista.developbase.injector.DependencyInjector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DependencyInjector.appComponent().inject(this);
    }
}
