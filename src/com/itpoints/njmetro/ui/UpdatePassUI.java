package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * 修改密码
 * 
 * @author peidongxu
 * 
 */
public class UpdatePassUI extends BaseUI {
	// 手机号、验证码、密码、确认密码
	private EditText et_old_pass, et_new_pass, et_new_pass_again;
	private String old_pass, new_pass, new_pass_again;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.update_pass);
	}

	@Override
	protected void findView_AddListener() {
		et_old_pass = (EditText) findViewById(R.id.et_update_pass_old_pass);
		et_new_pass = (EditText) findViewById(R.id.et_update_pass_new_pass);
		et_new_pass_again = (EditText) findViewById(R.id.et_update_pass_new_pass_again);

		Button btn_sure = (Button) findViewById(R.id.btn_update_pass);
		btn_sure.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("修改密码");
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update_pass:
			//
			old_pass = et_old_pass.getText().toString();
			new_pass = et_new_pass.getText().toString();
			new_pass_again = et_new_pass_again.getText().toString();
			if (Utils.isEmity(old_pass)) {
				MyApplication.getInstance().showToast("旧密码不能为空");
				return;
			}
			if (Utils.isEmity(new_pass)) {
				MyApplication.getInstance().showToast("新密码不能为空");
				return;
			}
			if (Utils.isEmity(new_pass_again)) {
				MyApplication.getInstance().showToast("请再次输入密码");
				return;
			}
			if (!new_pass.equals(new_pass_again)) {
				MyApplication.getInstance().showToast("两次密码输入不一致");
				return;
			}
			chgPass();
			break;
		default:
			break;
		}
	}

	/**
	 * 修改用户密码
	 */
	private void chgPass() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_CHGPASS_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("passOld", old_pass);
		params.put("passNew", new_pass);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(UpdatePassUI.this, returnBean.getCode())) {
					// 修改成功
					MyApplication.token = "";
					MyApplication.userBean = null;
					back();
				}
				MyApplication.getInstance().showToast(returnBean.getMessage());
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
