package biln.notreappeventful3;

/**
 * Created by Boris on 2015-03-26.
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Boris on 2015-03-26.
 */
public class MyMenu extends ActionBarActivity {


    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menuShowFavorites) {
            Toast.makeText(getApplicationContext(), "Consultons nos favoris", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, DetailsActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

