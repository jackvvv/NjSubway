package com.itpoints.njmetro.ui.gn;

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
import com.itpoints.njmetro.adapter.FeedBackListAdapter;
import com.itpoints.njmetro.bean.FeedBackBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout;
import com.itpoints.njmetro.view.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.itpoints.njmetro.view.pulltorefresh.PullableListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 反馈列表
 * 
 * @author peidongxu
 * 
 */
public class FeedBackListUI extends BaseUI implements OnRefreshListener, OnItemClickListener {
	private int currertPage = 1; // 当前页数
	private boolean isRef = true; // 是否刷新

	private PullToRefreshLayout ptrl;
	private PullableListView mListView;
	private List<FeedBackBean> listFeedBackBean;
	private FeedBackListAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.feed_back_list);
	}

	@Override
	protected void findView_AddListener() {
		ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_feed_back_list);
		ptrl.setOnRefreshListener(this);
		mListView = (PullableListView) findViewById(R.id.lv_feed_back_list);
		mListView.setOnItemClickListener(this);
		mListView.setPullLoadEnable(false);

	}

	@Override
	protected void prepareData() {
		setTitle("我的反馈");
		setRightButton("发起反馈");

		listFeedBackBean = new ArrayList<FeedBackBean>();

		adapter = new FeedBackListAdapter(this, listFeedBackBean);
		mListView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Utils.isEmity(MyApplication.token)) {
			getFeedBackList();
		}
	}

	/**
	 * 获取反馈列表
	 */
	private void getFeedBackList() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_COMPLAINTS_LIST_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getFeedBackList(response.toString());
				if (HttpUtil.checkHttpSuccess(FeedBackListUI.this, returnBean.getCode())) {
					List<FeedBackBean> listTemp = returnBean.getListObject();
					if (isRef) {
						listFeedBackBean = new ArrayList<FeedBackBean>();
						listFeedBackBean = listTemp;
					} else {
						if (listTemp != null && listTemp.size() > 0) {
							listFeedBackBean.addAll(listTemp);
						} else {
							if (currertPage > 0) {
								currertPage--;
							}
						}
					}
					adapter.setData(listFeedBackBean);
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
		switch (v.getId()) {
		case R.id.tv_common_headbar_right:
			//
			startActivity(new Intent(this, FeedBackLaunchUI.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FeedBackBean feedBackBean = listFeedBackBean.get(position);
		
		SeeBean seeBean = new SeeBean();
		seeBean.setConflictId(feedBackBean.getConflictId());
		DbHelper.getInstance(this).save(seeBean);
		
		Intent intent = new Intent(this, FeedBackDetailUI.class);
		intent.putExtra("bean", feedBackBean);
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		// 刷新
		currertPage = 1;
		isRef = true;
		getFeedBackList();
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		ptrl.stopLoadmore();
	}

	@Override
	protected void back() {
		finish();
	}
}
