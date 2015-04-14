package biln.notreappeventful3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by boris on 3/26/15.
 */
public class ServiceSearchAndPopulate extends IntentService{

    String city = "Montreal";
    static final String NAME = "download";

    public ServiceSearchAndPopulate(){

        super(NAME);

        Log.d("Service", "Constructeur appelé");
    }

    protected void onHandleIntent(Intent intent){


        Log.d("Service", "onHandleIntent appelé");

        DBHelper dbh = new DBHelper(this); //getApplicationContext si ça ne fonctionne pas
        SQLiteDatabase db = dbh.getWritableDatabase();

        //message de debut
        Intent in = new Intent("biln.notreappeventful3.BUSY"); // prend le meme nom que l'autre
        in.putExtra("begin", true);
        this.sendBroadcast(in);

        EventfulAPI web = new EventfulAPI();


        if(intent.getBooleanExtra("populateList", false)){
            web.getNextEvents(city);
            // mettre le web dans la DB
            ContentValues val = new ContentValues();
            for(int i=0;i<web.eventsFound.size();i++){
                // ON NE SPÉCIFIE PAS EPLICITEMENT LA VALEUR DE _id. ON LE LAISSE AUTOINCRÉMENTER
                val.put(DBHelper.C_ID_FROM_EVENTFUL, web.eventsFound.get(i).idFromEventful);
                val.put(DBHelper.C_TITLE, web.eventsFound.get(i).title);
                val.put(DBHelper.C_DATE_START, web.eventsFound.get(i).date_start);
                val.put(DBHelper.C_DATE_STOP, web.eventsFound.get(i).date_stop);
                val.put(DBHelper.C_LOCATION, web.eventsFound.get(i).location);
                val.put(DBHelper.C_DESCRIPTION, web.eventsFound.get(i).description);
                val.put(DBHelper.C_NEW, web.eventsFound.get(i).newRes);
                val.put(DBHelper.C_FAVORITE, 0);
                db.insertOrThrow(DBHelper.TABLE_EVENTS, null, val);
            }
            Log.d("DB", "nouveux titres ajoutes: ");



            //message de fin
            in = new Intent("biln.notreappeventful3.BUSY"); // prend le meme nom que l'autre
            in.putExtra("end", true);
            this.sendBroadcast(in);
        }

        else
            Toast.makeText(getApplicationContext(), "Rien n'est demande", Toast.LENGTH_SHORT).show();


    }

}
