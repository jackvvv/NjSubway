package com.itpoints.njmetro.ui.img;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.itpoints.njmetro.MyApplication;

/**
 * JS和webview交互类
 * 
 * @author peidongxu
 * 
 */
public class JSKit {
	private Context context;
	private ActionCallBack actionCallBack;

	public JSKit(Context context) {
		this.context = context;
	}

	/**
	 * 设置操作回调
	 * 
	 * @param actionCallBack
	 */
	public void setActionCallBack(ActionCallBack actionCallBack) {
		this.actionCallBack = actionCallBack;
	}

	/**
	 * 弹出消息
	 * 
	 * @param msg
	 */
	@JavascriptInterface
	public void showMsg(String msg) {
		MyApplication.getInstance().showToast(msg);
	}

	/**
	 * 查看站点信息
	 * 
	 * @param line
	 *            线路
	 * @param station
	 *            站点名
	 */
	@JavascriptInterface
	public void showStationDetail(String station) {
		if (actionCallBack != null) {
			actionCallBack.showStationDetail(station);
		}
	}

	/**
	 * 显示结果
	 * 
	 * @param type
	 *            类型：1：起点2：终点3：结果 4:替换
	 * @param start
	 *            起点
	 * @param end
	 *            终点
	 */
	@JavascriptInterface
	public void showResult(int type, String start, String end) {
		if (actionCallBack != null) {
			actionCallBack.showResult(type, start, end);
		}
	}

	/**
	 * 触发事件弹出框
	 * 
	 */
	@JavascriptInterface
	public void bombBox(String station) {
		if (actionCallBack != null) {
			actionCallBack.bombBox(station);
		}
	}

	/**
	 * 回调接口
	 * 
	 * @author peidongxu
	 */
	public interface ActionCallBack {

		public void showStationDetail(String station);

		public void showResult(int type, String start, String end);

		public void bombBox(String station);

	}
}
