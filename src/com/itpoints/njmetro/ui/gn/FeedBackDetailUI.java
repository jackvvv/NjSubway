package com.itpoints.njmetro.ui.gn;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.FeedBackBean;
import com.itpoints.njmetro.bean.FeedBackDetailBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.TimeUtils;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 反馈详情
 * 
 * @author peidongxu
 * 
 */
public class FeedBackDetailUI extends BaseUI {

	private FeedBackBean feedBackBean;

	private TextView tv_question, tv_answer, tv_time;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.feed_back_detail);
	}

	@Override
	protected void findView_AddListener() {
		tv_question = (TextView) findViewById(R.id.tv_feed_back_detail_item_question);
		tv_answer = (TextView) findViewById(R.id.tv_feed_back_detail_item_answer);
		tv_time = (TextView) findViewById(R.id.tv_feed_back_detail_item_time);
	}

	@Override
	protected void prepareData() {
		setTitle("反馈详情");

		Intent intent = getIntent();
		feedBackBean = (FeedBackBean) intent.getSerializableExtra("bean");

		if (feedBackBean == null) {
			feedBackBean = new FeedBackBean();
		}

		getFeedBackDetail();
	}

	/**
	 * 获取反馈详情
	 */
	private void getFeedBackDetail() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_COMPLAINTS_INFO_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("conflictId", feedBackBean.getConflictId());
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getFeedBackDetail(response.toString());
				if (HttpUtil.checkHttpSuccess(FeedBackDetailUI.this, returnBean.getCode())) {
					FeedBackDetailBean feedBackDetailBean = (FeedBackDetailBean) returnBean.getObject();
					setValue(feedBackDetailBean);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	private void setValue(FeedBackDetailBean feedBackDetailBean) {
		if (feedBackBean != null) {
			tv_question.setText(feedBackBean.getAttrib20());
		}
		if (feedBackDetailBean != null) {
			tv_answer.setText(feedBackDetailBean.getAttrib20());
			String time = TimeUtils.getFotmatData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", feedBackDetailBean.getCreated());
			tv_time.setText(time);
		}
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	protected void back() {
		finish();
	}

}
