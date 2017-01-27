package com.example.school.silverproductivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// ActionBarActivity
public class RegisterTwo extends Activity {

    EditText etName, etRegAdd;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Button bSkip, bSubmitInfo;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing from a real server:
    //private static final String REGISTER_URL = "http://syidah.web44.net/silverproductivity/register2.php";
    //private static final String REGISTER_URL = "http://127.0.0.1/silverproductivity/register2.php";
    //private static final String REGISTER_URL = "http://localhost/silverproductivity/register2.php";
    private static final String REGISTER_URL = "http://10.0.2.2/silverproductivity/register2.php";      // emulator
    // private static final String REGISTER_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/register2.php";


    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        setTitle("Register Account");

        etName = (EditText) findViewById(R.id.etName);
        etRegAdd = (EditText) findViewById(R.id.etRegAdd);

        /* Initialize Radio Group and attach click handler */
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        bSkip = (Button) findViewById(R.id.bSkip);
        bSubmitInfo = (Button) findViewById(R.id.bSubmitInfo);


        bSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterTwo.this, Login.class);
                startActivity(i);
            }
        });

        bSubmitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()==true){
                    new RegisterInfo().execute();
                }
                else {
                    //Toast.makeText(Login.this, "There is no internet connection", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder helpBuilder = new AlertDialog.Builder(RegisterTwo.this);
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

    class RegisterInfo extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterTwo.this);
            pDialog.setMessage("Updating Information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(RegisterTwo.this);
            String regUsername = sp.getString("regUsername", "anon");

            String username = regUsername;
            String name = etName.getText().toString();

            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);
            //Toast.makeText(RegisterTwo.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();

            String gender = radioSexButton.getText().toString().substring(0,1);
            String address = etRegAdd.getText().toString();

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("gender", gender));
                params.add(new BasicNameValuePair("address", address));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                // full json response
                Log.d("Register Information", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Information Updated!", json.toString());

                    Intent i = new Intent(RegisterTwo.this, Login.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Information Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }

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
                Toast.makeText(RegisterTwo.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_two, menu);
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
