package com.example.yuzhong.myruns3;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yuzhong.myruns3.view.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivityWithTab extends Activity {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private TabsAdapter mTabsAdapter;
    private HistoryFragment historyFragment;

    public HistoryFragment getHistoryFragment(){ return historyFragment; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_with_tab);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.three_tabs);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        historyFragment = new HistoryFragment();
        mFragments = new ArrayList<>();
        mFragments.add(new StartFragment());
        mFragments.add(historyFragment);
        mFragments.add(new SettingsFragment());

        mTabsAdapter = new TabsAdapter(getFragmentManager(), mFragments);
        mViewPager.setAdapter(mTabsAdapter);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    HistoryFragment fragment = (HistoryFragment) mFragments.get(position);
                    fragment.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
