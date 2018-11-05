package com.example.rplrus021.homebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class group_name extends AppCompatActivity {
    TextView tv_muhammad_idris,tv_annisa_rachelita,tv_lailil_firdausiyyah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        tv_muhammad_idris = (TextView) findViewById(R.id.tv_muhammad_idris);
        tv_annisa_rachelita = (TextView) findViewById(R.id.tv_annisa_rachelita);
        tv_lailil_firdausiyyah =(TextView) findViewById(R.id.tv_lailil_firdausiyyah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public boolean onSupportNavigateUp () {
        finish();
        return true;
    }

}
