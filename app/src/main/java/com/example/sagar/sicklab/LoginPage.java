package com.example.sagar.sicklab;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.database.DataStoreDisease;
import com.example.sagar.database.DataStoreDiseaseHistory;
import com.example.sagar.database.DataStoreDiseaseReported;
import com.example.sagar.database.DataStoreUser;
import com.example.sagar.database.GetUserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginPage extends AppCompatActivity implements ServerIP {
    private String user_name, pass_word;
    Dialog dialog;
    private boolean isLoginSuccessful = false;
    private static final String PREF_NAME = "LOGIN_PREF";
    private boolean has_error_occured = false;
    private String city, stringLatitude, stringLongitude;
    private static final String PREF_IS_LOGGED_IN = "IS_LOGGED";
    //call intent variable is used to ensure that web service is not called even if pw and un are not entered
    private boolean callintent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        getLatitudeLongitude();

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        boolean hasLogged = settings.getBoolean("FirstLogin", true);

        if (!hasLogged) {


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }

        TextView tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUP.class);

                startActivity(intent);

            }
        });

        TextView tv2 = (TextView) findViewById(R.id.textView5);
        tv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUP.class);

                startActivity(intent);

            }
        });


        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isAvailable = Utility.isNetworkAvailable(LoginPage.this);
                if (!isAvailable) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection Available", Toast.LENGTH_LONG).show();
                    callintent = true;
                } else {
                    EditText login_field = (EditText) findViewById(R.id.input_email);
                    EditText password_field = (EditText) findViewById(R.id.input_password);
                    if (isempty(login_field)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_LONG).show();
                        callintent = false;
                    }
                    if (isempty(password_field)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
                        callintent = false;

                    }
                    user_name = login_field.getText().toString();
                    pass_word = password_field.getText().toString();

                    if (callintent) {


                        LoginPage.ConnectToLogin connect = new LoginPage.ConnectToLogin();
                        connect.execute();

                    }

                }

            }
        });

    }


    private void getLatitudeLongitude() {
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            stringLatitude = String.valueOf(gpsTracker.latitude);


            stringLongitude = String.valueOf(gpsTracker.longitude);


            city = gpsTracker.getLocality(this);


//            String postalCode = gpsTracker.getPostalCode(this);
//
//            String addressLine = gpsTracker.getAddressLine(this);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }

    }


    private void storeInfo(String token, String type, String email, String user_name) {
        DataStoreUser dba = new DataStoreUser(getApplicationContext());
        dba.storeCredentials(token, type, email, user_name, true, false);
    }

    private boolean isempty(EditText et1) {

        return et1.getText().toString().trim().length() == 0;
    }

    private class ConnectToLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //showning dialog box
            dialog = new Dialog(LoginPage.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog_box);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = IP + login_head + login_email + user_name + login_password + pass_word;

            String jsonStr = sh.makeServiceCall(url);

            Log.e("LoginPage", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
//                    JSONArray loginresponse = jsonObj.json("contacts");
                    String check_message = jsonObj.getString("status");

                    if (check_message.equals("success")) {

                        String token = jsonObj.getString("token");
                        String user_type = jsonObj.getString("doctor");
                        String user_name = jsonObj.getString("name");
//                      String user_email = jsonObj.getString("email");
                        String user_email = "abc@gmail.com";

                        storeInfo(token, user_type, user_email, user_name);

                        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("FirstLogin", false);
                        editor.apply();


                        isLoginSuccessful = true;


                    } else {
                        dialog.dismiss();
                        has_error_occured = true;
                    }


                } catch (final JSONException e) {
                    Log.e("LoginPage", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                dialog.dismiss();
                Log.e("LoginPage", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server.",
//                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (has_error_occured) {
                Toast.makeText(getApplicationContext(), "Please Enter The Right Credentials", Toast.LENGTH_LONG).show();

            }

            if (isLoginSuccessful) {

                LoginPage.GetData connect = new LoginPage.GetData();
                connect.execute();


            }


        }
    }


    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            GetUserData get = new GetUserData(getApplicationContext());

            String url = IP + trending_head + trending_district + "kathmandu";

            String jsonStr = sh.makeServiceCall(url);

            Log.e("LoginPage", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

//                    // Getting JSON Array node
////                    JSONArray loginresponse = jsonObj.json("contacts");
//                    String check_message = jsonObj.getString("success");
//
//
//                    if (check_message.equals("1")) {


                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("disease");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String disease_name = c.getString("name");
                        String district = "kathmandu";
                        String no_of_reports = c.getString("count");

                        String image_link = c.getString("image_url");
                        String description = c.getString("description");
                        String prevention = c.getString("prevention");
                        String symptoms = c.getString("symptom");

                       String  first_reported="2014/15/15";
                        String last_reported="2014/15/15";
                        description=description+"\n\n\n\nPrevention \n"+prevention+"\n\n\n\nSymptoms \n"+symptoms;

                        DataStoreDisease store = new DataStoreDisease(getApplicationContext());
                        if (i == 0) {
                            store.storeDiseaseInfo(disease_name, district, no_of_reports, first_reported, last_reported, image_link, description, true, false);
                        } else {
                            store.storeDiseaseInfo(disease_name, district, no_of_reports, first_reported, last_reported, image_link, description, false, false);
                        }

                    }


//                    } else {
//                        dialog.dismiss();
//                        has_error_occured = true;
//                    }
//

                } catch (final JSONException e) {
                    Log.e("LoginPage", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("LoginPage", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server.",
//                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (has_error_occured) {
                Toast.makeText(getApplicationContext(), "Some Problem Occured", Toast.LENGTH_LONG).show();

            }

            if (isLoginSuccessful) {



                //these are just place holder data placed as server portion for his feature was not complete
                DataStoreDiseaseHistory store = new DataStoreDiseaseHistory(getApplicationContext());
                store.storeDiseaseInfo("bubonic plague", "kathmandu", "280", "2015-6-18", "2015-7-20",
                        "https://cdn.images.express.co.uk/img/dynamic/11/590x/secondary/Tuberculosis-breakthrough-Experts-have-found-a-drug-which-can-defy-antibiotic-resistant-TB-979059.jpg",
                        "Helllo world", true, false);
                store.storeDiseaseInfo("bubonic plague", "kathmandu", "280", "2015-6-18", "2015-7-20",
                        "https://cdn.images.express.co.uk/img/dynamic/11/590x/secondary/Tuberculosis-breakthrough-Experts-have-found-a-drug-which-can-defy-antibiotic-resistant-TB-979059.jpg",
                        "Helllo world", false, false);
                store.storeDiseaseInfo("bubonic plague", "kathmandu", "280", "2015-6-18", "2015-7-20",
                        "https://cdn.images.express.co.uk/img/dynamic/11/590x/secondary/Tuberculosis-breakthrough-Experts-have-found-a-drug-which-can-defy-antibiotic-resistant-TB-979059.jpg",
                        "Helllo world", false, false);


                LoginPage.GetDataForVisualtization connect = new LoginPage.GetDataForVisualtization();
                connect.execute();

            }


        }
    }


    private class GetDataForVisualtization extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            GetUserData get = new GetUserData(getApplicationContext());

            String url = IP + visualization_head;

            String jsonStr = sh.makeServiceCall(url);

            Log.e("LoginPage", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
////                    JSONArray loginresponse = jsonObj.json("contacts");
//                    String check_message = jsonObj.getString("success");
//
//
//                    if (check_message.equals("true")) {


                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("diseases");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String disease_name = c.getString("disease");
                        String district = c.getString("district");
                        String no_of_reports = c.getString("no_of_reports");
                        String first_reported = c.getString("start_date");
                        String last_reported = c.getString("end_date");
                        String image_link = c.getString("image_link");
                        String description = c.getString("description");

                        DataStoreDiseaseHistory store = new DataStoreDiseaseHistory(getApplicationContext());
                        if (i == 0) {
                            store.storeDiseaseInfo(disease_name, district, no_of_reports, first_reported, last_reported, image_link, description, true, false);
                        } else {
                            store.storeDiseaseInfo(disease_name, district, no_of_reports, first_reported, last_reported, image_link, description, false, false);
                        }

                    }
//
//
//                    } else {
//                        dialog.dismiss();
//                        has_error_occured = true;
//                    }


                } catch (final JSONException e) {
                    Log.e("LoginPage", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("LoginPage", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server.",
//                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (has_error_occured) {
                Toast.makeText(getApplicationContext(), "SomeProblem Occured", Toast.LENGTH_LONG).show();

            }

            if (isLoginSuccessful) {


                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

                finish();


            }


        }
    }
//
//
//    private class GetDiseaseReports extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler sh = new HttpHandler();
//            // Making a request to url and getting response
//            GetUserData get = new GetUserData(getApplicationContext());
//
//            String url = IP + unverified_head + trending_token + get.getData("Token") + trending_district + city + trending_latitude + stringLatitude + trending_longitude + stringLongitude;
//
//            String jsonStr = sh.makeServiceCall(url);
//
//            Log.e("LoginPage", "Response from url: " + jsonStr);
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
////                    JSONArray loginresponse = jsonObj.json("contacts");
//                    String check_message = jsonObj.getString("success");
//
//
//                    if (check_message.equals("true")) {
//
//
//                        // Getting JSON Array node
//                        JSONArray data = jsonObj.getJSONArray("diseases");
//
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject c = data.getJSONObject(i);
//                            String disease_name1 = c.getString("disease");
//                            String district1 = c.getString("district");
//                            String no_of_reports1 = c.getString("no_of_reports");
//                            String first_reported1 = c.getString("first_reported");
//                            String last_reported1 = c.getString("last_reported");
//                            String image_link1 = c.getString("image_link");
//                            String description1 = c.getString("description");
//
//                            DataStoreDiseaseReported store = new DataStoreDiseaseReported(getApplicationContext());
//                            if (i == 0) {
//                                store.storeDiseaseInfo(disease_name1, district1, no_of_reports1, first_reported1, last_reported1, image_link1,
//                                        description1, true, false);
//                            } else {
//                                store.storeDiseaseInfo(disease_name1, district1, no_of_reports1, first_reported1, last_reported1, image_link1,
//                                        description1, false, false);
//                            }
//
//                        }
//
//
//                    } else {
//                        dialog.dismiss();
//                        has_error_occured = true;
//                    }
//
//
//                } catch (final JSONException e) {
//                    Log.e("LoginPage", "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            Toast.makeText(getApplicationContext(),
////                                    "Json parsing error: " + e.getMessage(),
////                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e("LoginPage", "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Toast.makeText(getApplicationContext(),
////                                "Couldn't get json from server.",
////                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//            if (has_error_occured) {
//                Toast.makeText(getApplicationContext(), "Some  Problem Occured", Toast.LENGTH_LONG).show();
//
//            }
//
//            if (isLoginSuccessful) {
//
//                dialog.dismiss();
//                //starts another aCTIVITY
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//
//
//            }
//
//        }
//    }
//

}
