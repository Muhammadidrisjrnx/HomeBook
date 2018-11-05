package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;

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

public class Search extends AppCompatActivity {
    EditText ed_search;
    String name_search;
    Toolbar toolbar_back_button;
    MenuItem search;
    GridView search_book;
    buku all;
    public ArrayList<buku> homebook = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private String[] about = {"Novel", "Komik", "Manga"};
    String komik = "komik";
    String novel = "novel";
    String manga = "manga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ed_search = (EditText) findViewById(R.id.ed_search);
        toolbar_back_button = (Toolbar) findViewById(R.id.back_button);
        search_book = (GridView) findViewById(R.id.search_book);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, about);
        search_book.setAdapter(arrayAdapter);
        search_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    new SearchNovel().execute();
                } else if (i == 1) {
                    new SearchKomik().execute();
                } else if (i == 2) {
                    new SearchManga().execute();
                }
            }
        });

        setSupportActionBar(toolbar_back_button);
        toolbar_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_button = new Intent(Search.this, navigation.class);
                startActivity(back_button);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        search = menu.findItem(R.id.app_bar_search2);
        search.setVisible(true);
        if (ed_search.getText().toString() == "") {
            ed_search.setError("Please Insert Your book");
        }
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_search2) {
            //coding search di sini

            new SearchProcess().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class SearchProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            search.setVisible(false);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;


            try {
                name_search = ed_search.getText().toString().replaceAll(" ", "%20");
                String url = "https://homebookappandroid.000webhostapp.com/db_buku/search.php?judul_buku=" + name_search + "";
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
            search.setVisible(true);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Book name found ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Search.this, search_book.class);
                        intent.putExtra("judul_buku", name_search);
                        startActivity(intent);
                        finish();
                    } else if (sukses.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Book name not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            } else {
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SearchKomik extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;


            try {
                String url = "https://homebookappandroid.000webhostapp.com/db_buku/search_komik.php?tipe_buku=" + komik + "";
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

            try {
                JSONArray hasiljson = jsonObject.getJSONArray("Result");
                homebook = new ArrayList<buku>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    all = new buku();
                    all.setTipe_buku(hasiljson.getJSONObject(i).getString("tipe_buku"));
                    homebook.add(all);
                    Intent intent = new Intent(Search.this, search_book_komik.class);
                    intent.putExtra("tipe_buku", komik);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SearchNovel extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://homebookappandroid.000webhostapp.com/db_buku/search_komik.php?tipe_buku=" + novel + "";
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

            try {
                JSONArray hasiljson = jsonObject.getJSONArray("Result");
                homebook = new ArrayList<buku>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    all = new buku();
                    all.setTipe_buku(hasiljson.getJSONObject(i).getString("tipe_buku"));
                    homebook.add(all);
                    Intent intent = new Intent(Search.this, search_book_komik.class);
                    intent.putExtra("tipe_buku", novel);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SearchManga extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://homebookappandroid.000webhostapp.com/db_buku/search_komik.php?tipe_buku=" + manga + "";
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

            try {
                JSONArray hasiljson = jsonObject.getJSONArray("Result");
                homebook = new ArrayList<buku>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    all = new buku();
                    all.setTipe_buku(hasiljson.getJSONObject(i).getString("tipe_buku"));
                    homebook.add(all);
                    Intent intent = new Intent(Search.this, search_book_komik.class);
                    intent.putExtra("tipe_buku", manga);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

}
