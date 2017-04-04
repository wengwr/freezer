package com.lttd.freezer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseActivity;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.base.MyBaseAdapter;
import com.lttd.freezer.bean.FoodType;
import com.lttd.freezer.holder.FoodTypeListHolder;
import com.lttd.freezer.utils.UIUtils;

import java.util.List;


public class FoodTypeListActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener, SwipeMenuListView.OnMenuItemClickListener {


    private SwipeMenuListView rl_foodtype;
    private TextView tv_add;
    private List<FoodType> foodTypes;
    private MyAdapter myAdapter;

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_activity_addfoodtypelist_add:
                startActivity(FoodTypeActivity.class);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void initView() {
        foodTypes = mFoodsManager.getFoodTypes();
        if (foodTypes != null) {
            if (myAdapter == null) {
                myAdapter = new MyAdapter(foodTypes);
                rl_foodtype.setAdapter(myAdapter);
            } else {
                myAdapter.refreshList(foodTypes);
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_foodtypelist;
    }

    @Override
    public void findView() {
        rl_foodtype = (SwipeMenuListView) findViewById(R.id.lv_activity_foodtypelist);
        tv_add = (TextView) findViewById(R.id.tv_activity_addfoodtypelist_add);

        tv_add.setOnClickListener(this);

        // Left
        rl_foodtype.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(UIUtils.dip2px(mContext, 50));
                // set item title
                openItem.setTitle("修改");
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
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(10);
                // set item title font color
                deleteItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        rl_foodtype.setMenuCreator(creator);

        rl_foodtype.setOnMenuItemClickListener(this);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                Intent intent = new Intent(mContext, FoodTypeActivity.class);
                intent.putExtra("typeID", foodTypes.get(position).getId());
                startActivity(intent);
                break;
            case 1:
                mFoodsManager.deleteFoodType(foodTypes.get(position));
                initView();
                break;
        }
        return false;
    }

    class MyAdapter extends MyBaseAdapter<FoodType> {

        public MyAdapter(List<FoodType> list) {
            super(list);
        }

        @Override
        public BaseHolder<FoodType> getHolder() {

            return new FoodTypeListHolder(mContext);
        }

    }
}
