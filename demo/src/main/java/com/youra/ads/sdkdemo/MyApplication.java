package com.youra.ads.sdkdemo;

import static com.youra.ads.sdk.util.Constant.ADMOB;
import static com.youra.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.youra.ads.sdk.format.AppOpenAdManager;
import com.youra.ads.sdk.format.AppOpenAdMob;
import com.youra.ads.sdk.util.OnShowAdCompleteListener;
import com.youra.ads.sdkdemo.database.AdsSharedPreferences;
import com.youra.ads.sdkdemo.utilis.Ads;
import com.youra.ads.sdkdemo.utilis.Constant;

public class MyApplication extends Application implements ActivityLifecycleCallbacks, LifecycleObserver {

    private static MyApplication mInstance;
    private AppOpenAdMob appOpenAdMob;
    private AppOpenAdManager appOpenAdManager;
    Activity currentActivity;
    private AdsSharedPreferences sharedPreference;

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdMob = new AppOpenAdMob();
        appOpenAdManager = new AppOpenAdManager();
        sharedPreference = new AdsSharedPreferences(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        if (sharedPreference.getValueString(Ads.AD_NETWORK).equals(ADMOB)) {
            appOpenAdMob.showAdIfAvailable(currentActivity, Constant.ADMOB_APP_OPEN_AD_ID);
        } else if (sharedPreference.getValueString(Ads.AD_NETWORK).equals(GOOGLE_AD_MANAGER)) {
            appOpenAdManager.showAdIfAvailable(currentActivity, Constant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (sharedPreference.getValueString(Ads.AD_NETWORK).equals(ADMOB)) {
            if (!appOpenAdMob.isShowingAd) {
                currentActivity = activity;
            }
        } else if (sharedPreference.getValueString(Ads.AD_NETWORK).equals(GOOGLE_AD_MANAGER)) {
            if (!appOpenAdManager.isShowingAd) {
                currentActivity = activity;
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener, AdsSharedPreferences prefe) {
        if (prefe.getValueString(Ads.AD_NETWORK).equals(ADMOB)) {
            appOpenAdMob.showAdIfAvailable(activity, prefe.getValueString(Ads.UnityCode.OPEN_ID.getCodeAdmob()), onShowAdCompleteListener);
        } else if (prefe.getValueString(Ads.AD_NETWORK).equals(GOOGLE_AD_MANAGER)) {
            appOpenAdManager.showAdIfAvailable(activity, prefe.getValueString(Ads.UnityCode.OPEN_ID.getCodeGAM()), onShowAdCompleteListener);
        }
    }

}
