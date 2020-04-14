package com.aryanapps.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class settingActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        final EarthquakeActivity activity=new EarthquakeActivity();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(findViewById(R.id.frame)!=null){
            if (savedInstanceState!=null)
                return;
            //loading the fragment
            getFragmentManager().beginTransaction().replace(R.id.frame, new Mainfragment()).commit();
        }





    }


     //inner class which extends preferenceFragment
    public static class Mainfragment extends PreferenceFragment {
        //constants
        public static final String ORDER_KEY="ORDER_KEY";
         public static final String MIN_MAG_KEY="MAG_KEY";

        private  SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;



        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
             addPreferencesFromResource(R.xml.preference);
             //knowing the changes to update changes
             preferenceListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
                 @Override
                 public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                     Log.d("setting","changing");
                     if(key.equals(ORDER_KEY)){
                         Preference orderpref=findPreference(ORDER_KEY);
                         orderpref.setSummary("Ordering by "+sharedPreferences.getString(key,""));



                     }
                     else if(key.equals(MIN_MAG_KEY)){

                         Preference minmagpref=findPreference(MIN_MAG_KEY);
                         minmagpref.setSummary("The minumum magnitude to display is "+sharedPreferences.getString(key,""));

                     }




                 }
             };




        }

        @Override
        public void onResume() {
            super.onResume();
            //update the changes on screen
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceListener);
            //saving the changes
             Preference order_preference= findPreference(ORDER_KEY);
             order_preference.setSummary("Ordering by "+getPreferenceScreen().getSharedPreferences().getString(ORDER_KEY,""));

            Preference minmag_preference= findPreference(MIN_MAG_KEY);
            minmag_preference.setSummary("The minumum magnitude to display is "+getPreferenceScreen().getSharedPreferences().getString(MIN_MAG_KEY,""));


        }

        @Override
        public void onPause() {
            super.onPause();
            //update the changes on screen
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceListener);
        }
    }

}

