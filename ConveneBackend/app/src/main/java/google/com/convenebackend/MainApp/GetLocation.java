package google.com.convenebackend.MainApp;

/**
 * Created by Peru on 02-01-2016.
 */
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.sample.backend.registration.Registration;
import com.google.sample.backend.registration.model.RegistrationRecord;

import java.io.IOException;

public class GetLocation extends AsyncTask<Void, Void, RegistrationRecord> {
    private static Registration locationService = null;
    private String friendId;
    private double friendLatitude;
    private double friendLongitude;
    // private RegistrationRecord record;

    public GetLocation(String fb_id){
        this.friendId = fb_id;
    }

    @Override
    protected RegistrationRecord doInBackground(Void... params) {
        //super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (locationService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://convene-backend.appspot.com/_ah/api/");

            //builder.setApplicationName("Sample Application");
            locationService = builder.build();
        }
        RegistrationRecord record1 = null;
        try {
            //locationService.sendFriendId(friendId).execute();
            System.out.println("friendId given to getRecord is " + friendId);
            record1 = locationService.getRecord(friendId).execute();
            //friendLatitude= record.getLocationLatitude();
            //friendLongitude = record.getLocationLongitude();
            //System.out.println("GetLocation::Latitude "+friendLatitude);
            //System.out.println("GetLocation::Longitude "+friendLongitude);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //List<Double> quoteList = new ArrayList<>(2);
        //quoteList.add(friendLatitude);
        //quoteList.add(friendLongitude);
        return record1;
    }

    @Override
    protected void onPostExecute(RegistrationRecord result) {
        //for(Double q: result){
        //  System.out.println("Result is "+q);
        //}
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        //Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        friendLatitude = result.getLocationLatitude();
        friendLongitude = result.getLocationLongitude();
        System.out.println("Latitude "+friendLatitude+" Longitude "+friendLongitude);
    }

    public double getFriendLatitude(){return friendLatitude;}

    public double getFriendLongitude(){return friendLongitude;}
}