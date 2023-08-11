package com.example.musicdownload;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Ads {
    Context context;
    InterstitialAd mInterstitialAd;

    public Ads(Context context) {
        this.context = context;
    }

    public void initAds(){
        MobileAds.initialize(context);
    }




    public void loadBanner(LinearLayout linearLayout, AdSize adSize){
            AdView adView = new AdView(context);
            adView.setAdUnitId(context.getString(R.string.admob_banner_id));
            adView.setAdSize(adSize);
            linearLayout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

    }


    public void loadInterstitial(){
           AdRequest adRequest = new AdRequest.Builder().build();
           InterstitialAd.load(context, context.getString(R.string.admob_interstitial_id), adRequest, new InterstitialAdLoadCallback() {
               @Override
               public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                   mInterstitialAd = interstitialAd;
               }
           });

    }

    public void showInterstitial(Activity activity){
        if (mInterstitialAd!=null){
            mInterstitialAd.show(activity);
        }
    }

}
