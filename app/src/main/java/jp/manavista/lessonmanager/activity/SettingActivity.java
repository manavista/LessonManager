package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.SettingDeleteFragment;
import jp.manavista.lessonmanager.fragment.SettingFragment;

/**
 *
 * Activity of Preferences Setting
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the setting menu is selected.
 * </p>
 *
 * @see SettingFragment Setting Fragment
 */
public class SettingActivity extends AppCompatActivity {

    public static final String EXTRA_FRAGMENT_TYPE = "FRAGMENT_TYPE";

    /**
     *
     * Fragment Type
     *
     * <p>
     * overview:<br>
     *
     * </p>
     */
    public enum FragmentType {

        GENERAL("Settings"),
        DELETE("Delete using data");

        String title;
        FragmentType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        final Intent intent = getIntent();
        FragmentType type = (FragmentType) intent.getSerializableExtra(EXTRA_FRAGMENT_TYPE);

        if( type == null ) {
            type = FragmentType.GENERAL;
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(type.getTitle());
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switch (type) {
            case GENERAL:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_setting, SettingFragment.newInstance())
                        .commit();
                break;
            case DELETE:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_setting, SettingDeleteFragment.newInstance())
                        .commit();
                break;
        }

    }
}
