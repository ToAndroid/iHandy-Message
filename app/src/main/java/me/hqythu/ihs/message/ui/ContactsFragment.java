package me.hqythu.ihs.message.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihs.commons.notificationcenter.HSGlobalNotificationCenter;
import com.ihs.commons.notificationcenter.INotificationObserver;
import com.ihs.commons.utils.HSBundle;
import com.ihs.demo.message.Contact;
import com.ihs.demo.message.FriendManager;

import java.util.ArrayList;

import me.hqythu.ihs.message.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ArrayList<Contact> contacts;
    private ContactsAdapter mAdapter;
    private RecyclerView mContactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contacts, null);

        mContactList = (RecyclerView) view.findViewById(R.id.contacts_list);
        contacts = new ArrayList<>();
        mAdapter = new ContactsAdapter(getActivity(), contacts);
        mContactList.setAdapter(mAdapter);

        HSGlobalNotificationCenter.addObserver(FriendManager.NOTIFICATION_NAME_FRIEND_CHANGED,
            new INotificationObserver() {
                @Override
                public void onReceive(String s, HSBundle hsBundle) {
                    refreshContacts();
                }
            });
        refreshContacts();

        return view;
    }

    void refreshContacts() {
        contacts.clear();
        contacts.addAll(FriendManager.getInstance().getAllFriends());
        mAdapter.notifyDataSetChanged();
    }
}
