package com.mkit.puzzle.sudoku.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mkit.puzzle.sudoku.controller.GameController;
import com.mkit.puzzle.sudoku.controller.GameStateManager;
import com.mkit.puzzle.sudoku.controller.SaveLoadStatistics;
import com.mkit.puzzle.sudoku.controller.Symbol;
import com.mkit.puzzle.sudoku.controller.helper.GameInfoContainer;
import com.mkit.puzzle.sudoku.game.GameDifficulty;
import com.mkit.puzzle.sudoku.game.GameType;
import com.mkit.puzzle.sudoku.game.listener.IGameSolvedListener;
import com.mkit.puzzle.sudoku.game.listener.ITimerListener;
import com.mkit.puzzle.sudoku.ui.listener.IHintDialogFragmentListener;
import com.mkit.puzzle.sudoku.ui.listener.IResetDialogFragmentListener;
import com.mkit.puzzle.sudoku.ui.view.AdmobPopupAd;
import com.mkit.puzzle.sudoku.ui.view.R;
import com.mkit.puzzle.sudoku.ui.view.SudokuFieldLayout;
import com.mkit.puzzle.sudoku.ui.view.SudokuKeyboardLayout;
import com.mkit.puzzle.sudoku.ui.view.SudokuSpecialButtonLayout;
import com.mkit.puzzle.sudoku.ui.view.WinDialog;

import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

public class GameActivity extends BaseActivity implements IGameSolvedListener ,ITimerListener, IHintDialogFragmentListener, IResetDialogFragmentListener {

    GameController gameController;
    SudokuFieldLayout layout;
    SudokuKeyboardLayout keyboard;
    SudokuSpecialButtonLayout specialButtonLayout;
    TextView timerView;
    TextView viewName ;
    RatingBar ratingBar;
    private boolean gameSolved = false;
    SaveLoadStatistics statistics = new SaveLoadStatistics(this);
    WinDialog dialog = null;
    private BottomSheetDialog bottomSheetDialog;
    private String gamelevel = "";
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(gameSolved) {
            gameController.pauseTimer();
        } else {
            // start the game
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameController.startTimer();
                }
            }, MAIN_CONTENT_FADEIN_DURATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initControls(savedInstanceState);
        addBottomMenu();
    }

    private void addBottomMenu() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottom_menu);
        viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bottomSheetDialog.isShowing()){
                    bottomSheetDialog.show();
                }
            }
        });

        LinearLayout viewDailyQuest = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewDailyQuest);
        LinearLayout viewEasyQuest = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewEasyQuest);
        LinearLayout viewNormalQuest = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewNormalQuest);
        LinearLayout viewHardQuest = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewHardQuest);
        LinearLayout viewExpertQuest = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewExpertQuest);
        LinearLayout viewReset = (LinearLayout) bottomSheetDialog.findViewById(R.id.viewReset);

        viewDailyQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewEasyQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameActivity.this, GameActivity.class);
                mainIntent.putExtra("gameType", "Default_9x9");
                mainIntent.putExtra("gameDifficulty", getString(R.string.difficulty_moderate));
                mainIntent.putExtra("GAME_LEVEL", getString(R.string.str_lv_easy));
                finish();
                startActivity(mainIntent);
            }
        });

        viewNormalQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameActivity.this, GameActivity.class);
                mainIntent.putExtra("gameType", "Default_9x9");
                mainIntent.putExtra("gameDifficulty", getString(R.string.difficulty_moderate));
                mainIntent.putExtra("GAME_LEVEL", getString(R.string.str_lv_normal));
                finish();
                startActivity(mainIntent);
            }
        });

        viewHardQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameActivity.this, GameActivity.class);
                mainIntent.putExtra("gameType", "Default_9x9");
                mainIntent.putExtra("gameDifficulty", getString(R.string.difficulty_hard));
                mainIntent.putExtra("GAME_LEVEL", getString(R.string.str_lv_hard));
                finish();
                startActivity(mainIntent);
            }
        });

        viewExpertQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameActivity.this, GameActivity.class);
                mainIntent.putExtra("gameType", "Default_9x9");
                mainIntent.putExtra("gameDifficulty", getString(R.string.difficulty_challenge));
                mainIntent.putExtra("GAME_LEVEL", getString(R.string.str_lv_expert));
                finish();
                startActivity(mainIntent);
            }
        });

        viewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameActivity.this, GameActivity.class);
                mainIntent.putExtra("gameType", "Default_9x9");
                mainIntent.putExtra("gameDifficulty", getIntent().getExtras().getString("gameDifficulty", GameDifficulty.Moderate.name()));
                mainIntent.putExtra("GAME_LEVEL", getIntent().getExtras().getString("GAME_LEVEL"));
                finish();
                startActivity(mainIntent);
            }
        });
    }

    private void initControls(Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPref.getBoolean("pref_keep_screen_on", true)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        GameType gameType = GameType.Unspecified;
        GameDifficulty gameDifficulty = GameDifficulty.Unspecified;
        int loadLevelID = 0;
        boolean loadLevel = false;

        if(savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                gameType = GameType.valueOf(extras.getString("gameType", GameType.Default_9x9.name()));
                gameDifficulty = GameDifficulty.valueOf(extras.getString("gameDifficulty", GameDifficulty.Moderate.name()));
                gamelevel = extras.getString("gameDifficulty", GameDifficulty.Moderate.name());
                loadLevel = extras.getBoolean("loadLevel", false);
                if (loadLevel) {
                    loadLevelID = extras.getInt("loadLevelID");
                }
            }

            gameController = new GameController(sharedPref, getApplicationContext());

            List<GameInfoContainer> loadableGames = GameStateManager.getLoadableGameList();

            if (loadLevel && loadableGames.size() > loadLevelID) {
                // load level from GameStateManager
                gameController.loadLevel(loadableGames.get(loadLevelID));
            } else {
                // load a new level
                gameController.loadNewLevel(gameType, gameDifficulty);
            }
        } else {
            gameController = savedInstanceState.getParcelable("gameController");
            // in case we get the same object back
            // because parceling the Object does not always parcel it. Only if needed.
            if(gameController != null) {
                gameController.removeAllListeners();
                gameController.setContextAndSettings(getApplicationContext(), sharedPref);
            } else {
                // Error: no game could be restored. Go back to main menu.
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
            gameSolved = savedInstanceState.getInt("gameSolved") == 1;
        }


        setContentView(R.layout.activity_game_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.addView();


        //Create new GameField
        layout = (SudokuFieldLayout)findViewById(R.id.sudokuLayout);
        gameController.registerGameSolvedListener(this);
        gameController.registerTimerListener(this);
        statistics.setGameController(gameController);

        layout.setSettingsAndGame(sharedPref, gameController);

        //set KeyBoard
        keyboard = (SudokuKeyboardLayout) findViewById(R.id.sudokuKeyboardLayout);
        keyboard.removeAllViews();
        keyboard.setGameController(gameController);
        //keyboard.setColumnCount((gameController.getSize() / 2) + 1);
        //keyboard.setRowCount(2);
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);

        // set keyboard orientation
        int orientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.HORIZONTAL : LinearLayout.VERTICAL;

        keyboard.setKeyBoard(gameController.getSize(), p.x,layout.getHeight()-p.y, orientation);


        //set Special keys
        specialButtonLayout = (SudokuSpecialButtonLayout) findViewById(R.id.sudokuSpecialLayout);
        specialButtonLayout.setButtons(p.x, gameController, keyboard, getFragmentManager(), orientation);

        //set TimerView
        timerView = (TextView)findViewById(R.id.timerView);

        // set game level view
        TextView tvLevel = (TextView)findViewById(R.id.tvLevel);
        switch ( getIntent().getExtras().getString("GAME_LEVEL")){
            case "LV_EASY":
                tvLevel.setText("Dễ");
                break;
            case "LV_NORMAL":
                tvLevel.setText("Vừa");
                break;
            case "LV_HARD":
                tvLevel.setText("Khó");
                break;
            case "LV_EXPERT":
                tvLevel.setText("Chuyên gia");
                break;
        }
        //set GameName
        viewName = (TextView) findViewById(R.id.gameModeText);

        //set Rating bar
        List<GameDifficulty> difficutyList = GameDifficulty.getValidDifficultyList();
        int numberOfStarts = difficutyList.size();
        ratingBar = (RatingBar) findViewById(R.id.gameModeStar);
        ratingBar.setMax(numberOfStarts);
        ratingBar.setNumStars(numberOfStarts);
        ratingBar.setRating(difficutyList.indexOf(gameController.getDifficulty()) + 1);
        ((TextView)findViewById(R.id.difficultyText)).setText(getString(gameController.getDifficulty().getStringResID()));




        if(gameSolved) {
            layout.setEnabled(false);
            keyboard.setButtonsEnabled(false);
            specialButtonLayout.setButtonsEnabled(false);
        }

        gameController.notifyHighlightChangedListeners();
        gameController.notifyTimerListener(gameController.getTime());

        // run this so the error list gets build again.
        gameController.onModelChange(null);

        overridePendingTransition(0, 0);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.ad_banner_container);
        if(layout != null) {
            adView = new AdView(this);
            adView.setAdUnitId(getString(R.string.admob_banner_id));
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (adView.getParent() == null)
                        layout.addView(adView);
                }
            });
            adView.loadAd(new AdRequest.Builder().build());
        }
        //
    }

    private AdView adView;

    @Override
    public void onPause(){
        super.onPause();
        if(adView != null)adView.pause();
        if(!gameSolved) {
            gameController.saveGame(this);
        }
        gameController.deleteTimer();
    }
    @Override
    public void onResume(){
        super.onResume();
        if(adView != null)adView.resume();
        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        gameController.initTimer();

        if(!gameSolved) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameController.startTimer();
                }
            }, MAIN_CONTENT_FADEIN_DURATION);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Symbol s;
        try {
            s = Symbol.valueOf(sharedPref.getString("pref_symbols", Symbol.Default.name()));
        } catch(IllegalArgumentException e) {
            s = Symbol.Default;
        }
        layout.setSymbols(s);
        keyboard.setSymbols(s);
    }


    @Override
    public void onBackPressed() {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_view, menu);
        return true;
    }*/



    @Override
    public void onSolved() {
        gameSolved = true;

        gameController.pauseTimer();
        gameController.deleteGame(this);

        //Show time hints new plus old best time

        statistics.saveGameStats();

        boolean isNewBestTime = gameController.getUsedHints() == 0
                && statistics.loadStats(gameController.getGameType(),gameController.getDifficulty()).getMinTime() >= gameController.getTime();

        dialog = new WinDialog(this, R.style.WinDialog , timeToString(gameController.getTime()), String.valueOf(gameController.getUsedHints()), isNewBestTime);

        dialog.getWindow().setContentView(R.layout.win_screen_layout);
        //dialog.setContentView(getLayoutInflater().inflate(R.layout.win_screen_layout,null));
        //dialog.setContentView(R.layout.win_screen_layout);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        //((TextView)dialog.findViewById(R.id.win_hints)).setText(gameController.getUsedHints());
        //((TextView)dialog.findViewById(R.id.win_time)).setText(timeToString(gameController.getTime()));

        dialog.show();

        final Activity activity = this;
        ((Button)dialog.findViewById(R.id.win_continue_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                activity.finish();
            }
        });
        ((Button)dialog.findViewById(R.id.win_showGame_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        layout.setEnabled(false);
        keyboard.setButtonsEnabled(false);
        specialButtonLayout.setButtonsEnabled(false);
    }

    public String timeToString(int time) {
        int seconds = time % 60;
        int minutes = ((time - seconds) / 60) % 60;
        int hours = (time - minutes - seconds) / (3600);
        String h, m, s;
        s = (seconds < 10) ? "0" + String.valueOf(seconds) : String.valueOf(seconds);
        m = (minutes < 10) ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
        h = (hours < 10) ? "0" + String.valueOf(hours) : String.valueOf(hours);
        return h + ":" + m + ":" + s;
    }


    @Override
    public void onTick(int time) {

        // display the time
        timerView.setText(timeToString(time));

        if(gameSolved) return;
        // save time
        gameController.saveGame(this);
    }

    @Override
    public void onHintDialogPositiveClick() {
        gameController.hint();
    }

    @Override
    public void onResetDialogPositiveClick() {
        gameController.resetLevel();
    }

    @Override
    public void onDialogNegativeClick() {
        // do nothing
    }

    public static class ResetConfirmationDialog extends DialogFragment {

        LinkedList<IResetDialogFragmentListener> listeners = new LinkedList<>();

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // Verify that the host activity implements the callback interface
            if(activity instanceof IResetDialogFragmentListener) {
                listeners.add((IResetDialogFragmentListener) activity);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.reset_confirmation)
                    .setPositiveButton(R.string.reset_confirmation_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            for(IResetDialogFragmentListener l : listeners) {
                                l.onResetDialogPositiveClick();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        savedInstanceState.putParcelable("gameController", gameController);
        savedInstanceState.putInt("gameSolved", gameSolved ? 1 : 0);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if(adView != null)adView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
}
