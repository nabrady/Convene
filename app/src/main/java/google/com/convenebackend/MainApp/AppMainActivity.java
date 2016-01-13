package google.com.convenebackend.MainApp;

/**
 * Created by Lydia on 17/12/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.Signature;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import google.com.convenebackend.R;
import google.com.convenebackend.fragments.OneFragment;
import google.com.convenebackend.fragments.TwoFragment;
import google.com.convenebackend.fragments.ThreeFragment;
import google.com.convenebackend.fragments.mapsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppMainActivity extends AppCompatActivity {

    UserUtils utils;
    //Layout variables
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    private TextView info, comingSoon, orText, userName;
    private RelativeLayout userInfo;
    private Button searchContacts, backBtn, backBtn2;
    private LoginButton loginButton;
    private EditText enterLocation;
    private ImageView logo, profileImage;
    private ImageButton goBtn;
    private LinearLayout buttonWrap;
    public static ListView lvFriend, notList;
    private static ArrayList friendListArray = new ArrayList();

    private int[] tabIcons = {
            R.mipmap.add_friends_logo,
            R.mipmap.search_address,
            R.mipmap.convene_icon
    };

    //userInfo
    private String senderId;
    private String friendToMeet;
    private Map<String, String> friendMap = new HashMap<>();
    private ArrayList notListArray = utils.getNotList();

    //location vars
    private GoogleApiClient mGoogleApiClient = UserUtils.getmGoogleApiClient();
    private double userLatitude = UserUtils.getUserLatitude();
    private double userLongitude = UserUtils.getUserLongitude();
    private GetLocation friendLocation;
    private LatLng latLng;
    //frag manager
    public static FragmentManager fragmentManager;
    //request codes
    public final static int REQUEST_PLACE_PICKER = 1;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_app);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        Intent intent = this.getIntent();
        if(intent!=null) {
            String intentId = intent.getExtras().getString("UNIQUE_ID");
            switch (intentId) {
                case "CONVENE_REQUEST":
                    viewPager.setCurrentItem(2);

                    String senderName = intent.getExtras().getString("FRIEND_NAME");
                    NotificationItem notificationItem = new NotificationItem(0, senderName, "Convene Request");
                    notListArray.add(notificationItem);
                    Log.d("ArrayTest", notListArray.toString());

                    ThreeFragment.notListArray.clear();
                    ThreeFragment.notListArray.addAll(notListArray);
                    utils.setNotList(notListArray);

                    if( ThreeFragment.newadapter!=null){
                        ThreeFragment.newadapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    //
            }
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //disable the app icon as the Up (back) button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        tabLayout.setSelectedTabIndicatorHeight(5);

        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager().
        fragmentManager = getSupportFragmentManager();

        lvFriend = (ListView) findViewById(R.id.lvFriend);
        info = (TextView) findViewById(R.id.info);
        comingSoon = (TextView) findViewById(R.id.contactsComingSoon);
        orText = (TextView) findViewById(R.id.ortext);
        userName = (TextView) findViewById(R.id.userName);
        searchContacts = (Button) findViewById(R.id.btnSearchContacts);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        logo = (ImageView) findViewById(R.id.conveneLogo);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn2 = (Button) findViewById(R.id.backBtnFriends);
        profileImage = (ImageView) findViewById(R.id.profileimage);
        buttonWrap = (LinearLayout) findViewById(R.id.backButtonsContainer);
        userInfo = (RelativeLayout) findViewById(R.id.fbInfoContainer);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new OneFragment(), "");
        mAdapter.addFragment(new TwoFragment(), "");
        mAdapter.addFragment(new ThreeFragment(), "");
        viewPager.setAdapter(mAdapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            //not necessary to add title - using icons only
            //mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
            // to display title
            //return mFragmentTitleList.get(position);
        }
    }

    //fragment one button click functions
    //search our contacts for friends to request
    public void getContacts(View layout) {

        info = (TextView) findViewById(R.id.info);
        orText = (TextView) findViewById(R.id.ortext);
        searchContacts = (Button) findViewById(R.id.btnSearchContacts);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        logo = (ImageView) findViewById(R.id.conveneLogo);

        backBtn = (Button) findViewById(R.id.backBtn);
        comingSoon = (TextView) findViewById(R.id.contactsComingSoon);

        info.setVisibility(View.INVISIBLE);
        orText.setVisibility(View.INVISIBLE);
        searchContacts.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.INVISIBLE);

        comingSoon.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.VISIBLE);

    }

    public void getContactsFB(View layout) {

        backBtn2 = (Button) findViewById(R.id.backBtnFriends);
        comingSoon = (TextView) findViewById(R.id.contactsComingSoon);
        profileImage = (ImageView) findViewById(R.id.profileimage);
        buttonWrap = (LinearLayout) findViewById(R.id.backButtonsContainer);
        userInfo = (RelativeLayout) findViewById(R.id.fbInfoContainer);
        lvFriend = (ListView) findViewById(R.id.lvFriend);

        lvFriend.setVisibility(View.INVISIBLE);
        buttonWrap.setVisibility(View.INVISIBLE);
        userInfo.setVisibility(View.INVISIBLE);
        profileImage.setVisibility(View.INVISIBLE);

        comingSoon.setVisibility(View.VISIBLE);
        backBtn2.setVisibility(View.VISIBLE);

    }

    public void backToFriendListPage(View view) {

        backBtn2 = (Button) findViewById(R.id.backBtnFriends);
        profileImage = (ImageView) findViewById(R.id.profileimage);
        comingSoon = (TextView) findViewById(R.id.contactsComingSoon);
        buttonWrap = (LinearLayout) findViewById(R.id.backButtonsContainer);
        userInfo = (RelativeLayout) findViewById(R.id.fbInfoContainer);
        lvFriend = (ListView) findViewById(R.id.lvFriend);

        profileImage.setVisibility(View.VISIBLE);
        buttonWrap.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.VISIBLE);
        lvFriend.setVisibility(View.VISIBLE);

        comingSoon.setVisibility(View.INVISIBLE);
        backBtn2.setVisibility(View.INVISIBLE);
    }


    public void backToLogin(View layout) {
        info = (TextView) findViewById(R.id.info);
        orText = (TextView) findViewById(R.id.ortext);
        searchContacts = (Button) findViewById(R.id.btnSearchContacts);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        logo = (ImageView) findViewById(R.id.conveneLogo);

        backBtn = (Button) findViewById(R.id.backBtn);
        profileImage = (ImageView) findViewById(R.id.profileimage);
        comingSoon = (TextView) findViewById(R.id.contactsComingSoon);
        buttonWrap = (LinearLayout) findViewById(R.id.backButtonsContainer);
        userInfo = (RelativeLayout) findViewById(R.id.fbInfoContainer);


        info.setVisibility(View.VISIBLE);
        orText.setVisibility(View.VISIBLE);
        searchContacts.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);

        if (userInfo != null) {
            userInfo.setVisibility(View.INVISIBLE);
        }

        if (comingSoon != null) {
            comingSoon.setVisibility(View.INVISIBLE);
        }
        if (backBtn != null) {
            backBtn.setVisibility(View.INVISIBLE);
        }
        if (buttonWrap != null) {
            buttonWrap.setVisibility(View.INVISIBLE);
        }
        if (profileImage != null) {
            profileImage.setVisibility(View.INVISIBLE);
        }

        lvFriend = (ListView) findViewById(R.id.lvFriend);
        if (lvFriend != null) {
            lvFriend.setVisibility(View.INVISIBLE);
        }
    }

    public void logOutFB(View layout) {
        LoginManager.getInstance().logOut();
        backToLogin(layout);
        lvFriend = (ListView) findViewById(R.id.lvFriend);
        //if(lvFriend!=null){
        lvFriend.setVisibility(View.INVISIBLE);
        //}
    }


    public void sendRequest(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        friendToMeet = utils.getFriend();
        friendMap = utils.getFreindMap();

        if (friendToMeet != null) {
            alertDialogBuilder
                    //set dialog message
                    .setMessage("Do you want to meet " + friendToMeet + "?")
                            //sets whether this dialog is cancelable with the back button
                    .setCancelable(true)
                    .setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked: clear alert dialog
                            dialog.cancel();
                            OneFragment.newadapter.deselectIndex();
                            //then send get info and send request
                            //TODO SEND REQUEST
                            String friendFbId = friendMap.get(friendToMeet).toString();
                            utils.setFriend(null);

                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            if (mLastLocation != null) {
                                utils.setmLastLocation(mLastLocation);

                                userLatitude = mLastLocation.getLatitude();
                                UserUtils.setUserLatitude(userLatitude);

                                userLongitude = mLastLocation.getLongitude();
                                UserUtils.setUserLongitude(userLongitude);

                                //get UserID.
                                senderId = utils.getUserID();
                                new SendNotification(getApplicationContext(), friendFbId, "Convene Request",
                                        userLatitude, userLongitude, senderId).execute();
                            }
                        }
                    });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a friend",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 60);
            toast.show();
        }

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    } //end of sendConveneReq()*/


    //buttons/functions from Tab two
    public void backToSearchLocation() {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new OneFragment(), "");
        mAdapter.addFragment(new TwoFragment(), "");
        mAdapter.addFragment(new ThreeFragment(), "");

        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.setSelectedTabIndicatorHeight(5);

        viewPager.setCurrentItem(1);
    }

    public void getLocation(View view) {
        goBtn = (ImageButton) findViewById(R.id.goLocation);
        //Assign the text to a local message variable
        enterLocation = (EditText) findViewById(R.id.enterText);
        String location = enterLocation.getText().toString().trim();
        utils.setSearchLocation(location);
        enterLocation.getText().clear();

        List<Address> addressList = null;
        if (location.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid search",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 60);
            toast.show();
            return;
        }
        if (!location.equals("") || !location.equals(null)) {
            Geocoder geocoder = new Geocoder(this);
            for(int i=0; i<3; i++){
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    if(addressList!=null){
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(addressList!=null){
                Address address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Server Error. Please try again",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 60);
                toast.show();
                return;
            }

        }
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder().setLatLngBounds(new LatLngBounds(latLng, latLng));
            Intent intent = intentBuilder.build(this);

            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }

        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new OneFragment(), "");
        mAdapter.addFragment(new mapsFragment(), "");
        mAdapter.addFragment(new ThreeFragment(), "");

        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.setSelectedTabIndicatorHeight(5);

        viewPager.setCurrentItem(1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
            final double lat = place.getLatLng().latitude;
            final double lon = place.getLatLng().longitude;

            final String addr = place.getAddress().toString().trim();
            utils.setSearchLocation(addr);
            Log.d("SEARCH ADDRESS", addr);
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            utils.setSearchLat(lat);
            utils.setSearchLon(lon);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

}




   /*
    public void getFriendLocation(View layout){

        friendLocation = (GetLocation) new GetLocation("456").execute();//pass friend id as parameter
        double latitude = friendLocation.getFriendLatitude();//not working
        double longitude = friendLocation.getFriendLongitude();//not working
        System.out.println("latitude " + latitude + " longitude " + longitude);
    }*/

    /*void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("google.com.convenebackend",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("Digest: ", Base64.encodeToString(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Test", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("Test", e.getMessage());
        }
    }*/