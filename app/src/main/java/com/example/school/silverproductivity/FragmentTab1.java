package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentTab1 extends Fragment {

    Button btnAddMoreWorkload;

    // Progress Dialog
    private ProgressDialog pDialog;

    //testing from a real server:
    //private static final String ALL_STORIES_URL = "http://syidah.web44.net/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://127.0.0.1/silverproductivity/showAllStories.php";
    //private static final String ALL_STORIES_URL = "http://localhost/silverproductivity/showAllStories.php";


    private static final String ALL_STORIES_URL = Configure.server+"/silverproductivity/showAllStories.php";       // emulator
    private static final String ALL_WORKLOAD_URL = Configure.server+"/silverproductivity/showAllWorkload.php";     // emulator
    // private static final String ALL_STORIES_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllStories.php";
    // private static final String ALL_WORKLOAD_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/showAllWorkload.php";


    //JSON IDS: (from server)
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_WORKLOAD = "workload";

    private static final String TAG_WORKLOAD_ID = "workloadId";
    private static final String TAG_PHOTO_ID = "photoId";
    private static final String TAG_WORKLOAD_PATH = "photoUrl";
    private static final String TAG_WORKLOAD_USERNAME = "workloadUsername";
    private static final String TAG_WORKLOAD_DATETIME = "workloadDateTime";

    // ImageView im1, im2, im3;
    //Bitmap image;

    ListView list;
    LazyAdapter adapter;

    //An array of all of our comments
    private JSONArray aWorkload = null;
    //manages all of our comments in a list.
    //private ArrayList<HashMap<String, Object>> aWorkloadList;
    private ArrayList<HashMap<String, String>> aWorkloadList;

    private ArrayList<String> listOfImages = new ArrayList<String>();
    private String[] imgList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_workload, container, false);

        getActivity().setTitle("My Stories");

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddMoreWorkload = (Button) getView().findViewById(R.id.btnAddMoreWorkload);

        btnAddMoreWorkload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QueueList.class);
                startActivity(i);
            }
        });
    }

    /*
    private String imageUrls[] = {
            "http://syidah.web44.net/silverproductivity/images/13-08-2015-1439454972.jpg",
            "http://syidah.web44.net/silverproductivity/images/13-08-2015-1439455226.jpg",
            "http://syidah.web44.net/silverproductivity/images/13-08-2015-1439455285.jpg",
            "http://syidah.web44.net/silverproductivity/images/13-08-2015-1439455343.jpg",
    };
    */


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadWorkload().execute();
    }

    public void updateJSONdata() {
        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String saved_username = sp.getString("username", "anon");

        aWorkloadList = new ArrayList<HashMap<String, String>>();


        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(ALL_WORKLOAD_URL);

        try {

            //mComments will tell us how many "posts" or comments are available
            aWorkload = json.getJSONArray(TAG_WORKLOAD);

            // TAG_WORKLOAD, TAG_WORKLOAD_ID, TAG_PHOTO_ID, TAG_WORKLOAD_PATH, TAG_WORKLOAD_USERNAME, TAG_WORKLOAD_DATETIME

            // looping through all posts according to the json object returned
            for (int i = 0; i < aWorkload.length(); i++) {
                JSONObject c = aWorkload.getJSONObject(i);

                //gets the content of each tag from server
                String workloadId = c.getString(TAG_WORKLOAD_ID);
                String photoId = c.getString(TAG_PHOTO_ID);
                String path = c.getString(TAG_WORKLOAD_PATH);
                String workloadUser = c.getString(TAG_WORKLOAD_USERNAME);
                String workloadDateTime = c.getString(TAG_WORKLOAD_DATETIME);

                if((saved_username.toLowerCase()).matches(workloadUser.toLowerCase())){
                    // listOfImages.add("http://10.0.2.2/silverproductivity/" + path);
                    listOfImages.add(path);
                }

                imgList = new String[listOfImages.size()];
                imgList = listOfImages.toArray(imgList);

                /*
                URL urlConnection = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                */

                /*
                ImageView imagy = (ImageView) getView().findViewById(R.id.ivSingleWorkloadImg);
                imagy.setImageBitmap(myBitmap);

                //image = myBitmap;
                */

                /*
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_STORY_ID, id);
                map.put(TAG_STORY_PATH, path);
                map.put(TAG_STORY_DATE, subDate);
                //map.put("bitmapImg", myBitmap);

                aWorkloadList.add(map);
                */


                // SHOW ALL, uncomment this
                // mCommentList.add(map);
                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */
    }

    private void updateList() {

        /*
        lview = (ListView) findViewById(R.id.listView1);
        lview.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,month));
        lview.setOnItemClickListener(this);
        */

        list=(ListView) getView().findViewById(R.id.list);

        //adapter = new LazyAdapter(getActivity(), imageUrls);
        adapter = new LazyAdapter(getActivity(), imgList);
        list.setAdapter(adapter);
        //list.setOnItemClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Item clicked => " + imgList[position], Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), WorkloadDetails.class);

                String getImageUrl = imgList[position].toString();
                i.putExtra("ImageUrl", getImageUrl);

                startActivity(i);

            }
        });

        listOfImages.clear();

        /*
        ListAdapter adapter = new SimpleAdapter(getActivity(), aWorkloadList,
                //R.layout.fragment_single_workload, new String[] { "bitmapImg", TAG_STORY_PATH }, new int[] { R.id.ivSingleWorkloadImg, R.id.urlText });
                //R.layout.fragment_single_workload, new String[] { "bitmapImg" }, new int[] { R.id.ivSingleWorkloadImg });
                //R.layout.fragment_single_workload, new String[] { TAG_STORY_PATH }, new int[] { R.id.urlText });
                R.layout.fragment_single_workload, new String[] { TAG_STORY_PATH }, new int[] { R.id.ivSingleWorkloadImg });

        SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view.getId() == R.id.ivSingleWorkloadImg) {
                    //((ImageView) view).setImageBitmap((Bitmap) data);



                    return true;
                }
                else if (view.getId() == R.id.urlText) {
                    ((TextView) view).setText((String) data);
                    return true;
                }
                return false;
            }
        };
        ((SimpleAdapter) adapter).setViewBinder(viewBinder);

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
    }

    /*
    // THIS METHOD ALREADY IMPLEMENTED INSIDE ONITEMCLICK METHOD
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"Item clicked => "+imgList[position], Toast.LENGTH_SHORT).show();
    }
    */

    public class LoadWorkload extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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





}