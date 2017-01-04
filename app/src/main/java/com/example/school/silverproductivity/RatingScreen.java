package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RatingScreen extends Activity {

    String workloadId, workloadUrl, rateGiver;

    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;

    private String selectedRate;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String GIVE_RATING_URL = Configure.server+"/silverproductivity/changeStoryRate.php";
    // private static final String GIVE_RATING_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/changeStoryRate.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);

        setTitle("Rate Story");

        workloadId = getIntent().getStringExtra("WorkloadId");
        workloadUrl = getIntent().getStringExtra("WorkloadUrl");
        rateGiver = getIntent().getStringExtra("Rater");

        Log.d("RECEIVED STORY ID: ", workloadId);
        Log.d("RECEIVED STORY URL: ", workloadUrl);

        addListenerOnRatingBar();
        addListenerOnButton();

    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);
        //DrawableCompat.setTint(progress, Color.GRAY);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                selectedRate = String.valueOf(rating);

                txtRatingValue.setText(String.valueOf(rating));
                Log.d("Value Selected: ", String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(RatingScreen.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                new ProcessRating().execute();

            }

        });

    }

    class ProcessRating extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RatingScreen.this);
            pDialog.setMessage("Giving Rating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            try {

                Log.d("[TRY] photoId", workloadId);
                Log.d("[TRY] user", rateGiver);
                Log.d("[TRY] rateAmt", selectedRate);

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("photoId", workloadId));
                params.add(new BasicNameValuePair("user", rateGiver));
                params.add(new BasicNameValuePair("rateAmt", selectedRate));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(GIVE_RATING_URL, "POST", params);

                // full json response
                Log.d("Process rating attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Give Rating Successful!", json.toString());
                    //Intent i = new Intent(RatingScreen.this, EachStoryDetails.class);
                    //Intent i = new Intent(RatingScreen.this, TabBar.class);
                    //startActivity(i);

                    Intent intent = new Intent(getApplicationContext(), TabBar.class);      // link back to the main tabbar
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        // clear WriteStory 1 to 4



                    startActivity(intent);

                    return json.getString(TAG_MESSAGE);

                } else{
                    Log.d("Give Rating Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(RatingScreen.this, file_url, Toast.LENGTH_LONG).show();
            }
        }

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rating_screen, menu);
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
