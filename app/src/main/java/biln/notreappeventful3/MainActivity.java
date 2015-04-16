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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();


        //initialisation
        Cursor c = DBHelper.listEvents(db);

        adapter = new MyAdapter(this, c);
        listv = (ListView)findViewById(R.id.activity_list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(this);

        Intent in = new Intent(this, ServiceSearchAndPopulate.class);
        in.putExtra("populateSuggestedList", true);  //aller chercher l'information sur le web IMPORTANT
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
            int id= c.getInt(c.getColumnIndex(DBHelper.C_ID));

            // Format des dates
            DateFormat dateFormatFinal= new SimpleDateFormat("dd MMM yyyy hh:mm");
            DateFormat dateFormatIni= new SimpleDateFormat("yyyy-MM-dd hh:mm");

            String dateDebut = c.getString(c.getColumnIndex(DBHelper.C_DATE_START));
            String dateFin = c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP));
            try{
                Date dateOld1 = dateFormatIni.parse(dateDebut);
                Date dateOld2 = dateFormatIni.parse(dateFin);
                String newDate1 = dateFormatFinal.format(dateOld1);
                String newDate2 = dateFormatFinal.format(dateOld2);
                dateDebut = newDate1;
                dateFin = newDate2;
            }catch(ParseException e){
                e.printStackTrace();
            }

            //TODO: enlever "id supposé"
            title.setText(c.getString(c.getColumnIndex(DBHelper.C_TITLE))+ " id : "+id);
            location.setText(c.getString(c.getColumnIndex(DBHelper.C_LOCATION)));
            startT.setText(dateDebut);
            stopT.setText(dateFin);

            int valFavorite = c.getInt(c.getColumnIndex(DBHelper.C_FAVORITE));
            //pour ne pas que le setChecked soit considérer dans le recyclage
            myBtn.setOnCheckedChangeListener(null);
            myBtn.setChecked(valFavorite==1);

            myBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id = ((Integer) buttonView.getTag()).intValue();
                    //Toast.makeText(getApplicationContext(), "changé ds les favoris " + " " + isChecked, Toast.LENGTH_SHORT).show();
                    Log.d("Listener", "Checked " + isChecked + " "  + " " + id );
                    dbh.changeFavoriteStatus(db, id);//(int)getItemId(pos)


                    Cursor nc = DBHelper.listEvents(db);
                    MyAdapter.this.swapCursor(nc);

                    //voir listview: MyAdapter notifyDatasetchanged

                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "Ajouté aux favoris ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Retiré des favoris ", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            myBtn.setTag(new Integer(id));

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


