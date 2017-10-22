/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.constants.analytics.ContentType;
import jp.manavista.lessonmanager.constants.analytics.Event;
import jp.manavista.lessonmanager.fragment.MemberLessonScheduleListFragment;
import lombok.val;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CONTENT_TYPE;
import static jp.manavista.lessonmanager.activity.MemberLessonActivity.Extra.MEMBER_ID;
import static jp.manavista.lessonmanager.activity.MemberLessonActivity.Extra.MEMBER_LESSON_ID;
import static jp.manavista.lessonmanager.activity.SettingActivity.FragmentType.LESSON_AND_SCHEDULE;

/**
 *
 * MemberLessonSchedule list Activity
 *
 * <p>
 * Overview:<br>
 * Define the screen action of the Lesson Schedule List.
 * </p>
 */
public class MemberLessonScheduleListActivity extends AppCompatActivity {

    // TODO: 2017/10/22 create Extra class as MemberLessonActivity

    /** activity put extra argument: lesson id */
    public static final String EXTRA_LESSON_ID = "LESSON_ID";
    /** activity put extra argument: member id */
    public static final String EXTRA_MEMBER_ID = "MEMBER_ID";
    /** activity put extra argument: member name */
    public static final String EXTRA_MEMBER_NAME = "MEMBER_NAME";

    private long memberId;
    private long lessonId;

    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson_schedule_list);

        analytics = FirebaseAnalytics.getInstance(this);

        final Intent intent = getIntent();
        memberId = intent.getLongExtra(EXTRA_MEMBER_ID, 0);
        lessonId = intent.getLongExtra(EXTRA_LESSON_ID, 0);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra(EXTRA_MEMBER_NAME));
        setSupportActionBar(toolbar);

        final Activity activity = this;

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(activity, MemberLessonActivity.class);
                intent.putExtra(MEMBER_ID, memberId);
                intent.putExtra(MEMBER_LESSON_ID, lessonId);
                startActivityForResult(intent, MemberLessonActivity.RequestCode.CREATE);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        val fragment = MemberLessonScheduleListFragment.newInstance(memberId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_lesson_schedule_list, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member_lesson_schedule_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_setting:
                final Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra(SettingActivity.EXTRA_FRAGMENT_TYPE, LESSON_AND_SCHEDULE);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == MemberLessonActivity.RequestCode.CREATE && resultCode == RESULT_OK ) {

            final String name = data.getStringExtra(MemberLessonActivity.Extra.LESSON_NAME);
            final String message = getString(R.string.message_member_lesson_list_add_lesson, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Lesson.label());
            analytics.logEvent(Event.Add.label(), bundle);

        } else if( requestCode == MemberLessonActivity.RequestCode.EDIT && resultCode == RESULT_OK ) {

            final String name = data.getStringExtra(MemberLessonActivity.Extra.LESSON_NAME);
            final String message = getString(R.string.message_member_lesson_list_edit_lesson, name);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            final Bundle bundle = new Bundle();
            bundle.putString(CONTENT_TYPE, ContentType.Lesson.label());
            analytics.logEvent(Event.Edit.label(), bundle);
        }

    }
}
