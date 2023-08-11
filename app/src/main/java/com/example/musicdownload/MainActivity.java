package com.example.musicdownload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdSize;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        Ads ads = new Ads(context);
        ads.initAds();
        LinearLayout linearLayout = findViewById(R.id.banner_main);
        ads.loadBanner(linearLayout, AdSize.LARGE_BANNER);
        ads.loadInterstitial();





        EditText editText = findViewById(R.id.edittext_search);
        String string = editText.getText().toString();
        Button searchButton = findViewById(R.id.button_search);
        searchButton.setOnClickListener(v -> {
                ads.showInterstitial(MainActivity.this);
                Intent intent = new Intent(context, DisplayActivity.class);
                intent.putExtra("name", string);
                startActivity(intent);
        });
        Button allButton  = findViewById(R.id.button_all);
        allButton.setOnClickListener(view -> {
            ads.showInterstitial(MainActivity.this);
            Intent intent = new Intent(context, DisplayActivity.class);
            intent.putExtra("name", "");
            startActivity(intent);
        });



    }
}