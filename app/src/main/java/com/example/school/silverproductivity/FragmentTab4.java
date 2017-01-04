package com.example.school.silverproductivity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentTab4 extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;

    //testing from a real server:
    //private static final String ALL_USERS_URL = "http://syidah.web44.net/silverproductivity/allUsers.php";
    //private static final String ALL_USERS_URL = "http://127.0.0.1/silverproductivity/allUsers.php";
    //private static final String ALL_USERS_URL = "http://localhost/silverproductivity/allUsers.php";
    private static final String ALL_USERS_URL = Configure.server+"/silverproductivity/allUsers.php";    // emulator
    //private static final String ALL_USERS_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/allUsers.php";


    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";

    private static final String TAG_USERNAME = "username";
    private static final String TAG_PHONE_NUMBER = "phoneNumber";
    private static final String TAG_NAME = "name";
    private static final String TAG_POINTS = "rewardedPoints";
    private static final String TAG_REPUTATION = "userReputation";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_ADDRESS = "address";

    TextView profUsername, profPhoneNo, profAge, profGender, profAddress, profReputation, profPoints;
    String fetchUsername, fetchPhoneNo, fetchAge, fetchGender, fetchAddress, fetchReputation, fetchPoints, fetchName;

    //An array of all of our comments
    private JSONArray rUsers = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> rUsersList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*
        profUsername = (TextView) view.findViewById(R.id.profUsername);
        profPhoneNo = (TextView) view.findViewById(R.id.profPhoneNo);
        profAge = (TextView) view.findViewById(R.id.profAge);
        profGender = (TextView) view.findViewById(R.id.profGender);
        profAddress = (TextView) view.findViewById(R.id.profAddress);

        //profUsername.setText("HEHE");
        */

        getActivity().setTitle("My Profile");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profUsername = (TextView) getView().findViewById(R.id.profUsername);
        profPhoneNo = (TextView) getView().findViewById(R.id.profPhoneNo);
        profAge = (TextView) getView().findViewById(R.id.profAge);
        profGender = (TextView) getView().findViewById(R.id.profGender);
        profAddress = (TextView) getView().findViewById(R.id.profAddress);
        profReputation = (TextView) getView().findViewById(R.id.profReputation);
        profPoints = (TextView) getView().findViewById(R.id.profPoints);
    }

    @Override
    public void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadUsers().execute();
    }

    public void updateJSONdata() {
        //Retrieving Saved Username Data:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String saved_username = sp.getString("username", "anon");

        rUsersList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(ALL_USERS_URL);

        try{
            rUsers = json.getJSONArray(TAG_USERS);

            for (int i = 0; i < rUsers.length(); i++) {
                JSONObject u = rUsers.getJSONObject(i);

                String username = u.getString(TAG_USERNAME);
                String phoneNumber = u.getString(TAG_PHONE_NUMBER);
                String age = u.getString(TAG_AGE);
                String gender = u.getString(TAG_GENDER);
                String address = u.getString(TAG_ADDRESS);
                String reputation = u.getString(TAG_REPUTATION);
                String points = u.getString(TAG_POINTS);
                String name = u.getString(TAG_NAME);

                if((saved_username.toLowerCase()).matches(username.toLowerCase())){
                    fetchUsername = username;
                    fetchPhoneNo = phoneNumber;
                    fetchAge = age;
                    fetchGender = gender;
                    fetchAddress = address;
                    fetchReputation = reputation;
                    fetchPoints = points;
                    fetchName = name;
                }


            }

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void updateList(){

        String capUsername = fetchUsername.substring(0, 1).toUpperCase() + fetchUsername.substring(1);

        //profUsername.setText(capUsername);

        if(fetchName.matches("")){
            profUsername.setText(capUsername);
        }
        else{
            profUsername.setText(fetchName.substring(0, 1).toUpperCase() + fetchName.substring(1));
        }

        profPhoneNo.setText(fetchPhoneNo);

        if(fetchAge.matches("0")){
            profAge.setText("- age not set yet -");
        }
        else{
            profAge.setText(fetchAge);
        }

        if(fetchGender.matches("f")){
            profGender.setText("Female");
        }
        else if(fetchGender.matches("m")){
            profGender.setText("Male");
        }
        else{
            profGender.setText("- gender not set yet -");
        }

        if(fetchAddress.matches("")){
            profAddress.setText("- address not set yet -");
        }
        else{
            profAddress.setText(fetchAddress);
        }

        profReputation.setText(fetchReputation);
        profPoints.setText(fetchPoints);

    }

    public class LoadUsers extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            //we will develop this method in version 2

            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //we will develop this method in version 2
            updateList();
        }

    }

}