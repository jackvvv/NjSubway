package com.itpoints.njmetro.ui.info;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 车站设施
 * 
 * @author peidongxu
 * 
 */
public class StationFacilitiesTypeUI extends BaseUI {

	private String station;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_facilities_type);
	}

	@Override
	protected void findView_AddListener() {
		setListen();
	}

	@Override
	protected void prepareData() {
		setTitle("车站设施");

		Intent intent = getIntent();
		station = intent.getStringExtra("station");

	}

	@Override
	protected void onMyClick(View v) {
		int[] ids = getIds();
		List<StationFacilityBean> listStationFacilityBean;
		for (int i = 0; i < ids.length; i++) {
			if (v.getId() == ids[i]) {
				listStationFacilityBean = DbHelper.getInstance(this).getFacility(station, getNames()[i]);
				if (listStationFacilityBean != null && listStationFacilityBean.size() > 0) {
					Intent intent = new Intent(this, StationFacilityUI.class);
					intent.putExtra("station", station);
					intent.putExtra("name_type", getNames()[i]);
					startActivity(intent);
				} else {
					MyApplication.getInstance().showToast("没有相关设施");
				}
			}
		}
	}

	private void setListen() {
		int[] ids = getIds();
		TextView tv;
		for (int i = 0; i < ids.length; i++) {
			tv = (TextView) findViewById(ids[i]);
			tv.setOnClickListener(this);
		}
	}

	private String[] getNames() {
		return new String[] { "洗手间", "便利店", "电梯,轮椅升降", "充值", "ATM机", "自助缴费", "自动售货", "自助证件", "书报", "餐饮", "综合业务点" };
	}

	private int[] getIds() {
		return new int[] { R.id.tv_station_facilities_type_1, R.id.tv_station_facilities_type_2, R.id.tv_station_facilities_type_3, R.id.tv_station_facilities_type_4, R.id.tv_station_facilities_type_5, R.id.tv_station_facilities_type_6, R.id.tv_station_facilities_type_7, R.id.tv_station_facilities_type_8, R.id.tv_station_facilities_type_9, R.id.tv_station_facilities_type_10, R.id.tv_station_facilities_type_11 };
	}

	@Override
	protected void back() {
		finish();
	}

}
