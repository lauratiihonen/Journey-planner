package com.example.projekti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CalendarView calendar;
    public static String destination;
    public static String departure;
    public static String calendarDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Piilottaa sovelluksen yläpalkin
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);

        //Alustetaan spinnerit
        Spinner spinner1 = findViewById(R.id.departureSpinner);
        Spinner spinner2 = findViewById(R.id.destinationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departure, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.destination, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        //Otetaan spinnereistä dataa
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                if (text.equals("Tampere")) {
                    departure = "TPE";
                } else if (text.equals("Helsinki")) {
                    departure = "HKI";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();

                if (text.equals("Tampere")) {
                    destination = "TPE";
                } else if (text.equals("Helsinki")) {
                    destination = "HKI";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Kuunnellaan kalenteriin valittu päivä
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if (dayOfMonth < 10) {
                    calendarDate = year + "-" + (month + 1) + "-0" + dayOfMonth;
                } else {
                    calendarDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                }
            }
        });
    }

    //Virhetarkistus ja jos kaikki ok, avataan aktiviteetti JourneyActivity
    public void getJourney(View view) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
           if (calendarDate != null) {
                if (!departure.equals(destination)) {
                    Intent openJourney = new Intent(this, JourneyActivity.class);
                    startActivity(openJourney);
                } else {
                    String error = getString(R.string.error);
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
            } else {
                String date_error = getString(R.string.date_error);
                Toast.makeText(this, date_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            String error = getString(R.string.network_error);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }


    }
}