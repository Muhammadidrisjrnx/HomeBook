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
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("guest_login", MODE_PRIVATE);
        String email = sharedPreferences.getString("email_guest_login", "");
        SharedPreferences sharedPreferences2 = getSharedPreferences("login", MODE_PRIVATE);
        String username = sharedPreferences2.getString("email", "");
        name = sharedPreferences2.getString("", username);

        if (name == "") {
            Intent intent = new Intent(activity_control.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (name == username) {
            Intent intent = new Intent(activity_control.this, navigation.class);
            finish();
            startActivity(intent);
        }

        if (email == "") {
            Intent intent = new Intent(activity_control.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (email == email){
            Intent intent = new Intent(activity_control.this, navigation.class);
            finish();
            startActivity(intent);
        }
    }
}
