package br.com.gabrielferreira.moreaqui;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class RecordData extends AsyncTask<List<LocationEstate>, Void, Void> {

    private Exception exception;

    @Override
    protected Void doInBackground(List<LocationEstate>... locationEstates) {
        DaoImpl dao = new DaoImpl();
        SendData sendData = new SendData();
        Invoker invoker = new Invoker(DAO.HOST, DAO.PORT);

        for (LocationEstate estate: locationEstates[0]){
            sendData.setKey(Long.valueOf(estate.hashCode()));
            sendData.setValue((Serializable) estate);

            Log.v("Serializable", String.valueOf(sendData.getKey()));
            invoker.invoke(dao, sendData);
        }
        return null;
    }
}
