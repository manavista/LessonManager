package jp.manavista.lessonmanager.activity;

import android.app.Activity;
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

import jp.manavista.lessonmanager.R;
import jp.manavista.lessonmanager.fragment.LessonViewFragment;
import jp.manavista.lessonmanager.injector.DependencyInjector;

/**
 *
 * LessonView Activity
 *
 * <p>
 * Overview:<br>
 * Define the screen action when the main lesson weekly days.
 * </p>
 *
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
    private LessonViewFragment lessonViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_view);

        DependencyInjector.appComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // preference setting
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false);

        lessonViewFragment = LessonViewFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_lesson_view_content, lessonViewFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        switch (id) {

            case R.id.item_goto_today:
                lessonViewFragment.goToday();
                break;
            case R.id.item_view_weekly:
                lessonViewFragment.changeVisibleDays(7);
                break;
            case R.id.item_view_daily:
                lessonViewFragment.changeVisibleDays(1);
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
