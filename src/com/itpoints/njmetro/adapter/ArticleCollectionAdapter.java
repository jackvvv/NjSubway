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
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.TimeUtils;

/**
 * 收藏文章列表
 * 
 * @author peidongxu
 * 
 */
public class ArticleCollectionAdapter extends BaseAdapter {
	private Context context;
	private List<ArticleListBean> listArticleListBean;

	public ArticleCollectionAdapter(Context context, List<ArticleListBean> listArticleListBean) {
		this.context = context;
		this.listArticleListBean = listArticleListBean;
	}

	public void setData(List<ArticleListBean> listArticleListBean) {
		this.listArticleListBean = listArticleListBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listArticleListBean == null) {
			return 0;
		}
		return listArticleListBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listArticleListBean.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.collection_item_article, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_collection_item_article_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_collection_item_article_time);
			holder.rl_all = (RelativeLayout) convertView.findViewById(R.id.rl_collection_item_article_all);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ArticleListBean articleListBean = listArticleListBean.get(position);

		holder.tv_title.setText(articleListBean.getAttrib03());
		String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", articleListBean.getCreated());
		holder.tv_time.setText(time);

		holder.rl_all.setSelected(true);

		return convertView;
	}

	private class ViewHolder {
		TextView tv_title;
		TextView tv_time;
		RelativeLayout rl_all;
	}
}
