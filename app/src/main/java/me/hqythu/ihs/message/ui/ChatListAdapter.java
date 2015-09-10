package me.hqythu.ihs.message.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihs.account.api.account.HSAccountManager;
import com.ihs.message_2012010548.types.HSBaseMessage;
import com.ihs.message_2012010548.types.HSImageMessage;
import com.ihs.message_2012010548.types.HSTextMessage;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/6/2015.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<HSBaseMessage> messages;

    private final int MESSAGE_ME_TYPE = 1;
    private final int MESSAGE_OTHER_TYPE = 2;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mItem;
        ImageView mImageView;
        ViewGroup mContentView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
            mImageView = (ImageView) mItem.findViewById(R.id.chat_message_item_image);
            mContentView = (ViewGroup) mItem.findViewById(R.id.chat_message_item_content);
        }
    }

    public ChatListAdapter(ArrayList<HSBaseMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        HSBaseMessage message = messages.get(position);
        if (message.getFrom().equals(HSAccountManager.getInstance().getMainAccount().getMID())) {
            return MESSAGE_ME_TYPE;
        } else {
            return MESSAGE_OTHER_TYPE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MESSAGE_OTHER_TYPE) {
            item = inflater.inflate(R.layout.chat_message_item_left, parent, false);
        } else {
            item = inflater.inflate(R.layout.chat_message_item_right, parent, false);
        }
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HSBaseMessage message = messages.get(position);
        String text;
        View mContent;
        if (message instanceof HSTextMessage) {
            text = ((HSTextMessage) message).getText();
            mContent = new TextView(holder.mItem.getContext());
            ((TextView)mContent).setText(text);

        } else if (message instanceof HSImageMessage) {
            mContent = new ImageView(holder.mItem.getContext());
            Bitmap bitmap = BitmapFactory.decodeFile(((HSImageMessage) message).getNormalImageFilePath());
                ((ImageView) mContent).setImageBitmap(bitmap);
        } else {
            text = "Unsupported Yet";
            mContent = new TextView(holder.mItem.getContext());
            ((TextView)mContent).setText(text);
        }
        holder.mContentView.removeAllViews();
        holder.mContentView.addView(mContent);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
