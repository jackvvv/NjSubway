package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.LineBean;

/**
 * 线路adapter
 * 
 * @author peidongxu
 * 
 */
public class InfoMainLineAdapter extends BaseAdapter {
	private Context context;
	private List<LineBean> listLineBean;

	public InfoMainLineAdapter(Context context, List<LineBean> listLineBean) {
		this.context = context;
		this.listLineBean = listLineBean;
	}

	@Override
	public int getCount() {
		if (listLineBean == null)
			return 0;
		return listLineBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listLineBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.info_main_item_line, null);
			holder.tv_line_name = (TextView) convertView.findViewById(R.id.tv_info_main_item_line_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LineBean lineBean = listLineBean.get(position);

		holder.tv_line_name.setText(lineBean.getLine());
		holder.tv_line_name.setBackgroundColor(Color.parseColor(getColorIdS()[position]));
		return convertView;
	}

	
	private String[] getColorIdS(){
		return new String[]{"#5EAAFE","#FE4040","#87BC64","#FEB95E","#44BBB5","#B68F64"
		};
	}
	
	private class ViewHolder {
		private TextView tv_line_name;
	}

}
