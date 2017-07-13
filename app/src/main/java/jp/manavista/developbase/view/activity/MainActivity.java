package jp.manavista.developbase.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import jp.manavista.developbase.ManavistaApplication;
import jp.manavista.developbase.R;
import jp.manavista.developbase.dto.MainActivityDto;
import jp.manavista.developbase.entity.Timetable;
import jp.manavista.developbase.injector.DependencyInjector;
import jp.manavista.developbase.service.TimetableService;
import jp.manavista.developbase.util.DateUtil;
import jp.manavista.developbase.view.adapter.DailyFragmentStatePagerAdapter;
import jp.manavista.developbase.view.adapter.WeeklyFragmentStatePagerAdapter;
import jp.manavista.developbase.view.fragment.SettingFragment;
import lombok.val;

import static jp.manavista.developbase.view.adapter.WeeklyFragmentStatePagerAdapter.MAX_PAGE_NUM;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    final private Activity activity = this;

    @Inject
    SharedPreferences preferences;
    @Inject
    TimetableService timetableService;

    /** preference data access object */
    private MainActivityDto dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DependencyInjector.appComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        Calendar cal1 = Calendar.getInstance();
        cal1.set(2017,7,1,20,0,0);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2017,7,1,21,20,0);

        Timetable timetable = new Timetable();
        timetable.lessonNo = 3;
        timetable.startTime = new Time(cal1.getTimeInMillis());
        timetable.endTime = new Time(cal2.getTimeInMillis());

//        timetableService.deleteAll();
//        timetableService.save(timetable);
//        timetableService.deleteById(14).subscribe();

        timetableService.getListAll().subscribe(new Consumer<Timetable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Timetable timetable) throws Exception {
                Log.d(TAG, "row id: " + timetable.id + " lessonNo: " +
                        timetable.lessonNo + " start: " + timetable.startTime + " end: " + timetable.endTime);
            }
        });


        dto = MainActivityDto.builder()
                .viewPager((ViewPager) findViewById(R.id.pager))
                .viewMode(preferences.getString(SettingFragment.KEY_START_VIEW, StringUtils.EMPTY))
                .viewPosition(MAX_PAGE_NUM / 2)
                .displayDaySet(preferences.getStringSet(SettingFragment.KEY_DISPLAY_DAY_OF_WEEK, null))
                .displayFirstDay(Integer.valueOf(preferences.getString(SettingFragment.KEY_FIRST_DAY_OF_WEEK, "0")))
                .build();

        setUpViewPagerAdapter();
        setUpViewPagerListener();
        updateNavigationItem();

        Button nextButton = (Button) findViewById(R.id.btnNextWeek);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val viewPager = dto.getViewPager();
                int position = viewPager.getCurrentItem();
                if( position < MAX_PAGE_NUM ) {
                    position++;
                }
                viewPager.setCurrentItem(position, true);
            }
        });

        Button previousButton = (Button) findViewById(R.id.btnPreviousWeek);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val viewPager = dto.getViewPager();
                int position = viewPager.getCurrentItem();
                if( position > 0 ) {
                    position--;
                }
                viewPager.setCurrentItem(position, true);
            }
        });


        // preference setting
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        final int id = item.getItemId();
        val viewPager = dto.getViewPager();

        if ( id == R.id.nav_settings ) {
            Intent intent = new Intent(activity, SettingActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_timetable ) {
            Intent intent = new Intent(activity, TimetableActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_view_daily ) {
            dto.setViewMode("Daily");
            setUpViewPagerAdapter();
            updateNavigationItem();
            viewPager.setCurrentItem(MAX_PAGE_NUM/2);
        } else if( id == R.id.nav_view_weekly ) {
            dto.setViewMode("Weekly");
            setUpViewPagerAdapter();
            updateNavigationItem();
            viewPager.setCurrentItem(MAX_PAGE_NUM/2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        val daySet = preferences.getStringSet(SettingFragment.KEY_DISPLAY_DAY_OF_WEEK, null);
        val firstDay = preferences.getString(SettingFragment.KEY_FIRST_DAY_OF_WEEK, String.valueOf(Calendar.SUNDAY));
        dto.setDisplayDaySet(daySet);
        dto.setDisplayFirstDay(Integer.valueOf(firstDay));

        // Update already created View.
        // Due to the possibility that the day of the week and the start date have been changed.
        dto.getViewPager().getAdapter().notifyDataSetChanged();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        val viewPager = dto.getViewPager();
        final int position = dto.getViewPosition();
        viewPager.setCurrentItem(position);

        updateDisplayWeek(position);
    }

    @Override
    protected void onPause() {
        super.onPause();

        val viewPager = dto.getViewPager();
        dto.setViewPosition(viewPager.getCurrentItem());
    }

    /**
     *
     * Setup Viewpager Adapter
     *
     * <p>
     * Overview:<br>
     * Setup ViewPager page adapter.<br>
     * When change view mode, use this method.
     * </p>
     */
    private void setUpViewPagerAdapter(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentStatePagerAdapter adapter = Objects.equals("Weekly", dto.getViewMode())
                ? new WeeklyFragmentStatePagerAdapter(fm, dto)
                : new DailyFragmentStatePagerAdapter(fm);

        val viewPager = dto.getViewPager();
        viewPager.setAdapter(adapter);
    }

    /**
     *
     * Setup ViewPager Listener
     *
     * <p>
     * Overview:<br>
     * Setup ViewPager page change listener.<br>
     * When change view mode, use this method.
     * </p>
     *
     */
    private void setUpViewPagerListener() {

        val viewPager = dto.getViewPager();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDisplayWeek(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    /**
     *
     * Update Display Week
     *
     * <p>
     * Overview:<br>
     * update day or between day to day label to screen.
     * </p>
     *
     * @param position viewPager page position
     */
    private void updateDisplayWeek(int position) {

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = DateUtil.DATE_FORMAT_YYYYMMDD;
        final int diff = position - ( MAX_PAGE_NUM / 2 );
        TextView displayWeek = activity.findViewById(R.id.displayWeek);

        if( Objects.equals("Weekly", dto.getViewMode()) ){

            calendar.add(Calendar.WEEK_OF_YEAR, diff);

            final int startDayOfWeek = dto.getStartDisplayDay();
            final int endDayOfWeek = dto.getEndDisplayDay();

            val pair = DateUtil.getWeekRange(calendar.getTime(), startDayOfWeek, endDayOfWeek);
            final String date = sdf.format(pair.first.getTime()) + " - " + sdf.format(pair.second.getTime());
            displayWeek.setText(date);

        } else {
            calendar.add(Calendar.DAY_OF_MONTH, diff);
            displayWeek.setText(sdf.format(calendar.getTime()));
        }
    }

    /**
     *
     * Update Navigation Item
     *
     * <p>
     * Overview:<br>
     * Update navigation menu item.<br>
     * Switch display to Daily and Weekly.
     * </p>
     */
    private void updateNavigationItem() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_view_daily).setVisible(Objects.equals("Weekly", dto.getViewMode()));
        menu.findItem(R.id.nav_view_weekly).setVisible(Objects.equals("Daily", dto.getViewMode()));
    }
}
