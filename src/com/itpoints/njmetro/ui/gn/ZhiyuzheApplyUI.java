package com.itpoints.njmetro.ui.gn;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.view.SmoothCheckBox;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 志愿者申请
 * 
 * @author peidongxu
 * 
 */
public class ZhiyuzheApplyUI extends BaseUI {
	private SmoothCheckBox scb_man, scb_woman;

	private EditText et_name, et_age, et_desc;
	private String name, sex, age, desc;

	private String requestId;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.zhiyuzhe_apply);
	}

	@Override
	protected void findView_AddListener() {

		RelativeLayout rl_man = (RelativeLayout) findViewById(R.id.rl_zhiyuzhe_apply_sex_man);
		rl_man.setOnClickListener(this);
		RelativeLayout rl_woman = (RelativeLayout) findViewById(R.id.rl_zhiyuzhe_apply_sex_woman);
		rl_woman.setOnClickListener(this);
		scb_man = (SmoothCheckBox) findViewById(R.id.scb_zhiyuzhe_apply_sex_man);
		scb_man.setOnClickListener(this);
		scb_woman = (SmoothCheckBox) findViewById(R.id.scb_zhiyuzhe_apply_sex_woman);
		scb_woman.setOnClickListener(this);

		et_name = (EditText) findViewById(R.id.et_zhiyuzhe_applay_name);
		et_age = (EditText) findViewById(R.id.et_zhiyuzhe_applay_age);
		et_desc = (EditText) findViewById(R.id.et_zhiyuzhe_applay_desc);

		Button btn_submit = (Button) findViewById(R.id.btn_zhiyuzhe_applay_submit);
		btn_submit.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("红领巾申请");

		sex = "1";
		scb_man.setChecked(true);
		scb_woman.setChecked(false);

		getRequestId();
	}

	/**
	 * 获取请求ID
	 */
	private void getRequestId() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_INIT_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisData(response.toString());
				if (HttpUtil.checkHttpSuccess(ZhiyuzheApplyUI.this, returnBean.getCode())) {
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
	 * 获取请求IDPOST
	 */
	private void save() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_SAVE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("requestId", requestId);
		params.put("attrib01", name);
		params.put("attrib02", sex);
		params.put("attrib03", age);
		params.put("attrib20", desc);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(ZhiyuzheApplyUI.this, returnBean.getCode())) {
					MyApplication.getInstance().showToast("上报申请成功");
					back();
				} else {
					MyApplication.getInstance().showToast("上报申请失败");
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
		switch (v.getId()) {
		case R.id.btn_zhiyuzhe_applay_submit:
			//
			name = et_name.getText().toString();
			age = et_age.getText().toString();
			desc = et_desc.getText().toString();

			save();
			break;
		case R.id.rl_zhiyuzhe_apply_sex_man:
		case R.id.scb_zhiyuzhe_apply_sex_man:
			// 男
			scb_man.setChecked(true, true);
			scb_woman.setChecked(false, true);
			sex = "1";
			break;
		case R.id.rl_zhiyuzhe_apply_sex_woman:
		case R.id.scb_zhiyuzhe_apply_sex_woman:
			// 女
			scb_man.setChecked(false, true);
			scb_woman.setChecked(true, true);
			sex = "2";
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
