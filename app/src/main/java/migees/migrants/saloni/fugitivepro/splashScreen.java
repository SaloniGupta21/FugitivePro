package migees.migrants.saloni.fugitivepro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.applovin.sdk.AppLovinSdk;

import migees.migrants.saloni.fugitivepro.R;


public class splashScreen extends AppCompatActivity{
    private static int SPLASH_TIME_OUT = 3000;

    // Connection detector class
    ConnectivityReceiver cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
      /*
        AppLovinSdk.initializeSdk(getApplicationContext());

        final AppLovinSdk sdk = AppLovinSdk.getInstance(getApplicationContext());
        sdk.getSettings().setTestAdsEnabled( true );
      */

        ///splash = findViewById(R.id.splash);
      checkConnection();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(splashScreen.this, weatherConditions.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }


    private void checkConnection() {
        cd = new ConnectivityReceiver(getApplicationContext());
       showSnack(cd.isConnectingToInternet());
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.splash), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


}
