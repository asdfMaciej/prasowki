package io.maciej01.prasowki.activity;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MyApplication extends Application {
    public Resources resources = getResources();
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Resources getRes() {
        return this.resources;
    }

}
