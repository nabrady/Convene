package google.com.convenebackend.fragments;

/**
 * Created by Lydia on 17/12/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;

import google.com.convenebackend.MainApp.UserUtils;
import google.com.convenebackend.R;
import google.com.convenebackend.facebook.RoundImage;
import google.com.convenebackend.facebook.FbProfileInfo;


public class OneFragment extends Fragment {
    UserUtils utils;

    private static String friendToMeet;
    public static ArrayList friendListArray = new ArrayList();
    private RelativeLayout userInfo;
    private ListView lvFriend;
    private LoginButton loginButton;
    private Button btnSyncContacts;
    private LinearLayout backButtons;
    private ImageView profileImage, logo;
    private TextView info, orText, userName;
    public static ArrayAdapter adapter;
    private View view;
    private AccessTokenTracker accessTokenTracker;

    // The CallbackManager is used to manage the callbacks used in the app.
    private CallbackManager callbackManager;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getContext());

        //accessTokenTracker tracks whether user is logged in or not
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize our instance of CallbackManager
        callbackManager = CallbackManager.Factory.create();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);

        logo = (ImageView) view.findViewById(R.id.conveneLogo);
        logo.bringToFront();

        userName = (TextView) view.findViewById(R.id.userName);
        btnSyncContacts = (Button) view.findViewById(R.id.btnSearchContacts);
        info = (TextView) view.findViewById(R.id.info);
        orText = (TextView) view.findViewById(R.id.ortext);
        profileImage = (ImageView) view.findViewById(R.id.profileimage);
        backButtons = (LinearLayout) view.findViewById(R.id.backButtonsContainer);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        lvFriend = (ListView) view.findViewById(R.id.lvFriend);
        userInfo = (RelativeLayout) view.findViewById(R.id.fbInfoContainer);

        //get user permission to access to friends list
        loginButton.setReadPermissions(Arrays.asList("user_friends"));
        loginButton.setFragment(this);
        //create a callback to handle the results of the login attempts and
        // register it with the CallbackManager
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FragmentOne.FBLOGIN", "Login attempt a success!!.");
            }

            @Override
            public void onCancel() {
                Log.d("FragmentOne.FBLOGIN", "Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("FragmentOne.FBLOGIN", "Login attempt failed.");
            }
        });

        friendListArray.clear();
        //NOTE: simple list item 1 = Android predefined TextView resource id
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friendListArray);
        lvFriend.setAdapter(adapter);

        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                int friendListID = (int) id;
                friendToMeet = (lvFriend.getItemAtPosition(friendListID).toString());
            }
        });

        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            //profileImage.setBackgroundResource(R.mipmap.default_profile);
            //friendListArray.clear();
            //adapter.notifyDataSetChanged();
        } else {
            FbProfileInfo FBInfo = new FbProfileInfo(getActivity());
            FBInfo.getProfileInformation(getActivity());
            setUpFriendListPage();
        }

        return view;
    }


    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            FbProfileInfo FBInfo = new FbProfileInfo(getActivity());
            FBInfo.getProfileInformation(getActivity());
            setUpFriendListPage();
        }
        else{
            backToLogin();
        }

    }

    public void backToLogin(){
        profileImage.setVisibility(View.INVISIBLE);
        backButtons.setVisibility(View.INVISIBLE);

        btnSyncContacts.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        orText.setVisibility(View.VISIBLE);

        //TODO remove friend list
    }

    public void setUpFriendListPage(){
        if (utils.getProfilePic() != null) {
            profileImage.setBackground(utils.getProfilePic());
        } else {
            profileImage.setBackgroundResource(R.mipmap.default_profile);
        }
        profileImage.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.VISIBLE);
        backButtons.setVisibility(View.VISIBLE);

        btnSyncContacts.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.INVISIBLE);
        info.setVisibility(View.INVISIBLE);
        orText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);

        //TODO display friend list
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}