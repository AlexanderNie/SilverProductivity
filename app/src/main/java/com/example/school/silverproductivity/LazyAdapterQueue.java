package com.example.school.silverproductivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WorkStation on 9/9/2015.
 */
public class LazyAdapterQueue extends BaseAdapter {
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapterQueue(Activity a, String[] d) {
        activity = a;
        data=d;
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_single_queue, null);

        TextView text =(TextView)vi.findViewById(R.id.urlQueueText);

        ImageView image =(ImageView)vi.findViewById(R.id.ivSingleQueueImg);

        //Button bAccept = (Button)vi.findViewById(R.id.bAccept);

        //if(position >= 0){
        //text.setText("item " + position);
        imageLoader.DisplayImage(Configure.server+"/silverproductivity/"+data[position], image);    // emulator
        //imageLoader.DisplayImage("http://www.agelesslily.org/liusiyuan/silverproductivity/"+data[position], image);
        Log.d("Img Url: ", data[position]);
        //}

        return vi;
    }
}
