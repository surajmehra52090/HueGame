package me.ccrama.huegame;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainMenu extends AppCompatActivity {

    public static SharedPreferences scores;
    public static long lastGame;
    public TextView highScore;
    public long score = 0;
    public String lastGameID = "";
    public static boolean high;

    @Override
    protected void onResume() {
        super.onResume();

        //Need to save lastGameID in the game screen because this will reload when the game is paused
        lastGameID = scores.getString("lastGame", "");
        updateUI();

        if(lastGame != 0) {
            final long minute = TimeUnit.MILLISECONDS.toMinutes(lastGame);
            final long second = TimeUnit.MILLISECONDS.toSeconds(lastGame) - (60 * minute);

            new AlertDialog.Builder(this).setTitle(high ? "New highscore!" : "Game over!").setMessage(String.format("You survived %02d:%02d", minute, second))
                    .setPositiveButton("CLOSE", null).show();
        }
    }

    public void updateUI(){
        score = scores.getLong("highscore", 0);

        final long minute = TimeUnit.MILLISECONDS.toMinutes(score);
        final long second = TimeUnit.MILLISECONDS.toSeconds(score) - (60 * minute);
        if (minute > 0) {
            highScore.setText(String.format("Longest game %02d:%02d", minute, second));
        } else {
            highScore.setText(String.format("Longest game %02d:%02d", minute, second));
        }
    }
    float[] hsv;
    int runColor;
    int hue = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        scores = getSharedPreferences("scores", 0);
        highScore = (TextView)findViewById(R.id.highscore);

        hsv = new float[3]; // Transition color
        hsv[1] = 1;
        hsv[2] = 1;

        final View background = findViewById(R.id.background);

        ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
        anim.setDuration(10000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                hsv[0] = 360 * animation.getAnimatedFraction();
                runColor = Color.HSVToColor(hsv);
                background.setBackgroundColor(runColor);
            }
        });

        anim.setRepeatCount(Animation.INFINITE);

        anim.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        View newGame = findViewById(R.id.startNew);
        View settings = findViewById(R.id.settings);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                Intent i = new Intent(MainMenu.this, GameActivity.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                Intent i = new Intent(MainMenu.this, SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    public void resumeGame(String lastGameID){
        //todo this
    }

    public void newGame(){
        //todo this
    }

    public void openSettings(){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void exitGame(){
        this.finish();
        System.exit(0);
    }
}
