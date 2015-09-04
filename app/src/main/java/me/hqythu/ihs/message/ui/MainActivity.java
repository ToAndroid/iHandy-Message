package me.hqythu.ihs.message.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    private boolean mDrawerOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
//        initData();
        setDrawer();
//        setView();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(mToolbar);
        // TODO: i18n
        mCollapsingToolbar.setTitle("Tsinghua Now");
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
                mToolbar.setTitleTextColor((((int) Math.floor(0xff * (double) slideOffset)) << 24) | 0xffffff);
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
        mDrawerList.setAdapter(new DrawerListAdapter(this, mDrawerLayout));
    }
}
