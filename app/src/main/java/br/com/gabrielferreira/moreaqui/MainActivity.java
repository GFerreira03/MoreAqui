package br.com.gabrielferreira.moreaqui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity {
    Button newBtn, recordBtn, listBtn;
    private static final String[] PERMISSIONS = {"android.permission.INTERNET",
            "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newBtn = (Button) findViewById(R.id.createNew);
        recordBtn = (Button) findViewById(R.id.btn_record);
        listBtn = (Button) findViewById(R.id.show);

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

        if (!checkPermission())
            requestPermission();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,  PERMISSIONS, PERMISSION_REQUEST_CODE);
        this.recreate();
    }

    /***
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        // verifca se as permissões solicitadas foram concedidas pelo usuário
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {

            boolean internetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean fineLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocationAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

            if (internetAccepted && fineLocationAccepted && coarseLocationAccepted) {
                Toast.makeText(this, "Permissões concedidas", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Permissões negadas", Toast.LENGTH_SHORT);

                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel("Você precisa autorizar todas as permissões.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(PERMISSIONS,
                                            PERMISSION_REQUEST_CODE);
                                }
                            });
                    return;
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }
    private boolean checkPermission() {
        int internetResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int fineLocationResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int coarseLocationResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        // retorna true caso tenha as três permissões ou false caso uma das permissões tenha
        // sido negada
        return internetResult == PackageManager.PERMISSION_GRANTED &&
                fineLocationResult == PackageManager.PERMISSION_GRANTED &&
                coarseLocationResult == PackageManager.PERMISSION_GRANTED;
    }
}