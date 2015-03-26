package biln.notreappeventful3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

/**
 * Created by Boris on 2015-03-14.
 */
public class ConfigurationActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    String sLat;
    String sLon;

    Button GoButton;
    EditText editT;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_on_first_opening);

        GoButton = (Button)findViewById(R.id.GoButton);
        editT = (EditText)findViewById(R.id.editT);
        GoButton.setOnClickListener(this);


        //TODO Ne fonctionne pas!
        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Toast.makeText(this, "On a détecté que vos coordonnées =" + sLat + " ; " + sLon, Toast.LENGTH_LONG).show();
        */
    }



    public void onClick(View v){
        SharedPreferences.Editor edit = Launcher.settings.edit();
        edit.putString("myCity", editT.getText().toString());
        edit.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }
    /*
    protected synchronized void buildGoogleApiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .addApi(LoctionServices.API)
               .build();
    }
    */

    @Override
    public void onConnected(Bundle connectionHint) {
        /*
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            sLat = ""+(mLastLocation.getLatitude());
            sLon = ""+(mLastLocation.getLongitude());
        }
        */
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Toast.makeText(this, "On a eu un probleme", Toast.LENGTH_LONG).show();
    }

}
