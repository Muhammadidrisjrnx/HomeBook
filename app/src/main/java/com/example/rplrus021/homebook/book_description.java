package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class book_description extends AppCompatActivity {
    public ArrayList<buku> homebook = new ArrayList<>();
    String id_buku, judul_buku, gambar, deskripsi, email;
    Bundle bundle;
    Button btn_booking;
    LinearLayout linearLayout_book_description;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        ImageView img_description_book = (ImageView) findViewById(R.id.img_description_book);
        TextView descriptionbar = (TextView) findViewById(R.id.txt_description_book);
        TextView txt_title_book = (TextView) findViewById(R.id.txt_title_book);
        TextView txt_description_text2 = (TextView) findViewById(R.id.txt_description_text2);
        linearLayout_book_description = (LinearLayout) findViewById(R.id.linier_layout_book_description);
        btn_booking = (Button) findViewById(R.id.btn_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        id_buku = bundle.getString("id_buku");
        judul_buku = bundle.getString("judul_buku");
        gambar = bundle.getString("gambar");
        deskripsi = bundle.getString("deskripsi");
        Glide.with(book_description.this)
                .load(gambar)
                .into(img_description_book);
        txt_title_book.setText(judul_buku);
        txt_description_text2.setText(deskripsi);
        descriptionbar.setText(judul_buku);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("guest_login", MODE_PRIVATE);
        if (email == email) {
            btn_booking.setVisibility(View.GONE);
        }
   //     new ambildata().execute();

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getApplicationContext(), booking.class);
                pindah.putExtra("gambar", gambar);
                pindah.putExtra("id_buku", id_buku);
                pindah.putExtra("judul_buku", judul_buku);
                startActivity(pindah);
                finish();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    @SuppressLint("StaticFieldLeak")
//    public class ambildata extends AsyncTask<Void, Void, JSONObject> {
//
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected JSONObject doInBackground(Void... params) {
//            JSONObject jsonObject;
//            id_buku = bundle.getString("id_buku");
//            try {
//                String url = "https://homebookappandroid.000webhostapp.com/db_buku/input_data.php?id_buku=" + id_buku + "";
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(url);
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                InputStream inputStream = httpEntity.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(
//                        inputStream, "iso-8859-1"
//                ), 8);
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//                inputStream.close();
//                String json = stringBuilder.toString();
//                jsonObject = new JSONObject(json);
//            } catch (Exception e) {
//                jsonObject = null;
//            }
//            return jsonObject;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
//            try {
//                JSONArray hasiljson = jsonObject.getJSONArray("Result");
//            } catch (Exception e) {
//                Log.d("errorku ", "onPostExecute: " + e.toString());
//            }
//        }
//    }

}
