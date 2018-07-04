package com.example.android.ckho;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SpashScreen extends Activity {
    boolean firstStart;
    SharedPrefApp sharedPref;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        sharedPref = SharedPrefApp.getInstance();

       /* for(int i=0;i<1000;i++)
        {
        }*/


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences settings = getSharedPreferences("PREFS",0);
                firstStart = settings.getBoolean("firs_time_start", true);

                if(sharedPref.getISLogged_IN(SpashScreen.this))
                {
                    startActivity(new Intent(SpashScreen.this,Login.class));//change to main class after saving login info
                }
                else
                {
                    startActivity(new Intent(SpashScreen.this,Login.class));
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}