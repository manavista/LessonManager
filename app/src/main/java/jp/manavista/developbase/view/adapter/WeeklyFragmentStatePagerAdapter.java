package jp.manavista.developbase.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.manavista.developbase.dto.MainActivityDto;
import jp.manavista.developbase.fragment.WeeklyFragment;

/**
 *
 * Weekly Fragment Pager Adapter
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 *
 */
public class WeeklyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_PAGE_NUM = 1000;

    private MainActivityDto dto;

    private final int[] calendarDay = {
            Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
            Calendar.TUESDAY, Calendar.FRIDAY, Calendar.SATURDAY};

    public WeeklyFragmentStatePagerAdapter(FragmentManager fm, MainActivityDto dto) {
        super(fm);
        this.dto = dto;
    }

    @Override
    public Fragment getItem(int position) {

        int diff = (position - (MAX_PAGE_NUM / 2));
        String[] displayDays = createDisplayDays(diff, this.dto);
        String[] displayDaysOfWeek = this.dto.getDisplayDaysOfWeek();
        return WeeklyFragment.newInstance(diff, displayDays, displayDaysOfWeek);
    }

    @Override
    public int getCount() {
        return MAX_PAGE_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "page " + position;
    }

    private String[] createDisplayDays(final int diff, MainActivityDto dto) {

        Calendar target = Calendar.getInstance(Locale.getDefault());
        target.add(Calendar.WEEK_OF_YEAR, diff);

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(target.getTime());

        final int index = dto.getDisplayFirstDay() < 1 ? 0 : dto.getDisplayFirstDay() - 1;
        cal.setFirstDayOfWeek(calendarDay[index]);

        SimpleDateFormat format = new SimpleDateFormat("dd",Locale.getDefault());

        List<String> dayList = new ArrayList<>();

        for( String day : dto.getDisplayDaysOfWeek() ) {
            cal.set(Calendar.DAY_OF_WEEK, Integer.valueOf(day));
            dayList.add(format.format(cal.getTime()));
        }

        return dayList.toArray(new String[0]);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
