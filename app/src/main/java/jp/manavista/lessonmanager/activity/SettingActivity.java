package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.SettingDeleteFragment;
import jp.manavista.lessonmanager.fragment.SettingEventFragment;
import jp.manavista.lessonmanager.fragment.SettingFragment;
import jp.manavista.lessonmanager.fragment.SettingLessonAndScheduleFragment;

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

        // TODO: 2017/09/21 use string xml

        GENERAL("Settings"),
        EVENT("Event"),
        DELETE("Delete using data"),
        LESSON_AND_SCHEDULE("Lesson and Schedule");

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
            case EVENT:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_setting, SettingEventFragment.newInstance())
                        .commit();
                break;
            case LESSON_AND_SCHEDULE:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_setting, SettingLessonAndScheduleFragment.newInstance())
                        .commit();
                break;
        }

    }
}
