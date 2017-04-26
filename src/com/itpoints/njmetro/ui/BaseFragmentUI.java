package com.itpoints.njmetro.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragmentUI extends Fragment implements OnClickListener {
	/** 视图 */
	protected View view;

	/**
	 * 描述：创建
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 描述：加载视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init(inflater, container);
		findView_AddListener();
		prepareData();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	/**
	 * 描述：初始化操作
	 */
	protected void init(LayoutInflater inflater, ViewGroup container) {
		loadViewLayout(inflater, container);
	}

	@Override
	public void onClick(View v) {
		onMyClick(v);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 描述：加载视图
	 */
	protected abstract void loadViewLayout(LayoutInflater inflater, ViewGroup container);

	/**
	 * 描述：初始化控件，添加事件
	 */
	protected abstract void findView_AddListener();

	/**
	 * 描述：准备数据
	 */
	protected abstract void prepareData();

	/**
	 * 描述：点击事件
	 */
	protected abstract void onMyClick(View v);

}
