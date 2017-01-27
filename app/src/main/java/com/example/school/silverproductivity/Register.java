package com.example.school.silverproductivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

// ActionBarActivity
public class Register extends FragmentActivity implements View.OnClickListener {

    EditText etRegUsername, etRegPhoneNo, etRegPassword, etRegConPassword, etDob;

    private DatePickerDialog dobDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    Button bSubmitRegister, bPhoneNoInfo;

    String yearChosen, monthChosen, dayChosen, dateOfBirth;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing from a real server:
    //private static final String REGISTER_URL = "http://syidah.web44.net/silverproductivity/register.php";
    //private static final String REGISTER_URL = "http://127.0.0.1/silverproductivity/register.php";
    //private static final String REGISTER_URL = "http://localhost/silverproductivity/register.php";
    private static final String REGISTER_URL = Configure.server+"/silverproductivity/register.php";         // emulator
    // private static final String REGISTER_URL = "http://www.agelesslily.org/liusiyuan/silverproductivity/register.php";


    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register Account");

        etRegUsername = (EditText) findViewById(R.id.etRegUsername);
        etRegPhoneNo = (EditText) findViewById(R.id.etRegPhoneNo);
        etRegPassword = (EditText) findViewById(R.id.etRegPassword);
        etRegConPassword = (EditText) findViewById(R.id.etRegConPassword);
        etDob = (EditText) findViewById(R.id.etDob);

        bSubmitRegister = (Button) findViewById(R.id.bSubmitRegister);
        bPhoneNoInfo = (Button) findViewById(R.id.bPhoneNoInfo);

//        etDob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PickerDialogs pickerDialogs = new PickerDialogs();
//                pickerDialogs.show(getSupportFragmentManager(), "date_picker");
//            }
//        });

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setDateTimeField();


        bSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()==true){
                    //updateDob();
                    new CreateUser().execute();
                }
                else {
                    //Toast.makeText(Login.this, "There is no internet connection", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder helpBuilder = new AlertDialog.Builder(Register.this);
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

        bPhoneNoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonePopUp();
            }
        });

    }

    private void updateDob() {
        Log.d(Register.class.toString(), "updateDob: " +etDob.getText());
    }

    private void setDateTimeField() {
        etDob.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if(view == etDob) {
            dobDatePickerDialog.show();
        }
    }

    public class PickerDialogs extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            DateSettings dateSettings = new DateSettings(getActivity());

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog;
            dialog = new DatePickerDialog(getActivity(), dateSettings, year, month, day);

            //return super.onCreateDialog(savedInstanceState);
            return dialog;
        }
    }

    public class DateSettings implements DatePickerDialog.OnDateSetListener {

        Context context;

        public DateSettings(Context context){
            this.context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dayChosen = Integer.toString(dayOfMonth);
            monthChosen = Integer.toString(monthOfYear+1);
            yearChosen = Integer.toString(year);

            Log.d("DAY CHOSEN", dayChosen);
            Log.d("MONTH CHOSEN", monthChosen);
            Log.d("YEAR CHOSEN", yearChosen);

            etDob.setText(dayChosen + "-" + monthChosen + "-" + yearChosen);

            //Toast.makeText(context, "Selected Date: " + dayOfMonth + " / " + (monthOfYear + 1) + " / " + year, Toast.LENGTH_LONG).show();
        }


    }

    class CreateUser extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            int success;
            String username = etRegUsername.getText().toString();
            String phoneNumber = etRegPhoneNo.getText().toString();
            dateOfBirth = etDob.getText().toString();
            String password = etRegPassword.getText().toString();
            String orgPassword = password;
            password = Utils.encryptPassword(password);
            String conPassword = etRegConPassword.getText().toString();
            conPassword = Utils.encryptPassword(conPassword);

            /////////// MOVE HERE
            try {

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("phoneNumber", phoneNumber));
                params.add(new BasicNameValuePair("dateOfBirth", dateOfBirth));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("conPassword", conPassword));
                params.add(new BasicNameValuePair("orgPassword", orgPassword));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                // full json response
                Log.d("Register attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());

                    // save user data
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Register.this);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("regUsername", username);
                    edit.commit();

                    Intent i = new Intent(Register.this, RegisterTwo.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Register Failure!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }



    private void phonePopUp(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Phone Number Information");
        helpBuilder.setMessage("When you forgot your password, it will be sent to your phone number.");
        helpBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
