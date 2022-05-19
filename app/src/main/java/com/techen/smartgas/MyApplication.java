package com.techen.smartgas;

import android.app.Application;

import com.itheima.retrofitutils.ItheimaHttp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ItheimaHttp.init(this, "https://www.oschina.net/");
    }
}
