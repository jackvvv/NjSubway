package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.RelativeLayout;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.view.SmoothCheckBox;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 设置性别
 * 
 * @author peidongxu
 * 
 */
public class SetSexUI extends BaseUI {

	private SmoothCheckBox scb_man;
	private SmoothCheckBox scb_woman;

	private String sex;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.set_sex);
	}

	@Override
	protected void findView_AddListener() {
		RelativeLayout rl_man = (RelativeLayout) findViewById(R.id.rl_set_sex_man);
		rl_man.setOnClickListener(this);
		RelativeLayout rl_woman = (RelativeLayout) findViewById(R.id.rl_set_sex_woman);
		rl_woman.setOnClickListener(this);
		scb_man = (SmoothCheckBox) findViewById(R.id.scb_set_sex_man);
		scb_man.setOnClickListener(this);
		scb_woman = (SmoothCheckBox) findViewById(R.id.scb_set_sex_woman);
		scb_woman.setOnClickListener(this);

	}

	@Override
	protected void prepareData() {
		setTitle("修改性别");
		setRightButton("保存");

		sex = MyApplication.userBean.getInfo().get("sex");
		if ("0".equals(sex) || "1".equals(sex)) {
			scb_man.setChecked(true);
			scb_woman.setChecked(false);
		} else if ("2".equals(sex)) {
			scb_man.setChecked(false);
			scb_woman.setChecked(true);
		}

	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_common_headbar_right:
			// 保存
			chgProfile();
			break;
		case R.id.rl_set_sex_man:
		case R.id.scb_set_sex_man:
			// 男
			scb_man.setChecked(true, true);
			scb_woman.setChecked(false, true);
			sex = "1";
			break;
		case R.id.rl_set_sex_woman:
		case R.id.scb_set_sex_woman:
			// 女
			scb_man.setChecked(false, true);
			scb_woman.setChecked(true, true);
			sex = "2";
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
		params.put("sex", sex);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(SetSexUI.this, returnBean.getCode())) {
					// 修改成功
					MyApplication.userBean.getInfo().put("sex", sex);
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
