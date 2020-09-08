package com.android.tensor2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.tensor2.info.FifthInfoFragment;
import com.android.tensor2.info.FirstInfoFragment;
import com.android.tensor2.info.FourthInfoFragment;
import com.android.tensor2.info.SecondInfoFragment;
import com.android.tensor2.info.ThirdInfoFragment;

public class InfoPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS=5;

    public InfoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // Quiz 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //First Info fragment
                FirstInfoFragment first = new FirstInfoFragment();
                return first;
            case 1:
                //Second Info fragment
                SecondInfoFragment second = new SecondInfoFragment();
                return second;
            case 2:
                //Third Info fragment
                ThirdInfoFragment third = new ThirdInfoFragment();
                return third;
            case 3:
                //Fourth Info fragment
                FourthInfoFragment fourth = new FourthInfoFragment();
                return fourth;
            case 4:
                //Fifth Info fragment
                FifthInfoFragment fifth = new FifthInfoFragment();
                return fifth;

            default:
                return null;
        }
    }

    // 전체 페이지 개수 반환
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
