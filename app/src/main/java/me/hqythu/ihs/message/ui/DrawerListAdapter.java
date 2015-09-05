package me.hqythu.ihs.message.ui;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/4/2015.
 */

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    enum DrawerListItem {
        ORIGIN_ACTIVITY(
            R.string.actionbar_menu_origin,
            0,
            R.color.gray_600),
        CHAT_ACTIVITY(
            R.string.actionbar_menu_chat_activity,
            0,
            R.color.gray_600),
//        CURRICULUM(
//            R.string.actionbar_menu_curriculum,
//            R.mipmap.ic_assignment_white_24dp,
//            R.color.deep_purple_300),
//        GPA_CALCULATOR(
//            R.string.actionbar_menu_gpacal,
//            R.mipmap.ic_trending_up_white_24dp,
//            R.color.red_300),
//        SETTINGS(
//            R.string.actionbar_menu_settings,
//            R.mipmap.ic_settings_white_24dp,
//            R.color.gray_600),
//        HELP(
//            R.string.actionbar_menu_help,
//            R.mipmap.ic_help_white_24dp,
//            R.color.gray_600),
//        FEEDBACK(
//            R.string.actionbar_menu_feedback,
//            R.mipmap.ic_chat_white_24dp,
//            R.color.gray_600),
//        UPDATE(
//            R.string.actionbar_menu_update,
//            R.mipmap.ic_play_download_white_24dp,
//            R.color.gray_600),
//        DEBUG(
//            R.string.actionbar_menu_debug,
//            R.mipmap.ic_bug_report_white_24dp,
//            R.color.gray_600),
        ;

        int textId;
        int iconId;
        int colorId;

        DrawerListItem(int textId, int iconId, int colorId) {
            this.textId = textId;
            this.iconId = iconId;
            this.colorId = colorId;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mItem;
        ImageView mIcon;
        TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
            mIcon = (ImageView) itemView.findViewById(R.id.drawer_list_icon);
            mTitle = (TextView) itemView.findViewById(R.id.drawer_list_title);
        }

        protected void bind(final DrawerListItem item, final Activity activity) {
            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.closeDrawers();
                    switch (item) {
                        case ORIGIN_ACTIVITY:
                            ActivityMixin.startOtherActivity(mActivity, com.ihs.demo.message.MainActivity.class);
                            break;
                        case CHAT_ACTIVITY:
                            ActivityMixin.startOtherActivity(mActivity, ChatActivity.class);
                            break;
//                        case SETTINGS:
//                            break;
//                        case HELP:
//                            break;
//                        case FEEDBACK:
//                            break;
//                        case UPDATE:
//                            break;
//                        case LEARN_HELPER:
//                            ActivityMixin.startOtherActivity(activity, LearnHelperActivity.class);
//                            break;
//                        case CURRICULUM:
//                            ActivityMixin.startOtherActivity(activity, CurriculumActivity.class);
//                            break;
//                        case GPA_CALCULATOR:
//                            ActivityMixin.startOtherActivity(activity, GpaCalculatorActivity.class);
//                            break;
//                        case DEBUG:
//                            break;
                        default:
                            break;
                    }
                }
            });
            mIcon.setImageResource(item.iconId);
            mIcon.setColorFilter(activity.getResources().getColor(item.colorId));
            mTitle.setText(item.textId);
            mTitle.setTextColor(activity.getResources().getColor(R.color.BLACK));
        }
    }

    private Activity mActivity;
    private DrawerLayout mDrawerLayout;

    public DrawerListAdapter(Activity activity, DrawerLayout drawerLayout) {
        super();
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_button, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DrawerListItem item = DrawerListItem.values()[position];
        holder.bind(item, mActivity);
    }

    @Override
    public int getItemCount() {
        return DrawerListItem.values().length;
    }
}
