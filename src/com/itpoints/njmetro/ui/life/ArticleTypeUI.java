package com.itpoints.njmetro.ui.life;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.ArticleTypeAdapter;
import com.itpoints.njmetro.bean.ArticleTypeBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 文章分类
 * 
 * @author peidongxu
 * 
 */
public class ArticleTypeUI extends BaseUI implements OnItemClickListener {
	// 我关注
	private GridView gv_article_type;
	private List<ArticleTypeBean> listArticleTypeBean;
	private ArticleTypeAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.article_type);
	}

	@Override
	protected void findView_AddListener() {
		gv_article_type = (GridView) findViewById(R.id.gv_article_type);
		gv_article_type.setOnItemClickListener(this);

	}

	@Override
	protected void prepareData() {
		setTitle("地铁生活");

		listArticleTypeBean = new ArrayList<ArticleTypeBean>();

		adapter = new ArticleTypeAdapter(this, listArticleTypeBean);
		gv_article_type.setAdapter(adapter);

		getArticleType();
	}

	/**
	 * 获取文章分类
	 */
	private void getArticleType() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_LIFE_TYPE_URL);
		HttpUtil.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getArticleType(response.toString());
				if (HttpUtil.checkHttpSuccess(ArticleTypeUI.this, returnBean.getCode())) {
					listArticleTypeBean = returnBean.getListObject();
					adapter.setData(listArticleTypeBean);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		ArticleTypeBean articleTypeBean = listArticleTypeBean.get(position);
		Intent intent = new Intent(this, ArticleListUI.class);
		intent.putExtra("bean", articleTypeBean);
		startActivity(intent);
	}

	@Override
	protected void back() {
		finish();
	}

}
