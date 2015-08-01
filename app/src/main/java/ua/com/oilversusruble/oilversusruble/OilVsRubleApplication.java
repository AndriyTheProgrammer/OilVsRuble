package ua.com.oilversusruble.oilversusruble;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by User on 11.05.2015.
 */
public class OilVsRubleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "AhosSceT32qMelN6BGJ8EHLv02WGzETHg47CEKrs", "oyjniEFtiKhQWMA8mdKrh3O7hxPIyPNV0uO0Svr6");
    }



}
