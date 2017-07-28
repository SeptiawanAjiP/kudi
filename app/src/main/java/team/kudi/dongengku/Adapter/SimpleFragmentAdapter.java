package team.kudi.dongengku.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import team.kudi.dongengku.Fragment.FragmentKegiatanKudi;
import team.kudi.dongengku.Fragment.TestFragment;


/**
 * Created by user HD 4/8/2017.
 */

public class SimpleFragmentAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Tentang", "Kegiatan" };

    public SimpleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TestFragment();
        } else {
            return new FragmentKegiatanKudi();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
