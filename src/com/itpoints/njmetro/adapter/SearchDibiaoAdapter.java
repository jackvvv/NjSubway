package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.SortModel;

/**
 * 搜索地标adapter
 * 
 * @author peidongxu
 * 
 */
public class SearchDibiaoAdapter extends BaseAdapter {
	private Context context;
	private List<SortModel> listSourceDate;

	public SearchDibiaoAdapter(Context context, List<SortModel> listSourceDate) {
		this.context = context;
		this.listSourceDate = listSourceDate;
	}

	public void setData(List<SortModel> listSourceDate) {
		this.listSourceDate = listSourceDate;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listSourceDate == null)
			return 0;
		return listSourceDate.size();
	}

	@Override
	public Object getItem(int position) {
		return listSourceDate.get(position);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SortModel sortModel = listSourceDate.get(position);

		holder.tv_name.setText(sortModel.getLandmark());

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_name;
	}

}
