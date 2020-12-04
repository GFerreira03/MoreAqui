package br.com.gabrielferreira.moreaqui;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    private List<LocationEstate> estates;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        PermissionsHandler permissionsHandler = new PermissionsHandler(this);
        EstateDB db = EstateDB.getInstance(Map.this);
        estates = db.getAllEstates();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (!permissionsHandler.checkPermission()){
            permissionsHandler.requestPermission();
        } else {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null){
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                    if (supportMapFragment != null) {
                        supportMapFragment.getMapAsync(Map.this);
                    }
                    userLocation = location;
                }
            });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        centerInUser(googleMap);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (estates != null){
            for (LocationEstate i : estates){
                LatLng latLng = new LatLng(i.LATITUDE, i.LONGITUDE);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(i.getTYPE() + " / " + i.getSIZE());
                googleMap.addMarker(markerOptions);
            }
        }
    }

    private void centerInUser(GoogleMap googleMap){
        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.app_map_actualPosition));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions);
    }
}