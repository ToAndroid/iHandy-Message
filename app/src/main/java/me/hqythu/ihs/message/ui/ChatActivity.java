package me.hqythu.ihs.message.ui;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ihs.account.api.account.HSAccountManager;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.ihs.message_2012010548.types.HSBaseMessage;
import com.ihs.message_2012010548.types.HSTextMessage;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.event.MessageReceiveEvent;

public class ChatActivity extends BaseActivity {

    public final static String CHAT_MID = "ChatMid";

    private String mid;
    private Toolbar mToolbar;
    private RecyclerView mMessageView;
    private ChatListAdapter mAdapter;
    private ArrayList<HSBaseMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        mid = intent.getStringExtra(CHAT_MID);

        setToolbar();
        setData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
        HSMessageManager.QueryResult result = HSMessageManager.getInstance().queryMessages(mid, 0, -1);
        messages = new ArrayList<>(result.getMessages());
        mMessageView = (RecyclerView) findViewById(R.id.chat_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMessageView.setLayoutManager(layoutManager);
        mAdapter = new ChatListAdapter(messages);
        mMessageView.setAdapter(mAdapter);
    }

    public void onEvent(MessageReceiveEvent event) {
        int position = messages.size();
        int count = 0;
        for (HSBaseMessage message : event.getMessages()) {
            if (message.getFrom().equals(mid) ||
                message.getTo().equals(HSAccountManager.getInstance().getMainAccount().getMID())) {
                messages.add(message);
                count++;
            }
        }
        mAdapter.notifyItemRangeInserted(position, count);
        mMessageView.scrollToPosition(messages.size() - 1);
    }
}
