package com.bitmaster;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ExchangeFragment exchangeFragment = ExchangeFragment.getInstance();
    WebViewFragment webViewFragment = WebViewFragment.getInstance();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return exchangeFragment;
            case 1:
                return webViewFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
