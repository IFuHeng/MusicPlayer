package com.example.musicplayer.jni;

public class NativeObject {
    // Used to load the 'musicplayer' library on application startup.
    static {
        System.loadLibrary("musicplayer");
    }

    /**
     * A native method that is implemented by the 'musicplayer' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
