package jp.manavista.developbase.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.manavista.developbase.R;
import jp.manavista.developbase.view.fragment.TimetableFragment;


/**
 *
 * Activity of Timetable
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the Timetable menu is selected.
 * </p>
 *
 */
public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_timetable, TimetableFragment.newInstance())
                .commit();
    }
}
