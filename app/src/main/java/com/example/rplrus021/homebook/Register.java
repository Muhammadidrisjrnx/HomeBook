package com.example.rplrus021.homebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Register extends AppCompatActivity {
    ImageView imageView2;
    EditText edName, edUsername, edPassword, edAlamat, edTelepon;
    Button btnRegister;
    userRegister user;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        edName = (EditText) findViewById(R.id.ednama);
        edUsername = (EditText) findViewById(R.id.edusername);
        edPassword = (EditText) findViewById(R.id.edpassword);
        edAlamat = (EditText) findViewById(R.id.edalamat);
        edTelepon = (EditText) findViewById(R.id.edtlp);
        btnRegister = (Button) findViewById(R.id.btnregister);
        user = new userRegister();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setMessage("Is the data you entered correct?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.setUsername(edUsername.getText().toString());
                        user.setPassword(edPassword.getText().toString());
                        user.setNama(edName.getText().toString());
                        user.setAlamat(edAlamat.getText().toString());
                        user.setTelepon(edTelepon.getText().toString());
                        new RegisterProcess().execute();
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
        });


    }

    public void HaveAccount(View view) {
        Intent pindah = new Intent(Register.this, MainActivity.class);
        startActivity(pindah);
    }

    @SuppressLint("StaticFieldLeak")
    public class RegisterProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading


        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;


            try {
                String tmpName = user.getNama().replaceAll(" ", "%20");
                String tmpAlamat = user.getAlamat().replaceAll(" ", "%20");
                String url = config_url.url+"db_buku/aksi_daftar.php?username=" + user.getUsername() + "&&password=" + user.getPassword() + "&&nama=" + tmpName + "&&telepon=" + user.getTelepon() + "&&alamat=" + tmpAlamat + "";
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

            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, navigation.class);
                        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        String email = user.getUsername();
                        String nama = user.getNama();
                        editor = preferences.edit();
                        editor.putString("email",email);
                        editor.putString("nama",nama);
                        editor.apply();
                        startActivity(intent);
                        finish();
                        Log.d("cek ","nama "+email+" "+nama);
                        //to main menu
                    } else if (sukses.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Register Fails", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            } else {
            }
        }
    }

}
