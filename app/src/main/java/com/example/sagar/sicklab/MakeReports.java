package com.example.sagar.sicklab;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.database.GetUserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MakeReports extends AppCompatActivity implements ServerIP {

    private String check_message;
    private String city, gender, age_group;
    private String disease_name, no_of_victims, token;
    private boolean callintent = true;
private int disease_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reports);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editText);
        textView.setAdapter(adapter);


        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
//            stringLatitude = String.valueOf(gpsTracker.latitude);
//
//
//            stringLongitude = String.valueOf(gpsTracker.longitude);


//            city = gpsTracker.getLocality(this);
            city = "kathmandu";

            TextView tv = (TextView) findViewById(R.id.textView9);
            tv.setText("Your Current Location:   " + " Kathmandu");
//            String postalCode = gpsTracker.getPostalCode(this);
//
//            String addressLine = gpsTracker.getAddressLine(this);


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }


        Button btn1 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//                startActivity(intent);


                boolean isAvailable = Utility.isNetworkAvailable(MakeReports.this);
                if (!isAvailable) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection Available", Toast.LENGTH_LONG).show();
                    callintent = true;
                } else {
                    AutoCompleteTextView et1 = (AutoCompleteTextView)
                            findViewById(R.id.editText);
//                    EditText et2 = (EditText) findViewById(R.id.editText2);
//                    if (isempty(et1)) {
//                        Toast.makeText(getApplicationContext(), "Please Enter Disease Name", Toast.LENGTH_LONG).show();
//                        callintent = false;
//                    }
//                    if (isempty(et2)) {
//                        Toast.makeText(getApplicationContext(), "Please Enter the Infected Number", Toast.LENGTH_LONG).show();
//                        callintent = false;
//
//                    }
                    Spinner sp1 = (Spinner) findViewById(R.id.editText2);
                    Spinner sp2 = (Spinner) findViewById(R.id.editText3);

                    gender = sp1.getSelectedItem().toString();

                    //formating for server specifications
                    if(gender.equals("Male")) gender="M";
                    else gender="F";

                    age_group = sp2.getSelectedItem().toString();

                    disease_name = et1.getText().toString();

                    //gettiing index of disease in the array

                    for(int i=0;i<COUNTRIES.length;i++){
                        if(disease_name.equals(COUNTRIES[i])) disease_id=i+1;
                    }
                    if (callintent) {

                        MakeReports.SendReport connect = new MakeReports.SendReport();
                        connect.execute();

                    }

                }
            }
        });


    }

    private boolean isempty(EditText et1) {

        return et1.getText().toString().trim().length() == 0;
    }

    private class SendReport extends AsyncTask<Void, Void, Void> {
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
            disease_name = replace(disease_name);

            String url = IP + report_head + report_disease_id + disease_id + report_token + token + report_age + age_group
                    + report_gender + gender + report_location + "kathmandu" ;

            String jsonStr = sh.makeServiceCall(url);

            Log.e("LoginPage", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
//                    JSONArray loginresponse = jsonObj.json("contacts");
                    check_message = jsonObj.getString("status");

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

            if (check_message.equals("success")) {


                Toast.makeText(getApplicationContext(), "Report Has Been Sent Successfully", Toast.LENGTH_LONG).show();


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

    private static final String[] COUNTRIES = new String[]{
            "influenza",
            "ebola virus",
            "cholera",
            "hiv/aids",
            "smallpox",
            "yellow fever",
            "measles",
            "severe acute respiratory syndrome",
            "viral hemorrhagic fever",
            "poliomyelitis",
            "emerging infectious",
            "bubonic plague",
            "swine influenza",
            "chikungunya virus infection",
            "typhus",
            "pertussis",
            "lassa fever",
            "epidemic typhus",
            "mumps",

            "rift valley fever",
            "hepatitis e",
            "relapsing fever",
            "pertussis",
            "lassa fever",
            "epidemic typhus",
            "mumps",
            "diphtheria",
            "rift valley fever",
            "hepatitis e",
            "relapsing fever",
            "pneumonic plague",
            "foot-and-mouth disease",
            "zika fever",
            "dengue fever", "diarrhoea"

    };

//    private static final String[] COUNTRIES = new String[]{
//            "Acinetobacter infections",
//            "Actinomycosis",
//            "African sleeping sickness (African trypanosomiasis)",
//            "AIDS (Acquired immunodeficiency syndrome)",
//            "Amebiasis",
//            "Anaplasmosis",
//            "Angiostrongyliasis",
//            "Anisakiasis",
//            "Anthrax",
//            "Arcanobacterium haemolyticum infection",
//            "Argentine hemorrhagic fever",
//            "Ascariasis",
//            "Aspergillosis",
//            "Astrovirus infection",
//            "Babesiosis",
//            "Bacillus cereus infection",
//            "Bacterial pneumonia",
//            "Bacterial vaginosis",
//            "Bacteroides infection",
//            "Balantidiasis",
//            "Bartonellosis",
//            "Baylisascaris infection",
//            "BK virus infection",
//            "Black piedra",
//            "Blastocystosis",
//            "Blastomycosis",
//            "Bolivian hemorrhagic fever",
//            "Botulism (and Infant botulism)",
//            "Brucellosis",
//            "Bubonic plague",
//            "Burkholderia infection",
//            "Buruli ulcer",
//
//            "Campylobacteriosis",
//            "Candidiasis (Moniliasis; Thrush)",
//            "Capillariasis",
//            "Carrion's disease",
//            "Cat-scratch disease",
//            "Cellulitis",
//            "Chagas Disease (American trypanosomiasis)",
//            "Chancroid",
//            "Chickenpox",
//            "Chikungunya",
//            "Chlamydia",
//            "Chlamydophila pneumoniae infection",
//            "Cholera Vibrio",
//            "Chromoblastomycosis",
//            "Chytridiomycosis",
//            "Clonorchiasis",
//            "Clostridium difficile colitis",
//            "Coccidioidomycosis",
//            "Colorado tick fever (CTF)",
//            "Creutzfeldt–Jakob disease (CJD)",
//            "Crimean-Congo hemorrhagic fever (CCHF)",
//            "Cryptococcosis",
//            "Cryptosporidiosis",
//            "Cutaneous larva migrans (CLM)",
//            "Cyclosporiasis",
//            "Cysticercosis",
//            "Cytomegalovirus infection",
//            "Dengue fever",
//            "Desmodesmus infection",
//            "Dientamoebiasis",
//            "Diphtheria",
//            "Diphyllobothriasis",
//            "Dracunculiasis",
//            "Ebola hemorrhagic fever",
//            "Echinococcosis",
//            "Ehrlichiosis",
//            "Enterobiasis (Pinworm infection)",
//            "Enterococcus infection",
//            "Enterovirus infection",
//            "Epidemic typhus",
//            "Erythema infectiosum (Fifth disease)",
//            "Exanthem subitum (Sixth disease)",
//            "Fasciolasis",
//            "Fasciolopsiasis",
//            "Fatal familial insomnia (FFI)",
//            "Filariasis",
//            "Food poisoning",
//            "Free-living amebic infection",
//            "Fusobacterium infection",
//            "Gas gangrene (Clostridial myonecrosis)",
//            "Geotrichosis",
//            "Giardiasis",
//            "Glanders",
//            "Gnathostomiasis",
//            "Gonorrhea",
//            "Granuloma inguinale (Donovanosis)",
//            "Group A streptococcal infection",
//            "Group B streptococcal infection",
//            "Haemophilus influenzae infection",
//            "Hand, foot and mouth disease (HFMD)",
//            "Hantavirus Pulmonary Syndrome (HPS)",
//            "Heartland virus disease",
//            "Helicobacter pylori infection",
//            "Hemolytic-uremic syndrome (HUS)",
//
//            "Hepatitis A",
//            "Hepatitis B",
//            "Hepatitis C",
//            "Hepatitis D",
//            "Hepatitis E",
//            "Herpes simplex",
//            "Histoplasmosis",
//            "Hookworm infection",
//            "Human bocavirus infection",
//            "Human ewingii ehrlichiosis",
//            "Human granulocytic anaplasmosis (HGA)",
//            "Human metapneumovirus infection",
//            "Human monocytic ehrlichiosis",
//            "Human papillomavirus (HPV) infection",
//            "Human parainfluenza virus infection",
//            "Hymenolepiasis",
//            "Influenza (flu)",
//            "Isosporiasis",
//            "Kawasaki disease",
//            "Keratitis",
//            "Kingella kingae infection",
//            "Kuru",
//            "Lassa fever",
//            "Legionellosis (Legionnaires' disease)",
//            "Legionellosis (Pontiac fever)",
//            "Leishmaniasis",
//            "Leprosy",
//            "Leptospirosis",
//            "Listeriosis",
//            "Lyme disease (Lyme borreliosis)",
//            "Lymphatic filariasis (Elephantiasis)",
//            "Lymphocytic choriomeningitis",
//            "Malaria",
//            "Marburg hemorrhagic fever (MHF)",
//            "Measles",
//            "Middle East respiratory syndrome (MERS)",
//            "Melioidosis (Whitmore's disease)",
//            "Meningitis",
//            "Meningococcal disease",
//            "Metagonimiasis",
//            "Microsporidiosis",
//            "Molluscum contagiosum (MC)",
//            "Monkeypox",
//            "Mumps",
//            "Murine typhus (Endemic typhus)",
//            "Mycoplasma pneumonia",
//            "Mycetoma (disambiguation)",
//            "Myiasis parasitic dipterous",
//            "Neonatal conjunctivitis (Ophthalmia neonatorum)",
//            "Norovirus (children and babies)",
//            "Variant Creutzfeldt–Jakob disease (vCJD, nvCJD)",
//            "Nocardiosis",
//            "Onchocerciasis (River blindness)",
//            "Opisthorchiasis",
//
//            "Paragonimiasis",
//            "Pasteurellosis",
//            "Pediculosis capitis (Head lice)",
//            "Pediculosis corporis (Body lice)",
//            "Pediculosis pubis (Pubic lice, Crab lice)",
//            "Pelvic inflammatory disease (PID)",
//            "Pertussis (Whooping cough)",
//            "Plague",
//            "Pneumococcal infection",
//            "Pneumocystis pneumonia (PCP)",
//            "Pneumonia",
//            "Poliomyelitis",
//            "Prevotella infection",
//            "Primary amoebic meningoencephalitis (PAM)",
//            "Progressive multifocal leukoencephalopathy",
//            "Psittacosis",
//            "Q fever",
//            "Rabies",
//            "Relapsing fever",
//            "Respiratory syncytial virus infection",
//            "Rhinosporidiosis",
//            "Rhinovirus infection",
//            "Rickettsial infection",
//            "Rickettsialpox",
//            "Rift Valley fever (RVF)",
//            "Rocky Mountain spotted fever (RMSF)",
//            "Rotavirus infection",
//            "Rubella",
//            "Salmonellosis",
//            "SARS (Severe Acute Respiratory Syndrome)",
//            "Scabies",
//            "Schistosomiasis",
//            "Sepsis",
//            "Shigellosis (Bacillary dysentery)",
//            "Shingles (Herpes zoster)",
//            "Smallpox (Variola)",
//            "Sporotrichosis",
//            "Staphylococcal food poisoning",
//            "Staphylococcal infection",
//            "Strongyloidiasis",
//            "Subacute sclerosing panencephalitis",
//            "Syphilis",
//            "Taeniasis",
//            "Tetanus (Lockjaw)",
//            "Tinea barbae (Barber's itch)",
//            "Tinea capitis (Ringworm of the Scalp)",
//            "Tinea corporis (Ringworm of the Body)",
//            "Tinea cruris (Jock itch)",
//            "Tinea manum (Ringworm of the Hand)",
//            "Tinea nigra",
//            "Tinea pedis (Athlete’s foot)",
//            "Tinea unguium (Onychomycosis)",
//            "Tinea versicolor (Pityriasis versicolor)",
//            "Toxocariasis (Ocular Larva Migrans (OLM))",
//            "Toxocariasis (Visceral Larva Migrans (VLM))",
//            "Trachoma",
//            "Toxoplasmosis",
//            "Trichinosis",
//            "Trichomoniasis",
//            "Trichuriasis (Whipworm infection)",
//            "Tuberculosis",
//            "Tularemia",
//            "Typhoid fever",
//            "Typhus fever",
//            "Ureaplasma urealyticum infection",
//            "Valley fever",
//            "Venezuelan equine encephalitis",
//            "Venezuelan hemorrhagic fever",
//            "Vibrio vulnificus infection",
//            "Vibrio parahaemolyticus enteritis",
//            "Viral pneumonia",
//            "West Nile Fever",
//            "White piedra (Tinea blanca)",
//            "Yersinia pseudotuberculosis infection",
//            "Yersiniosis",
//            "Yellow fever",
//            "Zygomycosis"
//    };
}
