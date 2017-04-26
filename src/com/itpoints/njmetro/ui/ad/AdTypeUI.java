package com.itpoints.njmetro.ui.ad;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.AdTypeAdapter;
import com.itpoints.njmetro.bean.AdTypeBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.ui.gn.QrcodeUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 公告分类
 * 
 * @author peidongxu
 * 
 */
public class AdTypeUI extends BaseUI implements OnItemClickListener {

	private List<AdTypeBean> listAdTypeBean;
	private ListView mListView;
	private AdTypeAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.ad_type);
	}

	@Override
	protected void findView_AddListener() {
		mListView = (ListView) findViewById(R.id.lv_ad_type);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("公告信息");

		listAdTypeBean = new ArrayList<AdTypeBean>();
		adapter = new AdTypeAdapter(this, listAdTypeBean);
		mListView.setAdapter(adapter);

		getAdType();
	}

	/**
	 * 获取分类
	 */
	private void getAdType() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_ARTICLE_TYPE_URL);
		HttpUtil.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getAdType(response.toString());
				if (HttpUtil.checkHttpSuccess(AdTypeUI.this, returnBean.getCode())) {
					listAdTypeBean = returnBean.getListObject();
					adapter.setData(listAdTypeBean);
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
		AdTypeBean adTypeBean = listAdTypeBean.get(position);
		if ("微博".equals(adTypeBean.getCodename())) {
			Intent intent = new Intent(this, WeiboListUI.class);
			startActivity(intent);
		} else if ("微信".equals(adTypeBean.getCodename())) {
			Intent intent = new Intent(this, QrcodeUI.class);
			intent.putExtra("type", "2");
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, AdListUI.class);
			intent.putExtra("bean", adTypeBean);
			startActivity(intent);
		}
	}

	@Override
	protected void back() {
		finish();
	}
}
