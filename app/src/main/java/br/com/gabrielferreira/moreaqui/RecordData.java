package br.com.gabrielferreira.moreaqui;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class RecordData extends AsyncTask<List<LocationEstate>, Void, Void> {

    private Exception exception;

    @Override
    protected Void doInBackground(List<LocationEstate>... LocationEstates) {
        DaoImpl daoImpl = new DaoImpl();
        // instancia um objeto do tipo Invoker para
        // invocar uma conexão com a aplicação servidor
        Invoker invoker = new Invoker(DAO.HOST, DAO.PORT);
        // Declara um objeto do tipo SendData
        // para serealizar um objeto do tipo Estate
        SendData sendData = new SendData();

        for (LocationEstate estate : LocationEstates[0]){
            // configura a chave (id) do objeto
            sendData.setKey(Long.valueOf(estate.hashCode()));
            // configura o objeto que será enviado
            // via serialização
            sendData.setValue(estate.toString());
            // invoker executa a ação de enviar os dados
            // serializados
            invoker.invoke(daoImpl, sendData);
        }
        return null;
    }
}
