package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.MyGridView;

/**
 * 车站出口信息adapter
 * 
 * @author peidongxu
 * 
 */
public class StationInfoMationAdapter extends BaseAdapter {

	private Context context;
	private List<StationInfoMationBean> listStationInfoMationBean;

	public StationInfoMationAdapter(Context context, List<StationInfoMationBean> listStationInfoMationBean) {
		this.context = context;
		this.listStationInfoMationBean = listStationInfoMationBean;
	}

	@Override
	public int getCount() {
		if (listStationInfoMationBean == null) {
			return 0;
		}
		return listStationInfoMationBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listStationInfoMationBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.station_infomation_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_station_infomation_item_name);
			holder.tv_lankmark = (TextView) convertView.findViewById(R.id.tv_station_infomation_item_landmark);
			holder.tv_bus = (TextView) convertView.findViewById(R.id.tv_station_infomation_bus);
			holder.gv_bus = (MyGridView) convertView.findViewById(R.id.gv_station_infomation_bus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationInfoMationBean stationInfoMationBean = listStationInfoMationBean.get(position);

		holder.tv_name.setText(stationInfoMationBean.getName());
		holder.tv_lankmark.setText(stationInfoMationBean.getLandmark());

		String bus = stationInfoMationBean.getBus();

		if (!Utils.isEmity(bus)) {
			holder.tv_bus.setText(bus);
//			StationBusAdapter busAdapter = new StationBusAdapter(context, bus.split(","));
//			holder.gv_bus.setAdapter(busAdapter);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
		TextView tv_lankmark;
		TextView tv_bus;
		MyGridView gv_bus;
	}
}
