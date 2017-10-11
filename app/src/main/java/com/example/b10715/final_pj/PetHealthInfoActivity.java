package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.nearby.messages.Message;


import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.sql.Types.NULL;


public class PetHealthInfoActivity extends AppCompatActivity {

    TextView pet_name, pet_birth, pet_breed, pet_sex, pet_number, number;
    ImageButton btn_weight;
    private ImageView pet_img;
    String pet_id, pet_image, pet_number_str,pet_species, pet_weight, api, channel;
    WebView chart1, chart2;
    Boolean isEdit = false;
    AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_health_info);

        pet_img = (ImageView) this.findViewById(R.id.pet_img);
        pet_name = (TextView) findViewById(R.id.name);
        pet_breed = (TextView) findViewById(R.id.breed);
        pet_sex = (TextView) findViewById(R.id.sex);
        pet_birth = (TextView) findViewById(R.id.birth);
        number = (TextView) findViewById(R.id.number);
        pet_number = (TextView) findViewById(R.id.pet_number);
        chart1 = (WebView) findViewById(R.id.chart1);
        chart2 = (WebView) findViewById(R.id.chart2);
        btn_weight = (ImageButton) findViewById(R.id.btn_weight);

        Intent intent = getIntent();
        pet_id = intent.getExtras().getString("pet_id");
        pet_image = intent.getExtras().getString("pet_image");
        pet_name.setText(intent.getExtras().getString("pet_name"));
        pet_birth.setText(intent.getExtras().getString("pet_birth"));
        pet_species = intent.getExtras().getString("pet_species");
        pet_breed.setText(intent.getExtras().getString("pet_breed"));
        pet_sex.setText(intent.getExtras().getString("pet_sex"));
        pet_number_str=intent.getExtras().getString("pet_number");
        Log.i(pet_number_str,intent.getExtras().getString("pet_number"));
        if(pet_number_str.contains("NULL")){
            pet_number.setText(" ");
        }else{
            pet_number.setText(pet_number_str);
        }
        Glide.with(PetHealthInfoActivity.this).load(pet_image).into(pet_img);
        api = intent.getStringExtra("api");
        channel = intent.getStringExtra("channel");

        if (api.equals("") || channel.equals("")) {
            chart1.setVisibility(View.INVISIBLE);
            chart2.setVisibility(View.INVISIBLE);
        } else {
            chart1.setWebViewClient(new WebViewClient());
            chart1.setInitialScale(245);
            WebSettings set = chart1.getSettings();
            set.setJavaScriptEnabled(true);
            set.setBuiltInZoomControls(true);
            set.setLoadWithOverviewMode(true);
            chart1.loadUrl("https://thingspeak.com/channels/" + channel + "/charts/1");

            chart2.setWebViewClient(new WebViewClient());
            chart2.setInitialScale(245);
            WebSettings set2 = chart2.getSettings();
            set2.setJavaScriptEnabled(true);
            set2.setBuiltInZoomControls(true);
            set2.setLoadWithOverviewMode(true);
            chart2.loadUrl("https://thingspeak.com/channels/" + channel + "/charts/2");
            StrictMode.enableDefaults();
        }

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (api.equals("") || channel.equals("")) {
                    Toast.makeText(PetHealthInfoActivity.this, "펫 등록번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    ad = new AlertDialog.Builder(PetHealthInfoActivity.this);
                    ad.setTitle("현재 몸무게를 입력해주세요");       // 제목 설정
                    final EditText et = new EditText(PetHealthInfoActivity.this);
                    ad.setView(et);
                    // 내용 설정
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertToThingSpeak(api, et.getText().toString());
                            chart2.reload();
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
            }
        });

    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.editbtn:
                isEdit = true;
                Intent intent = new Intent(PetHealthInfoActivity.this, PetEditActivity.class);
                intent.putExtra("isEdit", isEdit);
                intent.putExtra("pet_id", pet_id);
                intent.putExtra("pet_name", pet_name.getText().toString());
                intent.putExtra("pet_breed", pet_breed.getText().toString());
                intent.putExtra("pet_sex", pet_sex.getText().toString());
                intent.putExtra("pet_birth", pet_birth.getText().toString());
                intent.putExtra("pet_species", pet_species);
                intent.putExtra("pet_weight", pet_weight);
                intent.putExtra("pet_number", pet_number.getText().toString());
                intent.putExtra("pet_image", pet_image);
                intent.putExtra("api", api);
                intent.putExtra("channel", channel);

                startActivity(intent);
                break;

            case R.id.btn_info:
                if (pet_number.getText().toString().equals("")) {
                    Toast.makeText(PetHealthInfoActivity.this, "펫 등록번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String pet_info = "http://www.animal.go.kr/mobile2/html/03_inquiry_result.jsp?search_dog_reg_no=" + pet_number.getText().toString();
                    Intent showIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pet_info));
                    startActivity(showIntent);
                }
        }
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void insertToThingSpeak(String api, String weight_str) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            get.setURI(new URI("https://api.thingspeak.com/update?api_key=" + api + "&field2=" + weight_str));
            HttpResponse resp = client.execute(get);
            BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String str = null;
            StringBuilder sb = new StringBuilder();

            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

