package com.lttd.freezer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lttd.freezer.activity.AddFoodActivity;
import com.lttd.freezer.activity.SettingActivity;
import com.lttd.freezer.base.BaseActivity;
import com.lttd.freezer.base.BaseFragment;
import com.lttd.freezer.bean.Food;
import com.lttd.freezer.factory.FragmentFactory;
import com.lttd.freezer.manager.BlueToothManager;
import com.lttd.freezer.manager.FoodsManager;
import com.lttd.freezer.service.NotificationService;

public class MainActivity extends BaseActivity implements BlueToothManager.BTReciveListner, View.OnClickListener, FoodsManager.FoodsRefreshListner {

    private TextView tv_temp, tv_humidity, tv_addfood;
    private RelativeLayout rl_set;


    private RelativeLayout rl_main;
    private BaseFragment mCurFragment;
    private RelativeLayout rl_fridge;
    private RelativeLayout rl_dustbin;
    private View v_fridge_stateL;
    private View v_dustbin_stateL;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mCurFragment != null) {

                        mCurFragment.initView();
                    }
                    break;
            }
        }
    };

    @Override
    public void initView() {

        blueToothManager.registRecive(this);
        mFoodsManager.registRecive(this);

        moveToFragment(FragmentFactory.FRAGMENT_FRIDGE);
    }


    /**
     * @param pos 需要切换的FragmentFactory 里的指针
     */
    public void moveToFragment(int pos) {
        FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
        BaseFragment nextFragment = FragmentFactory.getFragment(pos);

        if (!nextFragment.isAdded()) { // 先判断是否被add过
            if (mCurFragment != null) {
                beginTransaction = beginTransaction.hide(mCurFragment);
            }
            beginTransaction.add(R.id.rl_activity_main_main, nextFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中

        } else {
            if (mCurFragment != null) {
                beginTransaction = beginTransaction.hide(mCurFragment);
            }
            beginTransaction.show(nextFragment).commit(); // 隐藏当前的fragment，显示下一个
            nextFragment.initView();
        }
        mCurFragment = nextFragment;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurFragment.initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_mian_add:
                startActivity(AddFoodActivity.class);
                break;
            case R.id.rl_activity_main_set:
                startActivity(SettingActivity.class);
                break;
            case R.id.rl_activity_main_bottom_fridge:
                moveToFragment(FragmentFactory.FRAGMENT_FRIDGE);
                v_fridge_stateL.setVisibility(View.VISIBLE);
                v_dustbin_stateL.setVisibility(View.INVISIBLE);

                break;
            case R.id.rl_activity_main_bottom_dustbin:
                moveToFragment(FragmentFactory.FRAGMENT_DUSTBIN);
                v_dustbin_stateL.setVisibility(View.VISIBLE);
                v_fridge_stateL.setVisibility(View.INVISIBLE);

                break;
        }
    }


    @Override
    public void findView() {
        tv_temp = (TextView) findViewById(R.id.tv_activty_main_temp);
        tv_humidity = (TextView) findViewById(R.id.tv_activty_main_humidity);

        tv_addfood = (TextView) findViewById(R.id.tv_activity_mian_add);
        rl_set = (RelativeLayout) findViewById(R.id.rl_activity_main_set);


        rl_main = (RelativeLayout) findViewById(R.id.rl_activity_main_main);
        rl_fridge = (RelativeLayout) findViewById(R.id.rl_activity_main_bottom_fridge);
        rl_dustbin = (RelativeLayout) findViewById(R.id.rl_activity_main_bottom_dustbin);
        v_fridge_stateL = findViewById(R.id.v_activity_main_bottom_fridge_stateL);
        v_dustbin_stateL = findViewById(R.id.v_activity_main_bottom_dustbin_stateL);

        tv_addfood.setOnClickListener(this);
        rl_set.setOnClickListener(this);

        rl_fridge.setOnClickListener(this);
        rl_dustbin.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void reciveMsg(String msg) {
        String[] split = msg.split(",");
        if (split.length != 4) {
            return;
        }
        if (Double.parseDouble(split[2]) > mFrePreference.getTempAlert()) {
            mNotificationManager.showNotify(null);
        }
        tv_temp.setText(split[2] + "℃");
        tv_humidity.setText(split[3] + "%");
    }

    @Override
    public void refresh() {
        handler.sendEmptyMessage(0);
    }

}
