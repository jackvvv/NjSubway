package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SetPhoneUI extends BaseUI {
	// 手机号、验证码、密码、确认密码
	private EditText et_phone, et_code;
	private String phone, code;

	// 获取验证码
	private TextView tv_get_code;
	private MyCount mc;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.set_phone);
	}

	@Override
	protected void findView_AddListener() {
		Button btn_set_phone = (Button) findViewById(R.id.btn_set_phone);
		btn_set_phone.setOnClickListener(this);

		et_phone = (EditText) findViewById(R.id.et_set_phone_content);
		et_code = (EditText) findViewById(R.id.et_set_phone_code);

		tv_get_code = (TextView) findViewById(R.id.tv_set_phone_get_code);
		tv_get_code.setOnClickListener(this);

	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");

		if (Utils.isEmity(phone)) {
			setTitle("绑定手机号");
		} else {
			setTitle("修改手机号");
			et_phone.setText(phone);
		}

	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_phone:
			// 设置手机号
			phone = et_phone.getText().toString();
			code = et_code.getText().toString();
			if (Utils.isEmity(phone)) {
				MyApplication.getInstance().showToast("手机号不能为空");
				return;
			}
			if (!Utils.isMobileNO(phone)) {
				MyApplication.getInstance().showToast("手机号格式有误");
				return;
			}
			if (Utils.isEmity(code)) {
				MyApplication.getInstance().showToast("验证码不能为空");
				return;
			}
			chgProfile();
			break;
		case R.id.tv_register_get_code:
			// 获取验证码
			phone = et_phone.getText().toString();
			if (Utils.isEmity(phone)) {
				MyApplication.getInstance().showToast("手机号不能为空");
				return;
			}
			if (!Utils.isMobileNO(phone)) {
				MyApplication.getInstance().showToast("手机号格式有误");
				return;
			}
			if (mc == null) {
				mc = new MyCount(60000, 1000); // 第一参数是总的时间，第二个是间隔时间
			}
			mc.start();
			getCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_SENDODE_URL);
		RequestParams params = new RequestParams();
		params.put("loginId", phone);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisData(response.toString());
				if (HttpUtil.checkHttpSuccess(SetPhoneUI.this, returnBean.getCode())) {
					//
					et_code.setText(returnBean.getObject().toString());
					MyApplication.getInstance().showToast("发送验证码成功");
				} else {
					mc.cancel();
					tv_get_code.setEnabled(true);
					tv_get_code.setText("发送验证码");
					MyApplication.getInstance().showToast(returnBean.getMessage());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 修改用户信息
	 */
	private void chgProfile() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_RESETPHONE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("loginId", phone);
		params.put("smscode", code);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(SetPhoneUI.this, returnBean.getCode())) {
					// 修改成功
					MyApplication.userBean.getInfo().put("loginId", phone);
					back();
					MyApplication.getInstance().showToast("修改成功");
				} else {
					MyApplication.getInstance().showToast("修改失败");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/* 定义一个倒计时的内部类 */
	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_get_code.setEnabled(true);
			tv_get_code.setText("发送验证码");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_get_code.setEnabled(false);
			tv_get_code.setText("(" + millisUntilFinished / 1000 + ")秒");
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
