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
import java.util.ArrayList;

/**
 * Created by Boris on 2015-03-10.
 */
public class EventfulAPI {

    ArrayList<Event> eventsFound;                                          //peuplé par la recherche
    String apiKey = "b5JxXhsHhJTW2mzP";
    String url = "http://api.eventful.com/json/events/search?app_key="+apiKey;



    public EventfulAPI(){
        eventsFound = new ArrayList<Event>();
    }


    /**
     * Trouve les événements qui s'en viennent dans la ville donnée en paramètre
     *
     * @param city
     */
    public void getNextEvents(String city){
        String query = url+"&location="+city+"&sort_order=popularity"+"&page_size=100";
        //nécessaire de faire sort_order par popularity car sinon les pages d'Eventful sont folles
        getEvents(query);
    }


    /**
     * Méthode utilitaire locale
     *
     * @param query
     */

    private void getEvents(String query){
        int page_count = 0;
        try{
            HttpEntity page = getHttp(query);
            JSONObject js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
            page_count = Integer.parseInt(js.getString("page_count"));
        } catch (ClientProtocolException e) {
            Log.d("HTTP ","Erreur: "+e.getMessage());
        } catch (IOException e) {
            Log.d("Web ","Erreur: "+e.getMessage());
        } catch (ParseException e) {
            Log.d("Parse ","Erreur: "+e.getMessage());
        } catch (JSONException e) {
            Log.d("JSON ","Erreur: "+e.getMessage());
        }
        //int i = 0;
        int i =  page_count - 1; //TODO ! Juste pour abréger ici on demande une seule page
        while(i < page_count) {
            try {
                HttpEntity page = getHttp(query+"&page_number="+Integer.toString(i+1));
                JSONObject js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
                JSONObject events = js.getJSONObject("events");
                JSONArray event = events.getJSONArray("event");
                Log.d("WEB", "Nombre d'événements: " + event.length());

                for (int j = 0; j < event.length(); j++) {
                    JSONObject item = event.getJSONObject(j);
                    //Si la valeur sous "stop_time" est null
                    if (item.isNull("stop_time")) {
                        eventsFound.add(new Event(item.getString("id"),
                                                    item.getString("title"),
                                                        item.getString("start_time"),
                                                            "2020-01-01",//TODO Important
                                                                item.getString("city_name")));
                    } else {
                        eventsFound.add(new Event(item.getString("id"),
                                                    item.getString("title"),
                                                        item.getString("start_time"),
                                                            item.getString("stop_time"),
                                                                item.getString("city_name")));
                    }
                }

            } catch (ClientProtocolException e) {
                Log.d("HTTP ", "Erreur: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Web ", "Erreur: " + e.getMessage());
            } catch (ParseException e) {
                Log.d("Parse ", "Erreur: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("JSON ", "Erreur: " + e.getMessage());
            }
            i++;
        }
    }


    private HttpEntity getHttp(String myUrl) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(myUrl);
        HttpResponse response = httpClient.execute(http);
        return response.getEntity();
    }

}


