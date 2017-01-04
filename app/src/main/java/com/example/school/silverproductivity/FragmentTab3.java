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
    private static final String REC_URL = Configure.server+"/silverproductivity/photoRecommendation.php";
    private static final String TAG_PHOTOS = "photos";
    private String[] imgList;
    ListView list;
    LazyAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
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
        adapter = new LazyAdapter(getActivity(), imgList);
        setListAdapter(adapter);
        listOfImages.clear();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgList = new String[listOfImages.size()];
        imgList = listOfImages.toArray(imgList);
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