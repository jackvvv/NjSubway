package com.itpoints.njmetro.ui.img;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.FBaseUI;
import com.itpoints.njmetro.ui.img.JSKit.ActionCallBack;
import com.itpoints.njmetro.ui.img.utils.SubwayShortPath;
import com.itpoints.njmetro.ui.info.InfoMainUI;
import com.itpoints.njmetro.ui.info.StationDetailUI;
import com.itpoints.njmetro.utils.AnimatorUtil;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.LineMenuPop;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.Utils;

/**
 * 线网图首页
 * 
 * @author peidongxu
 * 
 */
public class ImgMainUI extends FBaseUI implements ActionCallBack, OnTouchListener {
	// 地铁图控件
	private WebView webView;
	private JSKit js;

	// 起点、终点
	private TextView tv_addr_start, tv_addr_end;
	private String addr_start, addr_end;
	// 最优路径、最短路径、重置
	private TextView tv_least_transfer, tv_min_route, tv_reset;
	/** type 1：  最优路径 2： 最短路径 */
	private int type;

	// 价格
	private TextView tv_price;
	// 途经数量
	private TextView tv_station_num;
	// 耗时
	private TextView tv_all_time;

	// 头部菜单，底部结果
	private LinearLayout ll_menu, ll_bottom;
	// 是否显示底部结果线路
	private LinearLayout ll_detail;
	// 结果线路布局
	private View img_line;

	private FragmentTransaction transaction;
	private ImgLineFrament imgLineFrament;

	// 总耗时（分）
	int totol_time = 0;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.img_main);
	}

	@Override
	protected void findView_AddListener() {
		ImageView iv_back = (ImageView) findViewById(R.id.iv_img_main_back);
		iv_back.setOnClickListener(this);

		ImageView iv_fanzhuan = (ImageView) findViewById(R.id.iv_img_main_fanzhuan);
		iv_fanzhuan.setOnClickListener(this);

		tv_addr_start = (TextView) findViewById(R.id.tv_img_main_addr_start);
		tv_addr_start.setOnClickListener(this);
		tv_addr_end = (TextView) findViewById(R.id.tv_img_main_addr_end);
		tv_addr_end.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.wb_img_main);
		webView.setOnTouchListener(this);

		tv_min_route = (TextView) findViewById(R.id.tv_img_main_min_route);
		tv_min_route.setOnClickListener(this);
		tv_least_transfer = (TextView) findViewById(R.id.tv_img_main_least_transfer);
		tv_least_transfer.setOnClickListener(this);
		tv_reset = (TextView) findViewById(R.id.tv_img_main_reset);
		tv_reset.setOnClickListener(this);

		// 展开详情
		ll_detail = (LinearLayout) findViewById(R.id.ll_img_main_open_detail);
		ll_detail.setOnClickListener(this);

		img_line = findViewById(R.id.fragment_img_line);

		ll_menu = (LinearLayout) findViewById(R.id.ll_img_main_menu);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_img_main_bottom);

		tv_price = (TextView) findViewById(R.id.tv_img_main_price);
		tv_station_num = (TextView) findViewById(R.id.tv_img_main_station_num);
		tv_all_time = (TextView) findViewById(R.id.tv_img_main_time);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void prepareData() {

		// 设置结果线路隐藏
		img_line.setVisibility(View.GONE);

		// 实例化js对象
		js = new JSKit(this);
		js.setActionCallBack(this);

		webView.setVisibility(View.VISIBLE);
		// 加载服务器上的页面
		webView.setWebChromeClient(new WebChromeClient());
		
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				webView.loadUrl("javascript: scrollWindow()");
				getNearData();
				super.onPageFinished(view, url);
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.setBackgroundColor(0);
//		webView.scrollTo(1749, 1300);
		webView.addJavascriptInterface(js, "myjs");
		webView.loadUrl("file:///sdcard" + Constants._anex + "/line_img/njmetro.htm");
		
	}

	/**
	 * 最少换乘算法
	 */
	private String getLeastTransfer() {

		getListTransfer("", addr_start, addr_end);

		int num = Integer.MAX_VALUE;

		List<List<String>> listLine = new ArrayList<List<String>>();

		for (int i = 0; i < listAll.size(); i++) {
			if (num > listAll.get(i).size()) {
				num = listAll.get(i).size();
				listLine.clear();
				listLine.add(listAll.get(i));
			} else if (num == listAll.get(i).size()) {
				listLine.add(listAll.get(i));
			}
		}

		List<String> listStation;
		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		if (num == Integer.MAX_VALUE) {
			listStation = new ArrayList<String>();
			listStation.add(addr_start);
			listStation.add(addr_end);
			for (int i = 0; i < listStation.size() - 1; i++) {
				listStationDetailBean.addAll(DbHelper.getInstance(this).getStationLine(listStation.get(i), listStation.get(i + 1)));
			}
		} else {
			List<StationDetailBean> listTempBean;
			for (int i = 0; i < listLine.size(); i++) {
				listStation = listLine.get(i);
				listStation.add(0, addr_start);
				listStation.add(addr_end);
				listTempBean = new ArrayList<StationDetailBean>();
				for (int j = 0; j < listStation.size() - 1; j++) {
					listTempBean.addAll(DbHelper.getInstance(this).getStationLine(listStation.get(j), listStation.get(j + 1)));
				}
				if (listStationDetailBean.size() == 0) {
					listStationDetailBean = listTempBean;
				} else if (listStationDetailBean.size() > listTempBean.size()) {
					listStationDetailBean = listTempBean;
				}
			}
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < listStationDetailBean.size(); i++) {
			stringBuffer.append(listStationDetailBean.get(i).getStation());
			if (i != listStationDetailBean.size() - 1) {
				stringBuffer.append(",");
			}
		}
		return stringBuffer.toString();
	}

	private List<String> listStation = new ArrayList<String>();

	private List<List<String>> listAll = new ArrayList<List<String>>();

	/**
	 * 获取起点、终点经过的线路数据
	 * 
	 * @param addr_start
	 *            起点
	 * @param addr_end
	 *            终点
	 */
	public void getListTransfer(String line, String addr_start, String addr_end) {
		try {
			List<StationDetailBean> listStartBean;
			if (Utils.isEmity(line)) {
				listStartBean = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "station", addr_start);
			} else {
				listStartBean = DbHelper.getInstance(this).getListStartStation(line, addr_start);
			}

			List<StationDetailBean> listLine;
			for (int i = 0; i < listStartBean.size(); i++) {
				listLine = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "line", listStartBean.get(i).getLine());
				for (int j = 0; j < listLine.size(); j++) {
					StationDetailBean lineBean = listLine.get(j);
					if (lineBean.getStation().equals(addr_end)) {
						// 同一线路
						List<String> listTemp = new ArrayList<String>();
						for (int k = 0; k < listStation.size(); k++) {
							listTemp.add(listStation.get(k));
						}
						listAll.add(listTemp);
						return;
					}
				}
			}

			for (int i = 0; i < listStartBean.size(); i++) {
				listLine = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "line", listStartBean.get(i).getLine());
				for (int j = 0; j < listLine.size(); j++) {
					StationDetailBean lineBean = listLine.get(j);
					if (!listStation.contains(lineBean.getStation()) && !Utils.isEmity(lineBean.getTransfer_line())) {
						// 同一线路
						listStation.add(lineBean.getStation());
						getListTransfer(lineBean.getLine(), lineBean.getStation(), addr_end);
						listStation.remove(lineBean.getStation());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.iv_img_main_back:
			back();
			break;
		case R.id.iv_img_main_fanzhuan:
			// 顺序反转
			addr_start = tv_addr_end.getText().toString();
			addr_end = tv_addr_start.getText().toString();
			tv_addr_start.setText(addr_start);
			tv_addr_end.setText(addr_end);

			if (!Utils.isEmity(addr_start) && !Utils.isEmity(addr_end)) {
				exchange();
			} else {
				if (!Utils.isEmity(addr_start)) {
					webView.loadUrl("javascript: reset()");
					setStation("start", addr_start);
				}
				if (!Utils.isEmity(addr_end)) {
					webView.loadUrl("javascript: reset()");
					setStation("end", addr_end);
				}
			}
			break;
		case R.id.ll_img_main_open_detail:
			// 展开详情
			if (img_line.getVisibility() == View.VISIBLE) {
				ll_detail.setSelected(false);
				AnimatorUtil.animateHide(null, img_line);
				AnimatorUtil.animateBack(ll_menu, null);
				img_line.setVisibility(View.GONE);
			} else {
				ll_detail.setSelected(true);
				AnimatorUtil.animateBack(null, img_line);
				AnimatorUtil.animateHide(ll_menu, null);
				img_line.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.tv_img_main_least_transfer:
			// 最优路径
			type = 1;
			tv_least_transfer.setSelected(true);
			tv_min_route.setSelected(false);
			showResult();
			break;
		case R.id.tv_img_main_min_route:
			// 最短路径
			type = 2;
			tv_least_transfer.setSelected(false);
			tv_min_route.setSelected(true);
			showResult();
			break;
		case R.id.tv_img_main_reset:
			// 重置
			AnimatorUtil.animateHide(ll_menu, ll_bottom);
			addr_start = "";
			addr_end = "";
			tv_addr_start.setText(addr_start);
			tv_addr_end.setText(addr_end);

			listStation = new ArrayList<String>();
			listAll = new ArrayList<List<String>>();

			webView.loadUrl("javascript: reset()");
			break;
		case R.id.tv_img_main_addr_start:
			// 选择起点
			Intent start_intent = new Intent(this, StationSelectUI.class);
			start_intent.putExtra("type", "start");
			startActivityForResult(start_intent, 1);
			break;
		case R.id.tv_img_main_addr_end:
			// 选择终点
			Intent end_intent = new Intent(this, StationSelectUI.class);
			end_intent.putExtra("type", "end");
			startActivityForResult(end_intent, 1);
			break;
		default:
			break;
		}
	}

	/**
	 * 切换路线
	 * 
	 */
	public void showResult() {
		String lineplan = "";
		if (1 == type) {
			tv_least_transfer.setSelected(true);
			tv_min_route.setSelected(false);
			String lineplan1 = getLeastTransfer();
			String lineplan2 = SubwayShortPath.getInstance(this).getLinePlan(addr_start, addr_end);
			int lineplanLengh1 = lineplan1.split(",").length - 1;
			int lineplanLengh2 = lineplan2.split(",").length - 1;
			if (lineplanLengh1 - lineplanLengh2 > 3) {
				lineplan = lineplan2;
			} else {
				lineplan = lineplan1;
			}
		} else if (2 == type) {
			tv_least_transfer.setSelected(false);
			tv_min_route.setSelected(true);
			lineplan = SubwayShortPath.getInstance(this).getLinePlan(addr_start, addr_end);
		}
		tv_station_num.setText("途经" + (lineplan.split(",").length - 1) + "个站");
		// 绘制
		webView.loadUrl("javascript: canvasLine('" + lineplan + "')");
		// 设置价格
		String price = DbHelper.getInstance(this).getLinePrice(addr_start, addr_end);
		tv_price.setText(price);
		// 设置时间
		getTime(lineplan);
		// 设置时间轴页面
		selectImgLine(price, lineplan);
	}

	/**
	 * 反转显示结果
	 * 
	 */
	public void exchange() {
		// 赋值
		String lineplan = "";
		if (1 == type) {
			tv_least_transfer.setSelected(true);
			tv_min_route.setSelected(false);
			lineplan = getLeastTransfer();
		} else if (2 == type) {
			tv_least_transfer.setSelected(false);
			tv_min_route.setSelected(true);
			lineplan = SubwayShortPath.getInstance(this).getLinePlan(addr_start, addr_end);
		}
		tv_station_num.setText("途经" + (lineplan.split(",").length - 1) + "个站");
		// 绘制
		webView.loadUrl("javascript: exchange('" + lineplan + "')");
		// 设置价格
		String price = DbHelper.getInstance(this).getLinePrice(addr_start, addr_end);
		tv_price.setText(price);
		// 设置时间
		getTime(lineplan);
		// 设置时间轴页面
		selectImgLine(price, lineplan);
	}

	/**
	 * 显示结果
	 * 
	 * @param start
	 *            起点站
	 * @param end
	 *            终点站
	 */
	@Override
	public void showResult(int index, String start, String end) {
		if (1 == index) {
			// 起点
			addr_start = start;
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					tv_addr_start.setText(addr_start);
				}
			});
		} else if (2 == index) {
			// 终点
			addr_end = end;
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					tv_addr_end.setText(addr_end);
				}
			});
		} else if (3 == index) {
			// 结果
			type = 1;
			addr_start = start;
			addr_end = end;

			// 显示
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 赋值
					tv_addr_start.setText(addr_start);
					tv_addr_end.setText(addr_end);
					ll_menu.setVisibility(View.VISIBLE);
					ll_bottom.setVisibility(View.VISIBLE);
					AnimatorUtil.animateBack(ll_menu, ll_bottom);
					showResult();
				}
			});
		} else if (4 == index) {
			addr_start = start;
			addr_end = end;
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					tv_addr_start.setText(addr_start);
					tv_addr_end.setText(addr_end);
				}
			});
		}
	}

	/**
	 * 获取站点相隔时间
	 */
	private void getTime(String line) {
		String[] arrStation = line.split(",");
		// 线路分割后数据
		ArrayList<ArrayList<String>> listAll = new ArrayList<ArrayList<String>>();

		String station;
		ArrayList<String> listTemp = new ArrayList<String>();
		// 将线路按照换乘站点分割
		for (int i = 0; i < arrStation.length; i++) {
			station = arrStation[i];
			StationDetailBean stationDetailBean = (StationDetailBean) DbHelper.getInstance(this).searchOne(StationDetailBean.class, "station", station);
			if (stationDetailBean != null && Utils.isEmity(stationDetailBean.getTransfer_line())) {
				// 不是换乘线
				listTemp.add(station);
				if (i == arrStation.length - 1) {
					// 最后一个
					if (listTemp.size() > 1) {
						listAll.add(listTemp);
					}
				}
			} else {
				// 是换乘线
				listTemp.add(station);
				if (listTemp.size() > 1) {
					listAll.add(listTemp);
				}
				if (i != arrStation.length - 1) {
					// 不是最后一个
					listTemp = new ArrayList<String>();
					listTemp.add(station);
				}
			}
		}
		// 计算每一段线路时间
		ArrayList<String> arrayList;
		int millisecond;
		totol_time = 0;
		for (int i = 0; i < listAll.size(); i++) {
			arrayList = listAll.get(i);
			millisecond = DbHelper.getInstance(this).getMillisecond(arrayList.get(0), arrayList.get(arrayList.size() - 1));
			totol_time = totol_time + millisecond;
		}
		tv_all_time.setText("耗时" + totol_time + "分钟");
	}

	@Override
	public void showStationDetail(String station) {
		// 查看站点信息
		Intent intent = new Intent(this, StationDetailUI.class);
		intent.putExtra("station", station);
		intent.putExtra("isSet", true);
		startActivityForResult(intent, 1);
	}

	int x = 0;
	int y = 0;

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		x = (int) event.getX();
		y = (int) event.getY();
		// return false;
		return super.onTouchEvent(event);
	}

	@Override
	public void bombBox(String station) {
		if (Utils.isEmity(addr_start) || Utils.isEmity(addr_end)) {
			new LineMenuPop(webView, this, mHandler, station, x, y);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String type = data.getStringExtra("type");
		String station = data.getStringExtra("station");
		// 设置起点。终点
		setStation(type, station);
	}

	/**
	 * 设置起点、终点
	 * 
	 * @param type
	 *            类型
	 * @param station
	 *            站点名
	 */
	private void setStation(String type, String station) {
		String param = "";
		if ("start".equals(type)) {
			addr_start = station;
			tv_addr_start.setText(addr_start);
			param = addr_start + ",";
		} else if ("end".equals(type)) {
			addr_end = station;
			tv_addr_end.setText(addr_end);
			param = "," + addr_end;
		}
		// 绘制
		webView.loadUrl("javascript: appSetSite('" + param + "')");
	}

	/**
	 * 查看时间轴页面
	 * 
	 * @param linePlanBean
	 */
	private void selectImgLine(String price, String lineplan) {
		//
		transaction = this.getSupportFragmentManager().beginTransaction();
		imgLineFrament = new ImgLineFrament();
		Bundle bundle = new Bundle();
		bundle.putString("addr_start", addr_start);
		bundle.putString("addr_end", addr_end);
		bundle.putString("lineplan", lineplan);
		bundle.putString("time", String.valueOf(totol_time));
		bundle.putString("price", price);
		imgLineFrament.setArguments(bundle);
		if (!imgLineFrament.isAdded()) {
			transaction.add(R.id.fragment_img_line, imgLineFrament);
			transaction.commitAllowingStateLoss();
		} else {
			transaction.replace(R.id.fragment_img_line, imgLineFrament);
			transaction.commitAllowingStateLoss();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String station = (String) msg.obj;
			switch (msg.what) {
			case 1:
				// 查看详情
				Intent intent = new Intent(ImgMainUI.this, StationDetailUI.class);
				intent.putExtra("station", station);
				intent.putExtra("isSet", true);
				startActivityForResult(intent, 1);
				break;
			case 2:
				// 设置起点
				setStation("start", station);
				break;
			case 3:
				// 设置终点
				setStation("end", station);
				break;
			default:
				break;
			}
		}
	};

	
	/**
	 * 获取附近站点设置为起点
	 */
	private void getNearData() {
		List<StationDetailBean> listStationDetailBean = DbHelper.getInstance(this).search(StationDetailBean.class);
		StationDetailBean stationDetailBean = null;
		StationDetailBean tempBean;
		double longitude1 = Double.parseDouble(MyConfig.getConfig(this, "config", "lon", ""));
		double latitude1 = Double.parseDouble(MyConfig.getConfig(this, "config", "lat", ""));
		double minValue = 0;
		double temp = 0;
		for (int i = 0; i < listStationDetailBean.size(); i++) {
			tempBean = listStationDetailBean.get(i);
			temp = Utils.getDistance(longitude1, latitude1, Double.parseDouble(tempBean.getLon().replace(",", "")), Double.parseDouble(tempBean.getLat().replace(",", "")));
			if (temp <= minValue || i == 0) {
				minValue = temp;
				stationDetailBean = tempBean;
			}
		}
		if (stationDetailBean == null) {
			stationDetailBean = new StationDetailBean();
		}
		final String station = stationDetailBean.getStation();
		setStation("start", station);
	}
	
	@Override
	protected void back() {
		finish();
	}

}
