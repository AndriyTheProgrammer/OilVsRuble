package ua.com.oilversusruble.oilversusruble;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by User on 29.05.2015.
 */
public class Updater implements Runnable{
    public final int MILISECONDS_TIMEOUT = 5000;

    MainActivity mainActivity;
    Thread thread;

    boolean dontStop = true, running = false;

    Updater(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void startUpdater(){
        thread = new Thread(this);
        thread.start();
    }
    public void stopUpdater(){
        dontStop = false;

    }

    @Override
    public void run() {
        running = true;
        while (dontStop){
            try {
                Thread.sleep(MILISECONDS_TIMEOUT);
                mainActivity.update();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        running = false;
    }


}
