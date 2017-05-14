package io.maciej01.prasowki.activity;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.orm.SugarApp;

/**
 * Created by Maciej on 2017-05-14.
 */

public class MyApplication extends SugarApp {
    public boolean fetched = false;
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

    public boolean getFetched() {return this.fetched;}
    public void setFetched(boolean x) {this.fetched = x;}


}
