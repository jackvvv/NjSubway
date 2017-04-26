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
import com.itpoints.njmetro.bean.StationFacilityBean;

/**
 * 车站设施adapter
 * 
 * @author peidongxu
 * 
 */
public class StationFacilityAdapter extends BaseAdapter {

	private Context context;
	private List<StationFacilityBean> listStationFacilityBean;

	public StationFacilityAdapter(Context context, List<StationFacilityBean> listStationFacilityBean) {
		this.context = context;
		this.listStationFacilityBean = listStationFacilityBean;
	}

	@Override
	public int getCount() {
		if (listStationFacilityBean == null) {
			return 0;
		}
		return listStationFacilityBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listStationFacilityBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.station_facilities_item, null);
			holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_station_facilities_itme_pic);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_station_facilities_itme_name);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_station_facilities_itme_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		StationFacilityBean stationFacilityBean = listStationFacilityBean.get(position);

		String name = stationFacilityBean.getName();

		holder.tv_name.setText(stationFacilityBean.getName());
		int setBg = setBg(name);
		if (-1 != setBg) {
			holder.iv_pic.setBackgroundResource(setBg);
			holder.iv_pic.setVisibility(View.VISIBLE);
		} else {
			holder.iv_pic.setVisibility(View.GONE);
		}

		holder.tv_desc.setText(stationFacilityBean.getDesc());

		return convertView;
	}

	private int setBg(String name) {
		int res_id = -1;
		String[] arrStr = getStr();

		for (int i = 0; i < arrStr.length; i++) {
			if (name.contains(arrStr[i])) {
				res_id = getResIds()[i];
				break;
			}
		}
		return res_id;
	}

	private String[] getStr() {
		return new String[] { "洗手间", "便利店", "电梯", "充值", "升降平台" };
	}

	private int[] getResIds() {
		return new int[] { R.drawable.icon_xishoujian, R.drawable.icon_bianlidian, R.drawable.icon_dianti, R.drawable.icon_chongzhidian, R.drawable.icon_dianti };
	}

	private class ViewHolder {
		ImageView iv_pic;
		TextView tv_name;
		TextView tv_desc;
	}
}
