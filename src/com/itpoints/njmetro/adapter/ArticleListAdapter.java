package com.itpoints.njmetro.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.view.pulltorefresh.PullableObservableListView;

/**
 * 文章列表adapter
 * 
 * @author peidongxu
 * 
 */
public class ArticleListAdapter extends BaseAdapter {
	private Context context;
	private List<ArticleListBean> listArticleListBean;
	private PullableObservableListView mListView;

	public ArticleListAdapter(Context context, PullableObservableListView mListView, List<ArticleListBean> listArticleListBean) {
		this.context = context;
		this.mListView = mListView;
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
		View view = null;
		if (view == null) {
			view = View.inflate(context, R.layout.article_list_item, null);
		} else {
			view = convertView;
		}
		ImageView iv_img = (ImageView) view.findViewById(R.id.iv_article_list_item_img);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_article_list_item_title);
		TextView tv_laud = (TextView) view.findViewById(R.id.tv_article_list_item_land);

		final ArticleListBean articleListBean = listArticleListBean.get(position);
		tv_title.setText(articleListBean.getAttrib03());
		BitmapHelp.getInstance(context).display(iv_img, articleListBean.getAttrib37());

		mListView.imageParallax(iv_img);

		return view;
	}

}
