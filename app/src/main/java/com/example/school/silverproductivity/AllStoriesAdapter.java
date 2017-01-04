package com.example.school.silverproductivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WorkStation on 9/10/2015.
 */
public class AllStoriesAdapter extends BaseAdapter {
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    // String testThis;

    public AllStoriesAdapter(Activity a, String[] d) {
        activity = a;
        data = d ;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
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

    public View getView(int position, View convertView, ViewGroup parent) {
        //ViewHolder viewHolder;
        View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.activity_single_comment, null);

            TextView uname =(TextView)vi.findViewById(R.id.tvUsername);
            TextView umood =(TextView)vi.findViewById(R.id.tvMood);
            TextView bMoreInfo =(TextView)vi.findViewById(R.id.bMoreInfo);

            uname.setText(data[position]);
            umood.setText("Mood");

            //testThis = data[position];

            bMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.d("Button is being clicked, Story ID: ", testThis);
                    Log.d("Button is clicked.", "");

                }
            });



            /*
            viewHolder = new ViewHolder();
            viewHolder.tvUser = (TextView) vi.findViewById(R.id.tvUsername);
            viewHolder.tvMood = (TextView) vi.findViewById(R.id.tvMood);
            viewHolder.bMoreInfo = (Button) vi.findViewById(R.id.bMoreInfo);
            convertView.setTag(viewHolder);
            */

            /*
            TextView tvUser, tvMood, bMoreInfo;

            tvUser = (TextView) vi.findViewById(R.id.tvUsername);
            tvMood = (TextView) vi.findViewById(R.id.tvMood);
            bMoreInfo = (Button) vi.findViewById(R.id.bMoreInfo);
            bMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("BUTTON IS CLICKED: ", data[position]);
                }
            });
            */

        }


        // tvUsername, tvMood, bMoreInfo

        //TextView tvUser=(TextView)vi.findViewById(R.id.tvUsername);
        //TextView tvMood=(TextView)vi.findViewById(R.id.tvMood);
        //Button bMoreInfo=(Button)vi.findViewById(R.id.bMoreInfo);

        return vi;
    }

    /*
    public class ViewHolder {
        TextView tvUser, tvMood;
        Button bMoreInfo;
    }
    */

}
