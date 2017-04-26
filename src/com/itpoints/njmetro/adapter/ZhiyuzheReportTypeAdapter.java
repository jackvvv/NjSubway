package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.ZhiyuzheReportTypeBean;

/**
 * 志愿者上报分类adapter
 * 
 * @author peidongxu
 * 
 */
public class ZhiyuzheReportTypeAdapter extends BaseAdapter {
	private Context context;
	private int index;
	private List<ZhiyuzheReportTypeBean> listZhiyuzheReportTypeBean;

	public ZhiyuzheReportTypeAdapter(Context context, List<ZhiyuzheReportTypeBean> listZhiyuzheReportTypeBean, int index) {
		this.context = context;
		this.listZhiyuzheReportTypeBean = listZhiyuzheReportTypeBean;
		this.index = index;
	}

	public void setData(List<ZhiyuzheReportTypeBean> listZhiyuzheReportTypeBean, int index) {
		this.listZhiyuzheReportTypeBean = listZhiyuzheReportTypeBean;
		this.index = index;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listZhiyuzheReportTypeBean == null) {
			return 0;
		}
		return listZhiyuzheReportTypeBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listZhiyuzheReportTypeBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.zhiyuzhe_report_type_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_zhiyuzhe_report_type_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ZhiyuzheReportTypeBean zhiyuzheReportTypeBean = listZhiyuzheReportTypeBean.get(position);
		holder.tv_name.setText(zhiyuzheReportTypeBean.getName());

		if (index == position) {
			holder.tv_name.setSelected(true);
		} else {
			holder.tv_name.setSelected(false);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
	}

}
