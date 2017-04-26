package com.itpoints.njmetro;

import android.app.Application;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.itpoints.njmetro.bean.UserBean;
import com.itpoints.njmetro.utils.ImageLoaderConfig;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends Application {
	private static MyApplication application;
	public static String token;
	public static UserBean userBean;
	public boolean isTip = true;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		// 设置程序崩溃处理
		CrashHandler.getInstance().init(application);
		// 初始化图片加载器
		ImageLoaderConfig.initImageLoader(application);

		initUmeng();

		initJPush();

	}

	public static MyApplication getInstance() {
		return application;
	}

	public void showToast(String msg) {
		if (isTip) {
			Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化友盟
	 */
	private void initUmeng() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx0304715a96c4db16", "da179f6ebfc121595cfc84b0f368f5f7");
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("1669223336", "e0c71f54d98c2fff5f1ef77dc24fa651");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("1105402418", "vOunOW6mxw5GxstQ");
	}

	/**
	 * 初始化极光推送
	 */
	private void initJPush() {
		// 初始化极光推送
		JPushInterface.setDebugMode(false);
		JPushInterface.init(application);
	}

}
