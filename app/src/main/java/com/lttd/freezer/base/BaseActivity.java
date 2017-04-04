package com.lttd.freezer.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.HttpUtils;
import com.lttd.freezer.FreApplication;
import com.lttd.freezer.FrePreference;
import com.lttd.freezer.manager.BlueToothManager;
import com.lttd.freezer.manager.FoodsManager;
import com.lttd.freezer.manager.FreNotificationManager;
import com.lttd.freezer.utils.UIUtils;
import com.lttd.freezer.service.NotificationService;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected FreApplication mFreApplication;
    protected FrePreference mFrePreference;
    protected WindowManager mWindowManager;
    protected FragmentManager mFragmentManager;
    protected HttpUtils mHttpUtils;
    protected UserManager mUserManager;
    protected BlueToothManager blueToothManager;
    protected FoodsManager mFoodsManager;
    protected FreNotificationManager mNotificationManager;
    protected NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIUtils.setMIUISetStatusBarLightMode(getWindow(), true);
        super.onCreate(savedInstanceState);
        mContext = this;
        mFreApplication = FreApplication.getINSTANCE();
        mHttpUtils = mFreApplication.getHttpUtils();
        blueToothManager = mFreApplication.getBlueToothManager();
        mFrePreference = mFreApplication.getmFrePreference();
        mNotificationManager = mFreApplication.getNotificationManager();
        notificationService = mFreApplication.getNotificationService();
        mFragmentManager = getSupportFragmentManager();

        mFoodsManager = mFreApplication.getFoodsManager();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        setContentView(getLayoutId());
        ButterKnife.bind(this);

        findView();
        initView();

    }

    protected void startActivity(Class z) {
        startActivity(new Intent(mContext, z));
    }


    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    protected void showToast(String msg) {
        UIUtils.showToastTips(mContext, msg);
    }


    public abstract void initView();

    public abstract void findView();

    public abstract int getLayoutId();

}
