package com.lttd.freezer;

import android.content.Context;
import android.content.SharedPreferences;


import java.security.InvalidParameterException;

/**
 * 用来存储临时的config
 */
public class FrePreference {
    public static final String META = "META";
    private static FrePreference INSTANCE = null;
    public Context context;
    public SharedPreferences metaPref;

    private FrePreference(Context context) {
        this.context = context;

        metaPref = context.getSharedPreferences(META, Context.MODE_PRIVATE);
    }

    public static FrePreference getInstance(Context context) {
        if (context == null)
            throw new InvalidParameterException("Context Parameter is Null.");

        if (INSTANCE == null) {
            INSTANCE = new FrePreference(context);
        }
        return INSTANCE;
    }

    public static FrePreference getInstance() {
        if (INSTANCE == null)
            throw new InvalidParameterException("Context Parameter is Null.");

        return INSTANCE;

    }


    /**
     * 设置下载视频清晰度 默认为流畅
     *
     * @param
     */
    public void setTempAlert(int temp) {
        metaPref.edit().putInt("TempAlert", temp).commit();
    }


    /**
     * 获取当前的用户信息json
     *
     * @return
     */
    public int getTempAlert() {
        return metaPref.getInt("TempAlert", 10);
    }

    /**
     * 设置上次登录的用户名
     *
     * @param
     */
    public void setLastUserName(String lastusername) {
        metaPref.edit().putString("lastusername", lastusername).commit();
    }

    /**
     * 获取上次登录的用户名
     *
     * @return
     */
    public String getLastUserName() {
        return metaPref.getString("lastusername", "");
    }

    /**
     * 设置是否允许走流量观看视频
     *
     * @param
     */
    public void setIsRunUnderflow(boolean isrununderflow) {
        metaPref.edit().putBoolean("isrununderflow", isrununderflow).commit();
    }

    /**
     * 获取是否允许走流量观看视频
     *
     * @return true 允许
     */
    public boolean getIsRunUnderflow() {
        return metaPref.getBoolean("isrununderflow", false);
    }


}
