package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.utils.Utils;

/**
 * 线路时间轴adapter
 * 
 * @author peidongxu
 * 
 */
public class TimeLineAdapter extends BaseAdapter {

	private Context context;
	private List<StationDetailBean> listStationDetailBean;
	private List<String> listTime;

	public TimeLineAdapter(Context context, List<StationDetailBean> listStationDetailBean, List<String> listTime) {
		this.context = context;
		this.listStationDetailBean = listStationDetailBean;
		this.listTime = listTime;
	}

	public void setData(List<StationDetailBean> listStationDetailBean, List<String> listTime) {
		this.listStationDetailBean = listStationDetailBean;
		this.listTime = listTime;
		notifyDataSetChanged();
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

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.time_line_item, null);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_line_item_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_time_line_item_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_time_line_item_content);
			holder.iv_point = (ImageView) convertView.findViewById(R.id.iv_time_line_item_point);
			holder.line_down = (View) convertView.findViewById(R.id.view_time_line_item_down);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StationDetailBean stationDetailBean = listStationDetailBean.get(position);
		String line = stationDetailBean.getLine();
		// 站点名称
		String station = stationDetailBean.getStation();
		holder.tv_name.setText(station);

		String transfer_line = stationDetailBean.getTransfer_line();
		if (!Utils.isEmity(transfer_line)) {
			// 换乘
			holder.tv_content.setText("换乘" + stationDetailBean.getLine());
			holder.tv_content.setVisibility(View.VISIBLE);
			holder.iv_point.setBackgroundResource(getIdTransfer()[getLineIndex(line)]);
		} else {
			// 不是换乘
			holder.tv_content.setVisibility(View.GONE);
			holder.iv_point.setBackgroundResource(getIdPoint()[getLineIndex(line)]);
		}
		if (position == listStationDetailBean.size() - 1) {
			// 最后一个
			holder.line_down.setVisibility(View.INVISIBLE);

		} else {
			holder.line_down.setVisibility(View.VISIBLE);
			holder.line_down.setBackgroundResource(getIdLine()[getLineIndex(line)]);
		}

		if (position == 0) {
			// 第一个，起点
			holder.tv_time.setText(listTime.get(position));
			holder.tv_time.setVisibility(View.VISIBLE);
		} else if (position == listStationDetailBean.size() - 1) {
			// 最后一个，终点
			holder.tv_time.setText(listTime.get(position));
			holder.tv_time.setVisibility(View.VISIBLE);
		} else {
			// 途经站点
			holder.tv_time.setVisibility(View.INVISIBLE);
			if (!Utils.isEmity(transfer_line)) {
				// 换乘
				holder.tv_time.setText(listTime.get(position));
				holder.tv_time.setVisibility(View.VISIBLE);
			}
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

	public int[] getIdLine() {
		return new int[] { R.drawable.vertical_line_1, R.drawable.vertical_line_2, R.drawable.vertical_line_3, R.drawable.vertical_line_10, R.drawable.vertical_line_s1, R.drawable.vertical_line_s8 };
	}

	public int[] getIdPoint() {
		return new int[] { R.drawable.station_point_1, R.drawable.station_point_2, R.drawable.station_point_3, R.drawable.station_point_10, R.drawable.station_point_s1, R.drawable.station_point_s8 };
	}

	public int[] getIdTransfer() {
		return new int[] { R.drawable.station_point_1_transfer, R.drawable.station_point_2_transfer, R.drawable.station_point_3_transfer, R.drawable.station_point_10_transfer, R.drawable.station_point_s1_transfer, R.drawable.station_point_s8_transfer };
	}

	public static class ViewHolder {
		TextView tv_time;
		TextView tv_name;
		TextView tv_content;
		ImageView iv_point;
		View line_down;
	}
}
