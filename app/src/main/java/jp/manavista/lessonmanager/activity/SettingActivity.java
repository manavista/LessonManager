/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
     * Define fragment type and title string resource.
     * </p>
     */
    public enum FragmentType {

        GENERAL(R.string.title_preference_general_toolbar),
        EVENT(R.string.title_preference_event_toolbar),
        DELETE(R.string.title_preference_delete_toolbar),
        LESSON_AND_SCHEDULE(R.string.title_preference_lesson_and_schedule_toolbar);

        final int resourceId;
        FragmentType(int resourceId) {
            this.resourceId = resourceId;
        }

        public int getResourceId() {
            return resourceId;
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
        toolbar.setTitle(getApplicationContext().getString(type.getResourceId()));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> finish());

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
