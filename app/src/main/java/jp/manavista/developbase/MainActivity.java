package jp.manavista.developbase;

import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

import jp.manavista.developbase.entity.OrmaDatabase;
import jp.manavista.developbase.entity.Timetable;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DB_NAME = "main.db";

    private OrmaDatabase database;


    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = OrmaDatabase.builder(this)
                .name(DB_NAME)
//                .readOnMainThread(AccessThreadConstraint.NONE)
//                .writeOnMainThread(AccessThreadConstraint.NONE)
                .build();

        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                Timetable timetable = new Timetable();
                timetable.lessonNo = 1;
                timetable.startTime = new Time(cal.getTimeInMillis());
                timetable.endTime = new Time(cal.getTimeInMillis());
                database.insertIntoTimetable(timetable);

                for( Timetable row : database.selectFromTimetable() ) {
                    Log.d(TAG, "row id: " + row.id);
                    Log.d(TAG, "row start time: " + row.startTime);
                    Log.d(TAG, "row end time: " + row.endTime);
                }
            }
        });

        database.transactionNonExclusiveSync(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                Timetable timetable = new Timetable();
                timetable.lessonNo = 1;
                timetable.startTime = new Time(cal.getTimeInMillis());
                timetable.endTime = new Time(cal.getTimeInMillis());
                database.insertIntoTimetable(timetable);
            }
        });


        for( Timetable row : database.selectFromTimetable() ) {
            Log.d(TAG, "row id: " + row.id);
            Log.d(TAG, "row start time: " + row.startTime);
            Log.d(TAG, "row end time: " + row.endTime);
        }
        ((ThisApplication) getApplication()).getAppComponent().inject(this);

        String val = preferences.getString("start_view","null");

        Log.d("preference", val);
    }
}
