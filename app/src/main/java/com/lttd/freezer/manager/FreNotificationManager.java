package com.lttd.freezer.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.lttd.freezer.FrePreference;
import com.lttd.freezer.MainActivity;
import com.lttd.freezer.R;
import com.lttd.freezer.bean.Food;


/**
 * Created by Caxk on 2017/3/21.
 */

public class FreNotificationManager {
    private static FreNotificationManager INSTANCE;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    public FreNotificationManager(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);


    }

    public static FreNotificationManager getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FreNotificationManager(context);
        }
        return INSTANCE;
    }

    // 通知栏消息
    private int messageNotificationID = 1000;

    public void showNotify(Food food) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        String title;
        String content;
        int resID;
        if (food == null) {
            title = "温度报警";
            content = "冰箱温度已经超过" + FrePreference.getInstance().getTempAlert() + "℃";
            resID = messageNotificationID;
            messageNotificationID++;
        } else {
            title = "过期提醒";
            content = food.getFood_name();
            resID = food.getId();
        }
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content) //设置通知栏显示内容
                .setColor(Color.RED)
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL, 1)) //设置通知栏点击意图
                .setTicker(title) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis() + 10)//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                // Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_1))
                .setSmallIcon(R.mipmap.ic_1);//设置通知小ICON

        mNotificationManager.notify(resID, mBuilder.build());
    }

    public PendingIntent getDefalutIntent(int flags, int id) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, new Intent(mContext, MainActivity.class), flags);
        return pendingIntent;
    }


}
