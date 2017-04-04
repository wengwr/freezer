package com.lttd.freezer.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.HttpUtils;
import com.lttd.freezer.FreApplication;
import com.lttd.freezer.activity.AddFoodActivity;
import com.lttd.freezer.manager.FoodsManager;

public abstract class BaseFragment extends Fragment {
    private View mView;
    protected Context mContext;
    protected FragmentManager mFragmentManager;
    protected FreApplication mFreApplication;
    protected FoodsManager mFoodsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFreApplication = FreApplication.getINSTANCE();
        mFoodsManager = mFreApplication.getFoodsManager();

        mFragmentManager = ((BaseActivity) (getActivity())).mFragmentManager;
    }

    public View getmView() {
        return mView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {

            mView = inflater.inflate(getFragmentViewId(), container, false);

        } else {
            ViewGroup viewGroup = (ViewGroup) (mView.getParent());

            if (viewGroup != null) {
                viewGroup.removeView(mView);
            }

        }
        return mView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        initView();
    }

    protected void startActivity(Class clz) {
        mContext.startActivity(new Intent(mContext, clz));
    }

    public abstract void initView();

    public abstract void findView();

    public abstract int getFragmentViewId();

    public View findViewById(int id) {
        return mView.findViewById(id);

    }

    public void setData(Object mEntity) {

    }

}
