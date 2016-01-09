package google.com.convenebackend.fragments;

/**
 * Created by Lydia on 17/12/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import google.com.convenebackend.R;

public class TwoFragment extends Fragment {

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


}
