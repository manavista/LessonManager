package jp.manavista.developbase.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import jp.manavista.developbase.view.fragment.DailyFragment;
import jp.manavista.developbase.view.fragment.WeeklyFragment;


public class DailyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_PAGE_NUM = 1000;

    public DailyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return MAX_PAGE_NUM;
    }

    @Override
    public Fragment getItem(int position) {

        int diff = (position - (MAX_PAGE_NUM / 2));

        Fragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        args.putInt(WeeklyFragment.CALENDAR_OFFSET_DAY_PROP, diff);
        fragment.setArguments(args);

        return fragment;
    }
}
