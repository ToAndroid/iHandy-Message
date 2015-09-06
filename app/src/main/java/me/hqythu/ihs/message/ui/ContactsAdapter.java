package me.hqythu.ihs.message.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihs.demo.message.Contact;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

/**
 * Created by hqythu on 9/6/2015.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<Contact> contacts;
    private DisplayImageOptions options;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView avatar;
        TextView title;
        TextView detail;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            avatar = (ImageView) view.findViewById(R.id.contact_avatar);
            title = (TextView) view.findViewById(R.id.contact_title);
            detail = (TextView) view.findViewById(R.id.contact_detail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityMixin.startOtherActivity(mActivity, ChatActivity.class);
                }
            });
        }
    }

    public ContactsAdapter(Activity activity, ArrayList<Contact> contactsList) {
        mActivity = activity;
        contacts = contactsList;

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.chat_avatar_default_icon).showImageForEmptyUri(R.drawable.chat_avatar_default_icon)
            .showImageOnFail(R.drawable.chat_avatar_default_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        holder.title.setText("" + contact.getName() + ": " + contact.getContent());
        holder.detail.setText("mid: " + contact.getMid());
        ImageLoader.getInstance().displayImage("content://com.android.contacts/contacts/" + contact.getContactId(), holder.avatar, options);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
