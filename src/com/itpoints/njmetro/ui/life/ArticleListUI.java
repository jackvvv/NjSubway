package com.itpoints.njmetro.ui.life;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.ArticleListAdapter;
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.bean.ArticleTypeBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.itpoints.njmetro.view.pulltorefresh.PullableObservableListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 文章分类
 * 
 * @author peidongxu
 * 
 */
public class ArticleListUI extends BaseUI implements OnItemClickListener, OnRefreshListener {
	private int currertPage = 1; // 当前页数
	private int totolPage = 1;
	private boolean isRef = true; // 是否刷新
	private PullToRefreshLayout ptrl;
	private PullableObservableListView mListView;
	private ArticleListAdapter adapter;
	private List<ArticleListBean> listArticleListBean;

	private ArticleTypeBean articleTypeBean;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.article_list);
	}

	@Override
	protected void findView_AddListener() {
		ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_article_list);
		ptrl.setOnRefreshListener(this);
		mListView = (PullableObservableListView) findViewById(R.id.lv_article_list);
		mListView.setOnItemClickListener(this);

	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		articleTypeBean = (ArticleTypeBean) intent.getSerializableExtra("bean");

		setTitle(articleTypeBean.getCodename());

		listArticleListBean = new ArrayList<ArticleListBean>();
		adapter = new ArticleListAdapter(this, mListView, listArticleListBean);
		mListView.setAdapter(adapter);

		getArticleList();
	}

	/**
	 * 获取列表
	 */
	private void getArticleList() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_ARTICLE_LIST_URL);
		RequestParams params = new RequestParams();
		params.put("_query_articleType", articleTypeBean.getCustomid());
		params.put("_query_tag", "");
		params.put("pageNumber", String.valueOf(currertPage));
		params.put("pageSize", "15");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getArticleList(response.toString());
				if (HttpUtil.checkHttpSuccess(ArticleListUI.this, returnBean.getCode())) {
					totolPage = Integer.parseInt(returnBean.getTotalPage());
					List<ArticleListBean> listTemp = returnBean.getListObject();
					if (isRef) {
						listArticleListBean = new ArrayList<ArticleListBean>();
						listArticleListBean = listTemp;
					} else {
						if (listTemp != null && listTemp.size() > 0) {
							listArticleListBean.addAll(listTemp);
							listArticleListBean.addAll(listTemp);
							listArticleListBean.addAll(listTemp);
							listArticleListBean.addAll(listTemp);
						} else {
							if (currertPage > 0) {
								currertPage--;
							}
						}
					}
					adapter.setData(listArticleListBean);
				} else {
					MyApplication.getInstance().showToast(returnBean.getMessage());
				}

				if (isRef) {
					ptrl.stopRefresh();
				} else {
					ptrl.stopLoadmore();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (isRef) {
					ptrl.stopRefresh();
				} else {
					ptrl.stopLoadmore();
				}
			}
		});
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		ArticleListBean articleListBean = listArticleListBean.get(position);
		Intent intent = new Intent(this, ArticleDetailUI.class);
		intent.putExtra("bean", articleListBean);
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// 刷新
		currertPage = 1;
		isRef = true;
		getArticleList();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		if (currertPage < totolPage) {
			currertPage++;
			isRef = false;
			getArticleList();
		} else {
			ptrl.stopLoadmore();
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
