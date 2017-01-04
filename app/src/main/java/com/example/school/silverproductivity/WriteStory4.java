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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// ActionBarActivity
public class WriteStory4 extends Activity {

    String saved_username;

    String workloadId, workloadUrl, getLocation, getMood, getLabels;
    String storyId, storyPath, storyDate, storyOccDate, storyLabels;

    EditText etStoryDescription;
    Button bNext4;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing from a real server:
    //private static final String ALL_STORIES_URL = "http://syidah.web44.net/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://127.0.0.1/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://localhost/silverproductivity/showAllStories.php";
    private static final String ALL_STORIES_URL = Configure.server+"/silverproductivity/showAllStories.php";        // emulator
    private static final String CREATE_STORY_URL = Configure.server+"/silverproductivity/createStory.php";          // emulator
    // private static final String ALL_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllStories.php";
    // private static final String CREATE_STORY_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/createStory.php";

    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_STORIES = "stories";

    private static final String TAG_STORY_ID = "id";
    private static final String TAG_STORY_PATH = "path";
    private static final String TAG_STORY_DATE = "date";
    private static final String TAG_STORY_OCC_DATE = "occDate";
    private static final String TAG_LABELS = "allLabels";

    //An array of all of our comments
    private JSONArray aStories = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aStoriesList;
    private ArrayList<HashMap<String, String>> aStoriesList;

    private ImageView imgView;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story4);

        setTitle("Share Your Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");
        getLocation = getIntent().getStringExtra("GetLocation");
        getMood = getIntent().getStringExtra("GetMood");
        getLabels = getIntent().getStringExtra("Labels");

        Log.d("RECEIVED STORY4 ID: ", workloadId);
        Log.d("RECEIVED STORY4 URL: ", workloadUrl);
        Log.d("RECEIVED STORY4 LOC: ", getLocation);
        Log.d("RECEIVED STORY4 MOOD: ", getMood);
        Log.d("RECEIVED STORY4 LABEL: ", getLabels);

        etStoryDescription = (EditText) findViewById(R.id.etStoryDescription);

        imgView = (ImageView) findViewById(R.id.ivStoryPhoto4);
        imgLoader = new ImageLoader(this);

        bNext4 = (Button) findViewById(R.id.bNext4);
        bNext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateStoryPost().execute();
            }
        });

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadDetails().execute();
    }

    public void updateJSONdata() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        saved_username = sp.getString("username", "anon");

        aStoriesList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(ALL_STORIES_URL);

        try{
            aStories = json.getJSONArray(TAG_STORIES);

            // looping through all posts according to the json object returned
            for (int i = 0; i < aStories.length(); i++) {
                JSONObject c = aStories.getJSONObject(i);

                //gets the content of each tag from server
                String id = c.getString(TAG_STORY_ID);
                String path = c.getString(TAG_STORY_PATH);
                String subDate = c.getString(TAG_STORY_DATE);
                String occDate = c.getString(TAG_STORY_OCC_DATE);
                String labels = c.getString(TAG_LABELS);

                if(workloadUrl.matches(path)){
                    storyId = id;
                    storyPath = "http://10.0.2.2/silverproductivity/"+path;     // emulator
                    //storyPath = "http://www.agelesslily.org/liusiyuan/silverproductivity/"+path;
                    storyDate = subDate;
                    storyOccDate = occDate;
                    storyLabels = labels;

                }

            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void updateList(){
        //imgLoader.DisplayImage("http://10.0.2.2/silverproductivity/" + workloadUrl, imgView);   // emulator
        imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/" + workloadUrl, imgView);

    }

    public class LoadDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteStory4.this);
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

    class CreateStoryPost extends AsyncTask<String, String, String> {
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteStory4.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String getDesc = etStoryDescription.getText().toString();

            int success;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("photoId", workloadId));
                params.add(new BasicNameValuePair("photoUrl", workloadUrl));
                params.add(new BasicNameValuePair("username", saved_username));
                params.add(new BasicNameValuePair("userLocation", getLocation));
                params.add(new BasicNameValuePair("userMood", getMood));
                params.add(new BasicNameValuePair("userLabel", getLabels));
                params.add(new BasicNameValuePair("userDescription", getDesc));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(CREATE_STORY_URL, "POST", params);

                // full json response
                Log.d("Register attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());

                    //finish();


                    //Intent intent = new Intent(getApplicationContext(), TabBar.class);      // link back to the main tabbar
                    //Intent intent = new Intent(getApplicationContext(), EachStoryDetails.class);    // link back to each story details
                    Intent intent = new Intent(WriteStory4.this, EachStoryDetails.class);

                    String getStoryId = workloadId.toString();
                    String getStoryPath = workloadUrl.toString();

                    intent.putExtra("WorkloadId", getStoryId);
                    intent.putExtra("WorkloadUrl", getStoryPath);

                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        // clear WriteStory 1 to 4
                    startActivity(intent);

                    /*
                    Intent i = new Intent(WriteStory4.this, AllStoriesList.class);

                    String getStoryId = workloadId.toString();
                    String getStoryPath = workloadUrl.toString();

                    i.putExtra("WorkloadId", getStoryId);
                    i.putExtra("WorkloadUrl", getStoryPath);

                    startActivity(i);
                    //return json.getString(TAG_MESSAGE);
                    */


                }else{
                    Log.d("Register Failure!", json.getString(TAG_MESSAGE));
                    //return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // After completing background task Dismiss the progress dialog
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(WriteStory4.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_story4, menu);
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
