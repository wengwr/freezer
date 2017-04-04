package com.lttd.freezer.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private List<T> mList;
    private BaseHolder<T> baseHolder;

    public MyBaseAdapter(List<T> list) {
        mList = list;
        baseHolder = getHolder();
    }

    public void refreshList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract BaseHolder<T> getHolder();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            baseHolder = (BaseHolder<T>) convertView.getTag();
        } else {
            baseHolder = getHolder();
        }
        baseHolder.setData(mList.get(position));
        return baseHolder.getView();
    }

}
