package com.itpoints.njmetro.ui.info;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.StationFacilityAdapter;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 车站设施
 * 
 * @author peidongxu
 * 
 */
public class StationFacilityUI extends BaseUI {

	private ListView mListView;
	private List<StationFacilityBean> listStationFacilityBean;
	private StationFacilityAdapter adapter;

	private String station;// 站点名称
	private String name_type;// 设施分类

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_facilities);
	}

	@Override
	protected void findView_AddListener() {
		mListView = (ListView) findViewById(R.id.lv_station_facilities);
	}

	@Override
	protected void prepareData() {
		setTitle("车站设施");

		Intent intent = getIntent();
		station = intent.getStringExtra("station");
		name_type = intent.getStringExtra("name_type");
		

		listStationFacilityBean = new ArrayList<StationFacilityBean>();

		listStationFacilityBean = DbHelper.getInstance(this).getFacility(station, name_type);

		adapter = new StationFacilityAdapter(this, listStationFacilityBean);
		mListView.setAdapter(adapter);
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	protected void back() {
		finish();
	}

}
