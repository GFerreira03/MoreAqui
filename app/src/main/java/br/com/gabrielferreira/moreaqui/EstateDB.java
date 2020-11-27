package br.com.gabrielferreira.moreaqui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EstateDB extends SQLiteOpenHelper {
    private static EstateDB estateDB;

    private static final String DATABASE_NAME = "EstatesDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "estates";

    private static final String _ID = "id";
    private static final String TYPE = "type";
    private static final String SIZE = "size";
    private static final String STATUS = "status";
    private static final String PHONE = "phone";

    public static synchronized EstateDB getInstance(Context context) {
        if (estateDB == null) {
            estateDB = new EstateDB(context.getApplicationContext());
        }
        return estateDB;
    }

    private EstateDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE + " TEXT NOT NULL, "
                + SIZE + " TEXT NOT NULL, "
                + STATUS + " TEXT, "
                + PHONE + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEstate (Estate estate){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TYPE, estate.TYPE);
            values.put(SIZE, estate.SIZE);
            values.put(STATUS, estate.STATUS);
            values.put(PHONE, estate.PHONE);

            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ADIÇÃO AO BD", "Erro ao inserir dados no banco de dados.");
        } finally {
            db.endTransaction();
        }
    }

    public List<Estate> getAllEstates() {
        List<Estate> estates = new ArrayList<>();

        String POST_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POST_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Estate newEstate = new Estate(cursor.getString(cursor.getColumnIndex(TYPE)),
                            cursor.getString(cursor.getColumnIndex(SIZE)),
                            cursor.getString(cursor.getColumnIndex(PHONE)),
                            cursor.getString(cursor.getColumnIndex(STATUS)));
                    estates.add(newEstate);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("SELEÇÃO DO DB", "Erro ao retornar dados do banco de dados.");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return estates;
    }
}
