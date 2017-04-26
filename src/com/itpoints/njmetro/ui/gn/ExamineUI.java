package com.itpoints.njmetro.ui.gn;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.ExamineBean;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 调查详情
 * 
 * @author peidongxu
 * 
 */
public class ExamineUI extends BaseUI {
	private WebView webView;
	private ExamineBean examineBean;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.ad_detail);
	}

	@Override
	protected void findView_AddListener() {
		webView = (WebView) findViewById(R.id.wb_ad_detail);
	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		examineBean = (ExamineBean) intent.getSerializableExtra("bean");

		if (examineBean == null) {
			return;
		}

		setTitle(examineBean.getAttrib12());

		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		// 加载服务器上的页面
		webView.loadUrl(examineBean.getUrl() + "?accessToken=" + MyApplication.token);
		// 得到webview设置
		WebSettings webSettings = webView.getSettings();
		// 允许使用javascript
		webSettings.setJavaScriptEnabled(true);

	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	protected void back() {
		finish();
	}

}
