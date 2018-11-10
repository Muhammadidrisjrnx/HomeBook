package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView gvbuku;
    public ArrayList<buku> homebook;
    public ArrayList<userLogin> userlogin;
    userLogin user;
    Bundle bundle;
    String nama, username, gambar;
    Intent intent;
    de.hdodenhof.circleimageview.CircleImageView image_profil;
    TextView nav_user;
    private int dataoffset = 1;
    private Parcelable more;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int default_data_offset = 1;
    private ambildata ambildata;
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homebook = new ArrayList<buku>();
        gvbuku = (RecyclerView) findViewById(R.id.gvbuku);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        user = new userLogin();
        ambildata = new ambildata(dataoffset);

        ambildata.execute();
        gvbuku.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    dataoffset += 4;
                    more = gvbuku.getLayoutManager().onSaveInstanceState();
                    progressBar.setVisibility(View.VISIBLE);
                    ambildata = new ambildata(dataoffset);
                    ambildata.execute();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homebook.clear();
                dataoffset = default_data_offset;
                ambildata = new ambildata(dataoffset);
                ambildata.execute();
            }
        });

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.txt_profil_name);
        image_profil = (de.hdodenhof.circleimageview.CircleImageView) hView.findViewById(R.id.icon_profil);
        //Guest Mode or User Mode Login

        //guest mode
        SharedPreferences sharedPreferences = getSharedPreferences("guest_login", MODE_PRIVATE);
        String username_guest_login = sharedPreferences.getString("email_guest_login", "");
        String password_guest_login = sharedPreferences.getString("password_guest_login", "");
        SharedPreferences sharedPreferences2 = getSharedPreferences("login", MODE_PRIVATE);
        String username_user = sharedPreferences2.getString("email", "");
        String password_user = sharedPreferences2.getString("password", "");
        if (username_guest_login == username_guest_login && password_guest_login == password_guest_login) {
            nav_user.setText(username_guest_login);
            Toast.makeText(getApplicationContext(), username_guest_login, Toast.LENGTH_SHORT).show();
            image_profil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(navigation.this, profil_layout.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
        //user mode
        else if (username_user == username_user && password_user == password_user) {
            new load_user().execute();
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you want to exit ?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    navigation.this.finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            Intent search_button = new Intent(navigation.this, Search.class);
            startActivity(search_button);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_book) {
            Intent i = new Intent(navigation.this, my_book_layout.class);
            i.putExtra(gambar, "gambar");
            startActivity(i);
        } else if (id == R.id.about_us) {
            Intent i = new Intent(navigation.this, About_Us.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            String username = sharedPreferences.getString("email", "");
            SharedPreferences sharedPreferences1 = getSharedPreferences("guest_login", MODE_PRIVATE);
            String email = sharedPreferences1.getString("email_guest_login", "");
            if (nama == username) {
                Intent i = new Intent(navigation.this, MainActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
                if (email == email) {
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.clear();
                    editor1.commit();
                    Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Logout Fails ", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("StaticFieldLeak")
    public class ambildata extends AsyncTask<Void, Void, JSONObject> {

        private int dataoffset;

        private ambildata(int dataoffset) {
            this.dataoffset = dataoffset;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String url = config_url.url + "db_buku/getData.php?offset=" + dataoffset + "";
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
            progressBar.setVisibility(View.GONE);
            if (jsonObject == null) {
                dataoffset -= 4;
            }
            swipeRefreshLayout.setRefreshing(false);
            if (jsonObject != null) {
                try {
                    JSONArray hasiljson = jsonObject.getJSONArray("Result");
                    buku all;
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
                    gvbuku.setLayoutManager(new LinearLayoutManager(navigation.this, LinearLayoutManager.VERTICAL, false));
                    CostumAdapter customAdapter = new CostumAdapter(navigation.this);
                    customAdapter.setArrayListBuku(homebook);
                    gvbuku.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                    gvbuku.getLayoutManager().onRestoreInstanceState(more);
                } catch (Exception e) {

                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class load_user extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                String username = sharedPreferences.getString("email", "");
                String url = config_url.url + "db_buku/load_image_profil.php?username=" + username + "";
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
                userlogin = new ArrayList<userLogin>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    user = new userLogin();
                    user.setUsername(hasiljson.getJSONObject(i).getString("username"));
                    user.setNama(hasiljson.getJSONObject(i).getString("nama"));
                    user.setGambar(hasiljson.getJSONObject(i).getString("image_path"));
                    userlogin.add(user);
                    nav_user.setText(user.getNama());
                    Glide.with(getApplicationContext())
                            .load(config_url.url3 + user.getGambar())
                            .placeholder(R.drawable.profil)
                            .into(image_profil);
                    System.out.println("url image " + config_url.url3 + user.getGambar());
                    image_profil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), profil_layout.class);
                            startActivityForResult(intent, 1);
                        }
                    });

                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class load_guest extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                String username = sharedPreferences.getString("email", "");
                String url = config_url.url + "db_buku/load_image_profil.php?username=" + username + "";
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
                userlogin = new ArrayList<userLogin>();
                for (int i = 0; i < hasiljson.length(); i++) {
                    user = new userLogin();
                    user.setUsername(hasiljson.getJSONObject(i).getString("username"));
                    user.setNama(hasiljson.getJSONObject(i).getString("nama"));
                    user.setGambar(hasiljson.getJSONObject(i).getString("image_path"));
                    userlogin.add(user);
                    nav_user.setText(user.getNama());
                    Glide.with(getApplicationContext())
                            .load(config_url.url3 + user.getGambar())
                            .placeholder(R.drawable.profil)
                            .into(image_profil);
                    System.out.println("url image " + config_url.url3 + user.getGambar());
                    image_profil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), profil_layout.class);
                            startActivityForResult(intent, 1);
                        }
                    });

                }
            } catch (Exception e) {
                Log.d("errorku ", "onPostExecute: " + e.toString());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("image_profil");
                Glide.with(getApplicationContext())
                        .load(result)
                        .placeholder(R.drawable.profil)
                        .into(image_profil);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
