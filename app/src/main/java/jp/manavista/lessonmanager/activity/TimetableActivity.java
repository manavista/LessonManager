package jp.manavista.lessonmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.addTimetable();
            }
        });

        fragment = TimetableFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_timetable, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
