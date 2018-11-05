package com.example.rplrus021.homebook;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class rate_us extends AppCompatActivity {
    RatingBar ratingBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate_us);
        textView = (TextView) findViewById(R.id.tv_rate_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

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
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rate Us");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Rate From You "+v);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("", "Finished sending email...");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(rate_us.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
