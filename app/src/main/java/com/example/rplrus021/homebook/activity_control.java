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

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences sharedPreferences2 = getSharedPreferences("login", MODE_PRIVATE);
        String username = sharedPreferences2.getString("email", "");
        Log.d("nama ", "onCreate: " + username);
        name = sharedPreferences.getString("",username);
        Log.d("nama ", "onCreate: "+name);
            if (name == "") {
                Intent intent = new Intent(activity_control.this, MainActivity.class);
                editor.putString("isLogged", username);
                editor.commit();
                startActivity(intent);
                finish();
            } else if (name == username){
                Intent intent = new Intent(activity_control.this, navigation.class);
                finish();
                startActivity(intent);
            }else{
                Intent intent = new Intent(activity_control.this, navigation.class);
                finish();
                startActivity(intent);
            }

    }
}
