package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//ActionBarActivity
public class WriteStory2 extends Activity {

    String workloadId, workloadUrl, getLocation, chosenMood;
    String storyId, storyPath, storyDate, storyOccDate, storyLabels;

    ImageView ivOne, ivTwo, ivThree, ivFour, ivFive, ivSix, ivSeven, ivEight;

    ImageView ivStoryPhoto2;
    Button bNext2;
    TextView tvDisplayEmotion;

    // Progress Dialog
    private ProgressDialog pDialog;

    //testing from a real server:
    //private static final String ALL_STORIES_URL = "http://syidah.web44.net/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://127.0.0.1/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://localhost/silverproductivity/showAllStories.php";
    private static final String ALL_STORIES_URL = Configure.server+"/silverproductivity/showAllStories.php";      // emulator
    // private static final String ALL_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllStories.php";

    //JSON IDS: (from server)
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

    private ImageView imgView;
    private ImageLoader imgLoader;

    TouchView touchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story2);

        setTitle("Share Your Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");
        getLocation = getIntent().getStringExtra("GetLocation");

        Log.d("LOCATION RETRIEVED: ", getLocation);


        chosenMood = "";
        tvDisplayEmotion = (TextView)findViewById(R.id.tvDisplayEmotion);

        touchView = (TouchView)findViewById(R.id.myTouchView);
        touchView.setBackgroundResource(R.drawable.ab0);
        touchView.setOnViewTouchedListener(new TouchView.OnViewTouchedListener() {
            @Override
            public void OnViewTouched(float x, float y, boolean touched) {

                // int step = getWallpaperDesiredMinimumWidth();
                // Log.d("THE STEP: ", Integer.toString(step));

                int widgetWidth = touchView.getWidth() - 1;
                int step = widgetWidth / 20;

                int affectButtonSelected = 0;

                tvDisplayEmotion = (TextView)findViewById(R.id.tvDisplayEmotion);

                if (x > 9 * step && x <= 11 * step && y > 9 * step && y <= 11 * step){
                    touchView.setBackgroundResource(R.drawable.ab0);
                    affectButtonSelected = 0;
                    chosenMood = "neutral";
                    Log.d("MOOD CLICKED: ", chosenMood);
                }
                else if(y <= 9*step){
                    if (x <= step){
                        touchView.setBackgroundResource(R.drawable.ab9);
                        affectButtonSelected = 9;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > step && x <= 2*step){
                        touchView.setBackgroundResource(R.drawable.ab8);
                        affectButtonSelected = 8;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 2*step && x <= 3 * step){
                        touchView.setBackgroundResource(R.drawable.ab7);
                        affectButtonSelected = 7;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 3*step && x <= 4 * step){
                        touchView.setBackgroundResource(R.drawable.ab6);
                        affectButtonSelected = 6;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 4*step && x <= 5 * step){
                        touchView.setBackgroundResource(R.drawable.ab5);
                        affectButtonSelected = 5;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 5*step && x <= 6 * step){
                        touchView.setBackgroundResource(R.drawable.ab4);
                        affectButtonSelected = 4;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 6*step && x <= 7 * step){
                        touchView.setBackgroundResource(R.drawable.ab3);
                        affectButtonSelected = 3;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 7*step && x <= 8 * step){
                        touchView.setBackgroundResource(R.drawable.ab2);
                        affectButtonSelected = 2;
                        chosenMood = "angry";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 8*step && x <= 9 * step){
                        touchView.setBackgroundResource(R.drawable.ab1);
                        affectButtonSelected = 1;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x >= 20 * step){
                        touchView.setBackgroundResource(R.drawable.ab18);
                        affectButtonSelected = 18;
                        chosenMood = "excited";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 20 * step && x >= 19*step){
                        touchView.setBackgroundResource(R.drawable.ab17);
                        affectButtonSelected = 17;
                        chosenMood = "excited";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 19 * step && x >= 18 * step){
                        touchView.setBackgroundResource(R.drawable.ab16);
                        affectButtonSelected = 16;
                        chosenMood = "excited";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 18 * step && x >= 17 * step){
                        touchView.setBackgroundResource(R.drawable.ab15);
                        affectButtonSelected = 15;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 17 * step && x >= 16 * step){
                        touchView.setBackgroundResource(R.drawable.ab14);
                        affectButtonSelected = 14;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 16 * step && x >= 15 * step){
                        touchView.setBackgroundResource(R.drawable.ab13);
                        affectButtonSelected = 13;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 15 * step && x >= 14 * step){
                        touchView.setBackgroundResource(R.drawable.ab12);
                        affectButtonSelected = 12;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 14 * step && x >= 13 * step) {
                        touchView.setBackgroundResource(R.drawable.ab11);
                        affectButtonSelected = 11;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 13 * step && x >= 12 * step){
                        touchView.setBackgroundResource(R.drawable.ab10);
                        affectButtonSelected = 10;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                }
                else if (y >= 12 * step){
                    if (x <= step){
                        touchView.setBackgroundResource(R.drawable.ab27);
                        affectButtonSelected = 27;
                        chosenMood = "surprised";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > step && x <= 2 * step){
                        touchView.setBackgroundResource(R.drawable.ab26);
                        affectButtonSelected = 26;
                        chosenMood = "surprised";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 2 * step && x <= 3 * step){
                        touchView.setBackgroundResource(R.drawable.ab25);
                        affectButtonSelected = 25;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 3 * step && x <= 4 * step){
                        touchView.setBackgroundResource(R.drawable.ab24);
                        affectButtonSelected = 24;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 4 * step && x <= 5 * step){
                        touchView.setBackgroundResource(R.drawable.ab23);
                        affectButtonSelected = 23;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 5 * step && x <= 6 * step){
                        touchView.setBackgroundResource(R.drawable.ab22);
                        affectButtonSelected = 22;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 6 * step && x <= 7 * step){
                        touchView.setBackgroundResource(R.drawable.ab21);
                        affectButtonSelected = 21;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 7 * step && x <= 8 * step){
                        touchView.setBackgroundResource(R.drawable.ab20);
                        affectButtonSelected = 20;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x > 8 * step && x <= 9 * step){
                        touchView.setBackgroundResource(R.drawable.ab19);
                        affectButtonSelected = 19;
                        chosenMood = "sad";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x >= 20 * step){
                        touchView.setBackgroundResource(R.drawable.ab36);
                        affectButtonSelected = 36;
                        chosenMood = "surprised";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 20 * step && x >= 19 * step){
                        touchView.setBackgroundResource(R.drawable.ab35);
                        affectButtonSelected = 35;
                        chosenMood = "surprised";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 19 * step && x >= 18 * step){
                        touchView.setBackgroundResource(R.drawable.ab34);
                        affectButtonSelected = 34;
                        chosenMood = "surprised";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 18 * step && x >= 17 * step){
                        touchView.setBackgroundResource(R.drawable.ab33);
                        affectButtonSelected = 33;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 17 * step && x >= 16 * step){
                        touchView.setBackgroundResource(R.drawable.ab32);
                        affectButtonSelected = 32;
                        chosenMood = "happy";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 16 * step && x >= 15 * step){
                        touchView.setBackgroundResource(R.drawable.ab31);
                        affectButtonSelected = 31;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 15 * step && x >= 14 * step){
                        touchView.setBackgroundResource(R.drawable.ab30);
                        affectButtonSelected = 30;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 14 * step && x >= 13 * step){
                        touchView.setBackgroundResource(R.drawable.ab29);
                        affectButtonSelected = 29;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                    else if (x < 13 * step && x >= 12 * step){
                        touchView.setBackgroundResource(R.drawable.ab28);
                        affectButtonSelected = 28;
                        chosenMood = "bored";
                        Log.d("MOOD CLICKED: ", chosenMood);
                    }
                }

                Log.d("X axis: " + Float.toString(x), ", Y axis: " + Float.toString(y));
                Log.d("AffectButtonSelected: ", Integer.toString(affectButtonSelected));

                tvDisplayEmotion.setText(chosenMood);

            }
        });


        ///////////////////////////////////////////////////////////////////////////////////

        Log.d("RECEIVED STORY2 URL: ", workloadUrl);

        imgView = (ImageView) findViewById(R.id.ivStoryPhoto2);
        imgLoader = new ImageLoader(this);

        bNext2 = (Button) findViewById(R.id.bNext2);
        bNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WriteStory2.this, WriteStory3.class);

                String getStoryId = workloadId.toString();
                String getStoryPath = workloadUrl.toString();
                String getLoc = getLocation.toString();
                String getMood = chosenMood.toString();

                i.putExtra("WorkloadId", getStoryId);
                i.putExtra("WorkloadUrl", getStoryPath);
                i.putExtra("GetLocation", getLoc);
                i.putExtra("GetMood", getMood);

                startActivity(i);
                //finish();
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
                    //storyPath = "http://10.0.2.2/silverproductivity/"+path;     // emulator
                    storyPath = "http://www.agelesslily.org/liusiyuan/silverproductivity/"+path;
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
        //imgLoader.DisplayImage("http://10.0.2.2/silverproductivity/"+workloadUrl, imgView);     // emulator
        imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/"+workloadUrl, imgView);
    }

    public class LoadDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteStory2.this);
            pDialog.setMessage("Loading...");
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
        getMenuInflater().inflate(R.menu.menu_write_story2, menu);
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
