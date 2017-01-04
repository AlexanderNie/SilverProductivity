package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QueueDetails extends Activity {

    TextView tvQueueId, tvQueuePath, tvQueueDate, tvStoryDate, tvStoryLabels;
    ImageView ivQueueDetail;
    Button bAccept, bReject;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String ALL_STORIES_URL = Configure.server+"/silverproductivity/showAllStories.php";                // emulator
    private static final String CHANGE_TO_WORKLOAD_URL = Configure.server+"/silverproductivity/changeQueueToWorkload.php";  // emulator
    private static final String CHANGE_TO_REJECT_URL = Configure.server+"/silverproductivity/changeQueueToReject.php";      // emulator

    // private static final String ALL_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllStories.php";
    // private static final String CHANGE_TO_WORKLOAD_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/changeQueueToWorkload.php";
    // private static final String CHANGE_TO_REJECT_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/changeQueueToReject.php";

    //JSON IDS: (from server)
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
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

    String storyId, storyPath, storyDate, storyOccDate, storyLabels;
    String imgUrl;
    Bitmap img;

    private ImageView imgView;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_details);

        setTitle("Pending Stories");

        bAccept = (Button) findViewById(R.id.bAccept);
        bReject = (Button) findViewById(R.id.bReject);

        tvQueueId = (TextView) findViewById(R.id.tvQueueId);
        //tvQueuePath = (TextView) findViewById(R.id.tvQueuePath);
        //tvQueueDate = (TextView) findViewById(R.id.tvQueueDate);
        ivQueueDetail = (ImageView) findViewById(R.id.ivQueueDetail);
        tvStoryDate = (TextView) findViewById(R.id.tvStoryDate);
        tvStoryLabels = (TextView) findViewById(R.id.tvStoryLabels);

        imgView = (ImageView) findViewById(R.id.ivQueueDetail);
        imgLoader = new ImageLoader(this);

        imgUrl = getIntent().getStringExtra("QueueImageUrl");
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

                if(imgUrl.matches(path)){
                    storyId = id;
                    storyPath = path;
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
        //tvQueueId.setText(storyId);
        //tvQueuePath.setText(storyPath);
        //tvQueueDate.setText(storyDate);
        //imgLoader.DisplayImage("http://10.0.2.2/silverproductivity/" + imgUrl, imgView);        // emulator
        imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/"+ imgUrl, imgView);
        tvQueueId.setText(storyId);
        tvStoryDate.setText(storyOccDate);
        tvStoryLabels.setText(storyLabels);
    }

    public class LoadStoryDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QueueDetails.this);
            pDialog.setMessage("Loading Story Details...");
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

            bAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ACCEPT IS CLICKED", "");

                    new ChangeToWorkload().execute();
                }
            });

            bReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("REJECT IS CLICKED", "");
                    new ChangeToReject().execute();
                }
            });

        }
    }

    class ChangeToWorkload extends AsyncTask<String, String, String> {
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QueueDetails.this);
            pDialog.setMessage("Adding to your stories...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QueueDetails.this);
            String saved_username = sp.getString("username", "anon");

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", saved_username));
                params.add(new BasicNameValuePair("photoId", storyId));
                params.add(new BasicNameValuePair("photoUrl", imgUrl));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(CHANGE_TO_WORKLOAD_URL, "POST", params);

                // full json response
                Log.d("Putting into workload..", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Added to workload!", json.toString());

                    //Intent i = new Intent(QueueDetails.this, QueueList.class);
                    //startActivity(i);

                    finish();

                    return json.getString(TAG_MESSAGE);

                }else{
                    //Log.d("Queue to Workload Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
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
                Toast.makeText(QueueDetails.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    class ChangeToReject extends AsyncTask<String, String, String> {
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QueueDetails.this);
            pDialog.setMessage("Removing from queue...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QueueDetails.this);
            String saved_username = sp.getString("username", "anon");

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", saved_username));
                params.add(new BasicNameValuePair("photoId", storyId));
                params.add(new BasicNameValuePair("photoUrl", imgUrl));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(CHANGE_TO_REJECT_URL, "POST", params);

                // full json response
                Log.d("Putting into reject..", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Added to reject!", json.toString());

                    //Intent i = new Intent(QueueDetails.this, QueueList.class);
                    finish();
                    //startActivity(i);
                    return json.getString(TAG_MESSAGE);

                }else{
                    //Log.d("Queue to Workload Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch(JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        // After completing background task Dismiss the progress dialog
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(QueueDetails.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_queue_details, menu);
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
