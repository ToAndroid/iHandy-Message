package me.hqythu.ihs.message.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ihs.account.api.account.HSAccountManager;
import com.ihs.commons.utils.HSError;
import com.ihs.demo.message.FriendManager;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.ihs.message_2012010548.types.HSBaseMessage;
import com.ihs.message_2012010548.types.HSImageMessage;
import com.ihs.message_2012010548.types.HSTextMessage;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.event.MessageAddEvent;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ChatActivity extends BaseActivity {

    public final static String CHAT_MID = "ChatMid";
    private static final int REQUEST_IMAGE = 2;

    private String mid;
    private Toolbar mToolbar;
    private RecyclerView mMessageView;
    private ChatListAdapter mAdapter;
    private ArrayList<HSBaseMessage> messages;

    private Button sendButton;
    private Button imageButton;
    private EditText sendText;
    private Button extendButton;
    private View extendArea;
    private boolean extend;

    private ArrayList<String> mSelectPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        mid = intent.getStringExtra(CHAT_MID);
        extend = false;

        setToolbar();
        setData();

        sendButton = (Button) findViewById(R.id.chat_button_send);
        imageButton = (Button) findViewById(R.id.chat_button_image);
        extendButton = (Button) findViewById(R.id.chat_extend_button);
        extendArea = findViewById(R.id.chat_extend_area);
        sendText = (EditText) findViewById(R.id.chat_text_send);
        sendText.clearFocus();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = sendText.getText().toString();
                sendText.setText("");
                HSBaseMessage message = new HSTextMessage(mid, text);
                // TODO 统一发送消息的回调
                HSMessageManager.getInstance().send(message, new HSMessageManager.SendMessageCallback() {
                    @Override
                    public void onMessageSentFinished(HSBaseMessage message, boolean success, HSError error) {
                        messages.add(0, message);
                        mAdapter.notifyItemInserted(0);
                        mMessageView.scrollToPosition(0);
                    }
                }, new Handler());
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                    true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                    9);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        extendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extend = !extend;
                if (extend) {
                    extendArea.setVisibility(View.VISIBLE);
                } else {
                    extendArea.setVisibility(View.GONE);
                }
            }
        });

        sendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    sendButton.setVisibility(View.VISIBLE);
                    extendButton.setVisibility(View.GONE);
                    extendArea.setVisibility(View.GONE);
                } else {
                    sendButton.setVisibility(View.GONE);
                    extendButton.setVisibility(View.VISIBLE);
                }
            }
        });

        sendText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    extendArea.setVisibility(View.GONE);
                }
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String path : mSelectPath) {
                    sendImage(path);
                }
            }
        }
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_blue));
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.gray_600));
        getSupportActionBar().setTitle(FriendManager.getInstance().getFriend(mid).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setData() {
        HSMessageManager.QueryResult result = HSMessageManager.getInstance().queryMessages(mid, 0, -1);
        messages = new ArrayList<>(result.getMessages());
        HSMessageManager.getInstance().markRead(messages);
        mMessageView = (RecyclerView) findViewById(R.id.chat_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mMessageView.setLayoutManager(layoutManager);
        mAdapter = new ChatListAdapter(messages);
        mMessageView.setAdapter(mAdapter);
    }

    public void sendImage(String imagePath) {
        HSBaseMessage message = new HSImageMessage(mid, imagePath);
        HSMessageManager.getInstance().send(message, new HSMessageManager.SendMessageCallback() {
            @Override
            public void onMessageSentFinished(HSBaseMessage message, boolean success, HSError error) {
                messages.add(0, message);
                mAdapter.notifyItemInserted(0);
                mMessageView.scrollToPosition(0);
            }
        }, new Handler());
    }

    public void onEvent(MessageAddEvent event) {
        int count = 0;
        for (HSBaseMessage message : event.getMessages()) {
            if (message.getFrom().equals(mid) ||
                message.getTo().equals(HSAccountManager.getInstance().getMainAccount().getMID())) {
                messages.add(0, message);
                count++;
            }
        }
        HSMessageManager.getInstance().markRead(messages);
        mAdapter.notifyItemRangeInserted(0, count);
        mMessageView.scrollToPosition(0);
    }
}
