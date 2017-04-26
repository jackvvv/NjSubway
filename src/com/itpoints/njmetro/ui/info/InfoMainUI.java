package com.itpoints.njmetro.ui.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.InfoMainLineAdapter;
import com.itpoints.njmetro.adapter.InfoMainSortAdapter;
import com.itpoints.njmetro.adapter.SearchDibiaoAdapter;
import com.itpoints.njmetro.adapter.SearchStationAdapter;
import com.itpoints.njmetro.bean.LineBean;
import com.itpoints.njmetro.bean.SortModel;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.CharacterParser;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.PinyinComparator;
import com.itpoints.njmetro.utils.Utils;

/**
 * 站点信息首页
 * 
 * @author peidongxu
 * 
 */
public class InfoMainUI extends BaseUI implements OnItemClickListener, OnTouchListener, TextWatcher, OnKeyListener {

	private RelativeLayout rl_chezhan, rl_dibiao;

	private EditText et_search;
	private TextView tv_back;
	private LinearLayout ll_header;
	private ListView lv_search_result;
	private List<SortModel> listDibiao;
	private SearchStationAdapter stationAdapter;
	private SearchDibiaoAdapter dibiaoAdapter;

	private TextView tv_tip;
	/** 类型 ： 1： 线路 2：出口地标 */
	private int type;

	private ListView lv_data;
	private List<LineBean> listLineBean;
	private InfoMainLineAdapter lineAdapter;
	private List<StationDetailBean> listStationDetailBean;
	private List<StationInfoMationBean> listStationInfoMationBean;
	// 汉字转换成拼音的类
	private CharacterParser characterParser;
	private List<SortModel> listSourceDate;
	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator;
	private InfoMainSortAdapter sortAdapter;

	private LinearLayout ll_near_all;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.info_main);
	}

	@Override
	protected void findView_AddListener() {
		ll_header = (LinearLayout) findViewById(R.id.ll_info_header);

		rl_chezhan = (RelativeLayout) findViewById(R.id.rl_info_mian_tab_chezhan);
		rl_chezhan.setOnClickListener(this);
		rl_dibiao = (RelativeLayout) findViewById(R.id.rl_info_mian_tab_dibiao);
		rl_dibiao.setOnClickListener(this);

		et_search = (EditText) findViewById(R.id.et_info_main_search);
		et_search.addTextChangedListener(this);
		et_search.setOnKeyListener(this);
		et_search.setOnTouchListener(this);
		tv_back = (TextView) findViewById(R.id.tv_info_main_back);
		tv_back.setOnClickListener(this);
		lv_search_result = (ListView) findViewById(R.id.lv_info_main_search_result);
		lv_search_result.setOnItemClickListener(onItemClickListener);

		tv_tip = (TextView) findViewById(R.id.tv_info_main_tip);

		lv_data = (ListView) findViewById(R.id.lv_info_main);
		lv_data.setOnItemClickListener(this);

		ll_near_all = (LinearLayout) findViewById(R.id.ll_info_main_near);
	}

	@Override
	protected void prepareData() {
		setTitle("搜索站点");

		rl_chezhan.setSelected(true);
		rl_dibiao.setSelected(false);
		type = 1;
		tv_tip.setText("按地铁线路查询");

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		listLineBean = DbHelper.getInstance(this).search(LineBean.class);
		lineAdapter = new InfoMainLineAdapter(this, listLineBean);
		lv_data.setAdapter(lineAdapter);
		setNearData();

	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.rl_info_mian_tab_chezhan:
			// 车站
			type = 1;
			tv_tip.setText("按地铁线路查询");
			et_search.setHint("请输入站点名称");
			rl_chezhan.setSelected(true);
			rl_dibiao.setSelected(false);
			setNearData();
			listLineBean = DbHelper.getInstance(this).search(LineBean.class);
			lineAdapter = new InfoMainLineAdapter(this, listLineBean);
			lv_data.setAdapter(lineAdapter);
			break;
		case R.id.rl_info_mian_tab_dibiao:
			// 地标
			type = 2;
			ll_near_all.setVisibility(View.GONE);
			tv_tip.setText("按出口地标查询");
			et_search.setHint("请输入出口地标");
			rl_chezhan.setSelected(false);
			rl_dibiao.setSelected(true);
			if (listSourceDate == null) {
				listSourceDate = filledData();
				// 根据a-z进行排序源数据
				Collections.sort(listSourceDate, pinyinComparator);

				listDibiao = new ArrayList<SortModel>();
				SortModel sortModel;

				List<String> listTemp = new ArrayList<String>();

				for (int i = 0; i < listSourceDate.size(); i++) {
					sortModel = listSourceDate.get(i);
					if (!listTemp.contains(sortModel.getLandmark())) {
						listDibiao.add(sortModel);
					}
					listTemp.add(sortModel.getLandmark());
				}
			}
			sortAdapter = new InfoMainSortAdapter(this, listDibiao);
			lv_data.setAdapter(sortAdapter);
			break;
		case R.id.tv_info_main_back:
			// 取消搜索
			cancleSearch();
			ll_near_all.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData() {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		listStationInfoMationBean = DbHelper.getInstance(this).search(StationInfoMationBean.class);
		if (listStationInfoMationBean == null) {
			return mSortList;
		}
		StationInfoMationBean stationInfoMationBean;
		for (int i = 0; i < listStationInfoMationBean.size(); i++) {
			stationInfoMationBean = listStationInfoMationBean.get(i);
			String landmark = stationInfoMationBean.getLandmark();
			if (!Utils.isEmity(landmark)) {
				String[] arrLandMark = landmark.split(",");
				SortModel sortModel = new SortModel();
				sortModel.setLine(stationInfoMationBean.getLine());
				sortModel.setBus(stationInfoMationBean.getBus());
				sortModel.setName(stationInfoMationBean.getName());
				sortModel.setStation(stationInfoMationBean.getStation());
				sortModel.setStation_en(stationInfoMationBean.getStation_en());
				for (int j = 0; j < arrLandMark.length; j++) {
					sortModel.setLandmark(arrLandMark[j].replace("。", ""));
					// 汉字转换成拼音
					String pinyin = characterParser.getSelling(arrLandMark[j].trim());
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						sortModel.setSortLetters(sortString.toUpperCase());
					} else {
						sortModel.setSortLetters("#");
					}
					mSortList.add(sortModel);
				}
			}
		}
		return mSortList;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent;
		if (1 == type) {
			// 线路点击
			LineBean lineBean = listLineBean.get(position);
			intent = new Intent(InfoMainUI.this, LineUI.class);
			intent.putExtra("line", lineBean.getLine());
			startActivity(intent);
		} else if (2 == type) {
			// 出口地标点击
			SortModel sortModel = listDibiao.get(position);
			intent = new Intent(InfoMainUI.this, StationDetailUI.class);
			intent.putExtra("station", sortModel.getStation());
			startActivity(intent);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		tv_back.setVisibility(View.VISIBLE);
		lv_search_result.setVisibility(View.VISIBLE);
		ll_header.setVisibility(View.GONE);
		tv_tip.setVisibility(View.GONE);
		lv_data.setVisibility(View.GONE);
		ll_near_all.setVisibility(View.GONE);
		et_search.requestFocus();// 获取焦点
		lv_search_result.setAdapter(null);
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		String tempName = et_search.getText().toString();
		if (!Utils.isEmity(tempName)) {
			queryData(tempName);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
			// 先隐藏键盘
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			String tempName = et_search.getText().toString().trim();
			queryData(tempName);
		}
		return false;
	}

	/**
	 * 查询历史模糊搜索数据
	 */
	private void queryData(String tempName) {
		if (1 == type) {
			// 搜索站点
			if (Utils.isEmity(tempName)) {
				listStationDetailBean = new ArrayList<StationDetailBean>();
			} else {
				listStationDetailBean = DbHelper.getInstance(this).searchLikeCriteria(StationDetailBean.class, "station", tempName);
			}
			stationAdapter = new SearchStationAdapter(this, listStationDetailBean);
			lv_search_result.setAdapter(stationAdapter);
		} else if (2 == type) {
			// 搜索地标
			if (Utils.isEmity(tempName)) {
				listDibiao = new ArrayList<SortModel>();
			} else {
				listDibiao = new ArrayList<SortModel>();
				if (listSourceDate == null) {
					listSourceDate = filledData();
					// 根据a-z进行排序源数据
					Collections.sort(listSourceDate, pinyinComparator);
				}

				SortModel sortModel;
				for (int i = 0; i < listSourceDate.size(); i++) {
					sortModel = listSourceDate.get(i);
					if (sortModel.getLandmark().contains(tempName)) {
						listDibiao.add(sortModel);
					}
				}
			}

			dibiaoAdapter = new SearchDibiaoAdapter(this, listDibiao);
			lv_search_result.setAdapter(dibiaoAdapter);
		}
	}

	private void cancleSearch() {
		tv_back.setVisibility(View.GONE);
		lv_search_result.setVisibility(View.GONE);
		ll_header.setVisibility(View.VISIBLE);
		tv_tip.setVisibility(View.VISIBLE);
		lv_data.setVisibility(View.VISIBLE);
		et_search.setText("");
		// 先隐藏键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			cancleSearch();
			Intent intent = new Intent(InfoMainUI.this, StationDetailUI.class);
			if (1 == type) {
				// 搜索站点
				StationDetailBean stationDetailBean = listStationDetailBean.get(position);
				intent.putExtra("station", stationDetailBean.getStation());
			} else if (2 == type) {
				// 搜索地标
				SortModel sortModel = listDibiao.get(position);
				intent.putExtra("station", sortModel.getStation());
			}
			startActivity(intent);
		}
	};

	/**
	 * 设置附近站点
	 */
	private void setNearData() {
		ll_near_all.setVisibility(View.VISIBLE);

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
		// 线路背景
		RelativeLayout rl_line_bg = (RelativeLayout) findViewById(R.id.rl_info_main_near_bg);
		if (!Utils.isEmity(station)) {
			rl_line_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(InfoMainUI.this, StationDetailUI.class);
					intent.putExtra("station", station);
					startActivity(intent);
				}
			});
		}
		// 线路logo
		ImageView iv_line_logo = (ImageView) findViewById(R.id.iv_info_main_near_line_logo);
		TextView tv_line_station = (TextView) findViewById(R.id.tv_info_main_near_station);
		TextView tv_line_station_distance = (TextView) findViewById(R.id.tv_info_main_near_station_distance);

		rl_line_bg.setBackgroundResource(getLineBg()[getLineIndex(stationDetailBean.getLine())]);
		iv_line_logo.setBackgroundResource(getLineLogo()[getLineIndex(stationDetailBean.getLine())]);
		tv_line_station.setText(stationDetailBean.getStation());
		tv_line_station_distance.setText("距离当前位置大约" + minValue + "米");

	}

	private int getLineIndex(String line) {
		String[] arrLineName = getLineName();
		int index = 0;
		for (int i = 0; i < arrLineName.length; i++) {
			if (line.equals(arrLineName[i])) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineName() {
		return new String[] { "1号线", "2号线", "3号线", "10号线", "S1机场线", "S8宁天城际" };
	}

	private int[] getLineBg() {
		return new int[] { R.drawable.station_select_line_1, R.drawable.station_select_line_2, R.drawable.station_select_line_3, R.drawable.station_select_line_10, R.drawable.station_select_line_s1, R.drawable.station_select_line_s8 };
	}

	private int[] getLineLogo() {
		return new int[] { R.drawable.station_detail_logo_1, R.drawable.station_detail_logo_2, R.drawable.station_detail_logo_3, R.drawable.station_detail_logo_10, R.drawable.station_detail_logo_s1, R.drawable.station_detail_logo_s8 };
	}

	@Override
	protected void back() {
		finish();
	}

}
