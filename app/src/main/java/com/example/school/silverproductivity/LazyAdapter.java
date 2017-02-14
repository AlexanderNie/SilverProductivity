package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WorkStation on 18/8/2015.
 */
public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private String[] data, comments;
    private Integer[] favorites, storyids;
    private String userid;

    private static boolean like;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    JSONParser jsonParser = new JSONParser();
    private List<NameValuePair> params;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String LIKE_URL = Configure.server + "/silverproductivity/likePhoto.php";

    public LazyAdapter(Activity a, String[] d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public LazyAdapter(Activity a, String[] d, Integer[] favorites, String[] comments, Integer[] storyids, String userid) {
        activity = a;
        data = d;
        this.favorites = favorites;
        this.comments = comments;
        this.storyids = storyids;
        this.userid = userid;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public static boolean isLike() {
        return like;
    }

    public static void setLike(boolean like) {
        LazyAdapter.like = like;
    }


    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.fragment_single_workload, null);
        final EditText editComment = (EditText) vi.findViewById(R.id.editComments);
        Button btnComment = (Button) vi.findViewById(R.id.btnComments);
        ImageButton btnMsg = (ImageButton) vi.findViewById(R.id.btnMessage);

        TextView text = (TextView) vi.findViewById(R.id.urlText);
        ImageView image = (ImageView) vi.findViewById(R.id.ivSingleWorkloadImg);
        LikeButton btnLike = (LikeButton) vi.findViewById(R.id.btnLike);
        params = new ArrayList<NameValuePair>();

        if (!isLike()) {

            btnLike.setVisibility(View.GONE);
            editComment.setVisibility(View.GONE);
            btnComment.setVisibility(View.GONE);
            btnMsg.setVisibility(View.GONE);
        } else {
            btnLike.setVisibility(View.VISIBLE);
            editComment.setVisibility(View.VISIBLE);
            btnComment.setVisibility(View.VISIBLE);
            btnMsg.setVisibility(View.VISIBLE);
            if (favorites[position].equals(1))
                btnLike.setLiked(true);
            else
                btnLike.setLiked(false);
            editComment.setText(comments[position]);
        }


        params.add(new BasicNameValuePair("userid", userid));

//        if (isLike()) {
//
//        }
        btnLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                params.add(new BasicNameValuePair("favorite", "1"));
                params.add(new BasicNameValuePair("storyid", storyids[position].toString()));
                new LikeStory().execute();
                favorites[position] = 1;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                params.add(new BasicNameValuePair("favorite", "0"));
                params.add(new BasicNameValuePair("storyid", storyids[position].toString()));
                new LikeStory().execute();
                favorites[position] = 0;
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.add(new BasicNameValuePair("storyid", storyids[position].toString()));
                params.add(new BasicNameValuePair("comments", editComment.getText().toString()));
                params.add(new BasicNameValuePair("favorite", favorites[position].toString() ));
                new LikeStory().execute();
                comments[position]=editComment.getText().toString();
            }
        });

        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("storyid", storyids[position].toString());
                edit.commit();

                showMessage();
            }
        });



        //if(position >= 0){
        text.setText("item " + position);
        imageLoader.DisplayImage(Configure.server + "/silverproductivity/" + data[position], image);
        // imageLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/"+data[position], image);
        Log.d("Img Url: ", data[position]);
        //}
        return vi;
    }

    private void showMessage() {
        // Instantiate a new fragment.
        ListCommentFragment newFragment = new ListCommentFragment();

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        ft.replace(R.id.fragment_container1, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    public class LikeStory extends AsyncTask<String, String, String> {
        int success;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params1) {

//          JSONParser jParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(LIKE_URL, "POST", params);
            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(activity, "comment posted", Toast.LENGTH_LONG).show();
        }

    }
}