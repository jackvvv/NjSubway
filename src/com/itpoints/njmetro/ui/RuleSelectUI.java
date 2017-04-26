package com.itpoints.njmetro.ui;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.RuleSelectAdapter;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 角色选择
 * 
 * @author peidongxu
 * 
 */
public class RuleSelectUI extends BaseUI implements OnItemClickListener {

	private int[] arrResId;
	private String[] arrName;
	private ListView mListView;
	private RuleSelectAdapter adapter;

	private String userRole;

	private boolean isUpdate;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.rule_select);
	}

	@Override
	protected void findView_AddListener() {
		mListView = (ListView) findViewById(R.id.lv_rule_select);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		isUpdate = intent.getBooleanExtra("isUpdate", false);

		if (isUpdate) {
			// 修改
			setTitle("我是...");
		} else {
			setTitleHideBack("我是...");
		}

		arrResId = new int[] { R.drawable.rule_laoren, R.drawable.rule_stuent, R.drawable.rule_shangban, R.drawable.rule_juming, R.drawable.rule_youke };
		arrName = new String[] { "老人", "学生", "上班", "居民", "游客" };

		adapter = new RuleSelectAdapter(this, arrResId);
		mListView.setAdapter(adapter);
	}

	/**
	 * 修改用户信息
	 */
	private void chgProfile() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_CHGPROFILE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("userRole", userRole);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(RuleSelectUI.this, returnBean.getCode())) {
					// 修改成功
					back();
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
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		MyConfig.setConfig(this, "config", "rule_id", arrResId[position]);

		if (isUpdate) {
			adapter.notifyDataSetChanged();
			userRole = arrName[position];
			chgProfile();
		} else {
			// 不是修改时
			Intent intent = new Intent(this, MainUI.class);
			startActivity(intent);
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
