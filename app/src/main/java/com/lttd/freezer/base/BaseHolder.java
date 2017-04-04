package com.lttd.freezer.base;

import android.content.Context;
import android.view.View;

public abstract class BaseHolder<T> {

	protected Context mContext;

	public BaseHolder(Context mContext) {
		this.mContext = mContext;

		initView();

	}

	private void initView() {
		mView = View.inflate(mContext, getLayoutID(), null);
		mView.setTag(this);
	}

	public View getView() {
		return mView;
	}

	private T mData;
	protected View mView;

	public View findViewById(int id) {
		return mView.findViewById(id);
	}

	public void setData(T t) {
		mData = t;
		refreshView();
	}

	public T getData() {
		return mData;
	}

	public abstract int getLayoutID();

	public abstract void refreshView();
}
