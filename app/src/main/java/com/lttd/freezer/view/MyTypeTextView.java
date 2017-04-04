package com.lttd.freezer.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;

import com.lttd.freezer.R;

public class MyTypeTextView extends RadioButton {


    public MyTypeTextView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundResource(R.drawable.bg_edit_kuang);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 5, 0);
        setLayoutParams(params);
        setTextSize(12);
        setPadding(4, 2, 4, 2);
        setTextColor(Color.BLACK);
    }

    public MyTypeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MyTypeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

}
