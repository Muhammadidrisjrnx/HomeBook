package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class my_book_layout extends AppCompatActivity {
    GridView gv_buku_my_book;
    public ArrayList<user_booking> homebook = new ArrayList<>();
    public ArrayList<buku> buku = new ArrayList<>();
    buku bukuid;
    user_booking all;
    Bundle bundle;
    String gambar;
    LinearLayout linier_layout_my_book_load, linier_layout_my_book;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LayoutInflater layoutInflater;
    View myLayout;
    TextView judul_buku;
    private SwipeRefreshLayout swipeRefreshLayout;
    private view_my_book view_my_book;
    private SharedPreferences sharedPreferences;
    private String username,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_back_mybook);
        gv_buku_my_book = (GridView) findViewById(R.id.gv_buku_my_book);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        linier_layout_my_book = (LinearLayout) findViewById(R.id.linier_layout_my_book);
        linier_layout_my_book_load = (LinearLayout) findViewById(R.id.linier_layout_my_book_load);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("user_login", MODE_PRIVATE);
        username = sharedPreferences.getString("email", "");
        status = sharedPreferences.getString("status","");
        this.registerForContextMenu(gv_buku_my_book);
        if (status == "1"){
            view_my_book = new view_my_book();
            view_my_book.execute();
            layoutInflater = getLayoutInflater();
            myLayout = layoutInflater.inflate(R.layout.row_item3, null);
            judul_buku = (TextView) myLayout.findViewById(R.id.tv_judul_my_book);
        }else if (status == "2"){

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homebook.clear();
                view_my_book = new view_my_book();
                view_my_book.execute();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class view_my_book extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            linier_layout_my_book.setVisibility(View.INVISIBLE);
            linier_layout_my_book_load.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = config_url.url + "db_buku/get_data_my_book.php?username=" + username + "";
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
            linier_layout_my_book.setVisibility(View.VISIBLE);
            linier_layout_my_book_load.setVisibility(View.INVISIBLE);
            try {
                JSONArray hasiljson = jsonObject.getJSONArray("Result");
                homebook = new ArrayList<user_booking>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    all = new user_booking();
                    all.setNo_urut(hasiljson.getJSONObject(i).getString("no_urut"));
                    all.setId_booking(hasiljson.getJSONObject(i).getString("id_booking"));
                    all.setId_buku(hasiljson.getJSONObject(i).getString("id_buku"));
                    all.setUsername(hasiljson.getJSONObject(i).getString("username"));
                    all.setTanggal_pinjam(hasiljson.getJSONObject(i).getString("tanggal_pinjam"));
                    all.setTanggal_kembali(hasiljson.getJSONObject(i).getString("tanggal_kembali"));
                    all.setJudul_buku(hasiljson.getJSONObject(i).getString("judul_buku"));
                    all.setGambar(hasiljson.getJSONObject(i).getString("gambar"));
                    homebook.add(all);
                }
                CostumerAdapter3 customAdapter = new CostumerAdapter3(homebook, my_book_layout.this);
                gv_buku_my_book.setAdapter(customAdapter);
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

}
