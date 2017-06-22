package jp.manavista.developbase.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import jp.manavista.developbase.view.fragment.DailyFragment;


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
        return DailyFragment.newInstance(diff);
    }
}
