package com.itpoints.njmetro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.MyConfig;

/**
 * adapter
 * 
 * @author peidongxu
 * 
 */
public class RuleSelectAdapter extends BaseAdapter {
	private Context context;
	private int[] arrResId;

	public RuleSelectAdapter(Context context, int[] arrResId) {
		this.context = context;
		this.arrResId = arrResId;
	}

	@Override
	public int getCount() {
		if (arrResId == null) {
			return 0;
		}
		return arrResId.length;
	}

	@Override
	public Object getItem(int position) {
		return arrResId[position];
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
			convertView = LayoutInflater.from(context).inflate(R.layout.rule_select_item, null);
			holder.iv_bg = (ImageView) convertView.findViewById(R.id.iv_rule_select_item_bg);
			holder.iv_ok = (ImageView) convertView.findViewById(R.id.iv_rule_select_item_ok);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int rule_id = MyConfig.getConfig(context, "config", "rule_id", 0);

		if (rule_id == arrResId[position]) {
			holder.iv_ok.setVisibility(View.VISIBLE);
		} else {
			holder.iv_ok.setVisibility(View.GONE);
		}

		holder.iv_bg.setBackgroundResource(arrResId[position]);

		return convertView;
	}

	private class ViewHolder {
		ImageView iv_bg;
		ImageView iv_ok;
	}

}
