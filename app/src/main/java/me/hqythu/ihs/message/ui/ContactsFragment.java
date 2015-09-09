package me.hqythu.ihs.message.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.event.FriendUpdateEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ArrayList<Contact> contacts;
    private ContactsAdapter mAdapter;
    private RecyclerView mContactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contacts, null);

        mContactList = (RecyclerView) view.findViewById(R.id.contacts_list);
        mContactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        contacts = new ArrayList<>();
        mAdapter = new ContactsAdapter(getActivity(), contacts);
        mContactList.setAdapter(mAdapter);

        refreshContacts();

        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(FriendUpdateEvent event) {
        refreshContacts();
    }

    void refreshContacts() {
        contacts.clear();
        contacts.addAll(FriendManager.getInstance().getAllFriends());
        mAdapter.notifyDataSetChanged();
    }
}
