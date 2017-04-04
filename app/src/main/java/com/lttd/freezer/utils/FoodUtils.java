package com.lttd.freezer.utils;

import com.lttd.freezer.bean.Food;

import java.text.NumberFormat;

/**
 * Created by Caxk on 2017/3/17.
 */

public class FoodUtils {

    public static int getPercentExpire(Food food) {
        int betweenDaysFromNow = DateUtils.getBetweenDaysFromNow(food.getFood_prod_time());
        if (betweenDaysFromNow == 0) {
            return 100;
        }
        if (food.getFood_quality_period() == 0) {
            return 0;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) (food.getFood_quality_period() - betweenDaysFromNow) / (float) (food.getFood_quality_period()) * 100);
        int parseInt = 0;
        try {

            parseInt = Integer.parseInt(result);
        } catch (Exception e) {
            return parseInt;
        }
        return parseInt;
    }


    public static String getExpireDate(Food food) {
        String nextDate = DateUtils.getNextDate(food.getFood_prod_time(), food.getFood_quality_period());
        return nextDate;
    }

}
