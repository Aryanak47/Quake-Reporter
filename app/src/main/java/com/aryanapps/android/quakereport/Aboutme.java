package com.aryanapps.android.quakereport;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Aboutme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

    }
    public void onInstaClick(View view){

        String instalink="https://www.instagram.com/aryan____.____/";
        Uri uri=Uri.parse(instalink);
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void onFacebookClick(View view){
        String fblink="https://www.facebook.com/aryan.bimaliofficial?fref=search&__tn__=%2Cd%2CP-R&eid=ARBeiconPKGvDPuqFyy5Gbt9moYsux4q2TsG4oPfbHR7TKrnftI3szp5Nt1Q48zHKO56a_GIztgkq27D";
        Uri uri=Uri.parse(fblink);
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
