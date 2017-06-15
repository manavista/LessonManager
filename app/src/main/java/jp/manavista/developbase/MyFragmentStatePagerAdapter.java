package jp.manavista.developbase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by atsushi on 2017/06/13.
 */

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Calendar calendar;

    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentStatePagerAdapter(FragmentManager fm, Calendar calendar) {
        super(fm);
        this.calendar = calendar;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
//            case 0:
//                return new Fragment0();
//            case 1:
//                return new Fragment1();
            default:
//                return new Fragment2();
                Fragment fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.SECTION_NUMBER_PROP, position + 1);
                fragment.setArguments(args);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        calendar.add(Calendar.DAY_OF_MONTH, position);
        return sdf.format(calendar.getTime());
    }
}
