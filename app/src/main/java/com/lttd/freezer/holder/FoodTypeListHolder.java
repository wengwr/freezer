package com.lttd.freezer.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.bean.FoodType;

/**
 * Created by Caxk on 2017/3/17.
 */
public class FoodTypeListHolder extends BaseHolder<FoodType> {
    public FoodTypeListHolder(Context mContext) {
        super(mContext);
    }

    @Override
    public int getLayoutID() {
        return R.layout.view_foodtype;
    }

    @Override
    public void refreshView() {

        FoodType data = getData();

        TextView tv_name = (TextView) findViewById(R.id.tv_view_foodtype_name);

        tv_name.setText(data.getType_name());
    }
}
