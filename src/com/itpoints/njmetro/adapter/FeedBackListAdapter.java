package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.FeedBackBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.TimeUtils;

/**
 * 我的投诉列表
 * 
 * @author peidongxu
 * 
 */
public class FeedBackListAdapter extends BaseAdapter {
	private Context context;
	private List<FeedBackBean> listFeedBackBean;

	public FeedBackListAdapter(Context context, List<FeedBackBean> listFeedBackBean) {
		this.context = context;
		this.listFeedBackBean = listFeedBackBean;
	}

	public void setData(List<FeedBackBean> listFeedBackBean) {
		this.listFeedBackBean = listFeedBackBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listFeedBackBean == null) {
			return 0;
		}
		return listFeedBackBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listFeedBackBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.feed_back_list_item, null);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_feed_back_list_item_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_feed_back_list_item_time);
			holder.rl_all = (RelativeLayout) convertView.findViewById(R.id.rl_feed_back_list_item_all);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FeedBackBean feedBackBean = listFeedBackBean.get(position);
		holder.tv_content.setText(feedBackBean.getAttrib20());
		String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", feedBackBean.getCreated());
		holder.tv_time.setText(time);

		SeeBean seeBean = (SeeBean) DbHelper.getInstance(context).searchOne(SeeBean.class, "conflictId", feedBackBean.getConflictId());
		if (seeBean != null) {
			holder.rl_all.setSelected(true);
		} else {
			holder.rl_all.setSelected(false);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_content;
		TextView tv_time;
		RelativeLayout rl_all;
	}
}
