package google.com.convenebackend.fragments;

/**
 * Created by Lydia on 17/12/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import google.com.convenebackend.MainApp.NotificationItem;
import google.com.convenebackend.MainApp.UserUtils;
import google.com.convenebackend.MainApp.myAdapter;
import google.com.convenebackend.R;


public class ThreeFragment extends Fragment {

    private static UserUtils utils;
    public static ArrayList<Object> notListArray = utils.getNotList();
    public static myAdapter newadapter;
    private ListView notificationList;


    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        notificationList = (ListView) view.findViewById(R.id.notifications);

        if(notListArray!=null){notListArray.clear();}
        //NOTE: simple list item 1 = Android predefined TextView resource id
        //adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friendListArray);

        newadapter = new myAdapter(getContext(),
                R.layout.list_row_not, utils.getNotList());

        notificationList.setAdapter(newadapter);

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                int friendListID = (int) id;
                newadapter.setSelectedIndex(position);
                NotificationItem notificationItem = (NotificationItem)notificationList.getItemAtPosition(friendListID);
                //utils.setFriend(person.name);
            }
        });

        return view;
    }
}
