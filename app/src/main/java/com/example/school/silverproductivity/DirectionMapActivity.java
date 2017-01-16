package com.example.school.silverproductivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by Alexander on 16/1/2017.
 */

public class DirectionMapActivity extends BaseDemoActivity {

    private ProgressDialog pDialog;
    String srsLat, srcLng, tarLat, tarLng;
    PolylineOptions polylineOptions;

    private static final String ALL_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=WALKING";

    private static final String TAG_ROUTES = "routes";
    private static final String TAG_LEGS = "legs";
    private static final String TAG_STEPS = "steps";
    private static final String TAG_START = "start_location";
    private static final String TAG_END = "end_location";


    @Override
    protected void startDemo() {
        getTargetLocation();
        getGeoInfo();
        new LoadMapDetails2().execute();
        //new LoadMapDetails().execute();

    }

    private void getTargetLocation()
    {
        Intent intent = getIntent();
        tarLat = intent.getExtras().get("targetlat").toString();
        tarLng = intent.getExtras().get("targetlng").toString();
    }
    private void getGeoInfo()
    {
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            srsLat = String.valueOf(gpsTracker.latitude);

            srcLng = String.valueOf(gpsTracker.longitude);

            String country = gpsTracker.getCountryName(this);


            String city = gpsTracker.getLocality(this);


            String postalCode = gpsTracker.getPostalCode(this);


            String addressLine = gpsTracker.getAddressLine(this);

            Toast.makeText(this.getApplicationContext(),"lat is " + srsLat + "lng is " + srcLng, Toast.LENGTH_LONG);

        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }


    public class LoadMapDetails2 extends AsyncTask<Void, Void, Boolean>
    {
        GMapV2Direction md;
        Document doc;

        @Override
        protected Boolean doInBackground(Void... params) {
            md = new GMapV2Direction();
            doc = md.getDocument(new LatLng(Double.valueOf(srsLat), Double.valueOf(srcLng)),
                    new LatLng(Double.valueOf(tarLat), Double.valueOf(tarLng)),
                    GMapV2Direction.MODE_DRIVING);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(8).color(
                    Color.RED);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            getMap().addPolyline(rectLine);
            BitmapDescriptor startIcon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("iconstart",200,200));
            Marker start = getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(srsLat), Double.valueOf(srcLng)))
                    .title("Start Point").snippet("Start Point").
                            icon(startIcon)
            );

            BitmapDescriptor endIcon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("iconend",200,200));
            getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(tarLat), Double.valueOf(tarLng)))
                    .title("End Point").snippet("End Point").icon(endIcon));
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.290270, 103.851959), 10.0f));
        }
    }

    public class LoadMapDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DirectionMapActivity.this);
            pDialog.setMessage("Loading Map Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {

            //we will develop this method in version 2
            polylineOptions = new PolylineOptions();
            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //we will develop this method in version 2
            updateMap();
        }
    }


    public void updateJSONdata() {

        JSONParser jParser = new JSONParser();
        String final_url = String.format(ALL_DIRECTION_URL, srsLat, srcLng, tarLat, tarLng);
        JSONObject json = jParser.getJSONFromUrl(final_url);

        try{
            JSONObject route = json.getJSONArray(TAG_ROUTES).getJSONObject(0);
            JSONObject leg = route.getJSONArray(TAG_LEGS).getJSONObject(0);
            JSONArray steps = leg.getJSONArray(TAG_STEPS);


            // looping through all posts according to the json object returned
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);

                //gets the content of each tag from server
                JSONObject start = step.getJSONObject(TAG_START);
                JSONObject end = step.getJSONObject(TAG_END);

                LatLng startLat = new LatLng(Double.valueOf(start.getString("lat")), Double.valueOf(start.getString("lng")));
                LatLng endLat = new LatLng(Double.valueOf(end.getString("lat")), Double.valueOf(end.getString("lng")));

                if(i==0)
                {
                    polylineOptions.add(startLat);
                }
                polylineOptions.add(endLat);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void updateMap()
    {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.290270, 103.851959), 10.0f));
        polylineOptions.color(Color.RED);
        //polygonOptions.fillColor(Color.BLUE);
        getMap().addPolyline(polylineOptions);

    }

    private Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}
