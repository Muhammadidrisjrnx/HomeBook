package com.example.rplrus021.homebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class app_description extends AppCompatActivity {
    TextView app_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_description);
        app_description =(TextView)findViewById(R.id.tv_app_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app_description.setText("Aplikasi ini digunakan untuk para pembaca buku yang ingin meminjam buku atau membaca buku tanpa keluar rumah dan hanya butuh mendownload aplikasi ini saja.");
    }
    public boolean onSupportNavigateUp () {
        finish();
        return true;
    }

}
