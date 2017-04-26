package com.itpoints.njmetro.ui.info;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.LineAdapter;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 线路详情
 * 
 * @author peidongxu
 * 
 */
public class LineUI extends BaseUI implements OnItemClickListener {

	private String line;
	private List<StationDetailBean> listStationDetailBean;
	private ListView listView;
	private LineAdapter lineAdapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.line_detail);
	}

	@Override
	protected void findView_AddListener() {
		listView = (ListView) findViewById(R.id.lv_line_detail);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		line = intent.getStringExtra("line");
		setTitle(line);

		listStationDetailBean = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "line", line);

		lineAdapter = new LineAdapter(this, listStationDetailBean);
		listView.setAdapter(lineAdapter);

	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		StationDetailBean stationDetailBean = listStationDetailBean.get(position);
		Intent intent = new Intent(this, StationDetailUI.class);
		intent.putExtra("station", stationDetailBean.getStation());
		startActivity(intent);
	}

	@Override
	protected void back() {
		finish();
	}

}
