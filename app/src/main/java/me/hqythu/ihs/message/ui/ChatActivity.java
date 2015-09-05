package me.hqythu.ihs.message.ui;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ihs.message_2012010548.types.HSTextMessage;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

public class ChatActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mMessageView;

    private ArrayList<HSTextMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setToolbar();
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_blue));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setData() {
        messages = new ArrayList<>();
        messages.add(new HSTextMessage("12", "hello"));
        messages.add(new HSTextMessage("13", "I'm fine"));
        messages.add(new HSTextMessage("12", "how are you"));
        messages.add(new HSTextMessage("13", "not bad"));
        messages.add(new HSTextMessage("12", "aaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbassssdfjioasoiejfdzxdf"));
        messages.add(new HSTextMessage("13", "aaaaajoiupjadsipofaabbbbbbbbbbbbbbbbbassssdfjioasoiejfdzxdf"));
        mMessageView = (RecyclerView) findViewById(R.id.chat_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMessageView.setLayoutManager(layoutManager);
        mMessageView.setAdapter(new ChatListAdapter(messages));
    }
}
