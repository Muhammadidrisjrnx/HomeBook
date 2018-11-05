package com.example.rplrus021.homebook;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class About_Us extends AppCompatActivity {
    ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private String[] about = {"Group Name", "Application Description", "Report Bug", "Contact Us", "Rate Us"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);
        listView = (ListView) findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, about);
        listView.setAdapter(arrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(view.getContext(), group_name.class);
                    startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(view.getContext(), app_description.class);
                    startActivity(intent);
                } else if (i == 2) {
                    String[] TO = {
                            "idris.xrplsmkrus@gmail.com"
                    };
                    String[] CC = {
                            "idris.xrplsmkrus@gmail.com"
                    };
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Bug");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Report Bug");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("", "Finished sending email...");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(About_Us.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }

                } else if (i == 3) {
                    String[] TO = {
                            "idris.xrplsmkrus@gmail.com"
                    };
                    String[] CC = {
                            "idris.xrplsmkrus@gmail.com"
                    };
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Your message ...");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("", "Finished sending email...");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(About_Us.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }

                } else if (i == 4) {
                    Intent intent = new Intent(view.getContext(), rate_us.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
