package com.itpoints.njmetro.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.itpoints.njmetro.R;

/**
 * 基类
 * 
 * @author peidongxu
 * 
 */
public abstract class FBaseUI extends FragmentActivity implements OnClickListener {

	/**
	 * 描述：创建
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		init();

		findView_AddListener();
		ImageView back = (ImageView) findViewById(R.id.iv_common_headbar_back);
		if (back != null) {
			back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					back();
				}
			});
		}
		prepareData();
	}

	/**
	 * 描述：初始化操作
	 */
	protected void init() {
		loadViewLayout();
	}

	@Override
	public void onClick(View v) {
		onMyClick(v);
	}

	/**
	 * 描述：加载视图
	 */
	protected abstract void loadViewLayout();

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

	/**
	 * 返回事件
	 */
	protected abstract void back();

	/**
	 * 只设置标题
	 */
	public void setTitle(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_common_headbar_title);
		tvTitle.setText(title);
	}

	/**
	 * 设置标题，并隐藏返回键
	 */
	public void setTitleHideBack(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_common_headbar_title);
		tvTitle.setText(title);
		ImageView tvBack = (ImageView) findViewById(R.id.iv_common_headbar_back);
		tvBack.setVisibility(View.GONE);
	}

	/**
	 * 设置右侧按钮-文字
	 */
	public void setRightButton(String name) {
		TextView tv_right = (TextView) findViewById(R.id.tv_common_headbar_right);
		tv_right.setText(name);
		tv_right.setOnClickListener(this);
		tv_right.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置右侧按钮-图片
	 */
	public void setRightButton(int resId) {
		ImageView iv_right = (ImageView) findViewById(R.id.iv_common_headbar_right);
		iv_right.setImageResource(resId);
		iv_right.setOnClickListener(this);
		iv_right.setVisibility(View.VISIBLE);
	}

}
