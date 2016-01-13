package google.com.convenebackend.fragments;

/**
 * Created by Lydia on 17/12/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import google.com.convenebackend.MainApp.UserUtils;
import google.com.convenebackend.R;

public class TwoFragment extends Fragment {

    private UserUtils utils;
    public final static int REQUEST_PLACE_PICKER = 1;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //http://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, getContext());
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
