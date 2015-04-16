package biln.notreappeventful3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Fleur de Lotus on 20/03/2015.
 */
public class Favorites extends MyMenu implements AdapterView.OnItemClickListener {
    ListView listv;
    SQLiteDatabase db;
    DBHelper dbh;
    MyAdapter adapter;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //city = LauncherActivity.settings.getString("myCity", "Ottawa");
        //listv = (ListView)findViewById(R.id.activity_list);
        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();
        Cursor c = DBHelper.listFavoris(db);// liste des favoris récupérée
        adapter = new MyAdapter(this, c);
        // j'adapte la partie listview à mes données de la base.
        listv = (ListView)findViewById(R.id.activity_list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(this);
        TextView titre = (TextView)findViewById(R.id.textView);
        titre.setText("Mes Favoris");

        //new SearchEventfulAndPopulate().execute("a", "B", "C");
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v){
        if(v.getId()== R.id.starButton){

        }
    }

    /**
     * Clic sur l'événement pour voir les détails
     * @param parent
     * @param viewClicked
     * @param position
     * @param viewID
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long viewID) {

        Log.d("db","Clic sur item en position= "+position+" et avec viewID = "+ viewID);
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle b = new Bundle();

        Cursor c = DBHelper.getEventByID(db, viewID);

        b.putString("title", c.getString(c.getColumnIndex(DBHelper.C_TITLE)) );
        b.putString("location", c.getString(c.getColumnIndex(DBHelper.C_LOCATION)) );
        b.putString("startT", c.getString(c.getColumnIndex(DBHelper.C_DATE_START)) );
        b.putString("stopT", c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP)) );// traitement fait ailleurs
        b.putString("description", c.getString(c.getColumnIndex(DBHelper.C_DESCRIPTION)) );
        intent.putExtras(b);
        startActivity(intent);

    }

    /**
     * Adapte la vue suivant les info de la BDD
     */
    public class MyAdapter extends CursorAdapter {

        LayoutInflater inflater;

        public MyAdapter(Context context, Cursor c) {
            super(context, c, true);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = inflater.inflate(R.layout.favoris, parent, false);

            }

            // si favoris
            Cursor c = getCursor();
            c.moveToPosition(position);
            final String id= c.getString(c.getColumnIndex(DBHelper.C_ID)); //TODO : voir pourquoi getInt fonctionne pas
            String favorite = c.getString(c.getColumnIndex(DBHelper.C_FAVORITE));
            CheckBox myBtn = (CheckBox)v.findViewById(R.id.starButton);
            myBtn.setChecked(true); //TODO vérifier pour que l'étoile soit toujours allumée
            int compteur_favoris = 0;

            Log.d("avant sel favorite ", favorite + " id : " + id);

            if(favorite.contains("1")){
                //ID dans favoris.xml
                TextView titleV = (TextView) v.findViewById(R.id.titleV);
                TextView startT = (TextView) v.findViewById(R.id.startTV);
                TextView stopT = (TextView)v.findViewById(R.id.stopTV);
                TextView location = (TextView) v.findViewById(R.id.locationV);
                //étoile sur évenement

                //selection des favoris dans la bdd
                Log.d("selection favorite ", favorite + " id : " + id);
                titleV.setText(c.getString(c.getColumnIndex(DBHelper.C_TITLE)) + " id supposé : " + id);
                location.setText(c.getString(c.getColumnIndex(DBHelper.C_LOCATION)));
                startT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_START)));
                stopT.setText(c.getString(c.getColumnIndex(DBHelper.C_DATE_STOP)));
            }

            // Gestion de l'étoile de l'événement

            myBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id = ((Integer)buttonView.getTag()).intValue();
                    //Toast.makeText(getApplicationContext(), "Retiré des favoris ", Toast.LENGTH_SHORT).show();
                    Log.d("Listener", "Checked "+isChecked+" " + " "+ id  );
                    dbh.changeFavoriteStatus(db, id);//(int)getItemId(pos)

                    //Rafraîchir l'activité pour mettre à jour la listView
                    finish();
                    startActivity(getIntent());

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