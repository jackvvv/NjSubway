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
import com.itpoints.njmetro.bean.AdListBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.TimeUtils;

/**
 * 公告列表
 * 
 * @author peidongxu
 * 
 */
public class AdListAdapter extends BaseAdapter {
	private Context context;
	private List<AdListBean> listAdListBean;

	public AdListAdapter(Context context, List<AdListBean> listAdListBean) {
		this.context = context;
		this.listAdListBean = listAdListBean;
	}

	public void setData(List<AdListBean> listAdListBean) {
		this.listAdListBean = listAdListBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listAdListBean == null) {
			return 0;
		}
		return listAdListBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listAdListBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.ad_list_item, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_ad_list_item_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_ad_list_item_time);
			holder.ll_all = (LinearLayout) convertView.findViewById(R.id.ll_ad_list_item_all);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AdListBean adListBean = listAdListBean.get(position);

		holder.tv_title.setText(adListBean.getAttrib03());
		String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", adListBean.getCreated());
		holder.tv_time.setText(time);

		SeeBean seeBean = (SeeBean) DbHelper.getInstance(context).searchOne(SeeBean.class, "conflictId", adListBean.getConflictId());
		if (seeBean != null) {
			holder.ll_all.setSelected(true);
		} else {
			holder.ll_all.setSelected(false);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_title;
		TextView tv_time;
		LinearLayout ll_all;
	}
}
