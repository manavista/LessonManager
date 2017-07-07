package jp.manavista.developbase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

import javax.inject.Inject;

import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.service.TimetableService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    SharedPreferences preferences;
    @Inject
    TimetableService timetableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ManavistaApplication) getApplication()).getAppComponent().inject(this);

        Calendar cal = Calendar.getInstance();
        Timetable timetable = new Timetable();
        timetable.lessonNo = 1;
        timetable.startTime = new Time(cal.getTimeInMillis());
        timetable.endTime = new Time(cal.getTimeInMillis());

//        timetableService.save(timetable);

        String val = preferences.getString("start_view","null");

        Log.d("preference", val);

        for( Timetable row : timetableService.getTimetablesAll() ) {
            Log.d(TAG, "row id: " + row.id);
            Log.d(TAG, "row lessonNo: " + row.lessonNo);
            Log.d(TAG, "row start time: " + row.startTime);
            Log.d(TAG, "row end time: " + row.endTime);
        }

    }
}
