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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//ActionBarActivity
public class WriteStory3 extends Activity {

    String workloadId, workloadUrl, getLocation, getMood;
    String storyId, storyPath, storyDate, storyOccDate, storyLabels, storyLabel1, storyLabel2, storyLabel3, storyLabel4, listLabels;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

    Button bNext3;

    OnClickListener checkBoxListener;


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
    private static final String TAG_LABEL1 = "label1";
    private static final String TAG_LABEL2 = "label2";
    private static final String TAG_LABEL3 = "label3";
    private static final String TAG_LABEL4 = "label4";
    //private static final String TAG_LABEL5 = "label5";

    //An array of all of our comments
    private JSONArray aStories = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aStoriesList;
    private ArrayList<HashMap<String, String>> aStoriesList;
    private ArrayList<String> selectedLabels = new ArrayList<String>();

    private ImageView imgView;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story3);

        setTitle("Share Your Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");
        getLocation = getIntent().getStringExtra("GetLocation");
        getMood = getIntent().getStringExtra("GetMood");

        Log.d("RECEIVED STORY3 URL: ", workloadUrl);
        Log.d("RECEIVED STORY3 LOC: ", getLocation);
        Log.d("RECEIVED STORY3 MOOD: ", getMood);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);

        // checkBox1.setText("TEST 1");
        checkBoxListener =new OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv.setText("I Like ");



                /*
                if(!cbS.isChecked()&amp;&amp;!cbW.isChecked()) {
                    tv.setText("");
                }
                */

            }
        };

        imgView = (ImageView) findViewById(R.id.ivStoryPhoto3);
        imgLoader = new ImageLoader(this);

        bNext3 = (Button) findViewById(R.id.bNext3);
        bNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WriteStory3.this, WriteStory4.class);

                if(checkBox1.isChecked()) {
                    //tv.setText(tv.getText().toString()+" "+ cbS.getText().toString());
                    // storyLabel1
                    selectedLabels.add(storyLabel1);
                }

                if(checkBox2.isChecked()) {
                    //tv.setText(tv.getText().toString()+ " "+cbW.getText().toString());
                    // storyLabel2
                    selectedLabels.add(storyLabel2);
                }

                if(checkBox3.isChecked()) {
                    //tv.setText(tv.getText().toString()+ " "+cbW.getText().toString());
                    // storyLabel3
                    selectedLabels.add(storyLabel3);
                }

                if(checkBox4.isChecked()) {
                    //tv.setText(tv.getText().toString()+ " "+cbW.getText().toString());
                    // storyLabel4
                    selectedLabels.add(storyLabel4);
                }

                /*
                int i = 1;
                for (String str : arrayString) {
                    if (i++ == arrayString.length) {
                        // end
                    }
                }
                 */
                listLabels = "";
                int a =1;
                for (String s : selectedLabels)
                {
                    //listLabels += s + "\t";
                    if(a++ == selectedLabels.size()){
                        listLabels += s;
                    }
                    else{
                        listLabels += s + ", ";
                    }
                }
                Log.d("SELECTED LABELS", listLabels);

                selectedLabels.clear();

                String getStoryId = workloadId.toString();
                String getStoryPath = workloadUrl.toString();
                String getLoc = getLocation.toString();
                String getSavedMood = getMood.toString();
                String getSelectedLabels = listLabels.toString();

                i.putExtra("WorkloadId", getStoryId);
                i.putExtra("WorkloadUrl", getStoryPath);
                i.putExtra("GetLocation", getLoc);
                i.putExtra("GetMood", getSavedMood);
                i.putExtra("Labels", getSelectedLabels);

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

    public void updateJSONdata(){

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

                String lab1 = c.getString(TAG_LABEL1);
                String lab2 = c.getString(TAG_LABEL2);
                String lab3 = c.getString(TAG_LABEL3);
                String lab4 = c.getString(TAG_LABEL4);

                if(workloadUrl.matches(path)){
                    storyId = id;
                    //storyPath = "http://10.0.2.2/silverproductivity/"+path;     // emulator
                    storyPath = "http://www.agelesslily.org/liusiyuan/silverproductivity/"+path;
                    storyDate = subDate;
                    storyOccDate = occDate;
                    storyLabels = labels;

                    storyLabel1 = lab1;
                    storyLabel2 = lab2;
                    storyLabel3 = lab3;
                    storyLabel4 = lab4;
                }

            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void updateList(){
        checkBox1.setText(storyLabel1);
        checkBox2.setText(storyLabel2);
        checkBox3.setText(storyLabel3);
        checkBox4.setText(storyLabel4);
        //imgLoader.DisplayImage("http://10.0.2.2/silverproductivity/" + workloadUrl, imgView);       // emulator
        imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/" + workloadUrl, imgView);
    }

    public class LoadDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WriteStory3.this);
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
        getMenuInflater().inflate(R.menu.menu_write_story3, menu);
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
