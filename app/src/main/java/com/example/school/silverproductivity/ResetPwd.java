package com.example.school.silverproductivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 26/1/2017.
 */

public class ResetPwd extends FragmentActivity implements View.OnClickListener {

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_MESSAGE = "message";

    private static final String RETRIEVEPWD_URL = Configure.server+"/silverproductivity/getpassword.php";
    private static final String SENDPWD_URL = Configure.server+"/phpmailer/examples/gmail.php";

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    EditText etFgtEmail, etFgtUsername;
    Button bResetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        bResetPwd = (Button) findViewById(R.id.bResetPwd);
        etFgtEmail = (EditText) findViewById(R.id.etFgtEmail);
        etFgtUsername = (EditText) findViewById(R.id.etFgtUsername);

        bResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()==true){
                    //updateDob();
                    new ResetPassword().execute();
                }
                else {
                    //Toast.makeText(Login.this, "There is no internet connection", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder helpBuilder = new AlertDialog.Builder(ResetPwd.this);
                    helpBuilder.setTitle("No Connectivity");
                    helpBuilder.setMessage("Please on your mobile data or connect to wifi.");
                    helpBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                        }
                    });

                    // Remember, create doesn't show the dialog
                    AlertDialog helpDialog = helpBuilder.create();
                    helpDialog.show();
                }
            }
    });
    }


    class ResetPassword extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResetPwd.this);
            pDialog.setMessage("Sending Email...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            int success;
            String fgtUsername = etFgtUsername.getText().toString();
            String fgtEmail = etFgtEmail.getText().toString();

            /////////// MOVE HERE
            try {

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", fgtUsername));

                Log.d("retrieve!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(RETRIEVEPWD_URL, "POST", params);

                // full json response
                Log.d("Retrieve attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 0) {
                    Log.d("Email Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

                Log.d("Retrieve Success!", json.toString());

                String password = json.getString(TAG_PASSWORD);
                password = Utils.decryptPassword(password);


                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sendto", fgtEmail));
                params.add(new BasicNameValuePair("password", password));

                Log.d("send!", "starting");

                //Posting user data to script
                json = jsonParser.makeHttpRequest(SENDPWD_URL, "POST", params);

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 0) {
                    Log.d("Send Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

                Intent i = new Intent(ResetPwd.this, Login.class);
                finish();
                startActivity(i);
                return json.getString(TAG_MESSAGE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        // After completing background task Dismiss the progress dialog
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(ResetPwd.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    public void onClick(View v) {

    }

    // Check all connectivities whether available or not
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
