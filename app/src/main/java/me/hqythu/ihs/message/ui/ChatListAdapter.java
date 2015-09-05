package me.hqythu.ihs.message.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihs.message_2012010548.types.HSTextMessage;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/6/2015.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<HSTextMessage> messages;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mItem;
        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
            mImageView = (ImageView) mItem.findViewById(R.id.chat_message_item_image);
            mTextView = (TextView) mItem.findViewById(R.id.chat_message_item_text);
        }
    }

    public ChatListAdapter(ArrayList<HSTextMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            item = inflater.inflate(R.layout.chat_message_item_left, parent, false);
        } else {
            item = inflater.inflate(R.layout.chat_message_item_right, parent, false);
        }
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HSTextMessage textMessage = messages.get(position);
        holder.mTextView.setText(textMessage.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
