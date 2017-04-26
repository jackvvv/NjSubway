package com.itpoints.njmetro.ui.life;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.LogUtils;

/**
 * 文章分类
 * 
 * @author peidongxu
 * 
 */
public class ArticleDetailUI extends BaseUI {
	private WebView webView;
	private ArticleListBean articleListBean;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.article_detail);
	}

	@Override
	protected void findView_AddListener() {
		webView = (WebView) findViewById(R.id.wb_article_detail);
	}

	@Override
	protected void prepareData() {
		// 接收参数
		Intent intent = getIntent();
		articleListBean = (ArticleListBean) intent.getSerializableExtra("bean");
		if (articleListBean == null) {
			return;
		}
		setTitle(articleListBean.getAttrib03());

		LogUtils.e("ArticleDetailUI:url", articleListBean.getUrl() + "?accessToken=" + MyApplication.token);
		// 加载服务器上的页面
		webView.loadUrl(articleListBean.getUrl() + "?accessToken=" + MyApplication.token);
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
