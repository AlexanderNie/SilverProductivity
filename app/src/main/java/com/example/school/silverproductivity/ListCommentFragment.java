package com.example.school.silverproductivity;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 3/2/2017.
 */

public class ListCommentFragment extends ListFragment {

    private SimpleAdapter adapter;
    private ListView list;
    private List<NameValuePair> params;
    JSONParser jsonParser = new JSONParser();

    private static final String ALL_COMMENTS_URL = Configure.server+"/silverproductivity/allComments.php";

    private ArrayList<Map<String, String>> rCommentList;
    private JSONArray rComments = null;
    private static final String TAG_USERNAME = "username";
    private static final String TAG_CMT = "comments";
    private static final String TAG_QUEUE = "queue";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listtab, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        getActivity().setTitle("Comments");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.street_array, android.R.layout.simple_list_item_1);
//        setListAdapter(adapter);
        new LoadComments().execute();
    }


    public class LoadComments extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            //we will develop this method in version 2
            rCommentList = new ArrayList<Map<String, String>>();
            JSONParser jParser = new JSONParser();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String storyid = sp.getString("storyid", "1");
            //String storyid = "35";
            JSONObject json = jParser.getJSONFromUrl(ALL_COMMENTS_URL+"?storyid="+storyid);
            try{
                rComments = json.getJSONArray(TAG_QUEUE);
                for (int i = 0; i < rComments.length(); i++) {
                    JSONObject u = rComments.getJSONObject(i);
                    String username = u.getString(TAG_USERNAME);
                    String comment = u.getString(TAG_CMT);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Name", username);
                    map.put("Comment", comment);

                    rCommentList.add(map);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            adapter = new SimpleAdapter(getActivity(), rCommentList,
                    android.R.layout.simple_list_item_2, new String[]{"Name", "Comment"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            setListAdapter(adapter);

        }
    }

}
