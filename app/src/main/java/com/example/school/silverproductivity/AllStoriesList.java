package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


//ActionBarActivity
public class AllStoriesList extends Activity {

    ListView allsamestorylist;
    AllStoriesAdapter adapter;

    String workloadId, workloadUrl;
    String storyId, photoId, photoUrl, username, location, mood, label, description, dateTime;
    ImageView ivAllUserStories;

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

    //An array of all of our comments
    private JSONArray aStories = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aStoriesList;
    //private ArrayList<HashMap<String, String>> aStoriesList;

    //private ArrayList<HashMap<String, String>> aUploadedStories;

    private ArrayList<String> aUploadedStories = new ArrayList<String>();
    private String[] sameStoryList;

    private ImageView imgView;
    private ImageLoader imgLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stories_list);

        setTitle("Stories");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");

        Log.d("SUBMITTED ID: ", workloadId);
        Log.d("SUBMITTED URL: ", workloadUrl);

        imgView = (ImageView) findViewById(R.id.ivAllUserStories);
        imgLoader = new ImageLoader(this);

        ivAllUserStories = (ImageView) findViewById(R.id.ivAllUserStories);
        //listDesc = (ListView) findViewById(R.id.listDescription);

        aUploadedStories.clear();           // to ensure there is nothing in the array in the beginning
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadDetails().execute();
    }

    public void updateJSONdata() {
        //aUploadedStories = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(UPLOADED_STORIES_URL);

        try{
            aStories = json.getJSONArray(TAG_STORIES);

            aUploadedStories.clear();

            // looping through all posts according to the json object returned
            for (int i = 0; i < aStories.length(); i++) {
                JSONObject c = aStories.getJSONObject(i);

                String sId = c.getString(TAG_STORY_ID);
                String pId = c.getString(TAG_PHOTO_ID);
                String pUrl = c.getString(TAG_PHOTO_URL);
                String user = c.getString(TAG_USERNAME);
                String loc = c.getString(TAG_LOCATION);
                String moo = c.getString(TAG_MOOD);
                String lab = c.getString(TAG_LABEL);
                String desc = c.getString(TAG_DESC);
                String dt = c.getString(TAG_DATETIME);

                if((workloadId.toLowerCase()).matches(pId.toLowerCase())){
                    aUploadedStories.add(sId);
                }

                /*
                HashMap<String, String> map = new HashMap<String, String>();

                if(workloadId.matches(pId)){
                    map.put(TAG_STORY_ID, sId);
                    map.put(TAG_PHOTO_ID, pId);
                    map.put(TAG_PHOTO_URL, pUrl);
                    map.put(TAG_USERNAME, user);
                    map.put(TAG_LOCATION, loc);
                    map.put(TAG_MOOD, moo);
                    map.put(TAG_LABEL, lab);
                    map.put(TAG_DESC, desc);
                    map.put(TAG_DATETIME, dt);

                    aUploadedStories.add(map);
                }
                */

                sameStoryList = new String[aUploadedStories.size()];
                sameStoryList = aUploadedStories.toArray(sameStoryList);

                if(sameStoryList != null){
                    //Log.d("List of DATA: ", sameStoryList.toString());
                    Arrays.asList(sameStoryList);
                }

            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void updateList(){
        /* To Display The Workload Image on top */
        imgLoader.DisplayImage(Configure.server+"/silverproductivity/" + workloadUrl, imgView);
        //imgLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/" + workloadUrl, imgView);

        allsamestorylist = (ListView) findViewById(R.id.allSameStoryList);
        adapter = new AllStoriesAdapter(this, sameStoryList);

        allsamestorylist.setAdapter(adapter);

        allsamestorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AllStoriesList.this, Testing.class);

                Toast.makeText(AllStoriesList.this, "Item clicked => " + sameStoryList[position], Toast.LENGTH_SHORT).show();

                //Intent i = new Intent(AllStoriesList.this, QueueDetails.class);

                Log.d("Row was clicked: ", sameStoryList[position]);

                startActivity(i);

            }
        });

        // aUploadedStories.clear();

        ////////////////////////////////////////////////////////////////////////////////////////

        /*


        //Log.d("TO VIEW PHOTO >>>> ", "http://10.0.2.2/silverproductivity/" + workloadUrl);      // emulator
        Log.d("TO VIEW PHOTO >>>> ", "http://www.agelesslily.org/liusiyuan/silverproductivity/" + workloadUrl);

        ListAdapter adapter = new SimpleAdapter(this, aUploadedStories,
                //R.layout.fragment_single_workload, new String[] { "bitmapImg", TAG_STORY_PATH }, new int[] { R.id.ivSingleWorkloadImg, R.id.urlText });
                //R.layout.fragment_single_workload, new String[] { "bitmapImg" }, new int[] { R.id.ivSingleWorkloadImg });
                //R.layout.fragment_single_workload, new String[] { TAG_STORY_PATH }, new int[] { R.id.urlText });
                //R.layout.fragment_single_workload, new String[] { TAG_STORY_PATH }, new int[] { R.id.ivSingleWorkloadImg });
                R.layout.activity_single_comment, new String[] { TAG_USERNAME, TAG_MOOD }, new int[] { R.id.tvUsername, R.id.tvMood });

        //listDesc = (ListView) findViewById(R.id.listDescription);

        setListAdapter(adapter);
        //listDesc.setAdapter(adapter);
        // Optional: when the user clicks a list item we could do something.  However, we will choose to do nothing...


        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d("Button is being clicked:", " OUTSIDE");

                Button testt;

                testt = (Button) findViewById(R.id.bMoreInfo);
                testt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Button is being clicked:", " INSIDE");
                    }
                });


                // This method is triggered if an item is click within our list. For our example we won't be using this, but it is useful to know in real life applications.
                Intent i = new Intent(AllStoriesList.this, EachStoryDetails.class);
                startActivity(i);

            }
        });
        */

    }

    public class LoadDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllStoriesList.this);
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
        getMenuInflater().inflate(R.menu.menu_all_stories_list, menu);
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
