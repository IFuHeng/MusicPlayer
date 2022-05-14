package com.example.musicplayer.base;

import android.content.Context;

import java.io.File;

/**
 * 网络上传的操作
 */
public class CrashNetHelper implements BaseHelper {
    private Context context;

    @Override
    public void execute(Context context, IHelperListener iHelperListener) {
        this.context = context;
        //上传日志
        //如果上传失败  就不执行下面的代码
        
        //删除以及上传的日志
        if(deleteFile()){
            iHelperListener.onSuccess();
        }else{
            iHelperListener.onFailed();
        }
    }

    @Override
    public boolean deleteFile() {
        String path = context.getFilesDir() + File.separator + "crash";
        //获取到存储日志文件的文件夹
        File file = new File(path);
        File[] files = file.listFiles();
        if(files ==null || files.length==0){
            return true;
        }
        for (File file1 : files) {
            boolean delete = file1.delete();
            return delete;
        }
        return true;
    }
}
