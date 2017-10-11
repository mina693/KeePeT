package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;

public class SavedLocationlistActivity extends AppCompatActivity {

    String myJSON;
    String DATA_URL = Config.URL + "getLocation.php";
    String DELETE_URL = Config.URL + "location_delete.php";
    private static final String TAG_RESULTS = "result";

    private static final String location_id = "id";
    private static final String location_latitude = "latitude";
    private static final String location_longitude = "longitude";
    private static final String path_namestr = "path_name";
    private static final String datestr = "date";
    private static final String user_email = "user_email";
    JSONObject lat_json;
    JSONObject long_json;
    ArrayList<Double> array_lat;
    String key_id;

    ArrayList<Double> array_long;
    ArrayList<String> test_str;
    AlertDialog.Builder ad;


    TextView text;

    JSONArray Locations = null;
    JSONArray saved_latLocations = null;
    JSONArray saved_longLocations = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locationlist);
        list = (ListView) findViewById(R.id.listView);

        test_str = new ArrayList<String>();

        personList = new ArrayList<HashMap<String, String>>();
        getData(DATA_URL);

        long_json = new JSONObject();


        list.setOnItemClickListener(listener);
        ad = new AlertDialog.Builder(SavedLocationlistActivity.this);
        // 내용 설정
        ad.setPositiveButton("경로보기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                changeActivity();
                dialog.dismiss();     //닫기
                //
            }
        });


        ad.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Deletedatabase(key_id);
                dialog.dismiss();     //닫기
                // Event
                SavedLocationlistActivity.this.recreate();
            }
        });


    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            Locations = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < Locations.length(); i++) {
                JSONObject c = Locations.getJSONObject(i);
                if (LoginActivity.call_email.equals(c.getString(user_email))) {
                    String id = c.getString(location_id);
                    String latitude = c.getString(location_latitude);
                    String longitude = c.getString(location_longitude);
                    String path_name = c.getString(path_namestr);
                    String date = c.getString(datestr);
                    String email = c.getString(user_email);


                    HashMap<String, String> locations = new HashMap<String, String>();

                    locations.put(location_id, id);
                    locations.put(location_latitude, latitude);
                    locations.put(location_longitude, longitude);
                    locations.put(path_namestr, path_name);
                    locations.put(datestr, date);

                    personList.add(locations);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    SavedLocationlistActivity.this, personList, R.layout.location_list,
                    new String[]{path_namestr, datestr},
                    new int[]{R.id.pathtxt, R.id.datetxt}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            text = (TextView) findViewById(R.id.email);
            array_lat = new ArrayList<Double>();
            array_long = new ArrayList<Double>();
            String jsonstr_lat = personList.get(position).get(location_latitude);
            String jsonstr_long = personList.get(position).get(location_longitude);
            JSONObject jsonObject_lat = new JSONObject();
            JSONObject jsonObject_long = new JSONObject();
            key_id = personList.get(position).get(location_id);
            try {

                saved_latLocations = new JSONArray(jsonstr_lat);
                saved_longLocations = new JSONArray(jsonstr_long);
                int j = saved_latLocations.length();

                String size = String.valueOf(j);
                ;

                for (int i = 0; i < saved_latLocations.length(); i++) {
                    jsonObject_lat = saved_latLocations.getJSONObject(i);
                    jsonObject_long = saved_longLocations.getJSONObject(i);

                    double dob_lat = Double.parseDouble(jsonObject_lat.get("lat").toString());
                    double dob_long = Double.parseDouble(jsonObject_long.get("long").toString());
                    array_lat.add(dob_lat);
                    array_long.add(dob_long);
                }
                Toast.makeText(SavedLocationlistActivity.this, array_long.toString(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }



           /* Intent intent = new Intent(SavedLocationlist.this, MainActivity.class);
            intent.putExtra("array_lat",array_lat);
            intent.putExtra("array_long",array_long);

            startActivity(intent);*/

            ad.show();
        }
    };

    public void changeActivity() {
        Intent intent = new Intent(SavedLocationlistActivity.this, ShowSavedLocationActivity.class);
        intent.putExtra("array_lat", array_lat);
        intent.putExtra("array_long", array_long);
        startActivity(intent);
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

    private void Deletedatabase(String key_id) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SavedLocationlistActivity.this, "Please Wait", null, true, true);
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
                    String key_id = (String) params[0];

                    String data = URLEncoder.encode("key_id", "UTF-8") + "=" + URLEncoder.encode(key_id, "UTF-8");

                    Log.i("delete", key_id);

                    URL url = new URL(DELETE_URL);
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
        task.execute(key_id);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SavedLocationlistActivity.this, GpsActivity.class);
        startActivity(intent);

    }
}