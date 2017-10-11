package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;

public class Change_password extends AppCompatActivity {
    String DATA_URL = Config.URL + "getdata_user.php";
    String EDIT_DATA_URL = Config.URL + "user_password_edit.php";
    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String userid = "id";
    private static final String userpassword="password";
    private static final String useremail="user_email";
    DrawerLayout mDrawerLayout;



    EditText newpassword;
    EditText checkpassword;
    Button okbtn;

    String user_email =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        okbtn=(Button)findViewById(R.id.okbtn);
        newpassword=(EditText)findViewById(R.id.newpassword);
        checkpassword=(EditText)findViewById(R.id.checkpassword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener());

        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(com.example.b10715.final_pj.Config.user_img));


        /*getData(DATA_URL);*/

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(newpassword.getText().toString().equals(checkpassword.getText().toString())) {
                    Toast.makeText(Change_password.this,"비밀번호 변경. 재 로그인 하십시오", Toast.LENGTH_SHORT).show();
                    insertToDatabase(LoginActivity.call_email, checkpassword.getText().toString());
                }else{
                    Toast.makeText(Change_password.this,"새 비밀번호와 확인 비밀번호가 다릅니다..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertToDatabase(String user_email, String password) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Change_password.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String user_email = (String) params[0];
                    String password = (String) params[1];


                    String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(EDIT_DATA_URL);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(user_email,password);
        logout();

    }


    private void logout() {
        Intent intent = new Intent(Change_password.this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
/*            case R.id.action_settings:
                return true;*/

            case R.id.menuLogout: {
                logout();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(Change_password.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(Change_password.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(Change_password.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(Change_password.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent feedIntent = new Intent(Change_password.this, SettingActivity.class);
                    startActivity(feedIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(Change_password.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(Change_password.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }
}