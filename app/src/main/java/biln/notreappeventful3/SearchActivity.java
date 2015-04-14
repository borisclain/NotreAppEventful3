package biln.notreappeventful3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by boris on 3/26/15.
 */
public class SearchActivity extends MyMenu implements View.OnClickListener, AdapterView.OnItemClickListener{

    Button concerts;
    Button cinema;
    Button education;

    Button today;
    Button thisWeekEnd;
    Button nextWeek;
    Button thisWeek;

    Button advSearch;

    ArrayList<String> categories;
    String time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search);

        concerts = (Button) findViewById(R.id.concerts);
        cinema = (Button) findViewById(R.id.cinema);
        education = (Button) findViewById(R.id.education);

        today = (Button) findViewById(R.id.today);
        thisWeekEnd = (Button) findViewById(R.id.thisWeekEnd);
        nextWeek = (Button) findViewById(R.id.nextWeek);
        thisWeek = (Button) findViewById(R.id.thisWeek);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.concerts){
            categories.add("music");
        }
        else if (view.getId() == R.id.cinema) {
            categories.add("movies_film");
        }
        else if (view.getId() == R.id.education) {
            categories.add("learning_education");
        }

        else if (view.getId() == R.id.today) {
            time = "Today";
        }
        else if (view.getId() == R.id.thisWeekEnd){
            time = "This+Weekend";
        }
        else if (view.getId() == R.id.nextWeek){
            time = "Next+Week";
        }
        else if (view.getId() == R.id.thisWeek){
            time = "This+Week";
        }
        else if (view.getId() == R.id.advSearch){
            Intent intent = new Intent(this, searchResults.class); //TODO : SearchResults
            Bundle b = new Bundle();

            b.putStringArrayList("categories", categories);
            b.putString("time", time);

            intent.putExtras(b);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
