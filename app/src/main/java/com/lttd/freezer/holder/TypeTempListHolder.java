package com.lttd.freezer.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.bean.TypeTemp;
import com.lttd.freezer.utils.UIUtils;

public class TypeTempListHolder extends BaseHolder<TypeTemp> {


    public TypeTempListHolder(Context mContext) {
        super(mContext);
    }

    @Override
    public int getLayoutID() {
        return R.layout.view_typetemp;
    }

    @Override
    public void refreshView() {
        TypeTemp typeTemp = getData();

        TextView et_starttemp = (TextView) findViewById(R.id.tv_view_typetemp_starttemp);
        TextView et_endtemp = (TextView) findViewById(R.id.tv_view_typetemp_endtemp);
        TextView np_quality = (TextView) findViewById(R.id.tv_view_typetemp_quality);
        et_starttemp.setText(typeTemp.getStart_tem() + " ℃");
        et_endtemp.setText(typeTemp.getEnd_tem() + " ℃");
        np_quality.setText(typeTemp.getFood_quality_period() + "天");
    }

}
