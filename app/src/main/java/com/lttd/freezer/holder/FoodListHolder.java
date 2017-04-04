package com.lttd.freezer.holder;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.bean.Food;
import com.lttd.freezer.manager.FoodsManager;
import com.lttd.freezer.manager.FreNotificationManager;
import com.lttd.freezer.utils.FoodUtils;

public class FoodListHolder extends BaseHolder<Food> {
    public FoodListHolder(Context mContext) {
        super(mContext);
    }

    @Override
    public int getLayoutID() {
        return R.layout.view_food;
    }

    @Override
    public void refreshView() {
        Food mFood = getData();

        DonutProgress view_progress = (DonutProgress) findViewById(R.id.view_food_donut_progress);
        TextView tv_name = (TextView) findViewById(R.id.tv_view_food_name);
        TextView tv_expiretime = (TextView) findViewById(R.id.tv_view_food_expiretime);
        tv_name.setText(mFood.getFood_name());

        tv_expiretime.setText(FoodUtils.getExpireDate(mFood));
        if (mFood.getIc_drawid() != -1) {

            view_progress.setAttributeResourceId(mFood.getIc_drawid());
        }

        switch (mFood.getStatus()) {
            case FoodsManager.FOOD_STATU_DISCARD:
                view_progress.setVisibility(View.GONE);
                tv_expiretime.setText("");

                break;
            case FoodsManager.FOOD_STATU_EXPIRE:
                tv_name.setTextColor(Color.RED);
                tv_expiretime.setTextColor(Color.RED);
                tv_expiretime.setText("已过期");
                break;
            case FoodsManager.FOOD_STATU_IN24:
                tv_name.setTextColor(Color.RED);
                tv_expiretime.setTextColor(Color.RED);
                view_progress.setFinishedStrokeColor(Color.RED);
                tv_expiretime.setText("24小时内过期");
                FreNotificationManager.getINSTANCE(mContext).showNotify(mFood);
                break;
            case FoodsManager.FOOD_STATU_NORMAL:
                tv_name.setTextColor(Color.BLACK);
                tv_expiretime.setTextColor(Color.BLACK);
                break;
        }

        int percentExpire = FoodUtils.getPercentExpire(mFood);
        view_progress.setMax(100);
        view_progress.setProgress(percentExpire);
        view_progress.setDonut_progress("");
        view_progress.setText("");
    }
}
