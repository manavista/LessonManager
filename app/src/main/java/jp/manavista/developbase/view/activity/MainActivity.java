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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import jp.manavista.developbase.R;
import jp.manavista.developbase.util.DateUtil;
import jp.manavista.developbase.view.adapter.DailyFragmentStatePagerAdapter;
import jp.manavista.developbase.view.adapter.WeeklyFragmentStatePagerAdapter;
import jp.manavista.developbase.view.fragment.SettingFragment;

import static jp.manavista.developbase.view.adapter.WeeklyFragmentStatePagerAdapter.MAX_PAGE_NUM;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    final private Activity activity = this;
    private String viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.pager);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.viewMode = sharedPreferences.getString(SettingFragment.KEY_START_VIEW, "");

        setUpViewPagerAdapter();
        setUpViewPagerListener();
        setUpNavigationItem();

        Button nextButton = (Button) findViewById(R.id.btnNextWeek);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if ( id == R.id.nav_settings ) {
            Intent intent = new Intent(activity, SettingActivity.class);
            activity.startActivity(intent);
        } else if( id == R.id.nav_view_daily ) {
            this.viewMode = "Daily";
            setUpViewPagerAdapter();
            setUpNavigationItem();
            viewPager.setCurrentItem(MAX_PAGE_NUM/2);
        } else if( id == R.id.nav_view_weekly ) {
            this.viewMode = "Weekly";
            setUpViewPagerAdapter();
            setUpNavigationItem();
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
    protected void onPostResume() {
        super.onPostResume();
        viewPager.setCurrentItem(MAX_PAGE_NUM/2);
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

        FragmentStatePagerAdapter adapter;
        FragmentManager fm = getSupportFragmentManager();

        adapter = Objects.equals("Weekly", this.viewMode)
                ? new WeeklyFragmentStatePagerAdapter(fm)
                : new DailyFragmentStatePagerAdapter(fm);

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                final int diff = position - ( MAX_PAGE_NUM / 2 );
                TextView displayWeek = activity.findViewById(R.id.displayWeek);

                if( Objects.equals("Weekly", viewMode) ){

                    calendar.add(Calendar.WEEK_OF_YEAR, diff);
                    Calendar[] calendars = DateUtil.getWeekRangeOfMonth(calendar.getTime(), Calendar.MONDAY, Calendar.SATURDAY);

                    final String date = sdf.format(calendars[0].getTime()) + " - " + sdf.format(calendars[1].getTime());
                    displayWeek.setText(date);

                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, diff);
                    displayWeek.setText(sdf.format(calendar.getTime()));
                }
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
        });    }

    /**
     *
     * Setup Navigation Item
     *
     * <p>
     * Overview:<br>
     * Set up navigation menu item.<br>
     * Switch display to Daily and Weekly.
     * </p>
     */
    private void setUpNavigationItem() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_view_daily).setVisible(Objects.equals("Weekly", viewMode));
        menu.findItem(R.id.nav_view_weekly).setVisible(Objects.equals("Daily", viewMode));
    }
}
