package com.itpoints.njmetro.http;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.ui.LoginUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.LogUtils;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络请求工具类
 * 
 * @author peidongxu
 * 
 */
public class HttpUtil {
	// 实例话对象
	private static AsyncHttpClient client = new AsyncHttpClient();

	static {
		client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
	}

	/**
	 * 无参数get请求
	 * 
	 * @param url
	 *            请求链接
	 * @param res
	 * @return 返回String
	 */
	public static void get(String url, AsyncHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		client.get(url, res);
	}

	/**
	 * 有参数get请求
	 * 
	 * @param url
	 *            请求链接
	 * @param params
	 *            参数
	 * @param res
	 * @return 返回String
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		LogUtils.i("params:", params.toString());
		client.get(url, params, res);
	}

	/**
	 * 无参数get请求
	 * 
	 * @param url
	 *            请求链接
	 * @param res
	 * @return 返回JSON对象或数组
	 */
	public static void get(String url, JsonHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		client.get(url, res);
	}

	/**
	 * 有参数get请求
	 * 
	 * @param url
	 *            请求链接
	 * @param params
	 *            参数
	 * @param res
	 * @return 返回JSON对象或数组
	 */
	public static void get(String url, RequestParams params, JsonHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		LogUtils.i("params:", params.toString());
		client.get(url, params, res);
	}

	/**
	 * 无参数get请求 （下载）
	 * 
	 * @param url
	 *            请求链接
	 * @param bHandler
	 * @return 返回byte数据
	 */
	public static void get(String url, BinaryHttpResponseHandler bHandler) {
		client.get(url, bHandler);
	}

	/**
	 * 无参数post请求
	 * 
	 * @param url
	 *            请求链接
	 * @param res
	 * @return 返回String
	 */
	public static void post(String url, AsyncHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		client.post(url, res);
	}

	/**
	 * 有参数post请求
	 * 
	 * @param url
	 *            请求链接
	 * @param params
	 *            参数
	 * @param res
	 * @return 返回String
	 */
	public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		LogUtils.i("====:", params.toString());
		client.post(context, url, params, res);
	}
	
	/**
	 * 有参数put请求
	 * 
	 * @param url
	 *            请求链接
	 * @param params
	 *            参数
	 * @param res
	 * @return 返回String
	 */
	public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler res) {
		LogUtils.i("url:", url);
		LogUtils.i("====:", "============");
		LogUtils.i("====:", params.toString());
		client.put(context, url, params, res);
	}

	/**
	 * 获取接口URL
	 * 
	 * @param moduleUrl
	 *            模块地址
	 * @return
	 */
	public static String getUrl(String moduleUrl) {
		return UrlConstants.SERVICE_HOST + moduleUrl;
	}

	/**
	 * 检测网络是否请求成功
	 * 
	 * @param context
	 * @param status
	 * @return true:成功;false:失败
	 */
	public static boolean checkHttpSuccess(Context context, String status) {
		boolean isSuccess = false;
		if (Constants.HTTP_STATUS_SUCCESS.equals(status)) {
			isSuccess = true;
			MyApplication.getInstance().isTip = true;
		} else if (Constants.HTTP_STATUS_TOKEN.equals(status)) {
			isSuccess = false;
			MyApplication.getInstance().isTip = false;
			MyApplication.token = "";
			MyConfig.saveToken(context, "");
			Intent intent = new Intent(context, LoginUI.class);
			context.startActivity(intent);
		}
		return isSuccess;
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}
