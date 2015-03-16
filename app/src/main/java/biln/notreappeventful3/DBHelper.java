package biln.notreappeventful3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Boris on 2015-03-15.
 */
public class DBHelper extends SQLiteOpenHelper {


    static final String DB_NAME = "eventful.db";
    static final int DB_VERSION = 5;                   //TODO Important pour le développement

    static final String TABLE_EVENTS = "events";
    static final String C_ID = "_id";
    static final String C_TITLE ="title";
    static final String C_DATE ="date";
    static final String C_LOCATION ="location";
    static final String C_FAVORITE = "favorite";       //1 si l'événement est favori, 0 sinon

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ TABLE_EVENTS +" ("
                +C_ID+" text primary key, "
                +C_TITLE+" text,"
                +C_DATE+" text,"
                +C_LOCATION+" text,"
                +C_FAVORITE+" integer," +
                "UNIQUE "+"("+C_ID+")"+" ON CONFLICT REPLACE)";
        db.execSQL(sql);
        Log.d("DB", "DB created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_EVENTS);
        onCreate(db);
    }

    public static Cursor listEvents(SQLiteDatabase db){
        Cursor c = db.rawQuery("select * from " + TABLE_EVENTS + " order by " + C_TITLE + " asc", null);
        Log.d("DB","liste events nb = "+c.getCount());
        return c;
    }

    /**
     * Permet de rechercher un mot dans le titre des événements
     *
     * @param db La database en entrée
     * @param word Le mot à rechercher dans les titres
     * @return c Le curseur permettant d'itérer sur ces mots
     */
    public static Cursor searchEventsByTitle(SQLiteDatabase db, String word){
        String args[] = new String[] { "%"+word+"%" };
        Cursor c = db.rawQuery("select * from "+TABLE_EVENTS+" where "+C_TITLE+" like ? order by datetime("+
                C_DATE+")"+" desc", args);
        Log.d("DB","cherche events nb = "+c.getCount());
        return c;
    }

    public static void setEventAsFavorite(SQLiteDatabase db, int id, int favorite) {
        ContentValues val = new ContentValues();
        val.put(C_FAVORITE, favorite);
        db.update(TABLE_EVENTS ,val, C_ID+" = "+id, null);
    }


}
