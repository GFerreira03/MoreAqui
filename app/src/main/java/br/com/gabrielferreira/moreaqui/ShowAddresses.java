package br.com.gabrielferreira.moreaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
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
            Resources res = getResources();
            String phone = res.getString(R.string.app_insert_phone);
            String type = res.getString(R.string.app_insert_type);
            String size = res.getString(R.string.app_insert_size);
            String condition = res.getString(R.string.app_insert_condition);
            for (LocationEstate i : estates){
                arrayList.add(phone+" "+i.getPHONE() +
                        "\n"+condition+":"+" "+i.getSTATUS() +
                        "\n" + type+" "+ i.getTYPE() +
                        "\n"+size+" "+i.getSIZE());

                adapter.notifyDataSetChanged();
            }
        }
    }
}