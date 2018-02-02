package com.example.sagar.sicklab;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Sagar on 11/13/2017.
 */

public class IntroActivity extends AppIntro {
    private static final String PREF_IS_LOGGED_IN = "IS_LOGGED";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Note here that we DO NOT use setContentView();
//
//        // Add your slide fragments here.
//        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Welcome to", "EpiCare", R.drawable.logo, getResources().getColor(R.color.bg_screen1)));
        addSlide(AppIntroFragment.newInstance("Helps You To", " Be Aware About Epidemics", R.drawable.cold, getResources().getColor(R.color.bg_screen2)));
        addSlide(AppIntroFragment.newInstance("You Get Preventive Measures", "And Get ToBe Safe", R.drawable.seth, getResources().getColor(R.color.bg_screen3)));
        addSlide(AppIntroFragment.newInstance("Help The Community", "By Reporting About The Diseases Near You ", R.drawable.report, getResources().getColor(R.color.bg_screen4)));


        //getColor(R.id.))

        // OPTIONAL METHODS
//        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));


       // setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        setFlowAnimation();
//        // Hide Skip/Done button.
//        showSkipButton(false);
//        setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);



    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, LoginPage.class);

        startActivity(intent);
        finish();

        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, LoginPage.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
