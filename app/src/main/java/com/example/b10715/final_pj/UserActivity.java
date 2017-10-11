package com.example.b10715.final_pj;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;


public class UserActivity extends AppCompatActivity {
    String DATA_URL = Config.URL + "getdata_pet.php";
    String myJSON;

    private long lastTimeBackPressed;

    private static final int MULTIPLE_PERMISSIONS = 101;
    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}; //권한 설정 변수


    DrawerLayout mDrawerLayout;
    Boolean isEdit = false;
    private ImageView iv_capture;
    JSONArray pets = null;
    ArrayList<HashMap<String, String>> petList;

    ListView list;
    ImageButton homeact, gpsact, petact, camact;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        checkPermissions(); // 권한불가
        homeact = (ImageButton) findViewById(R.id.home);
        gpsact = (ImageButton) findViewById(R.id.gps);
        petact = (ImageButton) findViewById(R.id.pet);
        camact = (ImageButton) findViewById(R.id.cam);

        iv_capture = (ImageView) this.findViewById(R.id.iv_capture);
        if (Config.user_img != null) {
            iv_capture.setImageURI(Uri.parse(Config.user_img));
        }
        Button btn_edit = (Button) this.findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                Intent editIntent = new Intent(UserActivity.this, UserRegisterActivity.class);
                editIntent.putExtra("isEdit", isEdit);
                editIntent.putExtra("user_image", Config.user_img);
                editIntent.putExtra("user_name", Config.user_name);
                editIntent.putExtra("user_email", LoginActivity.call_email);
                startActivity(editIntent);
            }
        });

        homeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                Intent editIntent = new Intent(UserActivity.this, HomeActivity.class);
                startActivity(editIntent);
            }
        });
        gpsact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                Intent editIntent = new Intent(UserActivity.this, GpsActivity.class);
                startActivity(editIntent);
            }
        });
        petact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                Intent editIntent = new Intent(UserActivity.this, PetActivity.class);
                startActivity(editIntent);
            }
        });
        camact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                Intent editIntent = new Intent(UserActivity.this, CamActivity.class);
                startActivity(editIntent);
            }
        });
        list = (ListView) findViewById(R.id.listView);
        petList = new ArrayList<HashMap<String, String>>();
        getData(DATA_URL);

        list.setOnItemClickListener(listener);

        TextView user_email = (TextView) findViewById(R.id.txt_email);
        TextView user_name = (TextView) findViewById(R.id.txt_name);

        user_email.setText(LoginActivity.call_email);
        //  edit_name.setText();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.User_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new UserActivity.MyOnNavigationItemSelectedListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(Config.user_img));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(UserActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_cam:
                        Intent camIntent = new Intent(UserActivity.this, CamActivity.class);
                        startActivity(camIntent);
                        break;
                    case R.id.nav_gps:
                        Intent gpsIntent = new Intent(UserActivity.this, GpsActivity.class);
                        startActivity(gpsIntent);
                        break;
                    case R.id.nav_user:
                        Intent userIntent = new Intent(UserActivity.this, PetActivity.class);
                        startActivity(userIntent);
                        break;
                    case R.id.nav_setting:
                        Intent setIntent = new Intent(UserActivity.this, SettingActivity.class);
                        startActivity(setIntent);
                        break;
                    case R.id.nav_petsitter:
                        Intent petsitterIntent = new Intent(UserActivity.this, PetSitterActivity.class);
                        startActivity(petsitterIntent);
                        break;
                    case R.id.nav_like_petsitter:
                        Intent likeIntent = new Intent(UserActivity.this, PetSitterEditActivity.class);
                        startActivity(likeIntent);
                        break;
                }
                return true;
            }

        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            pets = jsonObj.getJSONArray("result");

            for (int i = 0; i < pets.length(); i++) {
                JSONObject c = pets.getJSONObject(i);
                if (LoginActivity.call_email.equals(c.getString("user_email"))) {
                    String id = c.getString("id");
                    String email = c.getString("user_email");
                    String name = c.getString("pet_name");
                    String sex = c.getString("pet_sex");
                    String birth = c.getString("pet_birth");
                    String species = c.getString("pet_species");
                    String breed = c.getString("pet_breed");
                    String weight = c.getString("pet_weight");
                    String number = c.getString("pet_number");
                    String image = c.getString("pet_image");
                    HashMap<String, String> pets = new HashMap<String, String>();

                    pets.put("id", id);
                    pets.put("user_email", email);
                    pets.put("pet_name", name);
                    pets.put("pet_birth", birth);
                    pets.put("pet_sex", sex);
                    pets.put("pet_species", species);
                    pets.put("pet_breed", breed);
                    pets.put("pet_weight", weight);
                    pets.put("pet_number", number);
                    pets.put("pet_image", image);
                    petList.add(pets);

                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    UserActivity.this, petList, R.layout.list_pet,
                    new String[]{"pet_image", "pet_name", "pet_birth", "pet_sex", "pet_breed"},
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

            Intent intent = new Intent(UserActivity.this, PetEditActivity.class);
            intent.putExtra("pet_id", petList.get(position).get("pet_id"));
            intent.putExtra("pet_name", petList.get(position).get("pet_name"));
            intent.putExtra("pet_birth", petList.get(position).get("pet_birth"));
            intent.putExtra("pet_breed", petList.get(position).get("pet_breed"));
            intent.putExtra("pet_sex", petList.get(position).get("pet_sex"));
            intent.putExtra("pet_species", petList.get(position).get("pet_species"));
            intent.putExtra("pet_weight", petList.get(position).get("pet_weight"));
            intent.putExtra("pet_number", petList.get(position).get("pet_number"));
            intent.putExtra("pet_image", petList.get(position).get("pet_image"));

            startActivity(intent);
        }
    };


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

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(
                    new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    //권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다." +
                " 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
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
                    Intent homeIntent = new Intent(UserActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(UserActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(UserActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent feedIntent = new Intent(UserActivity.this, SettingActivity.class);
                    startActivity(feedIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(UserActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(UserActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }

    private void logout() {
        //Creating an alert dialog to confirm logout
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
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
                        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
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
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}

        /*  참조 http://blog.naver.com/putzzang/220918218278  */