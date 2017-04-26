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

/**
 * 注册页
 * 
 * @author peidongxu
 * 
 */
public class RegisterUI extends BaseUI {
	// 手机号、验证码、密码、确认密码
	private EditText et_phone, et_code, et_pass, et_pass_again;
	private String phone, code, pass, pass_again;
	// 获取验证码
	private TextView tv_get_code;
	private MyCount mc;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.register);
	}

	@Override
	protected void findView_AddListener() {
		Button btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);

		et_phone = (EditText) findViewById(R.id.et_register_phone);
		et_code = (EditText) findViewById(R.id.et_register_code);
		et_pass = (EditText) findViewById(R.id.et_register_pass);
		et_pass_again = (EditText) findViewById(R.id.et_register_pass_again);

		tv_get_code = (TextView) findViewById(R.id.tv_register_get_code);
		tv_get_code.setOnClickListener(this);

	}

	@Override
	protected void prepareData() {
		setTitle("注册");
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			// 注册
			phone = et_phone.getText().toString();
			code = et_code.getText().toString();
			pass = et_pass.getText().toString();
			pass_again = et_pass_again.getText().toString();
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
			if (Utils.isEmity(pass)) {
				MyApplication.getInstance().showToast("密码不能为空");
				return;
			}
			if (Utils.isEmity(pass_again)) {
				MyApplication.getInstance().showToast("请再次输入密码");
				return;
			}
			if (!pass.equals(pass_again)) {
				MyApplication.getInstance().showToast("两次密码输入不一致");
				return;
			}
			// register();
			checkRegister();
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
				if (HttpUtil.checkHttpSuccess(RegisterUI.this, returnBean.getCode())) {
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
	 * 检查手机号是否已经注册
	 */
	private void checkRegister() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_CHECKREG_URL);
		RequestParams params = new RequestParams();
		params.put("loginId", phone);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if ("0".equals(returnBean.getCode())) {
					//
					register();
				} else {
					MyApplication.getInstance().showToast("该手机号已注册");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 注册
	 */
	private void register() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_REGISTER_URL);
		RequestParams params = new RequestParams();
		params.put("loginId", phone);
		params.put("password", pass);
		params.put("smscode", code);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(RegisterUI.this, returnBean.getCode())) {
					// 注册成功
					startActivity(new Intent(RegisterUI.this, LoginUI.class));
					back();
				} else {
					MyApplication.getInstance().showToast(returnBean.getMessage());
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
