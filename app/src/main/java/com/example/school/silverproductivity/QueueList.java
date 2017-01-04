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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// ActionBarActivity
public class QueueList extends Activity {

    ListView queuelist;
    LazyAdapterQueue adapter;
    Button bBackToWorkload;

    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String ALL_QUEUE_URL = Configure.server+"/silverproductivity/showAllQueue.php";      // emulator
    // private static final String ALL_QUEUE_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllQueue.php";

    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_QUEUE = "queue";

    private static final String TAG_QUEUE_ID = "queueId";
    private static final String TAG_PHOTO_ID = "photoId";
    private static final String TAG_QUEUE_PATH = "photoUrl";
    private static final String TAG_QUEUE_USERNAME = "queueUsername";
    private static final String TAG_QUEUE_DATETIME = "queueDateTime";

    //An array of all of our comments
    private JSONArray aQueue = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aWorkloadList;
    private ArrayList<HashMap<String, String>> aQueueList;

    private ArrayList<String> listOfImages = new ArrayList<String>();
    private String[] imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_list);

        setTitle("Pending Stories");


        bBackToWorkload = (Button) findViewById(R.id.bBackToWorkload);
        bBackToWorkload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabBar.class);      // link back to the main tabbar
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        // clear WriteStory 1 to 4
                    startActivity(intent);
            }
        });



    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadQueue().execute();
    }

    public void updateJSONdata() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_username = sp.getString("username", "anon");

        aQueueList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(ALL_QUEUE_URL);

        try {
            aQueue = json.getJSONArray(TAG_QUEUE);

            for (int i = 0; i < aQueue.length(); i++) {
                JSONObject c = aQueue.getJSONObject(i);

                //gets the content of each tag from server
                String queueId = c.getString(TAG_QUEUE_ID);
                String photoId = c.getString(TAG_PHOTO_ID);
                String path = c.getString(TAG_QUEUE_PATH);
                String queueUser = c.getString(TAG_QUEUE_USERNAME);
                String queueDateTime = c.getString(TAG_QUEUE_DATETIME);

                if((saved_username.toLowerCase()).matches(queueUser.toLowerCase())){
                    listOfImages.add(path);
                }

                imgList = new String[listOfImages.size()];
                imgList = listOfImages.toArray(imgList);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateList() {
        queuelist = (ListView) findViewById(R.id.queueList);

        adapter = new LazyAdapterQueue(this, imgList);
        queuelist.setAdapter(adapter);
        queuelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(QueueList.this, "Item clicked => " + imgList[position], Toast.LENGTH_SHORT).show();

                Intent i = new Intent(QueueList.this, QueueDetails.class);

                String getImageUrl = imgList[position].toString();
                i.putExtra("QueueImageUrl", getImageUrl);

                startActivity(i);
            }
        });

        listOfImages.clear();
    }

    public class LoadQueue extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QueueList.this);
            pDialog.setMessage("Loading Workload...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        //protected Bitmap doInBackground(Void... arg0) {
        protected Bitmap doInBackground(Void... params) {

            //we will develop this method in version 2

            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //we will develop this method in version 2
            updateList();

        }
    }

    /*
    @Override
    CreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_queue_list, menu);
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
