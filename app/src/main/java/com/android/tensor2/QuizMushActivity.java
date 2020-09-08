package com.android.tensor2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class QuizMushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mush);

        // id가 vpPager인 ViewPager 객체를 얻어옴
        ViewPager vpPager = findViewById(R.id.vpPager);

        // ViewPager 객체에 앞서 정의한 PagerAdapter객체를 설정
        FragmentPagerAdapter adapterViewPager = new InfoPagerAdapter(getSupportFragmentManager());

        //adapterViewPager을 ViewPager객체에 연결
        vpPager.setAdapter(adapterViewPager);

        //ViewPager객체의 현재 페이지 설정(퀴즈 첫번째 info fragment)
        vpPager.setCurrentItem(0);

    }
}