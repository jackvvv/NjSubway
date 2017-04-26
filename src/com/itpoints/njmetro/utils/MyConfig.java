package com.itpoints.njmetro.utils;

import com.google.gson.Gson;
import com.itpoints.njmetro.bean.UserBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 配置文件
 * 
 * @author peidongxu
 * 
 */
public class MyConfig {
	/**
	 * 描述：设置int类型的 config 数据
	 * 
	 * @param context
	 * @param name
	 *            配置文件名称
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setConfig(Context context, String name, String key, int value) {
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		Editor editor = sharedata.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 描述：获取int类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return 值
	 */
	public static int getConfig(Context context, String name, String key, int defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		return sharedata.getInt(key, defaultValue);
	}

	/**
	 * 描述：设置long类型的 config 数据
	 * 
	 * @param context
	 * @param name
	 *            配置文件名称
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setConfig(Context context, String name, String key, long value) {
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		Editor editor = sharedata.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 描述：获取long类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return 值
	 */
	public static long getConfig(Context context, String name, String key, long defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		return sharedata.getLong(key, defaultValue);
	}

	/**
	 * 描述：设置String类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setConfig(Context context, String name, String key, String value) {
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		Editor editor = sharedata.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 描述：获取String类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return 值
	 */
	public static String getConfig(Context context, String name, String key, String defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		return sharedata.getString(key, defaultValue);
	}

	/**
	 * 描述：设置boolean类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void setConfig(Context context, String name, String key, boolean value) {
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		Editor editor = sharedata.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 描述：获取boolean类型的 config 数据
	 * 
	 * @param context
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return 值
	 */
	public static boolean getConfig(Context context, String name, String key, boolean defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		return sharedata.getBoolean(key, defaultValue);
	}

	/**
	 * 清除配置文件
	 * 
	 * @param context
	 * @param name
	 */
	public static void clear(Context context, String name) {
		SharedPreferences sharedata = context.getSharedPreferences(name, 0);
		Editor edit = sharedata.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * 保存token
	 * 
	 * @param context
	 * @param ulib
	 * 
	 * @author peidongxu
	 */
	public static void saveToken(Context context, String token) {
		setConfig(context, "config", "token", token);
	}

	/**
	 * 获取token
	 * 
	 * @param context
	 * @param ulib
	 * 
	 * @author peidongxu
	 */
	public static String getToken(Context context) {
		return getConfig(context, "config", "token", "");
	}

	/**
	 * 保存用户信息
	 * 
	 * @param context
	 * @param ulib
	 * 
	 * @author peidongxu
	 */
	public static void saveUserInfo(Context context, UserBean userBean) {
		Gson gson = new Gson();
		if (userBean == null) {
			setConfig(context, "config", "userbean", "");
		} else {
			setConfig(context, "config", "userbean", gson.toJson(userBean));
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 * @author peidongxu
	 */
	public static UserBean getUserInfo(Context context) {
		Gson gson = new Gson();
		String json = getConfig(context, "config", "userbean", "");
		UserBean userBean = gson.fromJson(json, UserBean.class);
		if (userBean == null) {
			userBean = new UserBean();
		}
		return userBean;
	}

}
