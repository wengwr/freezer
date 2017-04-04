package com.lttd.freezer.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.lttd.freezer.bean.Food;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Caxk on 2017/3/15.
 */

public class BlueToothManager {

    private final UUID BT_UUID = UUID
            .fromString("db764ac8-4b08-7f25-aafe-59d03c27bae3");
    private final String BT_NAME = "BlueTooth_Socket";
    private static BlueToothManager INSTANCE;
    private Context mContext;
    private BTReciveListner bTReciveListner;
    private BluetoothAdapter mBluetoothAdapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (bTReciveListner != null) {
                        FoodsManager.getINSTANCE(mContext).setCurTemp((String) msg.obj);
                        bTReciveListner.reciveMsg((String) msg.obj);
                    }
                    break;

            }
        }
    };



    public static BlueToothManager getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BlueToothManager(context);
        }
        return INSTANCE;
    }

    public interface BTReciveListner {
        void reciveMsg(String msg);
    }

    private BlueToothManager(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        // 因为蓝牙搜索到设备和完成搜索都是通过广播来告诉其他应用的
        // 这里注册找到设备和完成搜索广播
        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(receiver, filter);

        new ReciveThread().start();
    }

    // 注册广播接收者
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // 获取到广播的action
            String action = intent.getAction();
            // 判断广播是搜索到设备还是搜索完成
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                // 找到设备后获取其设备
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            } else if (action
                    .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            }
        }
    };


    public void registRecive(BTReciveListner bTReciveListner) {
        this.bTReciveListner = bTReciveListner;
    }

    //接收信息线程
    private class ReciveThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private InputStream is;
        private OutputStream os;

        public ReciveThread() {
            try {
                serverSocket = mBluetoothAdapter
                        .listenUsingRfcommWithServiceRecord(BT_NAME, BT_UUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();

                while (true) { //死循环接受
                    byte[] buffer = new byte[128];
                    int count = is.read(buffer);//阻塞 所以不存在死循环
                    Message msg = Message.obtain();
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    // 发送数据
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
