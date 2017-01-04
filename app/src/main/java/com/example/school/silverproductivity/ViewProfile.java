package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

//ActionBarActivity
//public class ViewProfile extends ListActivity {
public class ViewProfile extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    //testing from a real server:
    //private static final String ALL_USERS_URL = "http://syidah.web44.net/silverproductivity/allUsers.php";
    //private static final String ALL_USERS_URL = "http://127.0.0.1/silverproductivity/allUsers.php";
    //private static final String ALL_USERS_URL = "http://localhost/silverproductivity/allUsers.php";
    private static final String ALL_USERS_URL = Configure.server+"/silverproductivity/allUsers.php";          // emulator
    // private static final String ALL_USERS_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/allUsers.php";


    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";

    private static final String TAG_USERNAME = "username";
    private static final String TAG_PHONE_NUMBER = "phoneNumber";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_ADDRESS = "address";

    TextView testUsername, testPhoneNo, testAge, testGender, testAddress;
    String fetchUsername, fetchPhoneNo, fetchAge, fetchGender, fetchAddress;

    //An array of all of our comments
    private JSONArray rUsers = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> rUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_profile);
        setContentView(R.layout.test_profile);

        setTitle("My Profile");

        testUsername = (TextView) findViewById(R.id.testUsername);
        testPhoneNo = (TextView) findViewById(R.id.testPhoneNo);
        testAge = (TextView) findViewById(R.id.testAge);
        testGender = (TextView) findViewById(R.id.testGender);
        testAddress = (TextView) findViewById(R.id.testAddress);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadUsers().execute();
    }

    public void updateJSONdata() {
        //Retrieving Saved Username Data:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ViewProfile.this);
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

                /*
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                if((saved_username.toLowerCase()).matches(username.toLowerCase())){
                    map.put(TAG_USERNAME, username);
                    map.put(TAG_PHONE_NUMBER, phoneNumber);
                    map.put(TAG_AGE, age);
                    if(gender.matches("f")){
                        map.put(TAG_GENDER, "female");
                    }
                    else{
                        map.put(TAG_GENDER, "male");
                    }
                    // map.put(TAG_GENDER, gender);
                    map.put(TAG_ADDRESS, address);

                    rUsersList.add(map);
                }
                */

                if((saved_username.toLowerCase()).matches(username.toLowerCase())){
                    fetchUsername = username;
                    fetchPhoneNo = phoneNumber;
                    fetchAge = age;
                    fetchGender = gender;
                    fetchAddress = address;
                }

            }

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void updateList(){

        /*
        ListAdapter adapter = new SimpleAdapter(this, rUsersList,
                R.layout.activity_single_user, new String[] { TAG_USERNAME, TAG_PHONE_NUMBER,
                TAG_AGE, TAG_GENDER, TAG_ADDRESS}, new int[] { R.id.tvUsername, R.id.tvPhoneNumber,
                R.id.tvAge, R.id.tvGender, R.id.tvAddress });


        setListAdapter(adapter);
        */

        /*
        // Optional: when the user clicks a list item we could do something.  However, we will choose to do nothing...
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // This method is triggered if an item is click within our list. For our example we won't be using this, but it is useful to know in real life applications.
            }
        });
        */

        testUsername.setText(fetchUsername);
        testPhoneNo.setText(fetchPhoneNo);
        testAge.setText(fetchAge);
        testGender.setText(fetchGender);
        testAddress.setText(fetchAddress);
    }

    public class LoadUsers extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Loading Users...");
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
