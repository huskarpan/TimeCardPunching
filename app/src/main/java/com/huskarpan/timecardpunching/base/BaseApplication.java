package com.huskarpan.timecardpunching.base;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Huskar on 2015/9/22.
 *
 * @description 基类应用程序，可在其中配置信息
 */
public class BaseApplication extends Application {
    public BaseApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
