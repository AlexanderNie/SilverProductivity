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
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.location.Geocoder;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// ActionBarActivity
public class Login extends Activity {

    EditText etLoginUsername, etLoginPassword;
    Button bSubmitLogin, bRegLink, bForgotPassLink;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing from a real server:
    //private static final String LOGIN_URL = "http://syidah.web44.net/silverproductivity/login.php";
    //private static final String ALL_STORIES_URL = "http://127.0.0.1/silverproductivity/login.php";
    //private static final String LOGIN_URL = "http://localhost/silverproductivity/login.php";
    private static final String LOGIN_URL = Configure.server+"/silverproductivity/login.php";       // emulator
    // private static final String LOGIN_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String username = "";
    String password = "";
    IBinder iBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        bSubmitLogin = (Button) findViewById(R.id.bSubmitLogin);
        bRegLink = (Button) findViewById(R.id.bRegLink);
        bForgotPassLink = (Button) findViewById(R.id.bForgotPassLink);

        bSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i = new Intent(Login.this, Testing.class);
                startActivity(i);
                */

                if(isNetworkAvailable()==true){
                    username = etLoginUsername.getText().toString().toLowerCase();
                    password = etLoginPassword.getText().toString();
                    password = Utils.encryptPassword(password);
                    iBinder = etLoginUsername.getWindowToken();
                    new AttemptLogin().execute();
                }
                else {
                    //Toast.makeText(Login.this, "There is no internet connection", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder helpBuilder = new AlertDialog.Builder(Login.this);
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

                //new AttemptLogin().execute();
            }
        });

        bRegLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        bForgotPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ResetPwd.class);
                startActivity(i);
            }
        });
    }

    //AsyncTask is a seperate thread than the thread that runs the GUI
    //Any type of networking should be done with asynctask.
    class AttemptLogin extends AsyncTask<String, String, String> {
        //three methods get called, first preExecture, then do in background, and once do
        //in back ground is completed, the onPost execture method will be called.

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                    // save user data
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.putString("userId", json.getString("userId"));
                    edit.commit();

                    //Intent i = new Intent(Login.this, Testing.class);
                    Intent i = new Intent(Login.this, TabBar.class);
                    finish();

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(iBinder, 0);

                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
