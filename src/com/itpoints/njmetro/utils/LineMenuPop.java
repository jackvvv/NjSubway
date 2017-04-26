package com.itpoints.njmetro.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itpoints.njmetro.R;

/**
 * 线网图菜单
 * 
 * @author peidongxu
 * 
 */
public class LineMenuPop implements OnClickListener {
	private PopupWindow popupWindow;
	private View v;
	private Handler mHandler;
	private String station;

	/**
	 * @param v
	 *            点击的控件
	 * @param context
	 * @param handler
	 *            接收返回值的handler
	 */
	public LineMenuPop(View v, Context context, Handler mHandler, String station, int x, int y) {
		this.v = v;
		this.station = station;
		this.mHandler = mHandler;

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_line_menu, null);

		TextView tv_station = (TextView) view.findViewById(R.id.tv_dialog_line_menu_station);
		tv_station.setText(station);
		tv_station.setOnClickListener(this);
		TextView tv_addr_start = (TextView) view.findViewById(R.id.tv_dialog_line_menu_addr_start);
		tv_addr_start.setOnClickListener(this);
		TextView tv_addr_end = (TextView) view.findViewById(R.id.tv_dialog_line_menu_addr_end);
		tv_addr_end.setOnClickListener(this);

		// 设置popwindow弹出大小
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 弹出popwindow
		showAsDropDown(x, y);

		context = null;
	}

	/**
	 * 下拉式 弹出 pop菜单
	 * 
	 * @param parent
	 */
	public void showAsDropDown(int x, int y) {
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		// 设置弹出位置
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, x, y);
	}

	private void dismissPop() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		dismissPop();
		Message msg = new Message();
		switch (v.getId()) {
		case R.id.tv_dialog_line_menu_station:
			// 查看详情
			msg.what = 1;
			msg.obj = station;
			mHandler.sendMessage(msg);
			break;
		case R.id.tv_dialog_line_menu_addr_start:
			// 设置起点
			msg.what = 2;
			msg.obj = station;
			mHandler.sendMessage(msg);
			break;
		case R.id.tv_dialog_line_menu_addr_end:
			// 设置终点
			msg.what = 3;
			msg.obj = station;
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
		dismissPop();
	}
}
