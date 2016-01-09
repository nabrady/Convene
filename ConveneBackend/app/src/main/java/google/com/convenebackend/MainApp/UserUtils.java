package google.com.convenebackend.MainApp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Lydia on 05/01/2016.
 */
public class UserUtils {

    private static Drawable profiler=null;
    private static String UserId, UserName;
    private static ArrayList friendListArray = new ArrayList();

    private static Location mLastLocation = null;
    private static GoogleApiClient mGoogleApiClient = null;
    private static double userLatitude = 0;
    private static double userLongitude = 0;

    private static String searchLocation=null;
    private static double searchLocationLat=0;
    private static double searchLocationLon=0;

    //getter and setter methods
    public static Drawable getProfilePic(){return profiler;}
    public static void setProfilePic(Drawable newProfiler){profiler=newProfiler;}

    public static String getUserID(){return UserId;}
    public static void setUserID(String newID){UserId=newID;}

    public static String getUserName(){return UserName;}
    public static void setUserName(String newName){UserName=newName;}

    public static ArrayList getFriendList(){return friendListArray;}
    public static void setFriendList(ArrayList newFriends){friendListArray=newFriends;}



    public static GoogleApiClient getmGoogleApiClient(){return mGoogleApiClient;}
    public static void setmGoogleApiClient(GoogleApiClient newGoogleApiClient){mGoogleApiClient=newGoogleApiClient;}

    public static Location getmLastLocation(){return mLastLocation;}
    public static void setmLastLocation(Location newLastLocation){mLastLocation=newLastLocation;}

    public static double getUserLatitude(){return userLatitude;}
    public static void setUserLatitude(double newUserLat){userLatitude=newUserLat;}

    public static double getUserLongitude(){return userLongitude;}
    public static void setUserLongitude(double newUserLong){userLongitude=newUserLong;}

    public static String getSearchLocation(){return searchLocation;}
    public static void setSearchLocation(String newLocation){searchLocation=newLocation;}

    public static double getSearchLat(){return searchLocationLat;}
    public static void setSearchLat(double newLat){searchLocationLat=newLat;}

    public static double getSearchLon(){return searchLocationLon;}
    public static void setSearchLon(double newLon){searchLocationLon=newLon;}
}
