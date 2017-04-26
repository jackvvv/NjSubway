package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.WeiboListBean;
import com.itpoints.njmetro.utils.TimeUtils;

public class WeiboListAdapter extends BaseAdapter {

	private Context context;
	private List<WeiboListBean> listWeiboListBean;

	public WeiboListAdapter(Context context, List<WeiboListBean> listWeiboListBean) {
		this.context = context;
		this.listWeiboListBean = listWeiboListBean;
	}

	public void setData(List<WeiboListBean> listWeiboListBean) {
		this.listWeiboListBean = listWeiboListBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listWeiboListBean == null) {
			return 0;
		}
		return listWeiboListBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listWeiboListBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.weibo_list_item, null);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_weibo_list_item_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_weibo_list_item_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_weibo_list_item_content);
			holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_weibo_list_item_logo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WeiboListBean weiboListBean = listWeiboListBean.get(position);
		holder.tv_name.setText(weiboListBean.getAttrib03());
		holder.tv_content.setText(Html.fromHtml(weiboListBean.getAttrib19()));
		
		String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",weiboListBean.getCreated());
		
		holder.tv_time.setText(time);

		if (position == 0) {
			holder.iv_logo.setVisibility(View.VISIBLE);
		} else {
			holder.iv_logo.setVisibility(View.GONE);
		}

		return convertView;
	}

	public static class ViewHolder {
		TextView tv_time;
		TextView tv_name;
		TextView tv_content;
		ImageView iv_logo;
	}
}
