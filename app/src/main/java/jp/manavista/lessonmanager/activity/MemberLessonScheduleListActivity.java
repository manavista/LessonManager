package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberLessonScheduleListFragment;

/**
 *
 * MemberLessonSchedule list Activity
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonScheduleListActivity extends AppCompatActivity {

    /** activity put extra argument: lesson id */
    public static final String EXTRA_LESSON_ID = "LESSON_ID";

    private long memberId;
    private long lessonId;

    /** fragment */
    private MemberLessonScheduleListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson_schedule_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();
        lessonId = intent.getLongExtra(EXTRA_LESSON_ID, 0);

        fragment = MemberLessonScheduleListFragment.newInstance(lessonId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_lesson_schedule_list, fragment)
                .commit();
    }
}
