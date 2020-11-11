package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng amrita = new LatLng(10.9027, 76.9006);
        mMap.addMarker(new MarkerOptions().position(amrita).title("Marker at Amrita Coimbatore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amrita, 18));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("MapOnClkLis","Inside");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("picked_point",latLng);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}