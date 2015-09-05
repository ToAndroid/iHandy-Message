package com.ihs.demo.message;

import me.hqythu.ihs.message.ui.BaseActivity;
import com.ihs.message_2012010548.friends.api.HSContactFriendsMgr;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.ihs.message_2012010548.managers.HSMessageManager;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

public class MainActivity extends BaseActivity {

    private final static String TAG = MainActivity.class.getName();
    private PagerSlidingTabStrip tabs;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {
            getString(R.string.contacts),
            getString(R.string.messages),
            getString(R.string.settings),
            getString(R.string.sample)
        };

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int Index) {
            return fragments.get(Index);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihs_activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.ihs_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_green));
        mToolbar.setTitle("Message");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tabs = (PagerSlidingTabStrip) findViewById(R.id.ihs_tabs);
        mViewPager = (ViewPager) findViewById(R.id.ihs_viewpager);

        ContactsFragment contacts = new ContactsFragment();
        fragments.add(contacts);
        MessagesFragment messages = new MessagesFragment();
        fragments.add(messages);
        SettingsFragment settings = new SettingsFragment();
        fragments.add(settings);
        SampleFragment sample = new SampleFragment();
        fragments.add(sample);

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);

        tabs.setShouldExpand(true);
        tabs.setTabPaddingLeftRight(8);
        tabs.setViewPager(mViewPager);
        tabs.setBackgroundColor(getResources().getColor(R.color.primary_green));
        tabs.setIndicatorColor(getResources().getColor(R.color.primary_dark_green));
        tabs.setTextColor(getResources().getColor(R.color.WHITE));
        tabs.setTextSize((int) (16 * getResources().getDisplayMetrics().density));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ihs_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        HSMessageManager.getInstance().pullMessages();
        HSContactFriendsMgr.startSync(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
