package com.lttd.freezer.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.lttd.freezer.R;
import com.lttd.freezer.base.BaseActivity;
import com.lttd.freezer.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Caxk on 2017/3/21.
 */
public class SetAlertActivity extends BaseActivity {

    @BindView(R.id.et_activity_setalert_temp)
    EditText et_temp;


    @Override
    public void initView() {
        int tempAlert = mFrePreference.getTempAlert();

        et_temp.setText(tempAlert + "");

    }

    @OnClick(R.id.tv_activity_setalert_save)
    public void save() {
        String et_temps = et_temp.getEditableText().toString();
        if (TextUtils.isEmpty(et_temps)) {
            showToast("请输入冰箱报警温度！");
            return;
        }
        int parseInt = Integer.parseInt(et_temps);
        mFrePreference.setTempAlert(parseInt);
        showToast("设置报警温度成功！");
    }

    @Override
    public void findView() {


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setalert;
    }
}
