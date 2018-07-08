package com.mkit.puzzle.sudoku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.mkit.puzzle.sudoku.ui.view.AdmobPopupAd;
import com.mkit.puzzle.sudoku.ui.view.R;

/**
 * Created by yonjuni on 22.10.16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this,getString(R.string.app_id));
        new AdmobPopupAd(this);
        AdmobPopupAd.loadInterstitial();
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

}