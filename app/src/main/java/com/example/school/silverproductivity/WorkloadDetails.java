package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//ActionBarActivity
public class WorkloadDetails extends Activity {

    TextView id, date, labels;
    ImageView ivStoryDetail;
    Button bWriteStory, bShowNearby, bGetThere;

    // Progress Dialog
    private ProgressDialog pDialog;

    //testing from a real server:
    //private static final String ALL_STORIES_URL = "http://syidah.web44.net/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://127.0.0.1/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://localhost/silverproductivity/showAllStories.php";
    private static final String ALL_STORIES_URL = Configure.server+"/silverproductivity/showAllStories.php";      // emulator
    //private static final String ALL_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllStories.php";

    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_STORIES = "stories";

    private static final String TAG_STORY_ID = "id";
    private static final String TAG_STORY_PATH = "path";
    private static final String TAG_STORY_DATE = "date";
    private static final String TAG_STORY_OCC_DATE = "occDate";
    private static final String TAG_LABELS = "allLabels";
    private static final String TAG_LAT = "storylat";
    private static final String TAG_LNG = "storylng";
    private static final String TAG_TYPE = "storytype";


    //An array of all of our comments
    private JSONArray aStories = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aStoriesList;
    private ArrayList<HashMap<String, String>> aStoriesList;

    String storyId, storyPath, storyDate, storyOccDate, storyLabels;
    LatLng storyLatlng;
    String imgUrl;
    Bitmap img;

    private ImageView imgView;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workload_details);

        setTitle("Story Details");

        id = (TextView) findViewById(R.id.id);
        //path = (TextView) findViewById(R.id.path);
        date = (TextView) findViewById(R.id.date);
        labels = (TextView) findViewById(R.id.labels);
        ivStoryDetail = (ImageView) findViewById(R.id.ivStoryDetail);

        imgView = (ImageView) findViewById(R.id.ivStoryDetail);
        imgLoader = new ImageLoader(this);

        imgUrl = getIntent().getStringExtra("ImageUrl");

        bWriteStory = (Button) findViewById(R.id.bWriteStory);
        bWriteStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WorkloadDetails.this, WriteStory1.class);

                String getStoryId = storyId.toString();
                String getStoryPath = imgUrl.toString();

                //Log.d("STORY ID PUSH: ", getStoryId);
                //Log.d("STORY URL PUSH: ", getStoryPath);

                i.putExtra("WorkloadId", getStoryId);
                i.putExtra("WorkloadUrl", getStoryPath);

                startActivity(i);
            }
        });


        bShowNearby = (Button) findViewById(R.id.bShowNearby);
        bShowNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WorkloadDetails.this, AttractionMapActivity.class);
                startActivity(i);
            }
        });

        bGetThere = (Button) findViewById(R.id.bGetThere);
        bGetThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WorkloadDetails.this, DirectionMapActivity.class);
                i.putExtra("targetlat", storyLatlng.latitude);
                i.putExtra("targetlng", storyLatlng.longitude);
                startActivity(i);
            }
        });

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
                String lat = c.getString(TAG_LAT);
                String lng = c.getString(TAG_LNG);
                int type = Integer.valueOf(c.getString(TAG_TYPE));
                LatLng position = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
//                String tempPath = "http://www.agelesslily.org/liusiyuan/silverproductivity/"+path;
//                BitmapDrawable bd = new BitmapDrawable(imgLoader.getBitmap(tempPath));

                Attraction a = new Attraction(position, "", type);
                AttractionLoader.loadAttraction(a);

                if(imgUrl.matches(path)){
                    storyId = id;
                    //storyPath = "http://10.0.2.2/silverproductivity/"+path;     // emulator
                    storyPath = "http://www.agelesslily.org/liusiyuan/silverproductivity/"+path;
                    storyDate = subDate;
                    storyOccDate = occDate;
                    storyLabels = labels;
                    storyLatlng = position;
                }

            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void updateList(){
        id.setText(storyId);
        //path.setText(storyPath);
        date.setText(storyOccDate);
        labels.setText(storyLabels);
        //imgLoader.DisplayImage("http://10.0.2.2/silverproductivity/"+imgUrl, imgView);      // emulator
        imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/"+imgUrl, imgView);

    }

    public class LoadStoryDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkloadDetails.this);
            pDialog.setMessage("Loading Story Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            //we will develop this method in version 2
            AttractionLoader.clearAttraction();
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
        getMenuInflater().inflate(R.menu.menu_story_details, menu);
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
