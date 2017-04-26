package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.EditText;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 设置名称
 * 
 * @author peidongxu
 * 
 */
public class SetNameUI extends BaseUI {
	private EditText et_content;
	private String content;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.set_name);
	}

	@Override
	protected void findView_AddListener() {
		et_content = (EditText) findViewById(R.id.et_set_name_content);

	}

	@Override
	protected void prepareData() {
		setTitle("修改名称");
		setRightButton("保存");

		content = MyApplication.userBean.getInfo().get("attrib02");
		et_content.setText(content);
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
		params.put("attrib02", content);

		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(SetNameUI.this, returnBean.getCode())) {
					// 修改成功
					MyApplication.userBean.getInfo().put("attrib02", content);
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

	@Override
	protected void back() {
		finish();
	}

}
