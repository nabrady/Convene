package google.com.convenebackend.MainApp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import google.com.convenebackend.R;
import google.com.convenebackend.GoogleAPIs.GcmRegistrationAsyncTask;

/**
 * Created by Lydia on 17/12/2015.
 */
public class LaunchActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LoadingTask.LoadingTaskFinishedListener {

    private GoogleApiClient mGoogleApiClient = UserUtils.getmGoogleApiClient();
    private double userLatitude = UserUtils.getUserLatitude();
    private double userLongitude = UserUtils.getUserLongitude();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show the splash screen
        setContentView(R.layout.activity_launch);
        // Find the progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        // Start your loading
        new LoadingTask(progressBar, this).execute("www.google.co.uk");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Log.d("API CLIENT", mGoogleApiClient.toString());
    }

    // This is the callback for when async task has finished
    @Override
    public void onTaskFinished() {
        completeSplash();
    }

    private void completeSplash(){
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        Intent intent = new Intent(LaunchActivity.this, AppMainActivity.class);
        startActivity(intent);
    }

    //when our googleAPICLient is connected
    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnection");

        UserUtils.setmGoogleApiClient(mGoogleApiClient);

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            UserUtils.setmLastLocation(mLastLocation);

            userLatitude = mLastLocation.getLatitude();
            UserUtils.setUserLatitude(userLatitude);

            userLongitude = mLastLocation.getLongitude();
            UserUtils.setUserLongitude(userLongitude);

            new GcmRegistrationAsyncTask(this, "123", userLatitude, userLongitude).execute();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("ConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("ConnectionFailed");
    }

}

//TODO set default fb photo?