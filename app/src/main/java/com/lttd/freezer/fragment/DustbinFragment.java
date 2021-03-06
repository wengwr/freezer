package com.lttd.freezer.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lttd.freezer.R;
import com.lttd.freezer.activity.AddFoodActivity;
import com.lttd.freezer.base.BaseFragment;
import com.lttd.freezer.base.BaseHolder;
import com.lttd.freezer.base.MyBaseAdapter;
import com.lttd.freezer.bean.Food;
import com.lttd.freezer.holder.FoodListHolder;
import com.lttd.freezer.manager.FoodsManager;
import com.lttd.freezer.utils.UIUtils;

import java.util.List;

/**
 * Created by Caxk on 2017/3/15.
 */
public class DustbinFragment extends BaseFragment implements SwipeMenuListView.OnMenuItemClickListener {
    private SwipeMenuListView lv_dustbin;
    private List<Food> foods;
    private MyAdapter myAdapter;

    @Override
    public void initView() {
        foods = mFoodsManager.getFoodInDustbin();
        if (foods != null) {
            if (myAdapter == null) {
                myAdapter = new MyAdapter(foods);
                lv_dustbin.setAdapter(myAdapter);
            } else {
                myAdapter.refreshList(foods);
            }

        }
    }



    @Override
    public void findView() {
        lv_dustbin = (SwipeMenuListView) findViewById(R.id.lv_fragmant_dustbin);


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
                openItem.setTitle("重新添加");
                // set item title fontsize
                openItem.setTitleSize(12);
                // set item title font color
                openItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(openItem);

//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        mContext);
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(UIUtils.dip2px(mContext, 50));
//                // set item title
//                deleteItem.setTitle("扔掉");
//                deleteItem.setTitleSize(12);
//                // set item title font color
//                deleteItem.setTitleColor(Color.BLACK);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        lv_dustbin.setMenuCreator(creator);
        lv_dustbin.setOnMenuItemClickListener(this);
    }

    @Override
    public int getFragmentViewId() {
        return R.layout.fragmant_dustbin;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        Food food = foods.get(position);
        switch (index) {
//            case 0:
//                food.setStatus(FoodsManager.FOOD_STATU_CONSUME);
//                mFoodsManager.updateFoodState(food);
//                initView();
//                break;
            case 0:
                Intent intent = new Intent(mContext,AddFoodActivity.class);
                intent.putExtra("food_name",food.getFood_name());
                mContext.startActivity(intent);
                break;
        }
        return true;
    }



    class MyAdapter extends MyBaseAdapter<Food> {

        public MyAdapter(List<Food> list) {
            super(list);
        }

        @Override
        public BaseHolder<Food> getHolder() {

            return new FoodListHolder(mContext);
        }

    }
}
