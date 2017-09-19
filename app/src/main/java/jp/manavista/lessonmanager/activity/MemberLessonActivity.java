package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberLessonFragment;

/**
 *
 * MemberLesson Activity
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonActivity extends AppCompatActivity {

    /** activity put extra argument: member id */
    public static final String EXTRA_MEMBER_ID = "MEMBER_ID";
    /** activity put extra argument: member lesson id */
    public static final String EXTRA_MEMBER_LESSON_ID = "MEMBER_LESSON_ID";

    public class Extra {
        public static final String MEMBER_ID = "MEMBER_ID";
        public static final String MEMBER_LESSON_ID = "MEMBER_LESSON_ID";
        public static final String LESSON_NAME = "LESSON_NAME";
    }

    public class RequestCode {
        private final static int _BASE = 400;
        public final static int CREATE = 1 + _BASE;
        public final static int EDIT = 3 + _BASE;
    }

    /** fragment */
    private MemberLessonFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();
        final long memberId = intent.getLongExtra(EXTRA_MEMBER_ID, 0);
        final long memberLessonId = intent.getLongExtra(EXTRA_MEMBER_LESSON_ID, 0);

        fragment = MemberLessonFragment.newInstance(memberId, memberLessonId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_lesson, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member_lesson, menu);
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
