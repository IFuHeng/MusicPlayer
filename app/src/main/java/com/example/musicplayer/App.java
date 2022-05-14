package com.example.musicplayer;

import android.app.Application;

import com.example.musicplayer.base.CrashHandler;
import com.example.musicplayer.base.CrashNetHelper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //上传操作就是在应用重启之后执行
//        CrashHandler.getInstance().init(this,new CrashNetHelper());
    }
}
