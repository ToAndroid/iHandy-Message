package me.hqythu.ihs.message.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ihs.demo.message.MessagesFragment;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/4/2015.
 */

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private boolean mDrawerOpened = false;

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {
            getString(R.string.messages),
            getString(R.string.contacts),
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
        setContentView(R.layout.activity_main);

        setToolbar();
//        initData();
        setView();
        setDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(mToolbar);
        // TODO: i18n
        mCollapsingToolbar.setTitle("Tsinghua Now");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            mToolbar,
            R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setElevation(slideOffset * MAX_TOOLBAR_ELEVATION);
                } else if (Build.VERSION.SDK_INT >= 21) {
                    mToolbar.setElevation(slideOffset * MAX_TOOLBAR_ELEVATION);
                }
                mToolbar.setTitle(R.string.app_name);
                //TODO: also change toolbar background color/transparency
                if (slideOffset > 0) {
                    // TODO: disable recycler view
                } else if (slideOffset == 0) {
                    // TODO: activate recycler view
                }
            }

            //TODO: no menu when drawer open
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerOpened = true;
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerOpened = false;
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList = (RecyclerView) findViewById(R.id.drawer_list);
        mDrawerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mDrawerList.setAdapter(new DrawerListAdapter(this, mDrawerLayout, mViewPager));
    }

    private void setView() {
        mViewPager = (ViewPager) findViewById(R.id.main_activity_content);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        fragments.add(new MessagesFragment());
        fragments.add(new ContactsFragment());
    }

    public void setViewPager(int position) {
        mViewPager.setCurrentItem(0, true);
    }
}
