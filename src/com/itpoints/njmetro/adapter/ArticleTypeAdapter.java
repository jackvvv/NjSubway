package com.itpoints.njmetro.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.ArticleTypeBean;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.CircleImageView;

/**
 * adapter
 * 
 * @author peidongxu
 * 
 */
public class ArticleTypeAdapter extends BaseAdapter {
	private Context context;
	private List<ArticleTypeBean> listArticleTypeBean;
	private static TranslateAnimation taLeft, taRight, taTop, toBlow;

	public ArticleTypeAdapter(Context context, List<ArticleTypeBean> listArticleTypeBean) {
		this.context = context;
		this.listArticleTypeBean = listArticleTypeBean;

		taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		toBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taLeft.setDuration(1000);
		taRight.setDuration(1000);
		taTop.setDuration(1000);
		toBlow.setDuration(1000);
	}

	public void setData(List<ArticleTypeBean> listArticleTypeBean) {
		this.listArticleTypeBean = listArticleTypeBean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (listArticleTypeBean == null) {
			return 0;
		}
		return listArticleTypeBean.size();
	}

	@Override
	public Object getItem(int position) {
		return listArticleTypeBean.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.article_type_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_article_type_item_name);
			holder.iv_img = (CircleImageView) convertView.findViewById(R.id.iv_article_type_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ArticleTypeBean articleTypeBean = listArticleTypeBean.get(position);
		holder.tv_name.setText(articleTypeBean.getCodename());
		if (Utils.isEmity(articleTypeBean.getIcon())) {
			holder.iv_img.setBackgroundResource(R.drawable.bg_lift_icon);
		} else {
			BitmapHelp.getInstance(context).display(holder.iv_img, articleTypeBean.getIcon());
		}

		Random ran = new Random();
		int rand = ran.nextInt(4);
		switch (rand) {
		case 0:
			convertView.startAnimation(taLeft);
			break;
		case 1:
			convertView.startAnimation(taRight);
			break;
		case 2:
			convertView.startAnimation(taTop);
			break;
		case 3:
			convertView.startAnimation(toBlow);
			break;
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
		CircleImageView iv_img;
	}

}
