package com.example.mygroceryapps.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenAdManager implements LifecycleObserver ,Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenAdManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final MyApplication myApplication;
    private boolean isLoadingAd = false;
    private boolean isShowingAd = false;
    private Activity currentActivity;
    private long loadTime = 0;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        // Updating the currentActivity only when an ad is not showing.
        currentActivity = activity;
        }


    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity= activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}



    public AppOpenAdManager(MyApplication myApplication){
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver((LifecycleObserver) this);

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAdIFAvailable();
            }
        },2100);


    }

    public void fetchAd(){
        if ( isAdAvailable()) {
            return;
        }
        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                AppOpenAdManager.this.appOpenAd = ad;
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication,AD_UNIT_ID,request,AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,loadCallback);
    }

    private AdRequest getAdRequest(){
        return new AdRequest.Builder().build();
    }
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }
    private boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }
        public void showAdIFAvailable(){
        if(!isShowingAd && isAdAvailable()){
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenAdManager.this.appOpenAd = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        }else{
            fetchAd();
        }
        }

    }





