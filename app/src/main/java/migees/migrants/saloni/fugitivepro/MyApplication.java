package migees.migrants.saloni.fugitivepro;

import android.app.Application;
import com.google.android.gms.ads.MobileAds;

import migees.migrants.saloni.fugitivepro.R;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the AdMob app
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}