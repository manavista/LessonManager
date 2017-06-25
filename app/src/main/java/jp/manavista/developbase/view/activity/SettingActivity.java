package jp.manavista.developbase.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jp.manavista.developbase.R;
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
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_setting, SettingFragment.newInstance())
                .commit();

    }
}
