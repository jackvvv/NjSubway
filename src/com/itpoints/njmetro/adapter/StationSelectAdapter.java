package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.db.DbHelper;

public class StationSelectAdapter extends BaseAdapter {
	private Context context;
	private List<StationDetailBean> listStationDetailBean;

	public StationSelectAdapter(Context context, List<StationDetailBean> listStationDetailBean) {
		this.context = context;
		this.listStationDetailBean = listStationDetailBean;
	}
	
	public void setData(List<StationDetailBean> listStationDetailBean){
		this.listStationDetailBean = listStationDetailBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listStationDetailBean == null)
			return 0;
		return listStationDetailBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listStationDetailBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.station_select_item, null);
			holder.tv_station = (TextView) convertView.findViewById(R.id.tv_station_select_item_station);
			holder.tv_station_en = (TextView) convertView.findViewById(R.id.tv_station_select_item_station_en);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationDetailBean stationDetailBean = listStationDetailBean.get(position);

		holder.tv_station.setText(stationDetailBean.getStation());
		StationEnBean stationEnBean = (StationEnBean) DbHelper.getInstance(context).searchOne(StationEnBean.class, "station", stationDetailBean.getStation());
		if (stationEnBean!=null) {
			holder.tv_station_en.setText(stationEnBean.getStation_en());
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_station;
		TextView tv_station_en;
	}
}
