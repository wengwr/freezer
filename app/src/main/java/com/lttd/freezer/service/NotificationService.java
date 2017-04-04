package com.lttd.freezer.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.lttd.freezer.MainActivity;
import com.lttd.freezer.R;

public class NotificationService extends Service {


    // 点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;
    private NotificationManager messageNotificatioManager = null;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    public void refresh(String serverMessage) {
        if (serverMessage != null && !"".equals(serverMessage)) {


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

            mBuilder.setContentTitle("测试标题")//设置通知栏标题
                    .setContentText("测试内容") //设置通知栏显示内容
                    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                    .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    // Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_1))
                    .setSmallIcon(R.mipmap.ic_1);//设置通知小ICON
            messageNotificatioManager.notify(messageNotificationID, mBuilder.build());
            // 每次通知完，通知ID递增一下，避免消息覆盖掉
            messageNotificationID++;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        return super.onStartCommand(intent, flags, startId);
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), MainActivity.class), flags);
        return pendingIntent;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
