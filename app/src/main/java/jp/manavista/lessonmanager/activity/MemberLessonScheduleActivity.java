package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberLessonScheduleFragment;

public class MemberLessonScheduleActivity extends AppCompatActivity {

    /** activity put extra argument: member id */
    public static final String EXTRA_SCHEDULE_ID = "SCHEDULE_ID";

    private long scheduleId;

    /** fragment */
    private MemberLessonScheduleFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson_schedule);

        final Intent intent = getIntent();
        scheduleId = intent.getLongExtra(EXTRA_SCHEDULE_ID, 0);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final Activity activity = this;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fragment = MemberLessonScheduleFragment.newInstance(scheduleId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_lesson_schedule, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member_lesson_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_save:
                fragment.save();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
