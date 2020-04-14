package com.aryanapps.android.quakereport;/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

;

public class EarthquakeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {
    private RequestQueue queue;
    private JsonObjectRequest request;
    //constants here
    public static final String ORDER_KEYy = "ORDER_KEY";
    public static final String MIN_MAG_KEY = "MAG_KEY";
    final private String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2019-01-01&endtime=2030-01-01";
    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    //variables
    private String tempurl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&";
    private String permaurl;
    private String starttime;
    private String endtime;
    NavigationView navigationView;

    DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private JSONArray features;
    private ArrayList<Earthquakereport> earthquakes;
    private ListView earthquakeListView;
    private CustompageAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        progressBar = findViewById(R.id.progress);
        toolbar = findViewById(R.id.toolbar);
        //setting toolbar

        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opendrawer, R.string.closedrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        earthquakes = new ArrayList<>();
        if (earthquakes == null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        // Create list of earthquake locations.
        earthquakeListView = (ListView) findViewById(R.id.list);
        //fetching data from network
        doSomenetworking();


        // Find a reference to the {@link ListView} in the layout
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                try {

                    String url = features.getJSONObject(position).getJSONObject("properties").getString("url");
                    Log.d("Activity", url);
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void doSomenetworking() {
        String urll;
        //checking weather the url is null or not
        if (permaurl != null) {

            Log.d("Alu", permaurl);
            urll = permaurl;
        } else {
            urll = URL;
        }
        //getting value from sharepreference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String order = preferences.getString(ORDER_KEYy, "");
        String minMag = preferences.getString(MIN_MAG_KEY, "");

        Uri uri = Uri.parse(urll);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("limit", "10");
        builder.appendQueryParameter("minmag", minMag);
        builder.appendQueryParameter("orderby", order);
        String orglink = builder.toString();
        Log.d("org", orglink);

        queue = Volley.newRequestQueue(this.getBaseContext());

        // Request a jsonobject response from the provided URL.
        request = new JsonObjectRequest
                (Request.Method.GET, orglink, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        if (response != null) {
                            try {

                                features = response.getJSONArray("features");
                                check(features);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(request);

    }

    public void updateUi() {
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new CustompageAdapter(this, earthquakes);
        // Set the adapter on the listview
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    //freeing up resoucres when app is not active
    @Override
    protected void onStop() {
        super.onStop();
        if (request != null) {
            queue.cancelAll(request);
        }
    }

    //this method inflates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //calling datepicker method when search menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        datePicker();
        return super.onOptionsItemSelected(item);
    }

    //this method decide what to do when a item is selected from navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.link_id) {
            String url = "https://earthquake.usgs.gov/earthquakes/map/";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.setting) {
            Intent intent = new Intent(EarthquakeActivity.this, settingActivity.class);
            startActivity(intent);
        }
        else if (menuItem.getItemId() == R.id.Aboutme) {
            Intent intent = new Intent(EarthquakeActivity.this, Aboutme.class);
            startActivity(intent);
        }

        return true;
    }

    //this method pops up date picker to choose specific date when earthqauke took place
    private void datePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        dialog.show();


    }

    //this method buids new url when date is selected from date picker
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //getting time from datepicker
        starttime = i + "-" + i1 + "-" + i2;
        //getting current time
        endtime = Calendar.getInstance().get(Calendar.YEAR) + "-"
                + Calendar.getInstance().get(Calendar.MONTH) + "-" +
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //building url
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tempurl);
        stringBuilder.append("starttime=");
        stringBuilder.append(starttime);
        stringBuilder.append("&");
        stringBuilder.append("endtime=");
        stringBuilder.append(endtime);
        permaurl = stringBuilder.toString();
        doSomenetworking();


    }

    //this method extracts json response
    private void check(JSONArray jsonArray) {
        //clearing all the from arraylist if it has stored previous data
        earthquakes.clear();
        try {

            for (int i = 0; i < features.length(); i++) {

                Log.d(LOG_TAG, features.getJSONObject(i).getJSONObject("properties").get("place").toString());

                double mag = features.getJSONObject(i).getJSONObject("properties").getDouble("mag");

                String place = features.getJSONObject(i).getJSONObject("properties").getString("place");

                String url = features.getJSONObject(i).getJSONObject("properties").getString("url");

                long date = features.getJSONObject(i).getJSONObject("properties").getLong("time");

                earthquakes.add(new Earthquakereport(mag, place, date));
                updateUi();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //if drawer is open while pressing back button than close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
