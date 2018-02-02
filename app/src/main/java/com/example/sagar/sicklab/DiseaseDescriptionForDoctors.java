package com.example.sagar.sicklab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DiseaseDescriptionForDoctors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_description_for_doctors);

        Bundle extras = getIntent().getExtras();
        String description = extras.getString("description");
        final String disease_name = extras.getString("name");
        String img_url = extras.getString("img_url");


        ImageView img = (ImageView) findViewById(R.id.tv_header_title);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSuggestionsByDoctors.class);
                //tv_header_title
                Bundle extras = new Bundle();
                extras.putString("name", disease_name);
                intent.putExtras(extras);
                startActivity(intent);


            }
        });


        TextView tv = (TextView) findViewById(R.id.mainToolBar);
        tv.setText(disease_name);


        TextView tv1 = (TextView) findViewById(R.id.textView11);
        tv1.setText(description);


        ImageView imageview = (ImageView) findViewById(R.id.header);
        Picasso.with(getApplicationContext())
                .load(img_url).fit()

                .into(imageview);

    }
}
