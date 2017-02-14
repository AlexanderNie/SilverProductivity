package com.example.school.silverproductivity;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Alexander on 3/2/2017.
 */

public class FragMentFrameActivity extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LazyAdapter.setLike(true);
        View view = inflater.inflate(R.layout.fragment_tab_frame, container, false);
        getActivity().setTitle("Recommendation Photos");
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (view.findViewById(R.id.fragment_container1) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return view;
            }

            // Create a new Fragment to be placed in the activity layout
            FragmentTab3 firstFragment = new FragmentTab3();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getActivity().getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container1, firstFragment).commit();

        }
        return view;
    }
}
