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

public class PetSitterActivity extends AppCompatActivity {

    String PET_SITTER = com.example.b10715.final_pj.Config.URL + "pet_sitter.php";
    private static final String TAG_RESULTS = "result";
    private static final String ID = "id";
    private static final String USER_IMAGE = "sitter_image";
    private static final String USER_EMAIL = "user_email";
    private static final String SITTER_NAME = "sitter_name";
    private static final String SITTER_AGE = "sitter_age";
    private static final String SITTER_SEX = "sitter_sex";
    private static final String SITTER_ADR = "sitter_adr";
    private static final String SITTER_CAREER = "sitter_career";
    private static final String SITTER_PET = "sitter_pet";
    private static final String SITTER_PRICE = "sitter_price";
    private static final String SITTER_WORD = "sitter_word";

    private long lastTimeBackPressed;
    Button filter_location, filter_price, find, showallbtn;

    RecyclerView recyclerView;
    DrawerLayout mDrawerLayout;
    String myJSON;
    JSONArray contents = null;
    EditText pet_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_sitter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        getData(PET_SITTER);
        showallbtn = (Button) findViewById(R.id.showallbtn);
        showallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PetSitterActivity.this, "전체보기", Toast.LENGTH_SHORT).show();
                getData(PET_SITTER);
            }
        });
        filter_price = (Button) findViewById(R.id.pricebtn);
        filter_location = (Button) findViewById(R.id.location_btn);
        pet_search = (EditText) findViewById(R.id.edit_petsitter);
        find = (Button) findViewById(R.id.search_btn);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_petsitter(pet_search.getText().toString());
            }
        });

        FloatingActionsMenu option_btn = (FloatingActionsMenu) findViewById(R.id.btn_menu);
        com.getbase.floatingactionbutton.FloatingActionButton addedOnce = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedOnce.setTitle("찜 게시물");
        addedOnce.setIcon(R.drawable.ic_like);
        option_btn.addButton(addedOnce);

        com.getbase.floatingactionbutton.FloatingActionButton addedTwice = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        addedTwice.setTitle("펫시터 등록");
        addedTwice.setIcon(R.drawable.ic_write);
        option_btn.addButton(addedTwice);
        option_btn.removeButton(addedTwice);
        option_btn.addButton(addedTwice);
        addedTwice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditContentIntent = new Intent(PetSitterActivity.this, PetSitterEditActivity.class);
                startActivity(EditContentIntent);
            }
        });

        filter_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogItems_location();
            }
        });
        filter_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogItems_price();
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

    }

    public void find_petsitter(String sitter_email) {
        List<Item> itemsList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            contents = jsonObj.getJSONArray(TAG_RESULTS);
            Item[] item = new Item[contents.length()];

            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                if (sitter_email.equals(c.getString(USER_EMAIL))) {
                    String id = c.getString(ID);
                    String user_email = c.getString(USER_EMAIL);
                    String user_image = c.getString(USER_IMAGE);
                    String sitter_name = c.getString(SITTER_NAME);
                    String sitter_age = c.getString(SITTER_AGE);
                    String sitter_sex = c.getString(SITTER_SEX);
                    String sitter_phone_1 = c.getString("sitter_phone_1");
                    String sitter_phone_2 = c.getString("sitter_phone_2");
                    String sitter_phone_3 = c.getString("sitter_phone_3");
                    String sitter_adr = c.getString(SITTER_ADR);
                    String sitter_career_year = c.getString("sitter_career_year");
                    String sitter_career_month = c.getString("sitter_career_month");
                    String sitter_pet = c.getString(SITTER_PET);
                    int sitter_price = c.getInt(SITTER_PRICE);
                    String sitter_word = c.getString(SITTER_WORD);

                    item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                    itemsList.add(item[i]);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("입력하신 펫시터가 존재하지 않습니다.");
                    alertDialogBuilder.setPositiveButton("닫기",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    getData(PET_SITTER);
                                }
                            });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
                }


            }

            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), itemsList, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void showList() {
        List<Item> items = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            contents = jsonObj.getJSONArray(TAG_RESULTS);
            Item[] item = new Item[contents.length()];

            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);

                String id = c.getString(ID);
                String user_email = c.getString(USER_EMAIL);
                String user_image = c.getString(USER_IMAGE);
                String sitter_name = c.getString(SITTER_NAME);
                String sitter_age = c.getString(SITTER_AGE);
                String sitter_sex = c.getString(SITTER_SEX);
                String sitter_phone_1 = c.getString("sitter_phone_1");
                String sitter_phone_2 = c.getString("sitter_phone_2");
                String sitter_phone_3 = c.getString("sitter_phone_3");
                String sitter_adr = c.getString(SITTER_ADR);
                String sitter_career_year = c.getString("sitter_career_year");
                String sitter_career_month = c.getString("sitter_career_month");
                String sitter_pet = c.getString(SITTER_PET);
                int sitter_price = c.getInt(SITTER_PRICE);
                String sitter_word = c.getString(SITTER_WORD);

                item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                items.add(item[i]);
            }

            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), items, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private void showAlertDialogItems_location() {
        final String locations[] = getResources().getStringArray(R.array.adr);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("지역선택");
        adb.setItems(locations, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PetSitterActivity.this, locations[which], Toast.LENGTH_SHORT).show();
                locationRange(locations[which]);

            }
        });
        adb.setNeutralButton("닫기", null).show();
    }

    private void showAlertDialogItems_price() {
        final String prices[] = getResources().getStringArray(R.array.price);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("지역선택");
        adb.setItems(prices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PetSitterActivity.this, toString().valueOf(which), Toast.LENGTH_SHORT).show();
                switch (which) {
                    case 0:
                        priceRange(0, 5000);
                        break;
                    case 1:
                        priceRange(5000, 8000);
                        break;
                    case 2:
                        priceRange(8000, 10000);
                        break;
                    case 3:
                        priceRange(10000, 1111111111);
                        break;
                    case 4:
                        low_price_range();
                        break;
                    case 5:
                        high_price_range();
                        break;
                }

            }
        });
        adb.setNeutralButton("닫기", null).show();
    }


    public void locationRange(String location) {
        List<Item> itemsList = new ArrayList<>();
        try {
            Item[] item = new Item[contents.length()];
            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                if (c.getString(SITTER_ADR).equals(location)) {
                    String id = c.getString(ID);
                    String user_email = c.getString(USER_EMAIL);
                    String user_image = c.getString(USER_IMAGE);
                    String sitter_name = c.getString(SITTER_NAME);
                    String sitter_age = c.getString(SITTER_AGE);
                    String sitter_sex = c.getString(SITTER_SEX);
                    String sitter_phone_1 = c.getString("sitter_phone_1");
                    String sitter_phone_2 = c.getString("sitter_phone_2");
                    String sitter_phone_3 = c.getString("sitter_phone_3");
                    String sitter_adr = c.getString(SITTER_ADR);
                    String sitter_career_year = c.getString("sitter_career_year");
                    String sitter_career_month = c.getString("sitter_career_month");
                    String sitter_pet = c.getString(SITTER_PET);
                    int sitter_price = c.getInt(SITTER_PRICE);
                    String sitter_word = c.getString(SITTER_WORD);
                    item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                    itemsList.add(item[i]);
                }
            }
            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), itemsList, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void priceRange(int fromprice, int toprice) {
        List<Item> itemsList = new ArrayList<>();
        try {
            Item[] item = new Item[contents.length()];
            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                if (c.getInt(SITTER_PRICE) > fromprice && c.getInt(SITTER_PRICE) <= toprice) {
                    String id = c.getString(ID);
                    String user_email = c.getString(USER_EMAIL);
                    String user_image = c.getString(USER_IMAGE);
                    String sitter_name = c.getString(SITTER_NAME);
                    String sitter_age = c.getString(SITTER_AGE);
                    String sitter_sex = c.getString(SITTER_SEX);
                    String sitter_phone_1 = c.getString("sitter_phone_1");
                    String sitter_phone_2 = c.getString("sitter_phone_2");
                    String sitter_phone_3 = c.getString("sitter_phone_3");
                    String sitter_adr = c.getString(SITTER_ADR);
                    String sitter_career_year = c.getString("sitter_career_year");
                    String sitter_career_month = c.getString("sitter_career_month");
                    String sitter_pet = c.getString(SITTER_PET);
                    int sitter_price = c.getInt(SITTER_PRICE);
                    String sitter_word = c.getString(SITTER_WORD);
                    item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                    itemsList.add(item[i]);
                }
            }
            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), itemsList, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void low_price_range() {
        List<Item> itemsList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            contents = jsonObj.getJSONArray(TAG_RESULTS);
            Item[] item = new Item[contents.length()];
            int[][] price = new int[contents.length()][2];
            int[] order = new int[contents.length()];
            int[][] temp = new int[contents.length()][2];
            int min;
            int nextmin;
            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                price[i][0] = c.getInt(SITTER_PRICE);
                price[i][1] = i;
            }
            for (int i = 0; i < contents.length(); i++) {
                for (int k = i; k < contents.length() - 1; k++) {
                    if (price[k][0] > price[k + 1][0]) {
                        temp[k] = price[k + 1];
                        price[k + 1] = price[k];
                        price[k] = temp[k];
                    }
                }

            }

            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(price[i][1]);
                String id = c.getString(ID);
                String user_email = c.getString(USER_EMAIL);
                String user_image = c.getString(USER_IMAGE);
                String sitter_name = c.getString(SITTER_NAME);
                String sitter_age = c.getString(SITTER_AGE);
                String sitter_sex = c.getString(SITTER_SEX);
                String sitter_phone_1 = c.getString("sitter_phone_1");
                String sitter_phone_2 = c.getString("sitter_phone_2");
                String sitter_phone_3 = c.getString("sitter_phone_3");
                String sitter_adr = c.getString(SITTER_ADR);
                String sitter_career_year = c.getString("sitter_career_year");
                String sitter_career_month = c.getString("sitter_career_month");
                String sitter_pet = c.getString(SITTER_PET);
                int sitter_price = c.getInt(SITTER_PRICE);
                String sitter_word = c.getString(SITTER_WORD);


                item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                itemsList.add(item[i]);


            }

            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), itemsList, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void high_price_range() {
        List<Item> itemsList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            contents = jsonObj.getJSONArray(TAG_RESULTS);
            Item[] item = new Item[contents.length()];
            int[][] price = new int[contents.length()][2];
            int[] order = new int[contents.length()];
            int[][] temp = new int[contents.length()][2];
            int min;
            int nextmin;
            for (int i = 0; i < contents.length(); i++) {
                JSONObject c = contents.getJSONObject(i);
                price[i][0] = c.getInt(SITTER_PRICE);
                price[i][1] = i;
            }
            for (int i = 0; i < contents.length(); i++) {
                for (int k = i; k < contents.length() - 1; k++) {
                    if (price[k][0] > price[k + 1][0]) {
                        temp[k] = price[k + 1];
                        price[k + 1] = price[k];
                        price[k] = temp[k];
                    }
                }

            }

            for (int i = contents.length() - 1; i >= 0; i--) {
                JSONObject c = contents.getJSONObject(price[i][1]);
                String id = c.getString(ID);
                String user_email = c.getString(USER_EMAIL);
                String user_image = c.getString(USER_IMAGE);
                String sitter_name = c.getString(SITTER_NAME);
                String sitter_age = c.getString(SITTER_AGE);
                String sitter_sex = c.getString(SITTER_SEX);
                String sitter_phone_1 = c.getString("sitter_phone_1");
                String sitter_phone_2 = c.getString("sitter_phone_2");
                String sitter_phone_3 = c.getString("sitter_phone_3");
                String sitter_adr = c.getString(SITTER_ADR);
                String sitter_career_year = c.getString("sitter_career_year");
                String sitter_career_month = c.getString("sitter_career_month");
                String sitter_pet = c.getString(SITTER_PET);
                int sitter_price = c.getInt(SITTER_PRICE);
                String sitter_word = c.getString(SITTER_WORD);


                item[i] = new Item(id, user_email, user_image, sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word, sitter_price);
                itemsList.add(item[i]);


            }

            recyclerView.setAdapter(new PetSitterAdapter(getApplicationContext(), itemsList, R.layout.activity_pet_sitter));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

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
                        Intent intent = new Intent(PetSitterActivity.this, LoginActivity.class);
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
                    Intent homeIntent = new Intent(PetSitterActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(PetSitterActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(PetSitterActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(PetSitterActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent setIntent = new Intent(PetSitterActivity.this, SettingActivity.class);
                    startActivity(setIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(PetSitterActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(PetSitterActivity.this, PetSitterEditActivity.class);
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
