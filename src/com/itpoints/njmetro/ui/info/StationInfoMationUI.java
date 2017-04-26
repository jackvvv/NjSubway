package com.itpoints.njmetro.ui.info;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.StationInfoMationAdapter;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;
/**
 * 车站出口信息
 * @author peidongxu
 *
 */
public class StationInfoMationUI extends BaseUI {
	
	private ListView mListView;
	private List<StationInfoMationBean> listStationInfoMationBean;
	private StationInfoMationAdapter adapter;
	

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_infomation);
	}

	@Override
	protected void findView_AddListener() {
		mListView = (ListView) findViewById(R.id.lv_station_infomation);

	}

	@Override
	protected void prepareData() {
		setTitle("车站出口信息");

		Intent intent = getIntent();
		String station = intent.getStringExtra("station");

		listStationInfoMationBean = new ArrayList<StationInfoMationBean>();

		listStationInfoMationBean = DbHelper.getInstance(this).searchCriteria(StationInfoMationBean.class, "station", station);

		adapter = new StationInfoMationAdapter(this, listStationInfoMationBean);
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
