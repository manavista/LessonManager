package jp.manavista.lessonmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    private TimetableFragment timetableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timetableFragment = TimetableFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_timetable, timetableFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_add:
                timetableFragment.addTimetable();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
