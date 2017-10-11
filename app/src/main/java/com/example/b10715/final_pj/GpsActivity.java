package com.example.b10715.final_pj;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.lang.String;

import static com.example.b10715.final_pj.Config.EMAIL_SHARED_PREF;
import static com.example.b10715.final_pj.Config.LOGGEDIN_SHARED_PREF;
import static com.example.b10715.final_pj.Config.SHARED_PREF_NAME;
import static com.example.b10715.final_pj.R.id.btn_mylocation;
import static com.example.b10715.final_pj.R.id.map;

public class GpsActivity extends AppCompatActivity implements OnMapReadyCallback {
    final private String TAG = "LocationServicesTest";
    final private int MY_PERMISSION_REQUEST_LOCATION = 100;
    double latitude = 0.0;
    double longitude = 0.0;
    float precision = 0.0f;
    // UI Widgets.
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private Button mylocationbtn;
    private Button savebtn;
    private TextView mAddressTextView;
    private TextView mPrecisionTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    AlertDialog.Builder ad;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationListener mLocationListener;
    private boolean mRequestingLocationUpdates;
    JSONArray jsonArray;
    JSONArray jsonArray2;
    ArrayList<Double> array_lat;
    ArrayList<Double> array_long;
    ArrayList<Double> array_lat2;
    ArrayList<Double> array_long2;
    String user_email;
    String REGISTER_URL = Config.URL + "locationinsert.php";
    int num;
    JSONObject lat_sobject;
    JSONObject long_sobject;
    String str;
    String str2;
    String locationStr;
    EditText locationEdit;
    Date date;
    String formatDate;
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        num = 0;
        str = "[";
        str2 = "[";

        jsonArray = new JSONArray();
        jsonArray2 = new JSONArray();
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mylocationbtn = (Button) findViewById(R.id.btn_mylocation);
        user_email = LoginActivity.call_email;
        /*address_button = (Button) findViewById(R.id.address_button);*/

        if (mRequestingLocationUpdates && mGoogleApiClient.isConnected() && isPermissionGranted())
            startLocationUpdates();

        savebtn = (Button) findViewById(R.id.savebtn);
        mStartUpdatesButton.setBackgroundColor(getResources().getColor(R.color.btncolor));
        mStopUpdatesButton.setBackgroundColor(getResources().getColor(R.color.btncolor));
        /*address_button.setBackgroundColor(getResources().getColor(R.color.btncolor));*/
        savebtn.setBackgroundColor(getResources().getColor(R.color.btncolor));

        array_lat = new ArrayList<Double>();
        array_long = new ArrayList<Double>();
        lat_sobject = new JSONObject();
        long_sobject = new JSONObject();
        array_lat2 = new ArrayList<Double>();
        array_long2 = new ArrayList<Double>();

        long now = System.currentTimeMillis();
        date = new Date(now);
        SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        formatDate = sdfnow.format(date);
        Toast.makeText(getApplicationContext(), formatDate, Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new GpsActivity.MyOnNavigationItemSelectedListener());

        View header = navigationView.getHeaderView(0);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        ImageView header_img = (ImageView) header.findViewById(R.id.header_img);
        header_email.setText(LoginActivity.call_email);
        header_img.setImageURI(Uri.parse(com.example.b10715.final_pj.Config.user_img));


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(new MyConnectionCallBack())
                    .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                    .addApi(LocationServices.API)
                    .build();
        }
        ad = new AlertDialog.Builder(GpsActivity.this);
        ad.setTitle("경로이름을 지정해 주세요!");       // 제목 설정
        final EditText et = new EditText(GpsActivity.this);
        ad.setView(et);
        // 내용 설정
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "Yes Btn Click");

                // Text 값 받아서 로그 남기기
                String value = et.getText().toString();
                locationStr = value;
                LocationInfojson();
                array_lat.clear();
                array_long.clear();
                Log.v(TAG, value);

                dialog.dismiss();     //닫기
                // Event
            }
        });


        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "No Btn Click");
                dialog.dismiss();     //닫기
                // Event
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();

            }
        });

        mylocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GpsActivity.this, SavedLocationlistActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        LatLng location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        gMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint)));

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void inserToDatabase(String latitude, String longitude, String user_email, String path_name, String date) {
     /*   array_lat.clear();
        array_long.clear();*/
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(GpsActivity.this, "Please Wait", null, true, true);
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
                    String latitude = (String) params[0];
                    String logitude = (String) params[1];
                    String user_email = (String) params[2];
                    String path_name = (String) params[3];
                    String date = (String) params[4];


                    String link = REGISTER_URL;
                    String data = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                    data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(logitude, "UTF-8");
                    data += "&" + URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8");
                    data += "&" + URLEncoder.encode("path_name", "UTF-8") + "=" + URLEncoder.encode(path_name, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");


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

        InsertData task = new InsertData();
        task.execute(latitude, longitude, user_email, path_name, date);

    }

    private class MyConnectionCallBack implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(TAG, "onConnected");
            if (isPermissionGranted())
                if (ActivityCompat.checkSelfPermission(GpsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GpsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            precision = mCurrentLocation.getAccuracy();
            updateUI();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG, "onConnectionSuspended");
        }
    }

    public boolean isPermissionGranted() {
        String[] PERMISSIONS_STORAGE = {    // 요청할 권한 목록을 설정
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ActivityCompat.checkSelfPermission(GpsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(GpsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    GpsActivity.this,         // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    PERMISSIONS_STORAGE,        // 요청할 권한 목록을 설정한 String 배열
                    MY_PERMISSION_REQUEST_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return false;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    updateUI();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    private class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.i(TAG, "onConnectionFailed");
        }
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart, connect request");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && mGoogleApiClient.isConnected() && isPermissionGranted())
            startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            stopLocationUpdates();
    }

    // It is a good practice to remove location requests when the activity is in a paused or
    // stopped state. Doing so helps battery performance and is especially
    // recommended in applications that request frequent location updates.
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop, disconnect request");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
            savebtn.setEnabled(true);

        }
    }

    private void stopLocationUpdates() {
        if (mLocationListener != null && mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);

    }

    public void LocationInfojson() {
        for (int k = 0; k < array_lat.size() - 1; k++) {
            str += "{\"lat\":" + array_lat.get(k).toString() + "},";

        }

        for (int k = 0; k < array_long.size() - 1; k++) {
            str2 += "{\"long\":" + array_long.get(k).toString() + "},";

        }
        str += "{\"lat\":" + array_lat.get(array_lat.size() - 1).toString() + "}";
        str2 += "{\"long\":" + array_long.get(array_long.size() - 1).toString() + "}";
        str += "]";
        str2 += "]";

        inserToDatabase(str, str2, user_email, locationStr, formatDate);
        Intent intent = new Intent(GpsActivity.this, SavedLocationlistActivity.class);
        startActivity(intent);


    /*    Intent intent =new Intent(GpsActivity.this,SavedLocationlist.class);
        startActivity(intent);*/
    }

    private void updateUI() {
        double latitude = 0.0;
        double longitude = 0.0;
        float precision = 0.0f;
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            precision = mCurrentLocation.getAccuracy();
        }

        array_lat.add(mCurrentLocation.getLatitude());
        array_long.add(mCurrentLocation.getLongitude());
     /*   str+= String.valueOf(mCurrentLocation.getLatitude())+"},{";*/


        try {

            lat_sobject.put("lat", mCurrentLocation.getLatitude());
            long_sobject.put("long", array_long);
            jsonArray.put(num, lat_sobject);
            jsonArray2.put(num, long_sobject);
            num++;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    public void startUpdatesButtonHandler(View view) {
        /*if (!mRequestingLocationUpdates) {*/
        if (mGoogleApiClient.isConnected() && isPermissionGranted()) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();

           /* }*/
        }
    }

    private void startLocationUpdates() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(5000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                updateUI();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locRequest, mLocationListener);
    }


    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    public void getAddressButtonHandler(View view) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                mAddressTextView.setText(String.format("\n[%s]\n[%s]\n[%s]\n[%s]",
                        address.getFeatureName(),
                        address.getThoroughfare(),
                        address.getLocality(),
                        address.getCountryName()
                ));
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed in using Geocoder", e);
        }
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
                    Intent homeIntent = new Intent(GpsActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_cam:
                    Intent camIntent = new Intent(GpsActivity.this, CamActivity.class);
                    startActivity(camIntent);
                    break;
                case R.id.nav_gps:
                    Intent gpsIntent = new Intent(GpsActivity.this, GpsActivity.class);
                    startActivity(gpsIntent);
                    break;
                case R.id.nav_user:
                    Intent userIntent = new Intent(GpsActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    break;
                case R.id.nav_setting:
                    Intent feedIntent = new Intent(GpsActivity.this, SettingActivity.class);
                    startActivity(feedIntent);
                    break;
                case R.id.nav_petsitter:
                    Intent petsitterIntent = new Intent(GpsActivity.this, PetSitterActivity.class);
                    startActivity(petsitterIntent);
                    break;
                case R.id.nav_like_petsitter:
                    Intent likeIntent = new Intent(GpsActivity.this, PetSitterEditActivity.class);
                    startActivity(likeIntent);
                    break;
            }
            return true;
        }
    }

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
                        Intent intent = new Intent(GpsActivity.this, LoginActivity.class);
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
}