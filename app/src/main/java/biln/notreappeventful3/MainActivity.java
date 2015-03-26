package biln.notreappeventful3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 */
public class MainActivity extends MyMenu implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listv;
    SQLiteDatabase db;
    DBHelper dbh;
    MyAdapter adapter;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = LauncherActivity.settings.getString("myCity", "Ottawa");
        listv = (ListView)findViewById(R.id.activity_list);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        Cursor c = DBHelper.listEvents(db);
        adapter = new MyAdapter(this, c);
        listv = (ListView)findViewById(R.id.activity_list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(this);

        new SearchEventfulAndPopulate().execute("a", "B", "C");
    }

    protected void onResume(){
        super.onResume();
    }

    public void onClick(View v){
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long viewID) {


        Toast.makeText(getApplicationContext(), "Clic reçu", Toast.LENGTH_SHORT).show();



        Log.d("db","Clic sur item en position= "+position+" et avec viewID = "+ viewID);
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle b = new Bundle();

        Cursor c = DBHelper.getEventByID(db, viewID);

        b.putString("title", c.getString(c.getColumnIndex(DBHelper.C_TITLE)) );
        b.putString("location", c.getString(c.getColumnIndex(DBHelper.C_LOCATION)) );
        b.putString("startT", c.getString(c.getColumnIndex(DBHelper.C_DATE_START)) );
        //b.putString("stopT", c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP)) );
        b.putString("description", c.getString(c.getColumnIndex(DBHelper.C_DESCRIPTION)) );
        intent.putExtras(b);
        startActivity(intent);

        //adapter.changeCursor(c);
        // DBHelper.changeFavoriteStatus(db, (int)viewID); // TODO Vérifier !
        //Cursor c = DBHelper.listEvents(db);
        //adapter.changeCursor(c);
    }

    private class SearchEventfulAndPopulate extends AsyncTask<String, Integer, Cursor> {

        //TODO Implanter une barre de progrès ou une horloge qui tourne au lieu d'un toast
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Chargement", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        protected Cursor doInBackground(String... params) {

            this.publishProgress(50, 34, 66);

            EventfulAPI web = new EventfulAPI();
            web.getNextEvents(city);
            ContentValues val = new ContentValues();
            for(int i=0;i<web.eventsFound.size();i++){
                // ON NE SPÉCIFIE PAS EPLICITEMENT LA VALEUR DE _id. ON LE LAISSE AUTOINCRÉMENTER
                val.put(DBHelper.C_ID_FROM_EVENTFUL, web.eventsFound.get(i).idFromEventful);
                val.put(DBHelper.C_TITLE, web.eventsFound.get(i).title);
                val.put(DBHelper.C_DATE_START, web.eventsFound.get(i).date_start);
                val.put(DBHelper.C_DATE_STOP, web.eventsFound.get(i).date_stop);
                val.put(DBHelper.C_LOCATION, web.eventsFound.get(i).location);
                val.put(DBHelper.C_DESCRIPTION, web.eventsFound.get(i).description);
                val.put(DBHelper.C_FAVORITE, 0);
                db.insertOrThrow(DBHelper.TABLE_EVENTS, null, val);
            }
            Log.d("DB", "nouveux titres ajoutes: ");
            return DBHelper.listEvents(db);
        }

        protected void onProgressUpdate(String... s) {
        }

        protected void onPostExecute(Cursor c) {
            // rafraichir...
            adapter.changeCursor(c);
        }

    }

    public class MyAdapter extends CursorAdapter {

        LayoutInflater inflater;

        public MyAdapter(Context context, Cursor c) {
            super(context, c, true);
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;


            if(v==null){
                v = inflater.inflate(R.layout.my_rows, parent, false);
            }

            TextView title = (TextView)v.findViewById(R.id.title);
            TextView startT = (TextView)v.findViewById(R.id.startT);
            TextView stopT = (TextView)v.findViewById(R.id.stopT);
            TextView location = (TextView)v.findViewById(R.id.location);

            Cursor c = getCursor();
            c.moveToPosition(position);
            String id= c.getString(c.getColumnIndex(DBHelper.C_ID));

            title.setText(c.getString(c.getColumnIndex(DBHelper.C_TITLE))+ " id supposé : "+id);
            location.setText(c.getString(c.getColumnIndex(DBHelper.C_LOCATION)));
            startT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_START)));
            stopT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP)));

            return v;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }

    }


}


