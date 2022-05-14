package com.example.musicplayer.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义异常处理机制
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //上下文
    private Context context;
    //单例
    private static CrashHandler crashHandler = new CrashHandler();
    //默认的异常处理对象
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    //线程池
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    //日志信息的map
    private Map<String,String> mInfo = new HashMap<>();
    //当前的时间
    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    //后续操作的对象
    private BaseHelper baseHelper;

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        return crashHandler;
    }

    public void init(Context context,BaseHelper baseHelper){
        this.baseHelper = baseHelper;
        this.context = context;
        //保存原有的异常处理对象
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        baseHelper.execute(context, new IHelperListener() {
            @Override
            public void onSuccess() {
                //上传并删除成功
                Log.e("MN-------->","上传并删除成功");
            }
            @Override
            public void onFailed() {
                Log.e("MN-------->","上传并删除失败");
            }
        });
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        Log.e("MN-=--------->"," 我进来了"+Thread.currentThread().getName());
        //捕获异常之后的任何后续操作都是在这里完成
        if(throwable == null){
            //让应用默认的异常处理对象去处理
            if(defaultUncaughtExceptionHandler !=null){
                defaultUncaughtExceptionHandler.uncaughtException(thread,throwable);
            }
        }else{
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    //你可以做你想要做的事情
                    Toast.makeText(context,"uncaughtException",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            });
            //收集日志
            collectErrorInfo();
            //保存日志
            saveErrorInfo(throwable);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //会杀掉所有PID一样的进程，比如那些拥有相同UID的应用，统统都会被杀掉。
            Process.killProcess(Process.myPid());
            //是停止程序的虚拟机，杀死当前程序
            System.exit(1);
        }
    }

    /**
     * 保存错误日志
     * @param throwable
     */
    private void saveErrorInfo(Throwable throwable) {
        //创建一个StringBuffer来拼装日志
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> iterator = mInfo.keySet().iterator();
        //基本信息的拼装
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = mInfo.get(key);
            stringBuffer.append(key +"=" + value+"\n");
        }
        //异常信息
        stringBuffer.append("\n------------Crash Log Begin--------------\n");
        //通过StringWriter来获取堆栈信息
        StringWriter stringWriter = new StringWriter();
        //创建一个PrinWriter
        PrintWriter printWriter = new PrintWriter(stringWriter);
        //获取到堆栈信息
        throwable.printStackTrace(printWriter);
        //返回的是Throwable的原因 如果说原因不存在或者是未知的原因 返回一个null
        Throwable cause = throwable.getCause();
        while (cause !=null){
            cause.printStackTrace(printWriter);
            cause = throwable.getCause();
        }
        printWriter.close();
        //将获取到的异常信息转为字符串
        String errorStr = stringWriter.toString();
        //拼装
        stringBuffer.append(errorStr);
        stringBuffer.append("\n------------Crash Log End--------------\n");
        String format = dateFormat.format(new Date());
        //获取到当前这份日志的文件的名字
        String fileName = "crash-" + format +".log";
        //如果SD可用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //生成一个文件夹的名字
            //获取到私有路径然后拼装一个文件夹
            String path = context.getFilesDir() + File.separator + "crash";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fou = null;
            try {
                fou = new FileOutputStream(new File(path, fileName));
                fou.write(stringBuffer.toString().getBytes());
                fou.flush();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (fou != null) {
                        fou.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 收集设备以及应用的相关信息
     */
    private void collectErrorInfo() {
        //获取到包管理器
        PackageManager packageManager = context.getPackageManager();
        try {
            //获取到包信息类
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if(info !=null){
                //获取到版本名字
                String versionName = TextUtils.isEmpty(info.versionName) ?
                        "未设置版本名称" : info.versionName;
                //获取到版本号
                String versionCode = info.versionCode+"";
                //收集到map中
                mInfo.put("versionName",versionName);
                mInfo.put("versionCode",versionCode);
            }
            //获取到Build类中的公共属性
            Field[] fields = Build.class.getFields();
            //遍历所有的成员变量
            if(fields !=null && fields.length>0){
                for (Field field : fields) {
                    field.setAccessible(true);
                    mInfo.put(field.getName(),field.get(null).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
