package com.lttd.freezer.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseActivity;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.base.MyBaseAdapter;
import com.lttd.freezer.bean.FoodType;
import com.lttd.freezer.bean.TypeTemp;
import com.lttd.freezer.holder.TypeTempListHolder;
import com.lttd.freezer.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoodTypeActivity extends BaseActivity implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {


    private SwipeMenuListView lv_temp;
    private List<TypeTemp> typeTempList;
    private MyAdapter myAdapter;
    private TextView tv_temp_add;
    private TextView tv_foodtype_add;
    private EditText et_name;
    private FoodType mFoodType;

    @Override
    public void initView() {
        Intent intent = getIntent();
        int typeID = intent.getIntExtra("typeID", -1);

        if (typeID != -1) {
            mFoodType = mFoodsManager.getFoodTypeByID(typeID);
        }
        if (mFoodType != null) {
            typeTempList = mFoodsManager.findFoodTypeTemp(mFoodType.getType_code());
            et_name.setText(mFoodType.getType_name());
            tv_foodtype_add.setText("保存");
        }

        if (typeTempList == null) {
            typeTempList = new ArrayList<>();
        }
        reFreshView();
    }

    public void reFreshView() {

        if (typeTempList != null) {
            if (myAdapter == null) {
                myAdapter = new MyAdapter(typeTempList);
                lv_temp.setAdapter(myAdapter);
            } else {
                myAdapter.refreshList(typeTempList);
            }

        }
    }

    @Override
    public void findView() {
        lv_temp = (SwipeMenuListView) findViewById(R.id.lv_activity_type_temp);
        tv_temp_add = (TextView) findViewById(R.id.tv_activity_type_temp_add);
        tv_foodtype_add = (TextView) findViewById(R.id.tv_activity_foodtype_add);
        et_name = (EditText) findViewById(R.id.et_activity_type_name);
        tv_temp_add.setOnClickListener(this);
        tv_foodtype_add.setOnClickListener(this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        mContext);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(UIUtils.dip2px(mContext, 50));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(10);
                // set item title font color
                openItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(UIUtils.dip2px(mContext, 50));
                // set item title
                deleteItem.setTitle("修改");
                // set item title fontsize
                deleteItem.setTitleSize(10);
                // set item title font color
                deleteItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        lv_temp.setMenuCreator(creator);
        lv_temp.setOnMenuItemClickListener(this);
    }


    private void showNewTypeTempDialog(final TypeTemp typeTemp, final boolean isNew) {

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_newtypetempview, null);
        final EditText et_starttemp = (EditText) view.findViewById(R.id.et_view_newtypetemp_starttemp);
        final EditText et_endtemp = (EditText) view.findViewById(R.id.et_view_newtypetemp_endtemp);
        final EditText et_quality = (EditText) view.findViewById(R.id.et_view_newtypetemp_quality);

        if (!isNew) {
            et_starttemp.setText(typeTemp.getStart_tem() + "");
            et_endtemp.setText(typeTemp.getEnd_tem() + "");
            et_quality.setText(typeTemp.getFood_quality_period() + "");
        }

        view.findViewById(R.id.tv_btn_confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String et_starttempS = et_starttemp.getEditableText().toString();
                String et_endtempS = et_endtemp.getEditableText().toString();
                String et_qualityS = et_quality.getEditableText().toString();
                if (!TextUtils.isEmpty(et_starttempS) && !TextUtils.isEmpty(et_endtempS)
                        && !TextUtils.isEmpty(et_qualityS)) {

                    typeTemp.setFood_quality_period(Integer.parseInt(et_qualityS));
                    typeTemp.setEnd_tem(Double.parseDouble(et_endtempS));
                    typeTemp.setStart_tem(Double.parseDouble(et_starttempS));
                    if (isNew) {
                        typeTempList.add(typeTemp);
                    }
                    reFreshView();
                }
                alertDialog.dismiss();

            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_type_temp_add:
                showNewTypeTempDialog(new TypeTemp(), true);
                break;
            case R.id.tv_activity_foodtype_add:
                if (TextUtils.isEmpty(et_name.getEditableText().toString())) {
                    showToast("请输入种类名称");
                    return;
                }
                if (mFoodType != null) {
                    mFoodType.setType_name(et_name.getEditableText().toString());
                    if (mFoodsManager.updateFoodType(mFoodType, typeTempList)) {
                        showToast("保存成功");
                        finish();
                    } else {
                        showToast("保存失败");
                    }
                } else {
                    mFoodType = new FoodType();
                    mFoodType.setType_name(et_name.getEditableText().toString());
                    if (mFoodsManager.saveNewFoodType(mFoodType, typeTempList)) {
                        showToast("保存成功");
                        finish();
                    } else {
                        showToast("保存失败");
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                mFoodsManager.deleteFoodTypeTemp(typeTempList.get(position));
                initView();
                break;
            case 1:
                showNewTypeTempDialog(typeTempList.get(position), false);
                break;

        }
        return true;
    }

    class MyAdapter extends MyBaseAdapter<TypeTemp> {

        public MyAdapter(List<TypeTemp> list) {
            super(list);
        }

        @Override
        public BaseHolder<TypeTemp> getHolder() {

            return new TypeTempListHolder(mContext);
        }

    }


}
