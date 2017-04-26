package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 设置个性签名
 * 
 * @author peidongxu
 * 
 */
public class SetSignUI extends BaseUI implements TextWatcher {

	private EditText et_content;
	private String content;
	private TextView tv_num;
	private int num;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.set_sign);
	}

	@Override
	protected void findView_AddListener() {
		et_content = (EditText) findViewById(R.id.et_set_sign_content);
		et_content.addTextChangedListener(this);
		tv_num = (TextView) findViewById(R.id.tv_set_sign_num);
	}

	@Override
	protected void prepareData() {
		setTitle("修改个性签名");
		setRightButton("保存");

		num = 30;
		tv_num.setText(String.valueOf(num));
		tv_num.setMaxEms(num);
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_common_headbar_right:
			content = et_content.getText().toString();
			chgProfile();
			break;
		default:
			break;
		}
	}

	/**
	 * 修改用户信息
	 */
	private void chgProfile() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_CHGPROFILE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("", content);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(SetSignUI.this, returnBean.getCode())) {
					// 修改成功
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
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
	}

	@Override
	protected void back() {
		finish();
	}

}
