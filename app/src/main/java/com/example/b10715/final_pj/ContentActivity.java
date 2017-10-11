package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;

public class ContentActivity extends AppCompatActivity {

    String DELETE_PET_STAGRAM = Config.URL + "pet_stagram_delete.php";
    DrawerLayout mDrawerLayout;

    private String id, imageURI;
    private Boolean isEdit = false;
    private TextView user_email, content;
    private ImageView user_image, imageview;
    private ImageButton optionbtn;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);

        user_image = (ImageView) this.findViewById(R.id.user_image);
        user_email = (TextView) this.findViewById(R.id.user_email);
        imageview = (ImageView) this.findViewById(R.id.image);
        content = (TextView) findViewById(R.id.text);
        optionbtn = (ImageButton) findViewById(R.id.optionbtn);

        Intent showIntent = getIntent();

        id = showIntent.getStringExtra("id");
        user_image.setImageURI(Uri.parse(showIntent.getStringExtra("user_image")));
        user_email.setText(showIntent.getStringExtra("user_email"));
        Glide.with(this).load(showIntent.getStringExtra("image")).into(imageview);
        imageURI = showIntent.getStringExtra("image");
        content.setText(showIntent.getStringExtra("content"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new ContentActivity.MyOnNavigationItemSelectedListener());

        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(com.example.b10715.final_pj.Config.user_img));

    }


    private void deleteToDatabase(String id) {
        class DeleteData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ContentActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("실패", "onPostExecute");

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                Log.i("deleteToDatabase", "doInBackground");
                try {
                    String id = (String) params[0];

                    String link = DELETE_PET_STAGRAM;

                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                    URL url = new URL(link);
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

        DeleteData task = new DeleteData();
        task.execute(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        if (LoginActivity.call_email.equals(user_email.getText().toString())) {
            getMenuInflater().inflate(R.menu.menu_user, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_notuser, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int getid = item.getItemId();

        switch (getid) {
            case R.id.menuEdit:

                isEdit = true;
                Intent EditIntent = new Intent(ContentActivity.this, ContentEditActivity.class);
                EditIntent.putExtra("isEdit", isEdit);
                EditIntent.putExtra("id", id);
                EditIntent.putExtra("user_image", user_image.toString());
                EditIntent.putExtra("user_email", user_email.toString());
                EditIntent.putExtra("image", imageURI);
                EditIntent.putExtra("content", content.getText().toString());
                startActivity(EditIntent);
                return true;

            case R.id.menuDelete:
                new MaterialDialog.Builder(ContentActivity.this)
                        .title("게시물삭제")
                        .content("이 게시물을 삭제하시겠어요?")
                        .positiveText("삭제")
                        .negativeText("삭제 안 함")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                deleteToDatabase(id);
                                Intent deleteIntent = new Intent(ContentActivity.this, HomeActivity.class);
                                startActivity(deleteIntent);

                            }
                        }).theme(Theme.LIGHT)
                        .show();
                return true;
            case R.id.menuLike:
                Toast.makeText(ContentActivity.this, "개발중입니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuDeclare:
                Toast.makeText(ContentActivity.this, "개발중입니다.", Toast.LENGTH_SHORT).show();
                return true;

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
                    Intent homeIntent = new Intent(ContentActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(ContentActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(ContentActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(ContentActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent setIntent = new Intent(ContentActivity.this, SettingActivity.class);
                    startActivity(setIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(ContentActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(ContentActivity.this, PetSitterEditActivity.class);
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
            Intent goIntent = new Intent(ContentActivity.this, HomeActivity.class);
            startActivity(goIntent);
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
}
