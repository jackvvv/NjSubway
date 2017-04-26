package com.itpoints.njmetro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationCollectionBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.Utils;

/**
 * 收藏及历史adapter
 * 
 * @author peidongxu
 * 
 */
public class StationCollectionAdapter extends BaseAdapter {
	private Context context;
	private List<StationCollectionBean> listCollectionBean;

	public StationCollectionAdapter(Context context, List<StationCollectionBean> listCollectionBean) {
		this.context = context;
		this.listCollectionBean = listCollectionBean;
	}

	@Override
	public int getCount() {
		if (listCollectionBean == null) {
			return 0;
		}
		return listCollectionBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listCollectionBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.collection_item_station, null);
			holder.tv_station = (TextView) convertView.findViewById(R.id.tv_collection_item_station_station);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_collection_item_station_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationCollectionBean collectionBean = listCollectionBean.get(position);

		holder.tv_station.setText(collectionBean.getStation());

		String desc = collectionBean.getDesc();
		if (!Utils.isEmity(desc)) {
			holder.tv_desc.setText("(" + collectionBean.getDesc() + ")");
			holder.tv_desc.setVisibility(View.VISIBLE);
		} else {
			holder.tv_desc.setVisibility(View.GONE);
		}

		setStationLogo(context, convertView, collectionBean.getStation());

		return convertView;
	}

	/**
	 * 设置站点logo
	 */
	private void setStationLogo(Context context, View view, String station) {

		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		listStationDetailBean = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "station", station);
		List<String> listLine = new ArrayList<String>();
		StationDetailBean stationDetailBean;
		for (int i = 0; i < listStationDetailBean.size(); i++) {
			stationDetailBean = listStationDetailBean.get(i);
			if (!listLine.contains(stationDetailBean.getLine())) {
				listLine.add(stationDetailBean.getLine());
			}
		}
		TextView tv;
		int[] logoResId = getLogoResId();
		String line;
		for (int i = 0; i < listLine.size(); i++) {
			line = listLine.get(i);
			tv = (TextView) view.findViewById(logoResId[i]);
			tv.setBackgroundResource(getIds()[getLineIndex(line)]);
			tv.setText(getLineNameIndex()[getLineIndex(line)]);
			tv.setVisibility(View.VISIBLE);
		}
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
		return new int[] { R.drawable.station_collection_1, R.drawable.station_collection_2, R.drawable.station_collection_3, R.drawable.station_collection_10, R.drawable.station_collection_s1, R.drawable.station_collection_s8 };
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
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineNameIndex() {
		return new String[] { "1", "2", "3", "10", "S1", "S8" };
	}

	/**
	 * logo 布局ID
	 * 
	 * @return
	 */
	private int[] getLogoResId() {
		return new int[] { R.id.tv_line_logo_1, R.id.tv_line_logo_2, R.id.tv_line_logo_3, R.id.tv_line_logo_4, R.id.tv_line_logo_5 };
	}

	private class ViewHolder {
		TextView tv_station;
		TextView tv_desc;
	}

}
