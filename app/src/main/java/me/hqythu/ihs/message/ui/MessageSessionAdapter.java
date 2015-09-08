package me.hqythu.ihs.message.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.ihs.demo.message.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.db.SessionDBManager;

/**
 * Created by hqythu on 9/8/2015.
 */
public class MessageSessionAdapter
    extends RecyclerView.Adapter<MessageSessionAdapter.ViewHolder>
    implements SwipeableItemAdapter<MessageSessionAdapter.ViewHolder> {

    private ArrayList<SessionDBManager.MessageSessionInfo> sessionInfos;

    public class ViewHolder extends AbstractSwipeableItemViewHolder {
        View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

        @Override
        public View getSwipeableContainerView() {
            return mView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_session_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return sessionInfos.size();
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        int bgResId;
        if (sessionInfos.get(position).archived) {
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
    }
}
