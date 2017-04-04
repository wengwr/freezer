package com.lttd.freezer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.SystemClock;
import android.text.TextUtils;

public class DateUtils {

    private static final int WEEKDAYS = 7;

    private static String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 日期变量转成对应的星期字符串
     */
    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

    /**
     * 得到当前日期是星期几
     */
    public static String getCurrentDayOfWeek() {
        return getDayOfWeek(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd mm:ss
     */
    public static Date getFormatTime(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:ss");
        Date date = dateFormat.parse(time);
        return date;
    }

    /**
     * yyyy-MM-dd
     */
    public static Date getFormatDay(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {

        }
        return date;
    }

    /**
     * yyyy-MM-dd HH:mm
     */
    public static String getTimeString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
    }

    /**
     * yyyy-MM-dd HH:mm
     */
    public static String getTimeString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 获取当前的day信息yyyy-MM-dd HH:mm
     */
    public static String getCurrentTimeString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = new Date(System.currentTimeMillis());

        return dateFormat.format(date);
    }

    /** 获取当前的day信息 yyyyMMdd */

    /** get server date String */

    /**
     * 获取两个时间的间隔天数 保证第二个时间一定大于第一个时间
     */
    public static int getBetweenDays(String t1, String t2) {
        int betweenDays = 0;
        Date d1 = getFormatDay(t1);
        Date d2 = getFormatDay(t2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if (c1.after(c2)) {
            c1 = c2;
            c2.setTime(d1);
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }

    /**
     * 获取与当前时间的间隔天数
     */
    public static int getBetweenDaysFromNow(String t1) {
        if (TextUtils.isEmpty(t1)) {
            return 0;
        }
        return getBetweenDays(t1, new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
    }

    /**
     * 获取当前指定天数之后的日期
     */
    public static String getLeftTimeByDays(Long serverTime, float days) {
        String leftTime = null;
        Date date = new Date((long) (serverTime + days * 24 * 60 * 60 * 1000));
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        leftTime = format1.format(date);
        return leftTime;
    }

    public static void setCurTime(String time) {
        Calendar c = Calendar.getInstance();
        // c.set(Calendar.YEAR, year); // 年
        // c.set(Calendar.MONTH, month); // 月
        // c.set(Calendar.DAY_OF_MONTH, day); // 日
        // c.set(Calendar.HOUR_OF_DAY, hourOfDay); // 时
        // c.set(Calendar.MINUTE, minute); // 分
        // c.set(Calendar.SECOND, second); // 秒
        // long when = c.getTimeInMillis();
        // SystemClock.setCurrentTimeMillis(when);
    }

    public static String getNextDate(String food_prod_time, int food_quality_period) {

        if (TextUtils.isEmpty(food_prod_time)) {
            return "";
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(food_prod_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date = getFormatDay(food_prod_time);
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + food_quality_period);

        return getTimeString(now.getTime());

    }
}
