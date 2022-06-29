package com.example.musicplayer;

import android.app.Application;
import android.content.Context;

import com.example.musicplayer.preference.PreferenceUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App extends Application {

    public final Executor sIOExecutor = Executors.newSingleThreadExecutor();

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //上传操作就是在应用重启之后执行
//        CrashHandler.getInstance().init(this,new CrashNetHelper());

        initPreferenceUtils();
    }

    public static App sInstance;

    public static App getInstance(){
        return sInstance;
    }

    private void initPreferenceUtils() {
        try {
            Constructor<PreferenceUtils> method = PreferenceUtils.class.getDeclaredConstructor(Context.class);
            boolean isAccessible = method.isAccessible();
            method.setAccessible(true);
            method.newInstance(this);
            if (!isAccessible) {
                method.setAccessible(false);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
