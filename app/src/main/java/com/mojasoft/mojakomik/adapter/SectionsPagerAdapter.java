package com.mojasoft.mojakomik.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mojasoft.mojakomik.ui.ListGenreFragment;
import com.mojasoft.mojakomik.ui.ListManhuaFragment;
import com.mojasoft.mojakomik.ui.ListManhwaFragment;
import com.mojasoft.mojakomik.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_genre,
            R.string.tab_manhwa,
            R.string.tab_manhua
    };
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ListGenreFragment();
                break;
            case 1:
                fragment = new ListManhwaFragment();
                break;
            case 2:
                fragment = new ListManhuaFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}

