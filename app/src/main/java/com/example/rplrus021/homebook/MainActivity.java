package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText Username, Password;
    Button btnlogin;
    userRegister user;
    CheckBox checkBox;
    ProgressBar loading;
    private SharedPreferences sharedPreferences, sharedPreferences1;
    private SharedPreferences.Editor editor, editor1;
    private TextView textView_skip;
    private String email_user, password_user, email_guest, password_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Username = (EditText) findViewById(R.id.nama);
        Password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        loading = (ProgressBar) findViewById(R.id.loading);
        textView_skip = (TextView) findViewById(R.id.text_view_skip);
        user = new userRegister();
        sharedPreferences = getSharedPreferences("user_login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(Username.getText().toString());
                user.setPassword(Password.getText().toString());
                if (user.getUsername().isEmpty() && user.getPassword().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Cannot Log-in Please Insert Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    new LoginProcess().execute();
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                moveTaskToBack(true);
                MainActivity.this.finish();
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

    public void CreateAccount(View view) {
        Intent daftar = new Intent(MainActivity.this, Register.class);
        startActivity(daftar);
    }

    public void skip_action(View view) {
        Intent intent = new Intent(MainActivity.this, navigation.class);
        editor.putString("email", "GUEST");
        editor.putString("password", "GUEST_PASSWORD");
        editor.putString("status","2");
        editor.apply();
        startActivity(intent);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;


            try {
                String url = config_url.url + "db_buku/aksi_masuk.php?username=" + user.getUsername() + "&password=" + user.getPassword() + "";
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
            loading.setVisibility(View.INVISIBLE);
            btnlogin.setVisibility(View.VISIBLE);
            if (jsonObject == null) {
                Log.e("TAG", "onnull: " + jsonObject.toString());
            }

            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    System.out.println(sukses);
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        editor.putString("email", user.getUsername());
                        editor.putString("password", user.getPassword());
                        editor.putString("status","1");
                        editor.apply();
                        Intent login = new Intent(MainActivity.this, navigation.class);
                        startActivity(login);
                        finish();

                        //to main menu
                    } else if (sukses.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Login Fails", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            } else {
            }
        }
    }


}
