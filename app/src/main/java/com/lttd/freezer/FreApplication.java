package com.lttd.freezer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lidroid.xutils.HttpUtils;
import com.lttd.freezer.manager.BlueToothManager;
import com.lttd.freezer.manager.FoodsManager;
import com.lttd.freezer.manager.FreNotificationManager;
import com.lttd.freezer.service.NotificationService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Caxk on 2017/3/15.
 */

public class FreApplication extends Application {


    private static FreApplication INSTANCE;
    private FrePreference mFrePreference;
    private HttpUtils mHttpUtils;
    private BlueToothManager blueToothManager;
    private FoodsManager foodsManager;
    private FreNotificationManager notificationManager;

    /**
     * 定义service绑定的回调，传给bindService() 的
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            //我们已经绑定到了LocalService，把IBinder进行强制类型转换并且获取LocalService实例．
            NotificationService.MyBinder binder = (NotificationService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        notificationManager = FreNotificationManager.getINSTANCE(this);

        blueToothManager = BlueToothManager.getINSTANCE(this);
        mFrePreference = FrePreference.getInstance(getApplicationContext());
        foodsManager = FoodsManager.getINSTANCE(this);
        initImageLoader();

        mHttpUtils = new HttpUtils();
        mHttpUtils.configSoTimeout(1000);



    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mBound) {
            unbindService(mConnection);
        }
    }

    private NotificationService mService;
    private boolean mBound;

    public NotificationService getNotificationService() {
        return mService;
    }



    public FreNotificationManager getNotificationManager() {
        return notificationManager;
    }

    public FoodsManager getFoodsManager() {
        return foodsManager;
    }

    public BlueToothManager getBlueToothManager() {
        return blueToothManager;
    }

    //获取当前进程名字
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }


    public HttpUtils getHttpUtils() {
        return mHttpUtils;
    }


    private void initImageLoader() {
        // TODO Auto-generated method stub//创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

    }

    public static FreApplication getINSTANCE() {
        return INSTANCE;
    }

    public FrePreference getmFrePreference() {
        return mFrePreference;
    }

}
