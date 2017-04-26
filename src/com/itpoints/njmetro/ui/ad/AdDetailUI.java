package com.itpoints.njmetro.ui.ad;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.Utils;

/**
 * 公告详情
 * 
 * @author peidongxu
 * 
 */
public class AdDetailUI extends BaseUI {
	private WebView webView;
	private String url;

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
		// 接收参数
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		if (Utils.isEmity(url)) {
			return;
		}
		setTitle("公告详情");

		// 加载服务器上的页面
		webView.loadUrl(url + "?accessToken=" + MyApplication.token);
		// 得到webview设置
		WebSettings webSettings = webView.getSettings();
		// 允许使用javascript
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	protected void onMyClick(View v) {

	}

	@Override
	protected void back() {
		finish();
	}

}
