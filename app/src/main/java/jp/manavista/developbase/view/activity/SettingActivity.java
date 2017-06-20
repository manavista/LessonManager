package jp.manavista.developbase.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import jp.manavista.developbase.view.fragment.SettingFragment;

/**
 *
 * Activity of Preferences Setting
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the setting menu is selected.
 * </p>
 * @see SettingFragment
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SettingFragment.newInstance())
                .commit();

    }
}
