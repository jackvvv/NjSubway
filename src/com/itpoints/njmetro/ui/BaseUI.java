package com.itpoints.njmetro.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itpoints.njmetro.AppManager;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.SystemStatusManager;
import com.itpoints.njmetro.utils.Utils;

/**
 * 程序基类
 * 
 * @author peidongxu
 * 
 */
public abstract class BaseUI extends Activity implements OnClickListener {
	// 退出时间
	private long exitTime = 0;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		activity = this;
		AppManager.getAppManager().addActivity(activity);
		init();
		
//		setTranslucentStatus();

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
	 * 初始化
	 */
	protected void init() {
		loadViewLayout();
	}

	/**
	 * 退出事件
	 */
	protected void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			AppManager.getAppManager().AppExit(getApplicationContext());
		}
	}

	@Override
	public void onClick(View v) {
		onMyClick(v);
	}

	/**
	 * 加载布局
	 */
	protected abstract void loadViewLayout();

	/**
	 * findViewById
	 */
	protected abstract void findView_AddListener();

	/**
	 * 数据逻辑处理
	 */
	protected abstract void prepareData();

	/**
	 * 点击事件
	 */
	protected abstract void onMyClick(View v);

	/**
	 * 返回事件
	 */
	protected abstract void back();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

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

	/**
	 * 设置状态栏背景状态
	 */
	protected void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
			// 设置背景
			View layoutAll = findViewById(R.id.ll_layout_all);
			if (layoutAll != null) {
				SystemStatusManager tintManager = new SystemStatusManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(getResources().getColor(R.color.blue));// 状态栏无背景
				// 设置系统栏需要的内偏移
				layoutAll.setPadding(0, Utils.getStatusHeight(this), 0, 0);
			}
		}
	}

}
