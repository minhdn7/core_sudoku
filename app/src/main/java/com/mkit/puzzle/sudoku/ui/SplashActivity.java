package com.mkit.puzzle.sudoku.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.mkit.puzzle.sudoku.controller.NewLevelManager;
import com.mkit.puzzle.sudoku.controller.repository.TinyDB;
import com.mkit.puzzle.sudoku.game.GameDifficulty;
import com.mkit.puzzle.sudoku.game.GameType;
import com.mkit.puzzle.sudoku.ui.view.AdmobPopupAd;
import com.mkit.puzzle.sudoku.ui.view.R;

/**
 * Created by yonjuni on 22.10.16.
 */

public class SplashActivity extends AppCompatActivity {
    public TinyDB tinyDB;

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(this);

        MobileAds.initialize(this,getString(R.string.app_id));
        new AdmobPopupAd(this);
        AdmobPopupAd.loadInterstitial();
//        tinyDB.putString("lastChosenGameType", "Default_9x9");
//        tinyDB.putString("lastChosenDifficulty", getString(R.string.difficulty_easy));
        configStartLevel();

    }

    private void configStartLevel() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        final NewLevelManager newLevelManager = NewLevelManager.getInstance(getApplicationContext(), settings);
        newLevelManager.checkAndRestock();
        final GameType gameType = GameType.getValidGameTypes().get(1);
        final GameDifficulty gameDifficulty = GameDifficulty.getValidDifficultyList().get(1);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("lastChosenGameType", gameType.name());
//        editor.putString("lastChosenDifficulty", gameDifficulty.name());
//        editor.apply();

        Intent mainIntent = new Intent(SplashActivity.this, GameActivity.class);
        mainIntent.putExtra("gameType", gameType.name());
        mainIntent.putExtra("gameDifficulty", gameDifficulty.name());
        mainIntent.putExtra("GAME_LEVEL", getString(R.string.str_lv_easy));

        if(!newLevelManager.isLevelLoadable(gameType, gameDifficulty)) {
            // save current setting for later
            newLevelManager.checkAndRestock();
            Toast t = Toast.makeText(getApplicationContext(), R.string.generating, Toast.LENGTH_SHORT);
            t.show();
            newLevelManager.loadFirstStartLevels();
        }
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

}