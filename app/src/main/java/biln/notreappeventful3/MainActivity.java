package biln.notreappeventful3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
MainActivity est l'activité principale de l'app. Elle correspond à l'écran d'accueil que
l'utilisateur verra après la première connexion.

Test de commit

 */


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button buttonSearch;
    Button buttonConsult;

//String

    String title;
    String type;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSearch = (Button)findViewById(R.id.button1);
        buttonConsult = (Button)findViewById(R.id.button2);

        buttonSearch.setOnClickListener(this);
        buttonConsult.setOnClickListener(this);
    }

    protected void onResume(){
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        SharedPreferences.Editor edit = prefs.edit();
        if(!previouslyStarted){
            Toast.makeText(this, "Premiere fois", Toast.LENGTH_LONG).show();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            Intent i = new Intent(this, WelcomingActivity.class);
            startActivity(i);

        }


        /*
        Les deux lignes suivantes s'occupent de vider les préférences. En vidant les préférances,
        on aura l'écran de première connexion à chaque exécution de l'app.
         */
        //TODO: À enlever quand on sera content de l'écran de première connexion
        edit.clear() ;
        edit.commit();


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button1 :

                Toast.makeText(this, "Clic bien reçu!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, SecondActivity.class);
                startActivity(i);
                break;

            case R.id.button2 :
                //TODO Implanter la consultation des événements sauvegardés
                Toast.makeText(this, "Consultation des événements sauvegardés pas encore disponible", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }









}


