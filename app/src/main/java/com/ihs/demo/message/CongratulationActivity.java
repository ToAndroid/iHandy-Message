package com.ihs.demo.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ihs.app.framework.activity.HSActivity;
import me.hqythu.ihs.message.R;

public class CongratulationActivity extends HSActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihs_activity_congratulation);
        Button btPhone = (Button) findViewById(R.id.button1);
        btPhone.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ihs_congratulation, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, me.hqythu.ihs.message.ui.MainActivity.class);
        startActivity(intent);
        finish();
    }
}
