package com.itpoints.njmetro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;

/**
 * 车站公交adapter
 * 
 * @author peidongxu
 * 
 */
public class StationBusAdapter extends BaseAdapter {

	private Context context;
	private String[] arrBus;

	public StationBusAdapter(Context context, String[] arrBus) {
		this.context = context;
		this.arrBus = arrBus;
	}

	@Override
	public int getCount() {
		if (arrBus == null) {
			return 0;
		}
		return arrBus.length;
	}

	@Override
	public Object getItem(int position) {
		return arrBus[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.station_infomation_bus_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_station_infomation_bus_item_bus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_name.setText(arrBus[position]);

		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
	}
}
