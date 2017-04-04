package com.lttd.freezer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lttd.freezer.FreApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UIUtils {

    // 获取资源文件夹的方法
    public static Resources getResource() {
        return getContext().getResources();
    }

    private static Context getContext() {
        return FreApplication.getINSTANCE();
    }

    // 获取string操作
    public static String getString(int id) {
        return getResource().getString(id);
    }

    // 获取string数组操作
    public static String[] getStringArray(int id) {
        return getResource().getStringArray(id);
    }

    // dp--->px
    public static int dip2px(Context context, int dp) {
        // 获取当前手机的一个dp和px比例转换关系
        float scale = context.getResources().getDisplayMetrics().density;
        // 0.75 2*0.75 = 1.5接近于2
        return (int) (dp * scale + 0.5);
    }

    // px--->dp
    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }

    // java代码加载一个颜色选择器
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getResource().getColorStateList(mTabTextColorResId);
    }

    // 加载图片
    public static Drawable getDrawable(int mTabBackgroundResId) {
        return getResource().getDrawable(mTabBackgroundResId);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static int getColor(int id) {
        return getResource().getColor(id);
    }

    public static int getDimens(int id) {
        return getResource().getDimensionPixelSize(id);
    }

    /**
     * 显示屏幕分辨率
     */
    public static void showScreenSize(Activity activity) {
        if (activity == null) {
            return;
        }

        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        Toast.makeText(activity, "screenWidth=" + screenWidth + "&&screenHeight=" + screenHeight, Toast.LENGTH_SHORT).show();
    }

    public static void showToastTips(Context context, int resId) {
        if (context == null)
            return;

        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showToastTips(Context context, String msg) {
        if (context == null)
            return;

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void toggleSoftInput(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
        // InputMethodManager.HIDE_NOT_ALWAYS);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}
