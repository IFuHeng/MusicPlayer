package com.example.musicplayer.base;

import android.content.Context;

public class CrashEmailHelper implements BaseHelper {
    @Override
    public void execute(Context context, IHelperListener iHelperListener) {
        //发送Email等等
        //删除文件
    }

    @Override
    public boolean deleteFile() {
        return false;
    }
}
