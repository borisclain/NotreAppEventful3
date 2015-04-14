package biln.notreappeventful3;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 */
public class MainActivity extends MyMenu implements View.OnClickListener, AdapterView.OnItemClickListener {
    String city;
    ListView listv;
    MyAdapter adapter;
    SQLiteDatabase db;
    DBHelper dbh;
    BusyReceiver busyR;
    IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = Launcher.settings.getString("myCity", "Ottawa");
        listv = (ListView)findViewById(R.id.activity_list);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();

        //initialisation
        Cursor c = DBHelper.listEvents(db);

        adapter = new MyAdapter(this, c);
        listv = (ListView)findViewById(R.id.activity_list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(this);

        Intent in = new Intent(this, ServiceSearchAndPopulate.class);
        in.putExtra("populateList", true);  //aller chercher l'information sur le web IMPORTANT
        startService(in);

        busyR = new BusyReceiver();
        filter = new IntentFilter("biln.notreappeventful3.BUSY");

    }

    protected void onResume(){
        super.onResume();
        registerReceiver(busyR, filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(busyR);
    }

    public void onClick(View v){
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long viewID) {

        Toast.makeText(getApplicationContext(), "Clic reçu", Toast.LENGTH_SHORT).show();

        Log.d("db","Clic sur item en position= "+position+" et avec viewID = "+ viewID);
        Intent intent = new Intent(this, DetailsActivityLena.class); //TODO : DetailsActivity
        Bundle b = new Bundle();

        Cursor c = DBHelper.getEventByID(db, viewID);

        b.putString("title", c.getString(c.getColumnIndex(DBHelper.C_TITLE)) );
        b.putString("location", c.getString(c.getColumnIndex(DBHelper.C_LOCATION)) );
        b.putString("startT", c.getString(c.getColumnIndex(DBHelper.C_DATE_START)) );
        b.putString("description", c.getString(c.getColumnIndex(DBHelper.C_DESCRIPTION)) );
        b.putString("eventfulID", c.getString(c.getColumnIndex(DBHelper.C_ID_FROM_EVENTFUL)));
        intent.putExtras(b);
        startActivity(intent);
    }

    public class BusyReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent){
            if(intent.getBooleanExtra("begin", false)){
                Toast.makeText(context, "Downloading and putting in DB", Toast.LENGTH_SHORT).show();
            }
            if(intent.getBooleanExtra("end", false)){
                Cursor c = dbh.listEvents(db);
                adapter.changeCursor(c);
            }
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

            CheckBox myBtn = (CheckBox)v.findViewById(R.id.starButton);

            Cursor c = getCursor();
            c.moveToPosition(position);
            String id= c.getString(c.getColumnIndex(DBHelper.C_ID));

            title.setText(c.getString(c.getColumnIndex(DBHelper.C_TITLE))+ " ID in DB : "+id);
            location.setText(c.getString(c.getColumnIndex(DBHelper.C_LOCATION)));
            startT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_START)));
            stopT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP)));

            myBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = ((Integer)buttonView.getTag()).intValue();
                    Log.d("Listener", "Checked "+isChecked+" "+pos);
                    DBHelper.changeFavoriteStatus(db, pos);
                }
            });

            myBtn.setTag(new Integer(Integer.parseInt(id)));
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


