package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;

/**
 * 站点出口adapter
 * 
 * @author peidongxu
 * 
 */
public class StationDetailAdapter extends BaseAdapter {
	private Context context;
	private String station;
	private List<StationTimeBean> listStationTimeBean;

	public StationDetailAdapter(Context context, String station, List<StationTimeBean> listStationTimeBean) {
		this.context = context;
		this.station = station;
		this.listStationTimeBean = listStationTimeBean;
	}

	@Override
	public int getCount() {
		if (listStationTimeBean == null) {
			return 0;
		}
		return listStationTimeBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listStationTimeBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.station_detail_item, null);
			holder.tv_line_logo_up = (TextView) convertView.findViewById(R.id.tv_station_detail_item_logo_up);
			holder.tv_direction_up = (TextView) convertView.findViewById(R.id.tv_station_detail_item_direction_up);
			holder.tv_time_up = (TextView) convertView.findViewById(R.id.tv_station_detail_item_time_up);
			holder.tv_line_logo_down = (TextView) convertView.findViewById(R.id.tv_station_detail_item_logo_down);
			holder.tv_direction_down = (TextView) convertView.findViewById(R.id.tv_station_detail_item_direction_down);
			holder.tv_time_down = (TextView) convertView.findViewById(R.id.tv_station_detail_item_time_down);
			holder.ll_up = (LinearLayout) convertView.findViewById(R.id.ll_station_detail_item_up);
			holder.ll_down = (LinearLayout) convertView.findViewById(R.id.ll_station_detail_item_down);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationTimeBean stationTimeBean = listStationTimeBean.get(position);

		String line = stationTimeBean.getLine();

		List<StationDetailBean> listStationDetailBean = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", line);

		if (!station.equals(listStationDetailBean.get(0).getStation())) {
			holder.tv_line_logo_up.setBackgroundResource(getIds()[getLineIndex(line)]);
			holder.tv_direction_up.setText(listStationDetailBean.get(0).getStation());
			holder.tv_time_up.setText(stationTimeBean.getTime_up());
			holder.ll_up.setVisibility(View.VISIBLE);
		} else {
			holder.ll_up.setVisibility(View.GONE);
		}

		if (!station.equals(listStationDetailBean.get(listStationDetailBean.size() - 1).getStation())) {
			holder.tv_line_logo_down.setBackgroundResource(getIds()[getLineIndex(line)]);
			holder.tv_direction_down.setText(listStationDetailBean.get(listStationDetailBean.size() - 1).getStation());
			holder.tv_time_down.setText(stationTimeBean.getTime_down());
			holder.ll_down.setVisibility(View.VISIBLE);
		} else {
			holder.ll_down.setVisibility(View.GONE);
		}
		return convertView;
	}

	private int getLineIndex(String line) {
		String[] arrLineName = getLineName();
		int index = 0;
		for (int i = 0; i < arrLineName.length; i++) {
			if (line.equals(arrLineName[i])) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineName() {
		return new String[] { "1号线", "2号线", "3号线", "10号线", "S1机场线", "S8宁天城际" };
	}

	/**
	 * 获取图片资源ID
	 * 
	 * @return
	 */
	private int[] getIds() {
		return new int[] { R.drawable.station_detail_1, R.drawable.station_detail_2, R.drawable.station_detail_3, R.drawable.station_detail_10, R.drawable.station_detail_s1, R.drawable.station_detail_s8 };
	}

	private class ViewHolder {
		TextView tv_line_logo_up;
		TextView tv_direction_up;
		TextView tv_time_up;
		TextView tv_line_logo_down;
		TextView tv_direction_down;
		TextView tv_time_down;
		LinearLayout ll_up;
		LinearLayout ll_down;
	}

}
