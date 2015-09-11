package me.hqythu.ihs.message.ui;

import android.app.Activity;
import android.content.Intent;
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
    private Activity mActivity;

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

    public ChatListAdapter(Activity activity, ArrayList<HSBaseMessage> messages) {
        this.mActivity = activity;
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
        final HSBaseMessage message = messages.get(position);
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
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, ImageShowActivity.class);
                    intent.putExtra(ImageShowActivity.IMAGE_PATH, ((HSImageMessage) message).getNormalImageFilePath());
                    mActivity.startActivity(intent);
                }
            });
        } else {
            text = "Unsupported Yet";
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(text);
        }
        HSBaseMessage.HSMessageStatus status = message.getStatus();
        if (getItemViewType(position) == MESSAGE_OTHER_TYPE) {
            holder.status.setText("");
        } else {
            if (status == HSBaseMessage.HSMessageStatus.SENDING) {
                holder.status.setText(R.string.chat_status_sending);
            } else if (status == HSBaseMessage.HSMessageStatus.FAILED) {
                holder.status.setText(R.string.chat_status_failed);
            } else {
                holder.status.setText(R.string.chat_status_sent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
