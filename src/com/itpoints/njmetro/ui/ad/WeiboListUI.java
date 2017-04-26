package com.itpoints.njmetro.ui.ad;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.WeiboListAdapter;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.WeiboListBean;
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
 * 微博列表
 * @author peidongxu
 *
 */
public class WeiboListUI extends BaseUI implements OnRefreshListener {
	private int currertPage = 1; // 当前页数
	private int totolPage = 1;
	private boolean isRef = true; // 是否刷新
	private PullToRefreshLayout ptrl;
	private PullableListView mListView;
	private List<WeiboListBean> listWeiboListBean;
	private WeiboListAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.weibo_list);
	}

	@Override
	protected void findView_AddListener() {
		ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_weibo_list);
		ptrl.setOnRefreshListener(this);
		mListView = (PullableListView) findViewById(R.id.lv_weibo_list);
	}

	@Override
	protected void prepareData() {
		setTitle("微博");

		adapter = new WeiboListAdapter(this, listWeiboListBean);
		mListView.setAdapter(adapter);

		getWeiboList();
	}

	/**
	 * 获取列表
	 */
	private void getWeiboList() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_WEIBO_LIST_URL);
		RequestParams params = new RequestParams();
		params.put("_query_articleType", "I");
		params.put("pageNumber", String.valueOf(currertPage));
		params.put("pageSize", "15");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getWeiboList(response.toString());
				if (HttpUtil.checkHttpSuccess(WeiboListUI.this, returnBean.getCode())) {
					totolPage = Integer.parseInt(returnBean.getTotalPage());
					List<WeiboListBean> listTemp = returnBean.getListObject();
					if (isRef) {
						listWeiboListBean = new ArrayList<WeiboListBean>();
						listWeiboListBean = listTemp;
					} else {
						if (listTemp != null && listTemp.size() > 0) {
							listWeiboListBean.addAll(listTemp);
						} else {
							if (currertPage > 0) {
								currertPage--;
							}
						}
					}
					adapter.setData(listWeiboListBean);
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
	public void onRefresh() {
		// 刷新
		currertPage = 1;
		isRef = true;
		getWeiboList();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		if (currertPage < totolPage) {
			currertPage++;
			isRef = false;
			getWeiboList();
		} else {
			ptrl.stopLoadmore();
		}
	}

	@Override
	protected void back() {
		finish();
	}
}
