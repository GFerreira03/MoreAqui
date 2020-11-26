package br.com.gabrielferreira.moreaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Insert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        EditText input_phone = findViewById(R.id.editText);
        RadioGroup typeRadiogp = findViewById(R.id.typeRadioGp);
        RadioGroup sizeRadioGp = findViewById(R.id.sizeRadioGp);
        Button confirm_button =  findViewById(R.id.confirm_button);


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = input_phone.getText().toString();
                String expressao = "[1-9]{2}?[0-9]{8,9}";

                if (!numero.matches(expressao)) {
                    Toast.makeText(getApplicationContext(), "Numero Invalido", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}