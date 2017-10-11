package com.example.b10715.final_pj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.ActionBar;

public class PetActivity extends AppCompatActivity {

    String myJSON;
    String DATA_URL = Config.URL + "getdata_pet.php";

    private static final String TAG_RESULTS = "result";
    private static final String user_email = "user_email";
    private static final String pet_name = "pet_name";
    private static final String pet_birth = "pet_birth";
    private static final String pet_sex = "pet_sex";
    private static final String pet_species = "pet_species";
    private static final String pet_breed = "pet_breed";
    private static final String pet_number = "pet_number";
    private static final String pet_id = "id";
    private String pet_image = "pet_image";
    private ImageView pet_img;
    Boolean isEdit = false;
    TextView text;

    JSONArray peoples = null;
    DrawerLayout mDrawerLayout;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        list = (ListView) findViewById(R.id.listView);
        pet_img = (ImageView) this.findViewById(R.id.pet_img);
        personList = new ArrayList<HashMap<String, String>>();
        getData(DATA_URL);

        list.setOnItemClickListener(listener);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("User_Tab1");
        Drawable home = ContextCompat.getDrawable(this, R.drawable.ic_action_home);
        spec1.setIndicator("", home);
        spec1.setContent(R.id.tab1);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("User_Tab2");
        Drawable cam = ContextCompat.getDrawable(this, R.drawable.ic_action_cam);
        spec2.setIndicator("", cam);
        spec2.setContent(R.id.tab2);
        tabHost.addTab(spec2);

        TabHost.TabSpec spec3 = tabHost.newTabSpec("User_Tab3");
        Drawable gps = ContextCompat.getDrawable(this, R.drawable.ic_action_gps);
        spec3.setIndicator("", gps);
        spec3.setContent(R.id.tab3);
        tabHost.addTab(spec3);

        TabHost.TabSpec spec4 = tabHost.newTabSpec("User_Tab4");
        Drawable user = ContextCompat.getDrawable(this, R.drawable.ic_petlist);
        spec4.setIndicator("", user);
        spec4.setContent(R.id.tab4);
        tabHost.addTab(spec4);

        tabHost.setCurrentTab(3);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("User_Tab1")) {
                    Intent intenta = new Intent(PetActivity.this, HomeActivity.class);
                    startActivity(intenta);
                } else if (tabId.equals("User_Tab2")) {
                    Intent intents = new Intent(PetActivity.this, CamActivity.class);
                    startActivity(intents);
                } else if (tabId.equals("User_Tab3")) {
                    Intent intentd = new Intent(PetActivity.this, GpsActivity.class);
                    startActivity(intentd);
                } else if (tabId.equals("User_Tab4")) {
                    Intent intentf = new Intent(PetActivity.this, UserActivity.class);
                    startActivity(intentf);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.Pet_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new PetActivity.MyOnNavigationItemSelectedListener());

        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(Config.user_img));

    }

    public class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(PetActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(PetActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(PetActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(PetActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent feedIntent = new Intent(PetActivity.this, SettingActivity.class);
                    startActivity(feedIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(PetActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(PetActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                if (LoginActivity.call_email.equals(c.getString(user_email))) {
                    String id = c.getString(pet_id);
                    String email = c.getString(user_email);
                    String name = c.getString(pet_name);
                    String sex = c.getString(pet_sex);
                    String birth = c.getString(pet_birth);
                    String species = c.getString(pet_species);
                    String breed = c.getString(pet_breed);
                    String number = c.getString(pet_number);
                    String image = c.getString(pet_image);
                    String api = c.getString("api");
                    String channel = c.getString("channel");
                    HashMap<String, String> persons = new HashMap<String, String>();

                    persons.put(pet_id, id);
                    persons.put(user_email, email);
                    persons.put(pet_name, name);
                    persons.put(pet_birth, birth);
                    persons.put(pet_sex, sex);
                    persons.put(pet_species, species);
                    persons.put(pet_breed, breed);
                    persons.put(pet_number, number);
                    persons.put(pet_image, image);
                    persons.put("api", api);
                    persons.put("channel", channel);
                    personList.add(persons);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    PetActivity.this, personList, R.layout.list_item,
                    new String[]{pet_image, pet_name, pet_birth, pet_sex, pet_breed},
                    new int[]{R.id.pet_img, R.id.name, R.id.birth, R.id.sex, R.id.breed}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            Intent intent = new Intent(PetActivity.this, PetHealthInfoActivity.class);
            intent.putExtra("pet_id", personList.get(position).get(pet_id));
            intent.putExtra("pet_name", personList.get(position).get(pet_name));
            intent.putExtra("pet_birth", personList.get(position).get(pet_birth));
            intent.putExtra("pet_breed", personList.get(position).get(pet_breed));
            intent.putExtra("pet_sex", personList.get(position).get(pet_sex));
            intent.putExtra("pet_species", personList.get(position).get(pet_species));
            intent.putExtra("pet_number", personList.get(position).get(pet_number));
            intent.putExtra("pet_image", personList.get(position).get(pet_image));
            intent.putExtra("api", personList.get(position).get("api"));
            intent.putExtra("channel", personList.get(position).get("channel"));

            startActivity(intent);
        }
    };


    // php의 email을 가져오는 함수
    public void getEmail(int position) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu_pet_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.petAdd:
                Intent intent = new Intent(PetActivity.this, PetEditActivity.class);
                intent.putExtra("isEdit", isEdit);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

    /* 참조 : http://blog.naver.com/elder815/220875549924 */
  /* 코드 분석 : http://kin.naver.com/qna/detail.nhn?d1id=1&dirId=1040104&docId=263017102&qb=VVJMRW5jb2Rlci5lbmNvZGU=&enc=utf8&section=kin&rank=3&search_sort=0&spq=0 */
