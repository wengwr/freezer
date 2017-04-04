package com.lttd.freezer.config;

import android.os.Environment;

/**
 * Created by Caxk on 2017/3/15.
 */

public class FreConfig {
    //食物更新时间隔
//    public static final long REFRESH_TIME = 1000 * 60 * 60 * 24;
    public static final long REFRESH_TIME = 1000 * 60;

    //默认是放在了内置存储 方便查看数据 为空字符串的时候 就会存储到程序本身的data文件夹下
    public static final String DB_PATH = Environment.getExternalStorageDirectory().getPath();
//    public static final String DB_PATH = "";

}
