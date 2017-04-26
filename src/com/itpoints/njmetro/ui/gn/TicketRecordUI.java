package com.itpoints.njmetro.ui.gn;

import android.view.View;
import android.widget.ListView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.TicketRecordListAdapter;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 购票记录
 * 
 * @author peidongxu
 * 
 */
public class TicketRecordUI extends BaseUI {
	private ListView listView;
	private TicketRecordListAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.ticket_record_list);
	}

	@Override
	protected void findView_AddListener() {
		listView = (ListView) findViewById(R.id.lv_ticket_record_list);
	}

	@Override
	protected void prepareData() {
		setTitle("我的购票记录");

		adapter = new TicketRecordListAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	protected void back() {
		finish();
	}

}
