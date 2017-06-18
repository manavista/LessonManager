package jp.manavista.developbase.view.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import jp.manavista.developbase.view.fragment.SettingFragment;

/**
 * Created by atsushi on 2017/06/18.
 */

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }
}
