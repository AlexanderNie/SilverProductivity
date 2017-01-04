package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// ActionBarActivity
public class EachStoryDetails extends Activity {

    String workloadId, workloadUrl;

    String thisStoryId, thisPhotoId, thisPhotoUrl, thisPhotoUsername, thisPhotoLoc, thisPhotoMood, thisPhotoLabel, thisPhotoDesc, thisPhotoDateTime;

    TextView tvUsername, tvMood, tvLocation, tvLabel, tvDescription, tvAvgRating;
    Button bRateStory;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String UPLOADED_STORIES_URL = Configure.server+"/silverproductivity/showAllUploadedStories.php";
    // private static final String UPLOADED_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllUploadedStories.php";

    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_STORIES = "stories";

    private static final String TAG_STORY_ID = "storyId";
    private static final String TAG_PHOTO_ID = "photoId";
    private static final String TAG_PHOTO_URL = "photoUrl";
    private static final String TAG_USERNAME = "user";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_MOOD = "mood";
    private static final String TAG_LABEL = "label";
    private static final String TAG_DESC = "desc";
    private static final String TAG_DATETIME = "dateTime";

    private JSONArray aStories = null;

    private ArrayList<HashMap<String, String>> aStoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_story_details);

        setTitle("Uploaded Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");

        Log.d("RECEIVED STORY ID: ", workloadId);
        Log.d("RECEIVED STORY URL: ", workloadUrl);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvMood = (TextView) findViewById(R.id.tvMood);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLabel = (TextView) findViewById(R.id.tvLabel);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvAvgRating = (TextView) findViewById(R.id.tvAvgRating);
        bRateStory = (Button) findViewById(R.id.bRateStory);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadStoryDetails().execute();
    }

    public void updateJSONdata() {
        aStoriesList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(UPLOADED_STORIES_URL);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_username = sp.getString("username", "anon");

        try{
            aStories = json.getJSONArray(TAG_STORIES);

            // looping through all posts according to the json object returned
            for (int i = 0; i < aStories.length(); i++) {
                JSONObject c = aStories.getJSONObject(i);

                //gets the content of each tag from server
                String id = c.getString(TAG_STORY_ID);
                String photoId = c.getString(TAG_PHOTO_ID);
                String photoUrl = c.getString(TAG_PHOTO_URL);
                String photoUsername = c.getString(TAG_USERNAME);
                String photoLoc = c.getString(TAG_LOCATION);
                String photoMood = c.getString(TAG_MOOD);
                String photoLabel = c.getString(TAG_LABEL);
                String photoDesc = c.getString(TAG_DESC);
                String photoDateTime = c.getString(TAG_DATETIME);

                //if( workloadUrl.matches(photoUrl) ){
                if(saved_username.matches(photoUsername) && workloadUrl.matches(photoUrl) ){
                    thisStoryId = id;
                    thisPhotoId = photoId;
                    thisPhotoUrl = photoUrl;
                    thisPhotoUsername = photoUsername;
                    thisPhotoLoc = photoLoc;
                    thisPhotoMood = photoMood;
                    thisPhotoLabel = photoLabel;
                    thisPhotoDesc = photoDesc;
                    thisPhotoDateTime = photoDateTime;
                }

            }


        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void updateList(){
        tvUsername.setText(thisPhotoUsername);
        tvMood.setText(thisPhotoMood);
        tvLocation.setText(thisPhotoLoc);
        tvLabel.setText(thisPhotoLabel);
        tvDescription.setText(thisPhotoDesc);
        tvAvgRating.setText("nil");

        bRateStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Rate button has", "been clicked!");

                Intent i = new Intent(EachStoryDetails.this, RatingScreen.class);

                String getStoryId = workloadId.toString();
                String getStoryPath = workloadUrl.toString();
                String rateGiver = thisPhotoUsername.toString();

                i.putExtra("WorkloadId", getStoryId);
                i.putExtra("WorkloadUrl", getStoryPath);
                i.putExtra("Rater", rateGiver);

                startActivity(i);

            }
        });

    }

    public class LoadStoryDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EachStoryDetails.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

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
        getMenuInflater().inflate(R.menu.menu_each_story_details, menu);
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
