package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class search_book extends AppCompatActivity {
    GridView gvbuku2;
    public ArrayList<buku> homebook = new ArrayList<>();
    buku all;
    TextView tv_search_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        gvbuku2 = (GridView) findViewById(R.id.gvbuku2);
        tv_search_book = (TextView) findViewById(R.id.tv_search_book);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String judul_buku = bundle.getString("judul_buku");
        tv_search_book.setText(judul_buku);
        new search_data_buku().execute();
        this.registerForContextMenu(gvbuku2);
        gvbuku2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), book_description.class);
                String nama_buku = all.getJudul_buku();
                i.putExtra("judul_buku", nama_buku);
                i.putExtra("gambar", all.getNama_gambar());
                i.putExtra("deskripsi", all.getDescription());
                startActivity(i);
                finish();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search2) {
            //coding search di sini
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    public class search_data_buku extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String judul_buku = bundle.getString("judul_buku");
            try {
                String url = "https://homebookappandroid.000webhostapp.com/db_buku/getData2.php?judul_buku=" + judul_buku + "";
                System.out.println("url " + url);
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
            try {
                JSONArray hasiljson = jsonObject.getJSONArray("Result");
                homebook = new ArrayList<buku>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    all = new buku();
                    all.setId_buku(hasiljson.getJSONObject(i).getString("id_buku"));
                    all.setNama_gambar(hasiljson.getJSONObject(i).getString("nama_gambar"));
                    all.setJudul_buku(hasiljson.getJSONObject(i).getString("judul_buku"));
                    all.setId_penulis(hasiljson.getJSONObject(i).getString("id_penulis"));
                    all.setHalaman_buku(hasiljson.getJSONObject(i).getString("halaman_buku"));
                    all.setId_penerbit(hasiljson.getJSONObject(i).getString("id_penerbit"));
                    all.setTahun_terbit(hasiljson.getJSONObject(i).getString("tahun_terbit"));
                    all.setDescription(hasiljson.getJSONObject(i).getString("description"));
                    homebook.add(all);
                }
                CostumAdapter2 customAdapter = new CostumAdapter2(homebook, search_book.this);
                gvbuku2.setAdapter(customAdapter);
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

}
