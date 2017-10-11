package com.example.b10715.final_pj;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.lang.String;

import static com.example.b10715.final_pj.R.id.map;
import static com.example.b10715.final_pj.R.id.textview;

public class ShowSavedLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    PolylineOptions polylineOption;
    static Button btn;

    double latitude = 0.0;
    double longitude = 0.0;
    float precision = 0.0f;
    private ArrayList<LatLng> arraypoints;
    ArrayList<Double> array_lat;
    ArrayList<Double> array_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location);
        btn = (Button) findViewById(R.id.button);
        polylineOption = new PolylineOptions();
     /*   SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);*/

        arraypoints = new ArrayList<LatLng>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new MyConnectionCallBack())
                .addOnConnectionFailedListener(new MyOnConnectionFailedListener())
                .addApi(LocationServices.API)
                .build();
        array_lat = (ArrayList<Double>) getIntent().getSerializableExtra("array_lat");
        array_long = (ArrayList<Double>) getIntent().getSerializableExtra("array_long");

        for (int i = 0; i < array_long.size(); i++) {
            arraypoints.add(new LatLng(array_lat.get(i), array_long.get(i)));
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHangleAddr();

            }
        });

    }


    private class MyConnectionCallBack implements GoogleApiClient.ConnectionCallbacks {
        public void onConnected(Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(ShowSavedLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ShowSavedLocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        } // 연결 성공시 호출


        public void onConnectionSuspended(int i) {
        } // 일시 연결 해제시 호출
    }

    private class MyOnConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    public void onMapReady(GoogleMap gMap) {

        LatLng location = new LatLng(latitude, longitude);
        EditText editText = (EditText) findViewById(R.id.edit);
        String stext = editText.getText().toString();

        gMap.addMarker(new MarkerOptions().position(location).title(stext).icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint)));
        for (int i = 0; i < arraypoints.size(); i++) {
            gMap.addMarker(new MarkerOptions().position(arraypoints.get(i)).title(stext).icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint)));
        }

        polylineOption.color(Color.BLUE);
        polylineOption.width(5);
        polylineOption.add(location);
        polylineOption.addAll(arraypoints);
        polylineOption.geodesic(true);
        gMap.addPolyline(polylineOption);

        // move the camera
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


    }

    public void Addmarker() {
    }

    private void updateUI() {
        TextView mText = (TextView) findViewById(R.id.textview);
        //if (stext != null) {
        latitude = mCurrentLocation.getLatitude();
        longitude = mCurrentLocation.getLongitude();
        //precision = mCurrentLocation.getAccuracy();

        mText.setText("Latitude: " + latitude + "Longtitude" + longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }

    private void getHangleAddr() {
        try {
            TextView mText = (TextView) findViewById(textview);
            EditText editText = (EditText) findViewById(R.id.edit);
            String stext = editText.getText().toString();
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);

            List<Address> addresses = geocoder.getFromLocationName(stext, 1);
            if (addresses.size() > 0) {
                Address bestResult = (Address) addresses.get(0);
                LatLng changelocation = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                mText.setText(String.format("[위도: %s , 경도: %s ]",
                        bestResult.getLatitude(),
                        bestResult.getLongitude()));

                mCurrentLocation.setLatitude(bestResult.getLatitude());
                mCurrentLocation.setLongitude(bestResult.getLongitude());
                updateUI();
            }


        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }

    }

}