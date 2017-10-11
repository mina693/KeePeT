package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PetSitterInfoActivity extends AppCompatActivity {

    String DELETE_PET_Sitter = Config.URL + "pet_sitter_delete.php";
    DrawerLayout mDrawerLayout;

    private Boolean isEdit = false;
    private String id, user_email;
    private String img_str, name_str, age_str, sex_str, adr_str, career_year_str, career_month_str, pet_str, price_str, word_str, phone_1_str, phone_2_str, phone_3_str;
    private TextView sitter_name, sitter_age, sitter_sex, sitter_adr, sitter_career, sitter_pet, sitter_price, sitter_word;
    private Button btn_reserve;
    private ImageView img_pet_sitter;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_sitter_info);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);

        img_pet_sitter = (ImageView) findViewById(R.id.img_pet_sitter);
        sitter_name = (TextView) findViewById(R.id.sitter_name);
        sitter_age = (TextView) findViewById(R.id.sitter_age);
        sitter_sex = (TextView) findViewById(R.id.sitter_sex);
        sitter_adr = (TextView) findViewById(R.id.sitter_adr);
        sitter_career = (TextView) findViewById(R.id.sitter_career);
        sitter_pet = (TextView) findViewById(R.id.sitter_pet);
        sitter_price = (TextView) findViewById(R.id.sitter_price);
        sitter_word = (TextView) findViewById(R.id.sitter_word);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);

        Intent showIntent = getIntent();
        id = showIntent.getStringExtra("id");
        img_str = showIntent.getStringExtra("sitter_image");
        user_email = showIntent.getStringExtra("user_email");
        name_str = showIntent.getStringExtra("sitter_name");
        age_str = showIntent.getStringExtra("sitter_age");
        sex_str = showIntent.getStringExtra("sitter_sex");
        adr_str = showIntent.getStringExtra("sitter_adr");
        career_year_str = showIntent.getStringExtra("sitter_career_year");
        career_month_str = showIntent.getStringExtra("sitter_career_month");
        pet_str = showIntent.getStringExtra("sitter_pet");
        price_str = String.valueOf(showIntent.getExtras().getInt("sitter_price"));
        word_str = showIntent.getStringExtra("sitter_word");
        phone_1_str = showIntent.getStringExtra("sitter_phone_1");
        phone_2_str = showIntent.getStringExtra("sitter_phone_2");
        phone_3_str = showIntent.getStringExtra("sitter_phone_3");
        img_pet_sitter.setImageURI(Uri.parse(img_str));
        sitter_name.setText(name_str);
        sitter_age.setText(age_str);
        sitter_sex.setText(sex_str);
        sitter_adr.setText(adr_str);
        sitter_career.setText(career_year_str + "년" + career_month_str + "개월");
        sitter_pet.setText(pet_str);
        sitter_price.setText(price_str + "/ 시간");
        sitter_word.setText(word_str);

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reserveIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone_1_str + phone_2_str + phone_3_str));
                startActivity(reserveIntent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new PetSitterInfoActivity.MyOnNavigationItemSelectedListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menuEdit:
                isEdit = true;
                Intent editIntent = new Intent(PetSitterInfoActivity.this, PetSitterEditActivity.class);
                editIntent.putExtra("isEdit", isEdit);
                editIntent.putExtra("id", id);
                editIntent.putExtra("user_email", user_email);
                editIntent.putExtra("sitter_image", img_str);
                editIntent.putExtra("sitter_name", name_str);
                editIntent.putExtra("sitter_age", age_str);
                editIntent.putExtra("sitter_sex", sex_str);
                editIntent.putExtra("sitter_phone_1", phone_1_str);
                editIntent.putExtra("sitter_phone_2", phone_2_str);
                editIntent.putExtra("sitter_phone_3", phone_3_str);
                editIntent.putExtra("sitter_adr", adr_str);
                editIntent.putExtra("sitter_career_year", career_year_str);
                editIntent.putExtra("sitter_career_month", career_month_str);
                editIntent.putExtra("sitter_price", price_str);
                editIntent.putExtra("sitter_pet", pet_str);
                editIntent.putExtra("sitter_word", word_str);
                editIntent.putExtra("sitter_price", price_str);
                startActivity(editIntent);
                break;
            case R.id.menuDelete: {
                new MaterialDialog.Builder(PetSitterInfoActivity.this)
                        .title("게시물삭제")
                        .content("이 게시물을 삭제하시겠어요?")
                        .positiveText("삭제")
                        .negativeText("삭제 안 함")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteToDatabase(id);
                                Intent deleteIntent = new Intent(PetSitterInfoActivity.this, PetSitterActivity.class);
                                startActivity(deleteIntent);
                            }
                        }).theme(Theme.LIGHT)
                        .show();
                break;
            }
            case R.id.menuLike:
                break;
            case R.id.menuDeclare: {
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteToDatabase(String id) {
        class DeleteData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetSitterInfoActivity.this, "Please Wait", null, true, true);
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

                    String link = DELETE_PET_Sitter;

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

    class MyOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        MyOnNavigationItemSelectedListener() {
        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(PetSitterInfoActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(PetSitterInfoActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(PetSitterInfoActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(PetSitterInfoActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent setIntent = new Intent(PetSitterInfoActivity.this, SettingActivity.class);
                    startActivity(setIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(PetSitterInfoActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(PetSitterInfoActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        if (LoginActivity.call_email.equals(user_email)) {
            getMenuInflater().inflate(R.menu.menu_user, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.menu_notuser, menu);
            return true;
        }
    }


 /*   @Override
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
            Intent goIntent = new Intent(PetSitterInfoActivity.this, HomeActivity.class);
            startActivity(goIntent);
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }*/
}
