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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//ActionBarActivity
public class WriteStory1 extends Activity {

    String workloadId, workloadUrl, chosenLocation;
    String storyId, storyPath, storyDate, storyOccDate, storyLabels;
    ImageView ivStoryPhoto;
    Button bNext1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story1);

        setTitle("Share Your Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");


        //Log.d("RECEIVED STORY ID: ", workloadId);
        Log.d("RECEIVED STORY URL: ", workloadUrl);

        /*
        ivStoryPhoto = (ImageView) findViewById(R.id.ivStoryPhoto);
        ivStoryPhoto.setImageResource(R.drawable.dummyphoto);
        */

        ivStoryPhoto = (ImageView) findViewById(R.id.ivStoryPhoto);

        imgView = (ImageView) findViewById(R.id.ivStoryPhoto);
        imgLoader = new ImageLoader(this);

        Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.street_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                chosenLocation = (String) parent.getItemAtPosition(position);
                //Log.d("item", (String) parent.getItemAtPosition(position));
                if(chosenLocation.matches("-Choose-")){
                    chosenLocation = "";
                }

                Log.d("item", chosenLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        /*
        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        */

        bNext1 = (Button) findViewById(R.id.bNext1);
        bNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WriteStory1.this, WriteStory2.class);

                String getStoryId = workloadId.toString();
                String getStoryPath = workloadUrl.toString();
                String getLocation = chosenLocation.toString();

                i.putExtra("WorkloadId", getStoryId);
                i.putExtra("WorkloadUrl", getStoryPath);
                i.putExtra("GetLocation", getLocation);

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
            pDialog = new ProgressDialog(WriteStory1.this);
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
        getMenuInflater().inflate(R.menu.menu_write_story1, menu);
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
