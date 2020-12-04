package br.com.gabrielferreira.moreaqui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity implements android.location.LocationListener {
    private PermissionsHandler permissionsHandler;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newBtn = (Button) findViewById(R.id.createNew);
        Button recordBtn = (Button) findViewById(R.id.btn_record);
        Button listBtn = (Button) findViewById(R.id.show);
        Button mapBtn = (Button) findViewById(R.id.mapShow);
        permissionsHandler = new PermissionsHandler(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!permissionsHandler.checkPermission()){
            permissionsHandler.requestPermission();
        }else {
            // caso a permissão já tenha sido concedida, atualiza a localização
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

            // caso o localização não seja nula
            if (location != null) {
                onLocationChanged(location);
            } else {
                lat = 0.0;
                lng = 0.0;
            }
        }

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Insert.class);
                startActivity(intent);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowAddresses.class);
                startActivity(intent);
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EstateDB db = EstateDB.getInstance(MainActivity.this);
                List<LocationEstate> estates = db.getAllEstates();
                new RecordData().execute(estates);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // verifica as permissões concedidas
        if (permissionsHandler.checkPermission()) {
            // para de solicitar a atualização da localização
            locationManager.removeUpdates(this);
            // solita a atualização da localização
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 400, 1, this);
        } else {
            // caso contrário, solicita novamente a permissão de acesso para o usuário
            permissionsHandler.requestPermission();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}