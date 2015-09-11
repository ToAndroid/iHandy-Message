package me.hqythu.ihs.message.ui;

import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;
import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by hqythu on 9/6/2015.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<HSBaseMessage> messages;

    private DisplayImageOptions options;

    private final int MESSAGE_ME_TYPE = 1;
    private final int MESSAGE_OTHER_TYPE = 2;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mItem;
        ImageView mImageViewAvatar;
        TextView mTextView;
        ImageView mImageView;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
            mImageViewAvatar = (ImageView) mItem.findViewById(R.id.chat_message_item_avatar);
            mTextView = (TextView) mItem.findViewById(R.id.chat_message_item_text);
            mImageView = (ImageView) mItem.findViewById(R.id.chat_message_item_image);
            status = (TextView) mItem.findViewById(R.id.chat_message_state);
        }
    }

    public ChatListAdapter(ArrayList<HSBaseMessage> messages) {
        this.messages = messages;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.chat_avatar_default_icon).showImageForEmptyUri(R.drawable.chat_avatar_default_icon)
            .showImageOnFail(R.drawable.chat_avatar_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

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
        holder.mTextView.setVisibility(View.GONE);
        holder.mImageView.setVisibility(View.GONE);
        if (message instanceof HSTextMessage) {
            text = ((HSTextMessage) message).getText();
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(text);

        } else if (message instanceof HSImageMessage) {
            holder.mImageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                "file://" + ((HSImageMessage) message).getNormalImageFilePath(), holder.mImageView, options);
        } else {
            text = "Unsupported Yet";
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(text);
        }
        if (message.getStatus() == HSBaseMessage.HSMessageStatus.FAILED) {
            holder.status.setText(R.string.chat_status_failed);
        } else if (message.getStatus() == HSBaseMessage.HSMessageStatus.SENDING) {
            holder.status.setText(R.string.chat_status_sending);
        } else if (message.getStatus() == HSBaseMessage.HSMessageStatus.SENT) {
            holder.status.setText(R.string.chat_status_sent);
        } else {
            holder.status.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
