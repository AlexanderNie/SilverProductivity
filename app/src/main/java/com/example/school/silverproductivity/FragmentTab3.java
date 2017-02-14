package com.example.school.silverproductivity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.net.URLEncoder;

public class FragmentTab3 extends ListFragment {
    private ArrayList<String> listOfImages = new ArrayList<String>();
    private ArrayList<String> listOfComments = new ArrayList<String>();
    private ArrayList<Integer> listOfFavors = new ArrayList<Integer>();
    private ArrayList<Integer> listOfStoryIds = new ArrayList<Integer>();
    private static final String REC_URL = Configure.server+"/silverproductivity/photoRecommendation.php";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_COMMENTS = "comments";
    private static final String TAG_FAVORS= "favorites";
    private static final String TAG_STORYID= "storyid";

    private String[] imgList, cmtList;
    private  Integer[] favorList, storyids;

    ListView list;
    LazyAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        LazyAdapter.setLike(true);
        View view = inflater.inflate(R.layout.listtab, container, false);
        getActivity().setTitle("Recommendation Photos");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadWorkload().execute();
    }

    private void updateList() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = sp.getString("userId", "1");
        adapter = new LazyAdapter(getActivity(), imgList, favorList, cmtList, storyids, userId);
        setListAdapter(adapter);
        listOfImages.clear();
        listOfStoryIds.clear();
        listOfFavors.clear();
        listOfComments.clear();
    }

    public void updateJSONdata() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String saved_userId = sp.getString("userId", "1");
        String location = "Bukit Panjang";
        //String location = CityLocation.getCNBylocation(getActivity());
        try{
            location = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(REC_URL+"?userId="+saved_userId+"&location="+location);
        System.out.println("===="+json.toString());
        try {
            JSONArray photos = json.getJSONArray(TAG_PHOTOS);
            for (int i = 0; i < photos.length(); i++) {
                listOfImages.add(photos.getString(i));
            }

            JSONArray favors = json.getJSONArray(TAG_FAVORS);
            for (int i = 0; i < favors.length(); i++) {
                listOfFavors.add(favors.getInt(i));
            }

            JSONArray storyIds = json.getJSONArray(TAG_STORYID);
            for (int i = 0; i < favors.length(); i++) {
                listOfStoryIds.add(storyIds.getInt(i));
            }

            JSONArray comments = json.getJSONArray(TAG_COMMENTS);
            for (int i = 0; i < comments.length(); i++) {
                listOfComments.add(comments.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgList = new String[listOfImages.size()];
        imgList = listOfImages.toArray(imgList);
        cmtList = new String[listOfComments.size()];
        cmtList = listOfComments.toArray(cmtList);
        favorList = new Integer[listOfFavors.size()];
        favorList = listOfFavors.toArray(favorList);
        storyids  = new Integer[listOfStoryIds.size()];
        storyids  = listOfStoryIds.toArray(storyids);

    }
    public class LoadWorkload extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            updateJSONdata();
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            updateList();
        }
    }
}