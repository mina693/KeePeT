package com.example.b10715.final_pj;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CamActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;

    WebView webView;
    ImageButton stop_play;
    private Boolean isOpen = false;
    private static final int SCROLL_BY_PX = 100;

    static String horizontal = "100";
    static String Vertical = "105";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        stop_play = (ImageButton) findViewById(R.id.stop_play);
        webView = (WebView) findViewById(R.id.webview);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Cam_Tab1");
        Drawable home = ContextCompat.getDrawable(this, R.drawable.ic_action_home);
        spec1.setIndicator("", home);
        spec1.setContent(R.id.tab1);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Cam_Tab2");
        Drawable cam = ContextCompat.getDrawable(this, R.drawable.ic_action_change_cam);
        spec2.setIndicator("", cam);
        spec2.setContent(R.id.tab2);
        tabHost.addTab(spec2);

        TabHost.TabSpec spec3 = tabHost.newTabSpec("Cam_Tab3");
        Drawable gps = ContextCompat.getDrawable(this, R.drawable.ic_action_gps);
        spec3.setIndicator("", gps);
        spec3.setContent(R.id.tab3);
        tabHost.addTab(spec3);

        TabHost.TabSpec spec4 = tabHost.newTabSpec("Cam_Tab4");
        Drawable pet = ContextCompat.getDrawable(this, R.drawable.ic_petlist);
        spec4.setIndicator("", pet);
        spec4.setContent(R.id.tab4);
        tabHost.addTab(spec4);

        tabHost.setCurrentTab(1);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("Cam_Tab1")) {
                    Intent intenta = new Intent(CamActivity.this, HomeActivity.class);
                    startActivity(intenta);
                } else if (tabId.equals("Cam_Tab2")) {
                    Intent intents = new Intent(CamActivity.this, CamActivity.class);
                    startActivity(intents);
                } else if (tabId.equals("Cam_Tab3")) {
                    Intent intentd = new Intent(CamActivity.this, GpsActivity.class);
                    startActivity(intentd);
                } else if (tabId.equals("Cam_Tab4")) {
                    Intent intentf = new Intent(CamActivity.this, PetActivity.class);
                    startActivity(intentf);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.cam_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener());

        webView.setPadding(0, 0, 0, 0);
        //webView.setInitialScale(100);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webView.setInitialScale(40);
        String url = "http://192.168.0.12:9090/stream";
        webView.loadUrl(url);

    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.scroll_up:
                new SendMessage().execute(Vertical);
                Log.i("dd","camtestxxxxxxxxxxx");
                if (Vertical == "5") {
                    Vertical = "15";
                } else if (Vertical == "15") {
                    Vertical = "25";
                } else if (Vertical == "25") {
                    Vertical = "35";
                } else if (Vertical == "35") {
                    Vertical = "45";
                } else if (Vertical == "45") {
                    Vertical = "55";
                } else if (Vertical == "55") {
                    Vertical = "65";
                } else if (Vertical == "65") {
                    Vertical = "75";
                } else if (Vertical == "75") {
                    Vertical = "85";
                } else if (Vertical == "85") {
                    Vertical = "95";
                } else if (Vertical == "95") {
                    Vertical = "105";
                } else if (Vertical == "105") {
                    Vertical = "115";
                } else if (Vertical == "115") {
                    Vertical = "125";
                } else if (Vertical == "125") {
                    Vertical = "135";
                } else if (Vertical == "135") {
                    Vertical = "145";
                } else if (Vertical == "145") {
                    Vertical = "155";
                } else if (Vertical == "155") {
                    Vertical = "165";
                } else if (Vertical == "165") {
                    Vertical = "175";
                }
                break;
            case R.id.scroll_down:
                new SendMessage().execute(Vertical);
                Log.i("22","camtestxxxxxxxxxxx");
                if (Vertical == "175") {
                    Vertical = "165";
                } else if (Vertical == "165") {
                    Vertical = "155";
                } else if (Vertical == "155") {
                    Vertical = "145";
                } else if (Vertical == "145") {
                    Vertical = "135";
                } else if (Vertical == "135") {
                    Vertical = "125";
                } else if (Vertical == "125") {
                    Vertical = "115";
                } else if (Vertical == "115") {
                    Vertical = "105";
                } else if (Vertical == "105") {
                    Vertical = "95";
                } else if (Vertical == "95") {
                    Vertical = "85";
                } else if (Vertical == "85") {
                    Vertical = "75";
                } else if (Vertical == "75") {
                    Vertical = "65";
                } else if (Vertical == "65") {
                    Vertical = "55";
                } else if (Vertical == "55") {
                    Vertical = "45";
                } else if (Vertical == "45") {
                    Vertical = "35";
                } else if (Vertical == "35") {
                    Vertical = "25";
                } else if (Vertical == "25") {
                    Vertical = "15";
                } else if (Vertical == "15") {
                    Vertical = "5";
                }
                break;
            case R.id.scroll_left:
                new SendMessage().execute(horizontal);
                Log.i("33","camtestxxxxxxxxxxx");
                if (horizontal == "180") {
                    horizontal = "170";
                } else if (horizontal == "170") {
                    horizontal = "160";
                } else if (horizontal == "160") {
                    horizontal = "150";
                } else if (horizontal == "150") {
                    horizontal = "140";
                } else if (horizontal == "140") {
                    horizontal = "130";
                } else if (horizontal == "130") {
                    horizontal = "120";
                } else if (horizontal == "120") {
                    horizontal = "110";
                } else if (horizontal == "110") {
                    horizontal = "100";
                } else if (horizontal == "100") {
                    horizontal = "90";
                } else if (horizontal == "90") {
                    horizontal = "80";
                } else if (horizontal == "80") {
                    horizontal = "70";
                } else if (horizontal == "70") {
                    horizontal = "60";
                } else if (horizontal == "60") {
                    horizontal = "50";
                } else if (horizontal == "50") {
                    horizontal = "40";
                } else if (horizontal == "40") {
                    horizontal = "30";
                } else if (horizontal == "30") {
                    horizontal = "20";
                } else if (horizontal == "20") {
                    horizontal = "10";
                } else if (horizontal == "10") {
                    horizontal = "0";
                }
                break;
            case R.id.scroll_right:
                new SendMessage().execute(horizontal);
                Log.i("44","camtestxxxxxxxxxxx");
                if (horizontal == "0") {
                    horizontal = "10";
                } else if (horizontal == "10") {
                    horizontal = "20";
                } else if (horizontal == "20") {
                    horizontal = "30";
                } else if (horizontal == "30") {
                    horizontal = "40";
                } else if (horizontal == "40") {
                    horizontal = "50";
                } else if (horizontal == "50") {
                    horizontal = "60";
                } else if (horizontal == "60") {
                    horizontal = "70";
                } else if (horizontal == "70") {
                    horizontal = "80";
                } else if (horizontal == "80") {
                    horizontal = "90";
                } else if (horizontal == "90") {
                    horizontal = "100";
                } else if (horizontal == "100") {
                    horizontal = "110";
                } else if (horizontal == "110") {
                    horizontal = "120";
                } else if (horizontal == "120") {
                    horizontal = "130";
                } else if (horizontal == "130") {
                    horizontal = "140";
                } else if (horizontal == "140") {
                    horizontal = "150";
                } else if (horizontal == "150") {
                    horizontal = "160";
                } else if (horizontal == "160") {
                    horizontal = "170";
                } else if (horizontal == "170") {
                    horizontal = "180";
                }
                break;
            case R.id.camera:
                break;
            case R.id.btn_vibe:
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet();
                    get.setURI(new URI("http://192.168.0.36/LED=ON"));
                    HttpResponse resp = client.execute(get);
                    BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
                    String str = null;
                    StringBuilder sb = new StringBuilder();

                    Toast.makeText(CamActivity.this, "ㅇㅇㅇㅇㅇㅇㅇ", Toast.LENGTH_SHORT).show();

                    while ((str = br.readLine()) != null) {
                        sb.append(str).append("\n");
                    }
                    br.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_play:
                animateButton();
                //    stop_play.setVisibility(View.VISIBLE);
                break;
            case R.id.stop_play:
                break;
        }
    }

    public void animateButton() {
        if (isOpen) {
            stop_play.setVisibility(View.GONE);
            stop_play.setClickable(false);
            isOpen = false;
        } else {
            stop_play.setVisibility(View.VISIBLE);
            stop_play.setClickable(true);
            isOpen = true;
        }
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
                    Intent homeIntent = new Intent(CamActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(CamActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(CamActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(CamActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent setIntent = new Intent(CamActivity.this, SettingActivity.class);
                    startActivity(setIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(CamActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(CamActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }

    public void mOnCaptureClick(View v) {
        //전체화면
        //View rootView = getWindow().getDecorView();

        File screenShot = ScreenShot(webView);
        if (screenShot != null) {
            //갤러리에 추가
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
        }
    }

    public File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }


    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
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
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(CamActivity.this, LoginActivity.class);

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
        }

        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

}