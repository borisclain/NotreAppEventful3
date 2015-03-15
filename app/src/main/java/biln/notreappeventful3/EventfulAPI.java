package biln.notreappeventful3;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Boris on 2015-03-10.
 */
public class EventfulAPI {

    String title;

    //Deux Strings non utilisees
    //String type;
    //String date;

    String erreur;

    String apiKey = "b5JxXhsHhJTW2mzP";

    public EventfulAPI() {
        erreur = null;

        String url = "http://api.eventful.com/json/events/search?app_key="+apiKey+"&keywords=books&location=Montreal&date=Future";
        Log.d("URL", url);

        try {
            HttpEntity page = getHttp(url);
            // Interprète la page retournée comme un fichier JSON encodé en UTF-8
            JSONObject js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));

            JSONObject events = js.getJSONObject("events");
            JSONArray event  = events.getJSONArray("event");

            JSONObject firstEventFound = event.getJSONObject(0);//prendre le premier event de la liste
            title = firstEventFound.getString("title");


        } catch (ClientProtocolException e) {
            erreur = "Erreur HTTP (protocole) :" + e.getMessage();
        } catch (IOException e) {
            erreur = "Erreur HTTP (IO) :" + e.getMessage();
        } catch (ParseException e) {
            erreur = "Erreur JSON (parse) :" + e.getMessage();
        } catch (JSONException e) {
            erreur = "Erreur JSON :" + e.getMessage();
        }


    }
    private HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(url);
        HttpResponse response = httpClient.execute(http);
        return response.getEntity();
    }

}


