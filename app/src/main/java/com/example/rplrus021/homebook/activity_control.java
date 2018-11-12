package com.example.rplrus021.homebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.prefs.Preferences;

public class activity_control extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("user_login", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        int status = sharedPreferences.getInt("status",0);
        if (status == 0) {
            Intent intent = new Intent(activity_control.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (status == 1) {
            Intent intent = new Intent(activity_control.this, navigation.class);
            startActivity(intent);
            finish();
        }
        else if (status == 2) {
            Intent intent = new Intent(activity_control.this, navigation.class);
            startActivity(intent);
            finish();
        }
    }
}
