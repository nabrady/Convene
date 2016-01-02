package google.com.convenebackend;

/**
 * Created by Peru on 02-01-2016.
 */
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class NotifyResultActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private double userLatitude;
    private double userLongitude;
    private String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_result);

        Intent intent = getIntent();
        friendId = intent.getStringExtra("senderId");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnection");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        userLatitude = mLastLocation.getLatitude();
        userLongitude = mLastLocation.getLongitude();
    }
    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("ConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("ConnectionFailed");
    }

    public void notify(View v){
        new SendNotification(this,friendId,"Request accepted",userLatitude,userLongitude,"456").execute();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void notifyReject(View v){
        new SendNotification(this,friendId," Sorry Request rejected",userLatitude,userLongitude,"456").execute();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
