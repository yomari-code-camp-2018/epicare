package com.example.sagar.sicklab;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sagar.database.GetUserData;

import org.json.JSONException;
import org.json.JSONObject;

public class AddSuggestionsByDoctors extends AppCompatActivity implements ServerIP {
    private String description, check_message, disease_name;
    private boolean callintent = true;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suggestions_by_doctors);


        Bundle extras = getIntent().getExtras();
        disease_name = extras.getString("name");

        Button btn1 = (Button) findViewById(R.id.button3);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//                startActivity(intent);


                boolean isAvailable = Utility.isNetworkAvailable(AddSuggestionsByDoctors.this);
                if (!isAvailable) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection Available", Toast.LENGTH_LONG).show();
                    callintent = false;
                } else {
                    EditText suggestion = (EditText) findViewById(R.id.editText3);
                    if (isempty(suggestion)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_LONG).show();
                        callintent = false;
                    }
                    description = suggestion.getText().toString();


                    if (callintent) {


                        AddSuggestionsByDoctors.SendSuggestion connect = new AddSuggestionsByDoctors.SendSuggestion();
                        connect.execute();


                    }

                }
            }
        });


    }

    private boolean isempty(EditText et1) {

        return et1.getText().toString().trim().length() == 0;
    }


    private class SendSuggestion extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            GetUserData get = new GetUserData(getApplicationContext());
            token = get.getData("Token");

            //replacing spaces in disease name with %20
            description = replace(description);

            disease_name = replace(disease_name);
            String url = IP + suggestion_head + suggestion_text + description + suggestion_token + token + suggestion_disease + disease_name;


            String jsonStr = sh.makeServiceCall(url);

            Log.e("LoginPage", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
//                    JSONArray loginresponse = jsonObj.json("contacts");
                    check_message = jsonObj.getString("success");


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

        public String replace(String str) {
            String[] words = str.split(" ");
            StringBuilder sentence = new StringBuilder(words[0]);

            for (int i = 1; i < words.length; ++i) {
                sentence.append("%20");
                sentence.append(words[i]);
            }

            return sentence.toString();
        }

        @Override
        protected void onPostExecute(Void result) {

            if (check_message.equals("true")) {


                Toast.makeText(getApplicationContext(), "Suggestion Has Been Added Successfully", Toast.LENGTH_LONG).show();


                //cereates certain  delay and restarts the activity as attendance has been registered
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //this piece of code is run after 1 seconds i.e. 1000ms
                        finish();
                        startActivity(getIntent());
                    }
                }, 10);

            } else {
                Toast.makeText(getApplicationContext(), "Error!!! Please Try Again Later", Toast.LENGTH_LONG).show();


            }


        }
    }

}
