package com.lttd.freezer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseActivity;

import butterknife.OnClick;


public class SettingActivity extends BaseActivity implements OnClickListener {


    @OnClick({R.id.rl_activity_set_foodtype, R.id.rl_activity_set_alert, R.id.rl_activity_set_clean, R.id.rl_activity_set_bluet})
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.rl_activity_set_foodtype:
                startActivity(FoodTypeListActivity.class);
                break;
            case R.id.rl_activity_set_alert:
                startActivity(SetAlertActivity.class);
                break;
            case R.id.rl_activity_set_clean:
                new AlertDialog.Builder(mContext).setMessage("确定要清空数据库吗？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFoodsManager.cleanAll();
                        showToast("清理完毕！");
                        System.exit(0);
                    }
                }).setPositiveButton("取消", null).create().show();
                break;
            case R.id.rl_activity_set_bluet:
//                startActivity(BTSettingActivity.class);
                break;

        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void findView() {

    }


}
