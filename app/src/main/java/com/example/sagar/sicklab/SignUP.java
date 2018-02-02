package com.example.sagar.sicklab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.ByteArrayOutputStream;

public class SignUP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


//        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
//
//        ValueLineSeries series = new ValueLineSeries();
//        series.setColor(0xFF56B7F1);
//
//        series.addPoint(new ValueLinePoint("Jan", 2.4f));
//        series.addPoint(new ValueLinePoint("Feb", 3.4f));
//        series.addPoint(new ValueLinePoint("Mar", .4f));
//        series.addPoint(new ValueLinePoint("Apr", 1.2f));
//        series.addPoint(new ValueLinePoint("Mai", 2.6f));
//        series.addPoint(new ValueLinePoint("Jun", 1.0f));
//        series.addPoint(new ValueLinePoint("Jul", 3.5f));
//        series.addPoint(new ValueLinePoint("Aug", 2.4f));
//        series.addPoint(new ValueLinePoint("Sep", 2.4f));
//        series.addPoint(new ValueLinePoint("Oct", 3.4f));
//        series.addPoint(new ValueLinePoint("Nov", .4f));
//        series.addPoint(new ValueLinePoint("Dec", 1.3f));
//
//        mCubicValueLineChart.addSeries(series);
//        mCubicValueLineChart.startAnimation();

        Switch sw1 = (Switch) findViewById(R.id.switch1);
        sw1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onPickImage(v);
            }
        });

    }

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
