package com.itpoints.njmetro.ui.ad;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.AdListAdapter;
import com.itpoints.njmetro.bean.AdListBean;
import com.itpoints.njmetro.bean.AdTypeBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.itpoints.njmetro.view.pulltorefresh.PullableListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 公告列表表
 * 
 * @author peidongxu
 * 
 */
public class AdListUI extends BaseUI implements OnItemClickListener, OnRefreshListener {
	private int currertPage = 1; // 当前页数
	private int totolPage = 1;
	private boolean isRef = true; // 是否刷新
	private PullToRefreshLayout ptrl;
	private PullableListView mListView;
	private List<AdListBean> listAdListBean;
	private AdListAdapter adapter;

	private AdTypeBean adTypeBean;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.ad_list);
	}

	@Override
	protected void findView_AddListener() {
		ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_ad_list);
		ptrl.setOnRefreshListener(this);
		mListView = (PullableListView) findViewById(R.id.lv_ad_list);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		adTypeBean = (AdTypeBean) intent.getSerializableExtra("bean");

		setTitle(adTypeBean.getCodename());

		adapter = new AdListAdapter(this, listAdListBean);
		mListView.setAdapter(adapter);

	}
	@Override
	protected void onResume() {
		super.onResume();
		getAdList();
	}

	/**
	 * 获取列表
	 */
	private void getAdList() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_ARTICLE_LIST_URL);
		RequestParams params = new RequestParams();
		params.put("_query_articleType", adTypeBean.getCustomid());
		params.put("_query_tag", "");
		params.put("pageNumber", "1");
		params.put("pageSize", "15");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getAdList(response.toString());
				if (HttpUtil.checkHttpSuccess(AdListUI.this, returnBean.getCode())) {
					totolPage = Integer.parseInt(returnBean.getTotalPage());
					List<AdListBean> listTemp = returnBean.getListObject();
					if (isRef) {
						listAdListBean = new ArrayList<AdListBean>();
						listAdListBean = listTemp;
					} else {
						if (listTemp != null && listTemp.size() > 0) {
							listAdListBean.addAll(listTemp);
						} else {
							if (currertPage > 0) {
								currertPage--;
							}
						}
					}
					adapter.setData(listAdListBean);
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
		AdListBean adListBean = listAdListBean.get(position);
		
		SeeBean seeBean = new SeeBean();
		seeBean.setConflictId(adListBean.getConflictId());
		DbHelper.getInstance(this).save(seeBean);
		
		Intent intent = new Intent(this, AdDetailUI.class);
		intent.putExtra("url", adListBean.getUrl());
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// 刷新
		currertPage = 1;
		isRef = true;
		getAdList();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		if (currertPage < totolPage) {
			currertPage++;
			isRef = false;
			getAdList();
		} else {
			ptrl.stopLoadmore();
		}
	}

	@Override
	protected void back() {
		finish();
	}
}
