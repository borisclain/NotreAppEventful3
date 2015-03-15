package biln.notreappeventful3;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Boris on 2015-03-10.
 */
public class SecondActivity extends ActionBarActivity {


    TextView text;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_rows);

        text = (TextView)findViewById(R.id.text);
        new SearchEventful().execute();

    }



    private class SearchEventful extends AsyncTask<String, String, EventfulAPI> {

        protected void onPreExecute() {

        }

        protected EventfulAPI doInBackground(String... params) {
            EventfulAPI web=new EventfulAPI();
            return web;
        }

        protected void onProgressUpdate(String... s) {

        }

        protected void onPostExecute(EventfulAPI web) {
            // On s'assure que l'objet de retour existe
            // et qu'il n'ait pas d'erreurs
            if( web == null ) {
                Toast.makeText(SecondActivity.this, getText(R.string.fatal_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if( web.erreur != null ) {
                Toast.makeText(SecondActivity.this, web.erreur, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(SecondActivity.this, "Requête réussie", Toast.LENGTH_SHORT).show();


            //On met à jour les données de recherche!
            text.setText(web.title);

        }

    }





}
