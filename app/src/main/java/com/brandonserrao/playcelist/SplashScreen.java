package com.brandonserrao.playcelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // saving tokens and stuff





        // Start home activity
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        // close splash activity
        finish();
    }
}