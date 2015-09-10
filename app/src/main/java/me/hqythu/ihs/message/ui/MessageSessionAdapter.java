package me.hqythu.ihs.message.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.ihs.demo.message.Contact;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.data.MessageSession;
import me.hqythu.ihs.message.data.MessageSessionType;
import me.hqythu.ihs.message.db.SessionDBManager;
import me.hqythu.ihs.message.event.SessionDeleteEvent;
import me.hqythu.ihs.message.event.SessionStatusChangeEvent;
import me.hqythu.ihs.message.event.SessionUpdateEvent;

/**
 * Created by hqythu on 9/8/2015.
 */
public class MessageSessionAdapter
    extends RecyclerView.Adapter<MessageSessionAdapter.ViewHolder>
    implements SwipeableItemAdapter<MessageSessionAdapter.ViewHolder> {

    private ArrayList<MessageSession> mSessionInfos;
    private Activity mActivity;
    private int type;
    private DisplayImageOptions options;
    private int lastSwipePosition;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");

    public class ViewHolder extends AbstractSwipeableItemViewHolder {
        View mView;
        View mContainer;
        ImageView avatar;
        TextView name;
        TextView detail;
        TextView lastTime;
        TextView unread;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContainer = mView.findViewById(R.id.message_session_container);
            avatar = (ImageView) mView.findViewById(R.id.session_contact_avatar);
            name = (TextView) mView.findViewById(R.id.session_contact_name);
            detail = (TextView) mView.findViewById(R.id.session_message_detail);
            lastTime = (TextView) mView.findViewById(R.id.session_last_time);
            unread = (TextView) mView.findViewById(R.id.session_unread_count);

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, ChatActivity.class);
                    intent.putExtra(ChatActivity.CHAT_MID, mSessionInfos.get(getAdapterPosition()).contact.getMid());
                    ActivityMixin.startOtherActivity(mActivity, intent, false);
                }
            });

            mContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int position = getAdapterPosition();
                    new MaterialDialog.Builder(mActivity)
                        .title("Caution!")
                        .content("Sure to delete?")
                        .positiveText("OK")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                MessageSession session = mSessionInfos.get(position);
                                String mid = session.contactMid;
                                HSMessageManager.getInstance().deleteMessages(mid);
                                SessionDBManager.removeSession(session.getSessionInfo());
                                EventBus.getDefault().post(new SessionDeleteEvent(session));
                            }
                        })
                        .show();
                    return false;
                }
            });
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public MessageSessionAdapter(Activity activity, ArrayList<MessageSession> sessionInfos, int type) {
        mActivity = activity;
        mSessionInfos = sessionInfos;
        this.type = type;

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.chat_avatar_default_icon).showImageForEmptyUri(R.drawable.chat_avatar_default_icon)
            .showImageOnFail(R.drawable.chat_avatar_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(mSessionInfos.get(position).contactMid);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_session_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MessageSession session = mSessionInfos.get(position);
        Contact contact = session.contact;
        if (contact == null) {
            return;
        }
        holder.name.setText(contact.getName());
        holder.detail.setText(session.messageBrief);
        holder.unread.setText(Integer.toString(session.unreadCount));
        holder.lastTime.setText(formatter.format(session.lastMessageDate));
        ImageLoader.getInstance().displayImage("content://com.android.contacts/contacts/" + contact.getContactId(), holder.avatar, options);
    }

    @Override
    public int getItemCount() {
        return mSessionInfos.size();
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        if (type == MessageSessionType.TYPE_ALL) {
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
        } else {
            return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
        }
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        int bgResId;
        switch (type) {
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.swipe_item_neutral;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                if (this.type == MessageSessionType.TYPE_ARCHIVED) {
                    bgResId = R.drawable.swipe_right_item_undone;
                } else {
                    bgResId = R.drawable.swipe_right_item_done;
                }
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.swipe_left_item_snooze;
                break;
            default:
                bgResId = R.drawable.swipe_item_neutral;
        }
        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public int onSwipeItem(ViewHolder holder, int position, int result) {
        return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
    }

    @Override
    public void onPerformAfterSwipeReaction(ViewHolder holder, final int position, int result, int reaction) {
        lastSwipePosition = position;
        final MessageSession session = mSessionInfos.get(position);
        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            if (result == RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT) {
                int messageResId;
                if (type == MessageSessionType.TYPE_ARCHIVED) {
                    messageResId = R.string.main_session_item_unarchived;
                } else {
                    messageResId = R.string.main_session_item_archived;
                }
                Snackbar.make((
                    (MainActivity)mActivity).getContainter(),
                    messageResId,
                    Snackbar.LENGTH_SHORT)
                    .setAction(R.string.main_session_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSessionInfos.add(position, session);
                            notifyItemInserted(position);
                        }
                    })
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            if (event != DISMISS_EVENT_ACTION) {
                                session.archived = !session.archived;
                                SessionDBManager.setArchived(session.contactMid, session.archived);
                                EventBus.getDefault().post(new SessionStatusChangeEvent(session, session.getType()));
                            }
                        }
                    }).show();
                mSessionInfos.remove(position);
                notifyItemRemoved(position);
            } else if (result == RecyclerViewSwipeManager.RESULT_SWIPED_LEFT) {

            }
        }
    }
}
