package com.itpoints.njmetro.ui.gn;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 发起投诉
 * 
 * @author peidongxu
 * 
 */
public class FeedBackLaunchUI extends BaseUI {

	private String content;
	private EditText et_content;
	private String requestId;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.feed_back_launch);
	}

	@Override
	protected void findView_AddListener() {
		et_content = (EditText) findViewById(R.id.et_feed_back_launch_content);

		TextView tv_sure = (TextView) findViewById(R.id.tv_feed_back_launch_launch);
		tv_sure.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("发起反馈");

		getRequestId();

	}

	/**
	 * 获取请求ID
	 */
	private void getRequestId() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_COMPLAINTS_INIT_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisData(response.toString());
				if (HttpUtil.checkHttpSuccess(FeedBackLaunchUI.this, returnBean.getCode())) {
					requestId = (String) returnBean.getObject();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 提交反馈信息
	 */
	private void save() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_COMPLAINTS_SAVE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("requestId", requestId);
		params.put("attrib15", "B");
		params.put("attrib20", content);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(FeedBackLaunchUI.this, returnBean.getCode())) {
				}
				MyApplication.getInstance().showToast(returnBean.getMessage());
				back();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_feed_back_launch_launch:
			content = et_content.getText().toString();
			if (Utils.isEmity(content)) {
				MyApplication.getInstance().showToast("请输入反馈信息");
				return;
			}
			save();
			break;
		default:
			break;
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
