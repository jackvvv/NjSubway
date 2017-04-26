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

/**
 * 搜索历史adapter
 * 
 * @author peidongxu
 * 
 */
public class SearchStationAdapter extends BaseAdapter {
	private Context context;
	private List<StationDetailBean> listStationDetailBean;

	public SearchStationAdapter(Context context, List<StationDetailBean> listSearchHistoryBean) {
		this.context = context;
		this.listStationDetailBean = listSearchHistoryBean;
	}

	public void setData(List<StationDetailBean> listSearchHistoryBean) {
		this.listStationDetailBean = listSearchHistoryBean;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.search_item_history, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_search_item_history_name);
			holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_search_item_history_name_logo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationDetailBean stationDetailBean = listStationDetailBean.get(position);
		holder.tv_name.setText(stationDetailBean.getStation());

		holder.iv_logo.setBackgroundResource(getIds()[getLineIndex(stationDetailBean.getLine())]);

		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
		ImageView iv_logo;
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
	 * 获取图片资源ID
	 * 
	 * @return
	 */
	private int[] getIds() {
		return new int[] { R.drawable.station_detail_logo_1, R.drawable.station_detail_logo_2, R.drawable.station_detail_logo_3, R.drawable.station_detail_logo_10, R.drawable.station_detail_logo_s1, R.drawable.station_detail_logo_s8 };
	}

	/**
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineName() {
		return new String[] { "1号线", "2号线", "3号线", "10号线", "S1机场线", "S8宁天城际" };
	}
}
