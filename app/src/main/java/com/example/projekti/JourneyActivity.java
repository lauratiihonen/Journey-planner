package com.example.projekti;

import static com.example.projekti.MainActivity.calendarDate;
import static com.example.projekti.MainActivity.departure;
import static com.example.projekti.MainActivity.destination;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class JourneyActivity extends AppCompatActivity {

    private RequestQueue queue;
    private static String journeyText = "";
    private TextView mJourneyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Piilottaa sovelluksen yläpalkin
        getSupportActionBar().hide();
        setContentView(R.layout.journey_layout);

        mJourneyTextView = findViewById(R.id.journeyTextView);

        //Otetaan huomioon aktiviteetin elinkaari. Jos aiempaa dataa, otetaan se käyttöön
        if(savedInstanceState!= null) {
            //bundlessa dataa
            String lokimerkkijono = savedInstanceState.getString("LOG_DATA");
            mJourneyTextView.append(lokimerkkijono);
        }

        //Alustetaan jono
        queue = Volley.newRequestQueue(this);
        getJourneyForCity();
    }

    //Haetaan tiedot palvelimelta käyttäen parametreja
    public void getJourneyForCity() {

        //live-trains/station/<departure_station_code>/<arrival_station_code>?departure_date=<departure_date>&startDate=<startDate>&endDate=<endDate>&limit=<limit>
        String url = ("https://rata.digitraffic.fi/api/v1/live-trains/station/" + departure + "/" + destination + "?departure_date=" + calendarDate +"&limit=" + 20);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    parseJsonAndUpdateUI(response);
                },
                error -> {
                }
        );

        // Lisätään pyyntö jonoon
        queue.add(stringRequest);

    }

    //Haetaan tietystä paikasta dataa
    private void parseJsonAndUpdateUI(String response) {
        try {

            journeyText = "";
            //Kaivetaan dataa JSON objektista
            JSONArray rootObject = new JSONArray(response);
            for(int x = 0;x<20; x++) {

                String departureTime = rootObject.getJSONObject(x).getJSONArray("timeTableRows").getJSONObject(0).getString("scheduledTime");
                String arrivalTime = rootObject.getJSONObject(x).getJSONArray("timeTableRows").getJSONObject(89).getString("scheduledTime");

            String[] departureWords = departureTime.split("T || .");
            String dTime = "";


                for (int i = 12; i < 17; i++) {
                    dTime += departureWords[i];
                }

                String[] arrivalWords = arrivalTime.split("T || .");
                String aTime = "";

                for (int i = 12; i < 17; i++) {
                    aTime += arrivalWords[i];
                }

                journeyText += (getString(R.string.departureText) + "\n" +  dTime + "\n" + getString(R.string.arrivalText) + "\n" + aTime + "\n\n");

            }
            //Laitetaan data näytölle
            TextView weatherDescriptionTextView = findViewById(R.id.journeyTextView);
            weatherDescriptionTextView.setText(calendarDate + "\n\n" + journeyText);

            }
        catch(JSONException e){
                e.printStackTrace();

            }
        }

    //Tallennetaan datan tila
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String logs = mJourneyTextView.getText().toString();
        outState.putString("LOG_DATA", logs);
    }
}
