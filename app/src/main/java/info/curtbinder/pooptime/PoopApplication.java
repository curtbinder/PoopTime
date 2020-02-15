package info.curtbinder.pooptime;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class PoopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
