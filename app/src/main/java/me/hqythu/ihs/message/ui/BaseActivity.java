package me.hqythu.ihs.message.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.ihs.app.framework.HSSessionMgr;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/4/2015.
 */

public class BaseActivity extends AppCompatActivity {

    static final float MAX_TOOLBAR_ELEVATION = 5;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(MAX_TOOLBAR_ELEVATION);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_gray_200)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        HSSessionMgr.onActivityCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HSSessionMgr.onActivityDestroy(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HSSessionMgr.onActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HSSessionMgr.onActivityStop(this, isBackPressed);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            isBackPressed = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ATTENTION!!! If you really have to override onBackPressed() in any subclass, and somehow you still want to exit
     * your activity on back key pressed, then remember to call super.onBackPressed() instead of simply calling
     * finish(); Otherwise, the Back key event cannot be recorded and handled by our HSActivity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
