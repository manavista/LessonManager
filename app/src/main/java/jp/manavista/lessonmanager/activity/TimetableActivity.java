/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.TimetableFragment;

/**
 *
 * Activity of Timetable
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the Timetable menu is selected.
 * </p>
 */
public class TimetableActivity extends AppCompatActivity {

    /** Timetable Fragment */
    private TimetableFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> finish());

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> fragment.addTimetable());

        fragment = TimetableFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_timetable, fragment)
                .commit();
    }

}
