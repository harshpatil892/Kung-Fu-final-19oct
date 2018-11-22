package com.example.vrushali.kungfu123;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (InitApplication.getInstance().isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng first = new LatLng(19.125362, 72.999199);
        mMap.addMarker(new MarkerOptions().position(first).title("Budhha Mandir"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(first));

        LatLng second=new LatLng(19.033400,73.018997);
        mMap.addMarker(new MarkerOptions().position(second).title("Navi Mumbai"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(second));

        LatLng third=new LatLng(19.102930,72.998810);
        mMap.addMarker(new MarkerOptions().position(third).title("Sector 17"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(third));


        LatLng four=new LatLng(19.102930,72.998810);
        mMap.addMarker(new MarkerOptions().position(four).title("Sector 17"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(four));

        LatLng five=new LatLng(19.082884,73.028870);
        mMap.addMarker(new MarkerOptions().position(five).title("Sector 06"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(five));

        LatLng six=new LatLng(19.094560,73.012620);
        mMap.addMarker(new MarkerOptions().position(six).title("Sector 12"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(six));

        LatLng seven=new LatLng(19.097580,73.001070);
        mMap.addMarker(new MarkerOptions().position(seven).title("Sector 14"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seven));

        LatLng eight=new LatLng(19.073790,72.992490);
        mMap.addMarker(new MarkerOptions().position(eight).title("Sector 04"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eight));

        LatLng nine=new LatLng(19.055180,73.024610);
        mMap.addMarker(new MarkerOptions().position(nine).title("Turbhe"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nine));

        LatLng ten=new LatLng(19.004450,73.018320);
        mMap.addMarker(new MarkerOptions().position(ten).title("Nerul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ten));

        LatLng elevan=new LatLng(19.075350,73.001720);
        mMap.addMarker(new MarkerOptions().position(elevan).title("kalamboli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(elevan));

        float zoomLevel = 11.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(second, zoomLevel));

    }
}
