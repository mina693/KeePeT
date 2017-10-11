package com.example.b10715.final_pj;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.graphics.Bitmap.Config;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.configChanges;
import static android.R.attr.data;
import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;
import static com.example.b10715.final_pj.R.drawable.ic_action_user;
import static com.example.b10715.final_pj.R.drawable.ic_write;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String ID = "id";
    private static final String USER_IMAGE = "user_image";
    private static final String USER_EMAIL = "user_email";
    private static final String IMAGE = "image";
    private static final String TEXT = "text";

    private long lastTimeBackPressed;

    RecyclerView recyclerView;
    DrawerLayout mDrawerLayout;
    String myJSON;
    String myJSON2;
    String DATA_URL = com.example.b10715.final_pj.Config.URL + "pet_stagram.php";
    String USER_DATA_URL = com.example.b10715.final_pj.Config.URL + "getdata_user.php";
    JSONArray contents = null;
    JSONArray userArray = null;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        getData(DATA_URL);
        getData_user(USER_DATA_URL);

        FloatingActionsMenu option_btn = (FloatingActionsMenu) findViewById(R.id.btn_menu);
        com.getbase.floatingactionbutton.FloatingActionButton addedOnce = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedOnce.setTitle("내 게시물");
        addedOnce.setIcon(R.drawable.ic_range);
        option_btn.addButton(addedOnce);

        com.getbase.floatingactionbutton.FloatingActionButton addedTwice = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedTwice.setTitle("게시물 올리기");
        addedTwice.setIcon(R.drawable.ic_write);
        option_btn.addButton(addedTwice);
        option_btn.removeButton(addedTwice);
        option_btn.addButton(addedTwice);
        addedTwice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditContentIntent = new Intent(HomeActivity.this, ContentEditActivity.class);
                startActivity(EditContentIntent);
            }
        });

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("home_Tab1");
        Drawable home = ContextCompat.getDrawable(this, R.drawable.ic_action_change_home);
        spec1.setIndicator("", home);
        spec1.setContent(R.id.tab1);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("home_Tab2");
        Drawable cam = ContextCompat.getDrawable(this, R.drawable.ic_action_cam);
        spec2.setIndicator("", cam);
        spec2.setContent(R.id.tab2);
        tabHost.addTab(spec2);

        TabHost.TabSpec spec3 = tabHost.newTabSpec("home_Tab3");
        Drawable gps = ContextCompat.getDrawable(this, R.drawable.ic_action_gps);
        spec3.setIndicator("", gps);
        spec3.setContent(R.id.tab3);
        tabHost.addTab(spec3);

        TabHost.TabSpec spec4 = tabHost.newTabSpec("home_Tab4");
        Drawable user = ContextCompat.getDrawable(this, ic_action_user);
        spec4.setIndicator("", user);
        spec4.setContent(R.id.tab4);
        tabHost.addTab(spec4);

        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("home_Tab1")) {
                    Intent intenta = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intenta);
                } else if (tabId.equals("home_Tab2")) {
                    Intent intents = new Intent(HomeActivity.this, CamActivity.class);
                    startActivity(intents);
                } else if (tabId.equals("home_Tab3")) {
                    Intent intentd = new Intent(HomeActivity.this, GpsActivity.class);
                    startActivity(intentd);
                } else if (tabId.equals("home_Tab4")) {
                    Intent intentf = new Intent(HomeActivity.this, PetActivity.class);
                    startActivity(intentf);
                }
            }
        });

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

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            contents = jsonObj.getJSONArray(TAG_RESULTS);
            Item[] item = new Item[contents.length()];


          /*  for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                String id = c.getString(ID);
                String user_image = c.getString(USER_IMAGE);
                String user_email = c.getString(USER_EMAIL);
                String image = c.getString(IMAGE);
                String text = c.getString(TEXT);

                item[i] = new Item(id, user_image, user_email, image, text);
                items.add(item[i]);

            }*/

            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                String id = c.getString(ID);
                String user_image = c.getString(USER_IMAGE);
                String user_email = c.getString(USER_EMAIL);
                String image = c.getString(IMAGE);
                String text = c.getString(TEXT);

                item[contents.length()-i-1] = new Item(id, user_image, user_email, image, text);
                items.add(item[contents.length()-i-1]);

            }

            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_home));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void showList_user() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON2);
            userArray = jsonObj.getJSONArray(TAG_RESULTS);
            String user_id = null;
            String password = null;
            String user_email = null;
            String user_image = null;
            String tokenID = null;
            String user_name = null;
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject c = userArray.getJSONObject(i);
                if (LoginActivity.call_email.equals(c.getString("user_email"))) {
                    user_id = c.getString("id");
                    password = c.getString("password");
                    user_email = c.getString("user_email");
                    user_image = c.getString("user_image");
                    tokenID = c.getString("tokenID");
                    user_name = c.getString("user_name");
                }
            }
            com.example.b10715.final_pj.Config.con_password = password;
            com.example.b10715.final_pj.Config.con_user_id = user_id;
            com.example.b10715.final_pj.Config.con_user_name = user_name;
            com.example.b10715.final_pj.Config.con_tokenID = tokenID;
            com.example.b10715.final_pj.Config.con_user_image = user_image;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("dd", com.example.b10715.final_pj.Config.con_user_name);

    }


    // php의 Data를 가져오는 함수
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void getData_user(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON2 = result;
                showList_user();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


/*
    public void animateFAB() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab.startAnimation(rotate_backward);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab.startAnimation(rotate_forward);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");
        }
    }
*/


    //Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes       ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No        ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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


    class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        MyOnNavigationItemSelectedListener() {

        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(HomeActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(HomeActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(HomeActivity.this, PetActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent setIntent = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(setIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(HomeActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(HomeActivity.this, UserActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                moveTaskToBack(true); // 본Activity finish후 다른 Activity가 뜨는 걸 방지.
                finish();
                return;
            }
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
