package jp.manavista.lessonmanager.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.LessonViewFragment;

/**
 *
 * LessonView Activity
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the toolbar_lesson_view lesson weekly days.
 * </p>
 */
public class LessonViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** Logger tag string */
    private static final String TAG = LessonViewActivity.class.getSimpleName();
    /** Activity of this */
    final private Activity activity = this;
    /** drawer layout menu */
    private DrawerLayout drawer;
    /** Lesson View Fragment */
    private LessonViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_view);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // preference setting
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false);

        fragment = LessonViewFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_lesson_view_content, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_lesson_view, menu);

        // TODO: 2017/09/07 add shared preference default view
        // https://stackoverflow.com/questions/29122447/checkbox-item-state-on-menu-android

        MenuItem item = menu.findItem(R.id.item_view_3_days);
        item.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_change_date:

                final Calendar day = fragment.getLessonView().getFirstVisibleDay();
                new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthYear, int dayOfMonth) {
                        final Calendar display = Calendar.getInstance(Locale.getDefault());
                        display.set(year, monthYear, dayOfMonth);
                        fragment.getLessonView().goToDate(display);
                    }
                }, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.item_view_1_day:
                item.setChecked(!item.isChecked());
                fragment.changeVisibleDays(1);
                break;
            case R.id.item_view_3_days:
                item.setChecked(!item.isChecked());
                fragment.changeVisibleDays(3);
                break;
            case R.id.item_view_5_days:
                item.setChecked(!item.isChecked());
                fragment.changeVisibleDays(5);
                break;
            case R.id.item_view_7_days:
                item.setChecked(!item.isChecked());
                fragment.changeVisibleDays(7);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        final int id = item.getItemId();

        if( id == R.id.nav_member_list ) {
            Intent intent = new Intent(activity, MemberListActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_member_lesson_list ) {
            Intent intent = new Intent(activity, MemberLessonListActivity.class);
            activity.startActivity(intent);
        } else if ( id == R.id.nav_settings ) {
            Intent intent = new Intent(activity, SettingActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_timetable ) {
            Intent intent = new Intent(activity, TimetableActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_member) {
            Intent intent = new Intent(activity, MemberActivity.class);
            activity.startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if ( drawer.isDrawerOpen(GravityCompat.START) ) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
