package com.example.b10715.final_pj;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;

public class SettingActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    Button pass_changebtn;
    AlertDialog.Builder ad;
    ImageButton home, gps, cam, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);


        pass_changebtn = (Button) findViewById(R.id.change_password);
        home = (ImageButton) findViewById(R.id.home);
        gps = (ImageButton) findViewById(R.id.gps);
        cam = (ImageButton) findViewById(R.id.cam);
        user = (ImageButton) findViewById(R.id.user);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new SettingActivity.MyOnNavigationItemSelectedListener());

        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(com.example.b10715.final_pj.Config.user_img));

        ad = new AlertDialog.Builder(SettingActivity.this);
        pass_changebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ad.setTitle("현재비밀번호를 입력해 주세요!");       // 제목 설정
                final EditText et = new EditText(SettingActivity.this);
                ad.setView(et);
                // 내용 설정
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        if (Config.con_password.toString().equals(et.getText().toString())) {
                            Intent intent = new Intent(SettingActivity.this, Change_password.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SettingActivity.this, "현재 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });


                ad.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                ad.show();
            }
        });

    }


    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.profile:
                Intent profileIntent = new Intent(SettingActivity.this, UserActivity.class);
                startActivity(profileIntent);
                break;

            case R.id.edit_pass:
                Intent passIntent = new Intent(SettingActivity.this, Change_password.class);
                startActivity(passIntent);
                break;

            case R.id.logout:
                new MaterialDialog.Builder(SettingActivity.this)
                        .title("로그아웃")
                        .content("로그아웃 하시겠어요?")
                        .positiveText("네")
                        .negativeText("아니요")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                logout();
                                Intent logoutIntent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(logoutIntent);
                            }
                        }).theme(Theme.LIGHT)
                        .show();
                break;

            case R.id.service:
                Intent serviceIntent = new Intent(SettingActivity.this, QnaActivity.class);
                startActivity(serviceIntent);
                break;

            case R.id.inform:
                Intent informIntent = new Intent(SettingActivity.this, DeveloperInformActivity.class);
                startActivity(informIntent);
                break;

            case R.id.user:
                Intent userIntent = new Intent(SettingActivity.this, UserActivity.class);
                startActivity(userIntent);
                break;

            case R.id.cam:
                Intent camIntent = new Intent(SettingActivity.this, CamActivity.class);
                startActivity(camIntent);
                break;

            case R.id.gps:
                Intent gpsIntent = new Intent(SettingActivity.this, GpsActivity.class);
                startActivity(gpsIntent);
                break;

            case R.id.home:
                Intent homeIntent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
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
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
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

    public class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(SettingActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(SettingActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(SettingActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(SettingActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent feedIntent = new Intent(SettingActivity.this, SettingActivity.class);
                    startActivity(feedIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(SettingActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(SettingActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }
}