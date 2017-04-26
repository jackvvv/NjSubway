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
import com.itpoints.njmetro.bean.AdTypeBean;

/**
 * 运营公告adapter
 * 
 * @author peidongxu
 * 
 */
public class AdTypeAdapter extends BaseAdapter {
	private Context context;
	private List<AdTypeBean> listAdTypeBean;

	public AdTypeAdapter(Context context, List<AdTypeBean> listAdTypeBean) {
		this.context = context;
		this.listAdTypeBean = listAdTypeBean;
	}

	public void setData(List<AdTypeBean> listAdTypeBean) {
		this.listAdTypeBean = listAdTypeBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listAdTypeBean == null) {
			return 0;
		}
		return listAdTypeBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listAdTypeBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.ad_type_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_ad_type_item_name);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_ad_type_item_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AdTypeBean adTypeBean = listAdTypeBean.get(position);

		String codename = adTypeBean.getCodename();

		if (codename != null) {
			if (codename.contains("政策法规")) {
				holder.iv_icon.setBackgroundResource(R.drawable.ad_law);
			} else if (codename.contains("运营公告")) {
				holder.iv_icon.setBackgroundResource(R.drawable.ad_placard);
			} else if (codename.contains("常见问题")) {
				holder.iv_icon.setBackgroundResource(R.drawable.ad_problem);
			} else if (codename.contains("微博")) {
				holder.iv_icon.setBackgroundResource(R.drawable.ad_weibo);
			} else if (codename.contains("微信")) {
				holder.iv_icon.setBackgroundResource(R.drawable.ad_wechat);
			}
		}

		holder.tv_name.setText(codename);
		return convertView;
	}

	private class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
	}
}
