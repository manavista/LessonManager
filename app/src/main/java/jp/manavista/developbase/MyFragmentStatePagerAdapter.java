package jp.manavista.developbase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by atsushi on 2017/06/13.
 */

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
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
        return "Page" + position;
    }
}
