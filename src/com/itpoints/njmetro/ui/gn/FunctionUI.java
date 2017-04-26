package com.itpoints.njmetro.ui.gn;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.ui.LoginUI;
import com.itpoints.njmetro.ui.UserInfoUI;
import com.itpoints.njmetro.ui.img.ImgMainUI;
import com.itpoints.njmetro.ui.info.InfoMainUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 功能表
 * 
 * @author peidongxu
 * 
 */
public class FunctionUI extends BaseUI {

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.function);
	}

	@Override
	protected void findView_AddListener() {
		TextView tv_user_info = (TextView) findViewById(R.id.tv_function_user_info);
		tv_user_info.setOnClickListener(this);
		TextView tv_line_query = (TextView) findViewById(R.id.tv_function_line_query);
		tv_line_query.setOnClickListener(this);
		TextView tv_site_info = (TextView) findViewById(R.id.tv_function_site_info);
		tv_site_info.setOnClickListener(this);
		TextView tv_collcation = (TextView) findViewById(R.id.tv_function_collcation);
		tv_collcation.setOnClickListener(this);
		TextView tv_update = (TextView) findViewById(R.id.tv_function_update);
		tv_update.setOnClickListener(this);
		TextView tv_examine = (TextView) findViewById(R.id.tv_function_examine);
		tv_examine.setOnClickListener(this);
		RelativeLayout rl_contact_us = (RelativeLayout) findViewById(R.id.rl_function_contact_us);
		rl_contact_us.setOnClickListener(this);
		TextView tv_about = (TextView) findViewById(R.id.tv_function_about);
		tv_about.setOnClickListener(this);
		RelativeLayout rl_function_clear_chche = (RelativeLayout) findViewById(R.id.rl_function_clear_chche);
		rl_function_clear_chche.setOnClickListener(this);
		TextView tv_function_zhiyuzhe = (TextView) findViewById(R.id.tv_function_zhiyuzhe);
		tv_function_zhiyuzhe.setOnClickListener(this);
		TextView tv_function_feedback = (TextView) findViewById(R.id.tv_function_feedback);
		tv_function_feedback.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("功能表");
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_function_user_info:
			if (Utils.isEmity(MyApplication.token)) {
				startActivity(new Intent(this, LoginUI.class));
			} else {
				startActivity(new Intent(this, UserInfoUI.class));
			}
			break;
		case R.id.tv_function_line_query:
			startActivity(new Intent(this, ImgMainUI.class));
			break;
		case R.id.tv_function_site_info:
			startActivity(new Intent(this, InfoMainUI.class));
			break;
		case R.id.tv_function_collcation:
			startActivity(new Intent(this, CollectionUI.class));
			break;
		case R.id.tv_function_update:
			startActivity(new Intent(this, UpdateUI.class));
			break;
		case R.id.tv_function_examine:
			startActivity(new Intent(this, ExamineListUI.class));
			break;
		case R.id.rl_function_contact_us:
			startActivity(new Intent(this, ContactUsUI.class));
			break;
		case R.id.tv_function_about:
			startActivity(new Intent(this, AboutUI.class));
			break;
		case R.id.rl_function_clear_chche:
			FileUtils.deleteCacheFile();
			MyApplication.getInstance().showToast("清除缓存成功");
			break;
		case R.id.tv_function_zhiyuzhe:
			// 志愿者
			if (Utils.isEmity(MyApplication.token)) {
				startActivity(new Intent(this, LoginUI.class));
			} else {
				getState();
			}
			break;
		case R.id.tv_function_feedback:
			if (Utils.isEmity(MyApplication.token)) {
				startActivity(new Intent(this, LoginUI.class));
			} else {
				startActivity(new Intent(this, FeedBackListUI.class));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取志愿者状态
	 */
	private void getState() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_VIEW_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getVolunteerState(response.toString());
				if (HttpUtil.checkHttpSuccess(FunctionUI.this, returnBean.getCode())) {
					String state = (String) returnBean.getObject();
					// 审核状态 0.待审核 1.通过 2.拒绝
					if ("0".equals(state)) {
						MyApplication.getInstance().showToast("审核中，请耐心等待！");
					} else if ("1".equals(state)) {
						// 是志愿者
						startActivity(new Intent(FunctionUI.this, ZhiyuzheReportUI.class));
					} else if ("2".equals(state)) {
						MyApplication.getInstance().showToast("您的审核已拒绝，请重新申请！");
						// 不是志愿者,申请
						startActivity(new Intent(FunctionUI.this, ZhiyuzheApplyUI.class));
					} else {
						// 不是志愿者,申请
						startActivity(new Intent(FunctionUI.this, ZhiyuzheApplyUI.class));
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	protected void back() {
		finish();
	}

}
