package biln.notreappeventful3;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by boris on 3/25/15.
 */
public class MenuActivities extends ActionBarActivity {

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menuShowFavorites) {

            Toast.makeText(getApplicationContext(), "Consultons nos favoris", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
