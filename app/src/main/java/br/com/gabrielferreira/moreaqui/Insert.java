package br.com.gabrielferreira.moreaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Insert extends AppCompatActivity {
    public String type, size, status, phone;
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

        dunnoType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
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
            }
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = input_phone.getText().toString();
                String expressao = "[1-9]{2}?[0-9]{8,9}";

                if (!phone.matches(expressao)) {
                    Toast.makeText(getApplicationContext(), "Numero Invalido.", Toast.LENGTH_SHORT).show();
                } else {
                    if((typeRadiogp.getCheckedRadioButtonId() == -1 && !dunnoType.isChecked()) || (sizeRadioGp.getCheckedRadioButtonId() == -1 && !dunnoSize.isChecked()))
                        Toast.makeText(getApplicationContext(), "Opções foram deixadas em branco.", Toast.LENGTH_SHORT).show();
                    else {
                        status = isBuilt.isChecked() ? "Construída" : "Em construção";
                        Estate estate = new Estate(size, status, phone, type);

                        Log.v("Added", estate.PHONE + " | " + estate.SIZE  + " | " + estate.TYPE  + " | " + estate.STATUS);

                        EstateDB db = EstateDB.getInstance(Insert.this);

                        db.addEstate(estate);

                        Intent intent = new Intent(Insert.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(Insert.this, "Salvo!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}