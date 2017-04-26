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
import com.itpoints.njmetro.bean.ExamineBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.TimeUtils;

/**
 * 我的投诉列表
 * 
 * @author peidongxu
 * 
 */
public class ExamineListAdapter extends BaseAdapter {
	private Context context;
	private List<ExamineBean> listExamineBean;

	public ExamineListAdapter(Context context, List<ExamineBean> listExamineBean) {
		this.context = context;
		this.listExamineBean = listExamineBean;
	}

	public void setData(List<ExamineBean> listExamineBean) {
		this.listExamineBean = listExamineBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listExamineBean == null) {
			return 0;
		}
		return listExamineBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listExamineBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.examine_list_item, null);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_examine_list_item_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_examine_list_item_time);
			holder.ll_all = (LinearLayout) convertView.findViewById(R.id.rl_examine_list_item_all);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ExamineBean examineBean = listExamineBean.get(position);
		holder.tv_content.setText(examineBean.getAttrib12());
		String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", examineBean.getCreated());
		holder.tv_time.setText(time);

		SeeBean seeBean = (SeeBean) DbHelper.getInstance(context).searchOne(SeeBean.class, "conflictId", examineBean.getConflictId());
		if (seeBean != null) {
			holder.ll_all.setSelected(true);
		} else {
			holder.ll_all.setSelected(false);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tv_content;
		TextView tv_time;
		LinearLayout ll_all;
	}
}
