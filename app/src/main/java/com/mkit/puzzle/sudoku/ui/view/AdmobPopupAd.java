package com.mkit.puzzle.sudoku.ui.view;

import android.app.Activity;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by Admin on 12/17/2017.
 */

public class AdmobPopupAd {
    static InterstitialAd interstitial;
    private static boolean isLastFailed = false;
    public interface InterstitialAdListener {
        void onLoaded();

        void onFailed();

        void onClosed();
    }

    private static InterstitialAdListener interstitialAdListener = null;
    public AdmobPopupAd(Activity activity)
    {
        interstitial = new InterstitialAd(activity);
        interstitial.setAdUnitId(activity.getString(R.string.admob_popup_id));
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAdListener != null) interstitialAdListener.onLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if (interstitialAdListener != null) interstitialAdListener.onFailed();
                isLastFailed = true;
            }

            @Override
            public void onAdClosed() {
                if (interstitialAdListener != null) interstitialAdListener.onClosed();
                loadInterstitial();
            }
        });

    }

    public static void loadInterstitial() {
        isLastFailed = false;
        Runnable run = new Runnable() {

            @Override
            public void run() {
                interstitial.loadAd(new AdRequest.Builder().build());
            }
        };
        getHandler().post(run);
    }

    public static boolean isInterstitialAdLoaded()
    {
        return interstitial.isLoaded();
    }

    public static boolean isLastFailed()
    {
        return isLastFailed;
    }


    public static void showInterstitial() {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        };
        getHandler().post(run);
    }

    private static Handler handler;

    private static Handler getHandler() {
        if (handler == null) handler = new Handler();
        return handler;
    }

    public static void setInterstitialAdListener(InterstitialAdListener interstitialAdListener_) {
        interstitialAdListener = interstitialAdListener_;
    }
}
