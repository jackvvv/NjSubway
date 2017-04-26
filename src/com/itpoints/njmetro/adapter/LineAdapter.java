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

/**
 * adapter
 * 
 * @author peidongxu
 * 
 */
public class LineAdapter extends BaseAdapter {
	private Context context;
	private List<StationDetailBean> listStationDetailBean;

	public LineAdapter(Context context, List<StationDetailBean> listStationDetailBean) {
		this.context = context;
		this.listStationDetailBean = listStationDetailBean;
	}

	@Override
	public int getCount() {
		if (listStationDetailBean == null) {
			return 0;
		}
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
		View view = LayoutInflater.from(context).inflate(R.layout.line_detail_item, null);
		TextView tv_station = (TextView) view.findViewById(R.id.tv_line_detail_item_station);
		TextView tv_station_en = (TextView) view.findViewById(R.id.tv_line_detail_item_station_en);

		StationDetailBean stationDetailBean = listStationDetailBean.get(position);

		tv_station.setText(stationDetailBean.getStation());
		StationEnBean stationEnBean = (StationEnBean) DbHelper.getInstance(context).searchOne(StationEnBean.class, "station", stationDetailBean.getStation());
		if (stationEnBean != null) {
			tv_station_en.setText(stationEnBean.getStation_en());
		}
		// ViewHolder holder;
		// if (convertView == null) {
		// holder = new ViewHolder();
		// convertView =
		// LayoutInflater.from(context).inflate(R.layout.line_detail_item,
		// null);
		// holder.tv_station = (TextView)
		// convertView.findViewById(R.id.tv_line_detail_item_station);
		// holder.tv_station_en = (TextView)
		// convertView.findViewById(R.id.tv_line_detail_item_station_en);
		// holder.tv_station_en.setTag(position);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		//
		// StationDetailBean stationDetailBean =
		// listStationDetailBean.get(position);
		//
		// holder.tv_station.setText(stationDetailBean.getStation());
		// StationEnBean stationEnBean = (StationEnBean)
		// DbHelper.getInstance(context).searchOne(StationEnBean.class,
		// "station", stationDetailBean.getStation());
		// if (stationEnBean != null) {
		// holder.tv_station_en.setTag(stationEnBean.getStation_en());
		// if (holder.tv_station_en.getTag() != null &&
		// holder.tv_station_en.getTag().equals(stationEnBean.getStation_en()))
		// {
		// holder.tv_station_en.setText(stationEnBean.getStation_en());
		// }
		// }

		return view;
	}

	private class ViewHolder {
		TextView tv_station;
		TextView tv_station_en;
	}

}
