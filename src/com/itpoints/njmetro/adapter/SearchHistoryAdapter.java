package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.SearchHistoryBean;

/**
 * 搜索历史adapter
 * 
 * @author peidongxu
 * 
 */
public class SearchHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<SearchHistoryBean> listSearchHistoryBean;

	public SearchHistoryAdapter(Context context, List<SearchHistoryBean> listSearchHistoryBean) {
		this.context = context;
		this.listSearchHistoryBean = listSearchHistoryBean;
	}

	public void setData(List<SearchHistoryBean> listSearchHistoryBean) {
		this.listSearchHistoryBean = listSearchHistoryBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listSearchHistoryBean == null)
			return 0;
		return listSearchHistoryBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listSearchHistoryBean.get(position);
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

		SearchHistoryBean searchHistoryBean = listSearchHistoryBean.get(position);

		holder.tv_name.setText(searchHistoryBean.getName());

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_name;
	}

}
