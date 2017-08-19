package jp.manavista.lessonmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 *
 * Main activity
 *
 * <p>
 * Overview:<br>
 * Define it as the facade to the activity to display initially.
 * </p>
 */
public class MainActivity extends AppCompatActivity {

    /** Activity of this */
    final private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(activity, LessonViewActivity.class);
        activity.startActivity(intent);
    }

}
