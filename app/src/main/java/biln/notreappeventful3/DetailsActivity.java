package biln.notreappeventful3;

import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * Created by Boris on 2015-03-25.
 */
public class DetailsActivity extends MyMenu {

    TextView title;
    TextView location;
    TextView startT;
    TextView stopT;
    TextView description;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_complete_description2);



        title = (TextView)findViewById(R.id.title);
        location = (TextView)findViewById(R.id.location);
        startT = (TextView)findViewById(R.id.startT);
        stopT = (TextView)findViewById(R.id.stopT);
        description = (TextView)findViewById(R.id.description);


        Bundle b = getIntent().getExtras();

        title.setText(b.getString("title"));
        location.setText(b.getString("location"));
        startT.setText(b.getString("startT"));
        stopT.setText(b.getString("stopT"));


        Document doc = Jsoup.parse(b.getString("description"));


        description.setText(doc.body().text());





    }



}
