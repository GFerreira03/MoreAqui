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
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class Insert extends AppCompatActivity implements LocationListener {
    public String type, size, status, phone;
    public double lat, lng;
    // declara constante com a tag que será mostrada no Log
    private static final String TAG = "MainActivity";

    // declara o objeto LocationManager para recuperar a localização do usuário
    private LocationManager locationManager;
    // declara um string para determinar o tipo de provedor de localização
    private String provider;
    // declara uma constante que guarda as permissões necessárias para acessar
    // a internet o GPS

    private PermissionsHandler permissionsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        EditText input_phone = findViewById(R.id.editText);

        RadioGroup typeRadiogp = findViewById(R.id.typeRadioGp);
        RadioGroup sizeRadioGp = findViewById(R.id.sizeRadioGp);

        Button confirm_button =  findViewById(R.id.confirm_button);

        CheckBox dunnoSize = findViewById(R.id.dunnoSize);
        CheckBox dunnoType = findViewById(R.id.dunnoType);
        CheckBox isBuilt = findViewById(R.id.isBuilt);

        input_phone.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, input_phone));

        // recupera o serviço de localização
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // declara um critério de atualização da localização
        Criteria criteria = new Criteria();
        // inicializa provider com o provedor diponibilizado pelo locationManager
        provider = locationManager.getBestProvider(criteria, false);
        permissionsHandler = new PermissionsHandler(this);
        // verifica se tem permissão para acessar internet e o GPS
        if (permissionsHandler.checkPermission()) {
            // caso a permissão já tenha sido concedida, atualiza a localização
            Location location = locationManager.getLastKnownLocation(provider);

            // caso o localização não seja nula
            if (location != null) {
                Log.d(TAG, "Provider " + provider + "foi selecionado.");
                // chama o listener para atualizar a localização
                onLocationChanged(location);
            } else {
                lat = 0.0;
                lng = 0.0;
            }
        } else {
            // caso não tenha permissão, solicita a permissão ao usuário
            permissionsHandler.requestPermission();
        }

        typeRadiogp.setOnCheckedChangeListener(((radioGroup, i) -> {
            switch (i){
                case R.id.houseRadioBtn:
                    type = "Casa";
                    break;
                case R.id.aptoRadioBtn:
                    type = "Apartamento";
                    break;
                case R.id.shopRadioBtn:
                    type = "Loja";
                    break;
            }
        }));

        dunnoSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dunnoSize.isChecked()){
                    size = "Não sei";
                    sizeRadioGp.clearCheck();
                    for(int i = 0; i < sizeRadioGp.getChildCount(); i++){
                        ((RadioButton)sizeRadioGp.getChildAt(i)).setEnabled(false);
                    }
                } else {
                    size = null;
                    for(int i = 0; i < sizeRadioGp.getChildCount(); i++){
                        ((RadioButton)sizeRadioGp.getChildAt(i)).setEnabled(true);
                    }
                }
            }
        });

        sizeRadioGp.setOnCheckedChangeListener(((radioGroup1, i) -> {
            switch (i){
                case R.id.smallRadioBtn:
                    size = "Pequena";
                    break;
                case R.id.mediumRadioBtn:
                    size = "Média";
                    break;
                case R.id.bigRadioBtn:
                    size = "Grande";
                    break;
            }
        }));

        dunnoType.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (compoundButton, b) -> {
            if (dunnoType.isChecked()){
                type = "Não sei";
                typeRadiogp.clearCheck();
                for(int i = 0; i < typeRadiogp.getChildCount(); i++){
                    ((RadioButton)typeRadiogp.getChildAt(i)).setEnabled(false);
                }
            } else {
                type = null;
                for(int i = 0; i < typeRadiogp.getChildCount(); i++){
                    ((RadioButton)typeRadiogp.getChildAt(i)).setEnabled(true);
                }
            }
        });

        confirm_button.setOnClickListener(v -> {
            phone = input_phone.getText().toString();
            String expressao = "\\([1-9]{2}\\) [0-9]{5} [0-9]{3,4}";
            Log.v("Numero", phone);
            if (!phone.matches(expressao)) {
                Toast.makeText(getApplicationContext(), "Numero Invalido.", Toast.LENGTH_SHORT).show();
            } else {
                if((typeRadiogp.getCheckedRadioButtonId() == -1 && !dunnoType.isChecked()) || (sizeRadioGp.getCheckedRadioButtonId() == -1 && !dunnoSize.isChecked()))
                    Toast.makeText(getApplicationContext(), "Opções foram deixadas em branco.", Toast.LENGTH_SHORT).show();
                else {
                    status = isBuilt.isChecked() ? "Em construção" : "Construída";

                    LocationEstate locationEstate = new LocationEstate(type, size,phone ,status , lat, lng);
                    Log.v("Added", locationEstate.PHONE + " | " + locationEstate.SIZE  + " | " + locationEstate.TYPE  + " | " + locationEstate.STATUS + " | " + locationEstate.LATITUDE + " | " + locationEstate.LONGITUDE);

                    EstateDB db = EstateDB.getInstance(Insert.this);

                    db.addEstate(locationEstate);

                    Intent intent = new Intent(Insert.this, MainActivity.class);
                    startActivity(intent);
                    Resources res = getResources();
                    String msg = res.getString(R.string.app_msg_save);
                    Toast.makeText(Insert.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                }
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
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        } else {
            // caso contrário, solicita novamente a permissão de acesso para o usuário
            permissionsHandler.requestPermission();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // para de solicitar a atualização da localização
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