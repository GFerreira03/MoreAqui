package br.com.gabrielferreira.moreaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowAddresses extends AppCompatActivity {

    ListView listView;
    EstateDB estateDB;

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_addresses);

        listView = (ListView) findViewById(R.id.list_estates);
        estateDB = EstateDB.getInstance(this);

        listEstates();

    }

    private void listEstates() {
        List<LocationEstate> estates = estateDB.getAllEstates();
        if (estates != null){
            arrayList = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(this, R.layout.row, arrayList);

            listView.setAdapter(adapter);

            for (LocationEstate i : estates){
                arrayList.add("Telefone: "+i.getPHONE() +
                        "\nCondição: "+ i.getSTATUS() +
                        "\n\nTipo: " + i.getTYPE() +
                        "\nTamanho: "+ i.getSIZE());

                adapter.notifyDataSetChanged();
            }
        }
    }
}