package jp.manavista.lessonmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.MemberLessonListFragment;

/**
 *
 * MemberLesson list Activity
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
public class MemberLessonListActivity extends AppCompatActivity {

    /** Logger tag string */
    private static final String TAG = MemberLessonListActivity.class.getSimpleName();

    /** activity put extra argument: member id */
    public static final String EXTRA_MEMBER_ID = "MEMBER_ID";

    private long memberId;

    /** fragment */
    private MemberLessonListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Intent intent = getIntent();
        memberId = intent.getLongExtra(EXTRA_MEMBER_ID, 0);

        fragment = MemberLessonListFragment.newInstance(memberId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_member_lesson_list, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_member_lesson_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_add:
                Intent intent = new Intent(this, MemberLessonActivity.class);
                intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_ID, memberId);
                intent.putExtra(MemberLessonActivity.EXTRA_MEMBER_LESSON_ID, 0L);
                this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
