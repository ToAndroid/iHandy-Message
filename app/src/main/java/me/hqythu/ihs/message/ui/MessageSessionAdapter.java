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

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.ihs.demo.message.Contact;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.data.MessageSession;
import me.hqythu.ihs.message.db.SessionDBManager;

/**
 * Created by hqythu on 9/8/2015.
 */
public class MessageSessionAdapter
    extends RecyclerView.Adapter<MessageSessionAdapter.ViewHolder>
    implements SwipeableItemAdapter<MessageSessionAdapter.ViewHolder> {

    private ArrayList<MessageSession> mSessionInfos;
    private Activity mActivity;
    private DisplayImageOptions options;

    public class ViewHolder extends AbstractSwipeableItemViewHolder {
        View mView;
        ImageView avatar;
        TextView title;
        TextView detail;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            avatar = (ImageView) mView.findViewById(R.id.session_contact_avatar);
            title = (TextView) mView.findViewById(R.id.session_contact_title);
            detail = (TextView) mView.findViewById(R.id.session_contact_detail);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, ChatActivity.class);
                    intent.putExtra(ChatActivity.CHAT_MID, mSessionInfos.get(getAdapterPosition()).contact.getMid());
                    ActivityMixin.startOtherActivity(mActivity, intent, false);
                }
            });
        }

        @Override
        public View getSwipeableContainerView() {
            return mView;
        }
    }

    public MessageSessionAdapter(Activity activity, ArrayList<MessageSession> sessionInfos) {
        mActivity = activity;
        mSessionInfos = sessionInfos;

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.chat_avatar_default_icon).showImageForEmptyUri(R.drawable.chat_avatar_default_icon)
            .showImageOnFail(R.drawable.chat_avatar_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_session_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Contact contact = mSessionInfos.get(position).contact;
        if (contact == null) {
            return;
        }
        holder.title.setText("" + contact.getName() + ": " + contact.getContent());
        holder.detail.setText("mid: " + contact.getMid());
        ImageLoader.getInstance().displayImage("content://com.android.contacts/contacts/" + contact.getContactId(), holder.avatar, options);

    }

    @Override
    public int getItemCount() {
        return mSessionInfos.size();
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        int bgResId;
        if (mSessionInfos.get(position).archived) {
            bgResId = R.drawable.swipe_right_item_undone;
        } else {
            bgResId = R.drawable.swipe_left_item_done;
        }
        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public int onSwipeItem(ViewHolder holder, int position, int result) {
        return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
    }

    @Override
    public void onPerformAfterSwipeReaction(ViewHolder holder, int position, int result, int reaction) {
        MessageSession session = mSessionInfos.get(position);
        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            session.archived = !session.archived;
            SessionDBManager.setArchived(session.contactMid, session.archived);
            mSessionInfos.remove(position);
            notifyItemRemoved(position);
        }
    }
}
