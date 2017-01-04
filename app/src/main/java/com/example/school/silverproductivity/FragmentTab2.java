package com.example.school.silverproductivity;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentTab2 extends ListFragment {
    private static final String ALL_USERS_URL = Configure.server+"/silverproductivity/allUsers.php";
    private static final String FOLLOW_URL = Configure.server+"/silverproductivity/followUser.php";
    private ArrayList<Map<String, String>> rUsersList;
    private JSONArray rUsers = null;
    private static final String TAG_USERNAME = "username";
    private static final String TAG_FOLLOW = "followStatus";
    private static final String TAG_USERS = "users";
    private static final String TAG_USERID = "userId";

    private ListView list;
    private SimpleAdapter adapter;
    private List<NameValuePair> params;
    JSONParser jsonParser = new JSONParser();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listtab, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        getActivity().setTitle("Friends");
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("-=-=-=-"+position);
        Map<String, String> map = (Map<String, String>)adapter.getItem(position);
        System.out.println("-=-=-=-"+map.toString());
        String action = "";
        if (map.get("FollowStatus").equals("Unfollowed")){
            action = "follow";
        }else{
            action = "unfollow";
        }
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", action));
        params.add(new BasicNameValuePair("followerId", map.get("userId")));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = sp.getString("userId", "1");
        params.add(new BasicNameValuePair("followeeId", userId));
        new FollowUser().execute();
        new LoadUsers().execute();
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadUsers().execute();
    }

    public class FollowUser extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            JSONParser jParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(FOLLOW_URL, "POST", params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            adapter = new SimpleAdapter(getActivity(), rUsersList,
                    android.R.layout.simple_list_item_2, new String[]{"Name", "FollowStatus"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            setListAdapter(adapter);

        }

    }

    public class LoadUsers extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            //we will develop this method in version 2
            rUsersList = new ArrayList<Map<String, String>>();
            JSONParser jParser = new JSONParser();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String userId = sp.getString("userId", "1");
            JSONObject json = jParser.getJSONFromUrl(ALL_USERS_URL+"?userId="+userId);
            try{
                rUsers = json.getJSONArray(TAG_USERS);
                for (int i = 0; i < rUsers.length(); i++) {
                    JSONObject u = rUsers.getJSONObject(i);
                    String username = u.getString(TAG_USERNAME);
                    String followStatus = u.getString(TAG_FOLLOW);
                    String curUserId = u.getString(TAG_USERID);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Name", username);
                    map.put("FollowStatus", followStatus);
                    map.put("userId", curUserId);
                    rUsersList.add(map);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            adapter = new SimpleAdapter(getActivity(), rUsersList,
                    android.R.layout.simple_list_item_2, new String[]{"Name", "FollowStatus"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            setListAdapter(adapter);

        }
    }
}