package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class booking extends AppCompatActivity {
    Button btn_submit;
    TextView txt_tanggal, txt_tanggal2, txt_tanggal3;
    Bundle bundle;
    String judul_buku, username, id_buku, gambar, a, b, c, d;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_tanggal2 = (TextView) findViewById(R.id.txt_tanggal2);
        txt_tanggal3 = (TextView) findViewById(R.id.txt_tanggal3);
        txt_tanggal = (TextView) findViewById(R.id.txt_tanggal);
        loading = (ProgressBar) findViewById(R.id.loading_booking);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        gambar = bundle.getString("gambar");
        judul_buku = bundle.getString("judul_buku");
        id_buku = bundle.getString("id_buku");
        txt_tanggal.setText(judul_buku);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int pinjam = 1;
        int kembali = 14;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyMMdd");
        a = simpleDateFormat1.format(calendar.getTime());
        calendar.add(Calendar.DATE, pinjam);
        b = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, kembali);
        c = simpleDateFormat.format(calendar.getTime());

        txt_tanggal2.setText(b);
        txt_tanggal3.setText(c);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new booking_book().execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class booking_book extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            btn_submit.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                String username = sharedPreferences.getString("email", "");
                Intent intent = getIntent();
                bundle = intent.getExtras();
                id_buku = bundle.getString("id_buku");
                judul_buku = bundle.getString("judul_buku");
                gambar = bundle.getString("gambar");
                String tmp_judul_buku = judul_buku.replaceAll(" ", "%20");
                String id_booking = "HB" + username + a;
                String url = config_url.url+"db_buku/booking.php?id_booking=" + id_booking + "&&id_buku=" + id_buku + "&&username=" + username + "&&tanggal_pinjam=" + b + "&&tanggal_kembali=" + c + "&&judul_buku="+tmp_judul_buku+"&&gambar="+gambar+"";
                System.out.println(url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            btn_submit.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);

            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Booking Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(booking.this, navigation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (sukses.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Booking Fails", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            } else {
            }
        }
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
