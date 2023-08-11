package com.example.musicdownload;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdSize;

public class DisplayActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Context context = getApplicationContext();
        Ads ads = new Ads(context);
        LinearLayout adContainer = findViewById(R.id.banner_display);
        ads.loadBanner(adContainer, AdSize.BANNER);
        ads.loadInterstitial();
        Bundle bundle = getIntent().getExtras();
        String songName = bundle.getString("name");
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        ProgressBar progressBar = new ProgressBar(context);
        LinearLayout root = findViewById(R.id.linear_root);
        LinearLayout linearLayout = findViewById(R.id.progress_bar);
        linearLayout.addView(progressBar);
        progressBar.setIndeterminate(true);



        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {

            try {
                dbHelper.insertData();

            }catch (Exception e){
                e.printStackTrace();
            }

            handler.post(() -> {
                ads.showInterstitial(DisplayActivity.this);
                ItemAdapter itemAdapter = new ItemAdapter(context, dbHelper.readData(songName));
                recyclerView.setAdapter(itemAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                root.removeView(linearLayout);

            });

        }).start();




    }


}