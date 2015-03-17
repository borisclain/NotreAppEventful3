package biln.notreappeventful3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Boris on 2015-03-15.
 */
public class DBHelper extends SQLiteOpenHelper {


    static final String DB_NAME = "eventful.db";
    static final int DB_VERSION = 16;                   //TODO Important pour le développement

    static final String TABLE_EVENTS = "events";
    static final String C_ID = "_id";
    static final String C_ID_FROM_EVENTFUL = "_id_from_eventful";
    static final String C_TITLE ="title";
    static final String C_DATE_START ="date_start";
    static final String C_DATE_STOP = "date_stop";
    static final String C_LOCATION ="location";
    static final String C_FAVORITE = "favorite";       //1 si l'événement est favori, 0 sinon

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ TABLE_EVENTS +" ("
                +C_ID+" integer primary key autoincrement, "
                +C_ID_FROM_EVENTFUL+" text,"
                +C_TITLE+" text,"
                +C_DATE_START+" text,"
                +C_DATE_STOP+" text,"
                +C_LOCATION+" text,"
                +C_FAVORITE+" integer," +
                "UNIQUE "+"("+C_ID_FROM_EVENTFUL+")"+" ON CONFLICT REPLACE)";
        db.execSQL(sql);
        Log.d("DB", "DB created");
    }



    //TODO : Tester de fond en combe la méthode suivante et trouver le meilleur moment pour l'appeler
    /**
     * Dans la base de données, la colonne C_DATE_STOP contient sa valeur sous la forme d'un String:
     * ISO 8601 comme ceci: "2005-03-01 19:00:00"
     * @param db
     */
    public void deletePassedEvents(SQLiteDatabase db){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(c.getTime());

        System.out.println("Aujourd'hui nous sommes le "+today);
        db.execSQL("delete from "+TABLE_EVENTS+" where date("+C_DATE_STOP+") <"+today);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_EVENTS);
        onCreate(db);
    }

    public static Cursor listEvents(SQLiteDatabase db){
        Cursor c = db.rawQuery("select * from " + TABLE_EVENTS + " order by " + C_DATE_STOP + " asc", null);
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
                C_DATE_STOP+")"+" desc", args);
        Log.d("DB","cherche events nb = "+c.getCount());
        return c;
    }

    public static void setEventAsFavorite(SQLiteDatabase db, int id, int favorite) {
        ContentValues val = new ContentValues();
        val.put(C_FAVORITE, favorite);
        db.update(TABLE_EVENTS ,val, C_ID+" = "+id, null); //update l'élément dont on a récupéré le ID
    }


}
