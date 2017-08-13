package jp.manavista.developbase.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import jp.manavista.developbase.R;
import jp.manavista.developbase.fragment.MemberLessonFragment;

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

    /** fragment */
    private MemberLessonFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_lesson);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fragment = MemberLessonFragment.newInstance(0);
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
            case R.id.option_add:
//                fragment.save();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
