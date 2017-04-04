package com.lttd.freezer.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseActivity;
import com.lttd.freezer.bean.Food;
import com.lttd.freezer.bean.FoodType;
import com.lttd.freezer.utils.DateUtils;
import com.lttd.freezer.utils.UIUtils;
import com.lttd.freezer.view.MyTypeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFoodActivity extends BaseActivity implements NumberPicker.OnValueChangeListener, TextWatcher {

    @BindView(R.id.et_activity_addfood_quality)
    public EditText et_quality;

    @BindView(R.id.et_activity_addfood_name)
    public EditText et_name;

    private PopupWindow popupWindow;

    @BindView(R.id.tv_activity_addfood_type)
    public TextView tv_type;


    private FoodType mCurFoodType;

    @BindView(R.id.iv_activity_addfood_ic)
    public ImageView iv_ic;

    private int[] imageRes = {R.mipmap.ic_1, R.mipmap.ic_2, R.mipmap.ic_3, R.mipmap.ic_4, R.mipmap.ic_5, R.mipmap.ic_6};
    private int mCurFoodIc = imageRes[0];
    @BindView(R.id.view_activity_addfood_type)
    public View line;
    @BindView(R.id.rl_activity_addfood_quality)
    public RelativeLayout rl_type;

    @Override
    public void initView() {
        Intent intent = getIntent();
        String food_name = intent.getStringExtra("food_name");
        if(!TextUtils.isEmpty(food_name)){
            et_name.setText(food_name);
        }

    }

    @Override
    public void findView() {
        et_name.addTextChangedListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_addfood;
    }

    @OnClick({R.id.tv_activity_addfood_add, R.id.tv_activity_addfood_type, R.id.iv_activity_addfood_ic})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_addfood_add:
                if (TextUtils.isEmpty(et_name.getEditableText().toString())) {
                    UIUtils.showToastTips(mContext, "请输入食物名称");
                    return;
                }
                if (TextUtils.isEmpty(et_quality.getEditableText().toString())) {
                    UIUtils.showToastTips(mContext, "请输入保质期");
                    return;
                }
                Food food = new Food();
                food.setFood_quality_period(Integer.parseInt(et_quality.getEditableText().toString()));
                food.setFood_name(et_name.getEditableText().toString());
                food.setFood_prod_time(DateUtils.getCurrentTimeString());
                food.setIc_drawid(mCurFoodIc);
                if (mCurFoodType != null) {
                    food.setFoodType_id(mCurFoodType.getId());
                    food.setFoodType_name(mCurFoodType.getType_name());
                }
                boolean saveNewFood = mFoodsManager.saveNewFood(food);
                if (saveNewFood) {
                    showToast("放入冰箱成功");
                    finish();
                }
                break;
            case R.id.tv_activity_addfood_type:
                showPopupWindow(v);

                break;
            case R.id.iv_activity_addfood_ic:
                showPopupWindowChooseIC(v);

                break;

            default:
                break;
        }


    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }


    private void showPopupWindow(final View view) {

        ScrollView scrollView = new ScrollView(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        List<FoodType> foodTypes;

        foodTypes = mFoodsManager.getFoodTypes();
        if (foodTypes == null) {
            showToast("当前没有类别！");
            return;
        }

        for (int i = 0; i < foodTypes.size(); i++) {
            final FoodType mFoodType = foodTypes.get(i);
            final int position = i;
            MyTypeTextView textView = new MyTypeTextView(mContext);
            textView.setText(mFoodType.getType_name());
            textView.setPadding(4, 2, 4, 2);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(mContext, 50));
            textView.setLayoutParams(layoutParams);
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((TextView) view).setText(mFoodType.getType_name());

                    mCurFoodType = mFoodType;
                    popupWindow.dismiss();
                    line.setVisibility(View.GONE);
                    rl_type.setVisibility(View.GONE);

                }
            });
            textView.setTag(i);

            linearLayout.addView(textView);
        }
        scrollView.addView(linearLayout);
        // 设置按钮的点击事件

        popupWindow = new PopupWindow(scrollView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_btn_kuang_grey));

        // 设置好参数之后再show
        popupWindow.showAtLocation(this.findViewById(R.id.rl_activity_addfood),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void showPopupWindowChooseIC(final View view) {

        ScrollView scrollView = new ScrollView(mContext);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        for (int i = 0; i < imageRes.length; i++) {
            final int image = imageRes[i];
            final int position = i;
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(image);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(mContext, 50));
            layoutParams.weight = 1;
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((ImageView) view).setImageResource(image);

                    mCurFoodIc = image;
                    popupWindow.dismiss();
                }
            });
            imageView.setTag(i);

            linearLayout.addView(imageView);
        }
        scrollView.addView(linearLayout);
        // 设置按钮的点击事件

        popupWindow = new PopupWindow(scrollView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }

        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_btn_kuang_grey));

        // 设置好参数之后再show
        popupWindow.showAtLocation(this.findViewById(R.id.rl_activity_addfood),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Food food = mFoodsManager.findFood(s.toString());
        if (food != null) {
            showToast("已为您匹配记录");
            et_quality.setText(food.getFood_quality_period() + "");
            int foodType_id = food.getFoodType_id();
            if (foodType_id != -1) {
                mCurFoodType = mFoodsManager.getFoodTypeByID(foodType_id);
                tv_type.setText(mCurFoodType.getType_name());
                line.setVisibility(View.GONE);
                rl_type.setVisibility(View.GONE);
            } else {
                mCurFoodType = null;
                tv_type.setText("");
                line.setVisibility(View.VISIBLE);
                rl_type.setVisibility(View.VISIBLE);
            }
            if (food.getIc_drawid() != -1) {
                mCurFoodIc = food.getIc_drawid();
                iv_ic.setImageResource(food.getIc_drawid());
            }

        }
    }
}
