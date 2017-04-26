package com.itpoints.njmetro.ui.img;

import java.util.ArrayList;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.SearchDibiaoAdapter;
import com.itpoints.njmetro.adapter.SearchStationAdapter;
import com.itpoints.njmetro.adapter.StationSelectAdapter;
import com.itpoints.njmetro.bean.SortModel;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.ui.info.StationDetailUI;
import com.itpoints.njmetro.utils.Utils;

/**
 * 站点选择
 * 
 * @author peidongxu
 * 
 */
public class StationSelectUI extends BaseUI implements OnItemClickListener, OnTouchListener, TextWatcher, OnKeyListener {

	private String type;// 类型： start,end
	private ListView mListView;
	private List<StationDetailBean> listStationDetailBean;
	private StationSelectAdapter adapter;

	private EditText et_search;
	private TextView tv_back;
	private LinearLayout ll_header;
	private LinearLayout ll_bottom;
	private LinearLayout ll_search;
	private ListView lv_search_result;
	private SearchStationAdapter stationAdapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_select);
	}

	@Override
	protected void findView_AddListener() {
		ll_header = (LinearLayout) findViewById(R.id.ll_station_select_header);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_station_select_bottom);
		et_search = (EditText) findViewById(R.id.et_station_select_search);
		et_search.addTextChangedListener(this);
		et_search.setOnKeyListener(this);
		et_search.setOnTouchListener(this);
		tv_back = (TextView) findViewById(R.id.tv_station_select_back);
		tv_back.setOnClickListener(onClickListener);
		lv_search_result = (ListView) findViewById(R.id.lv_station_select_search_result);
		lv_search_result.setOnItemClickListener(onItemClickListener);

		mListView = (ListView) findViewById(R.id.lv_station_select);
		mListView.setOnItemClickListener(this);

		setListening();
	}

	@Override
	protected void prepareData() {
		setTitle("站点名");

		Intent intent = getIntent();
		type = intent.getStringExtra("type");

		listStationDetailBean = new ArrayList<StationDetailBean>();
		adapter = new StationSelectAdapter(this, listStationDetailBean);
		mListView.setAdapter(adapter);

		// 设置默认
		setLineShow(R.id.tv_station_select_line_1);
	}

	@Override
	protected void onMyClick(View v) {
		setLineShow(v.getId());
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 取消搜索
			cancleSearch();
		}
	};

	/**
	 * 设置点击事件
	 */
	private void setListening() {
		int[] lineIds = getLineIds();
		View view;
		for (int i = 0; i < lineIds.length; i++) {
			view = findViewById(lineIds[i]);
			view.setOnClickListener(this);
		}
	}

	/**
	 * 设置当前线路显示
	 * 
	 * @param res_id
	 *            当前点击线路
	 */
	private void setLineShow(int res_id) {

		int[] lineIds = getLineIds();
		int[] lineBigIds = getLineBigIds();
		String line = "";
		for (int i = 0; i < lineIds.length; i++) {
			if (res_id == lineIds[i]) {
				// 点击的那个：小的隐藏，大的显示
				findViewById(lineIds[i]).setVisibility(View.GONE);
				findViewById(lineBigIds[i]).setVisibility(View.VISIBLE);
				line = getLineName()[i];
			} else {
				findViewById(lineIds[i]).setVisibility(View.VISIBLE);
				findViewById(lineBigIds[i]).setVisibility(View.GONE);
			}
		}

		listStationDetailBean = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "line", line);
		adapter.setData(listStationDetailBean);
	}

	/**
	 * 左侧线路小的标签
	 * 
	 * @return
	 */
	private int[] getLineIds() {
		return new int[] { R.id.tv_station_select_line_1, R.id.tv_station_select_line_2, R.id.tv_station_select_line_3, R.id.tv_station_select_line_10, R.id.tv_station_select_line_s1, R.id.tv_station_select_line_s8 };
	}

	/**
	 * 左侧线路大的标签
	 * 
	 * @return
	 */
	private int[] getLineBigIds() {
		return new int[] { R.id.ll_station_select_line_1_big, R.id.ll_station_select_line_2_big, R.id.ll_station_select_line_3_big, R.id.ll_station_select_line_10_big, R.id.ll_station_select_line_s1_big, R.id.ll_station_select_line_s8_big };
	}

	/**
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineName() {
		return new String[] { "1号线", "2号线", "3号线", "10号线", "S1机场线", "S8宁天城际" };
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		StationDetailBean stationDetailBean = listStationDetailBean.get(position);
		Intent intent = new Intent();
		intent.putExtra("type", type);
		intent.putExtra("station", stationDetailBean.getStation());
		setResult(1, intent);
		back();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		tv_back.setVisibility(View.VISIBLE);
		lv_search_result.setVisibility(View.VISIBLE);
		ll_header.setVisibility(View.GONE);
		ll_bottom.setVisibility(View.GONE);
		et_search.requestFocus();// 获取焦点
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
		// 搜索站点
		if (Utils.isEmity(tempName)) {
			listStationDetailBean = new ArrayList<StationDetailBean>();
		} else {
			listStationDetailBean = DbHelper.getInstance(this).searchLikeCriteria(StationDetailBean.class, "station", tempName);
		}
		stationAdapter = new SearchStationAdapter(this, listStationDetailBean);
		lv_search_result.setAdapter(stationAdapter);
	}

	private void cancleSearch() {
		tv_back.setVisibility(View.GONE);
		lv_search_result.setVisibility(View.GONE);
		ll_header.setVisibility(View.VISIBLE);
		ll_bottom.setVisibility(View.VISIBLE);
		et_search.setText("");
		// 先隐藏键盘
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			cancleSearch();
			Intent intent = new Intent(StationSelectUI.this, StationDetailUI.class);
			intent.putExtra("type", type);
			StationDetailBean stationDetailBean = listStationDetailBean.get(position);
			intent.putExtra("station", stationDetailBean.getStation());
			setResult(1, intent);
			back();
		}
	};

	@Override
	protected void back() {
		finish();
	}

}
