package com.example.musicplayer.base;

import android.content.Context;

/**
 * 所有的后续操作 都是
 */
public interface BaseHelper {
    //处理文件  不管是上传也好  还是用Email也好  还是短信发送也好
    void execute(Context context, IHelperListener iHelperListener);

    //删除文件的方法
    boolean deleteFile();
}
