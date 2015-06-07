package com.luojunrong;

import android.app.Application;


public class NewsApplication extends Application {
    private static NewsApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static NewsApplication getInstance(){
        return app;
    }
}
