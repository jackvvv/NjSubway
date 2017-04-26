package com.itpoints.njmetro.ui;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdUI extends BaseUI {

	private ImageView iv_ad_img;
	private VideoView videoView;
	private Map<String, String> map;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.ad);
	}

	@Override
	protected void findView_AddListener() {
		iv_ad_img = (ImageView) findViewById(R.id.iv_ad_img);
		videoView = (VideoView) findViewById(R.id.vv_ad_mp4);
	}

	@Override
	protected void prepareData() {
		setTitle("广告");

		getAd();
	}

	/**
	 * 获取广告
	 */
	private void getAd() {
		String url = HttpUtil.getUrl(UrlConstants.COMM_V1_GET_FISRT_AD_URL);
		RequestParams params = new RequestParams();
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getAd(response.toString());
				map = (Map<String, String>) returnBean.getObject();
				setValue();
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

	private void setValue() {
		if (map == null) {
			return;
		}
		String code = map.get("code");
		String url = map.get("url");
		// 1:图片2:视频
		if ("1".equals(code)) {
			BitmapHelp.getInstance(this).display(iv_ad_img, url);
			iv_ad_img.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
		} else if ("2".equals(code)) {
			iv_ad_img.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			if (!Utils.isEmity(url)) {
				Uri uri = Uri.parse(url);
				videoView.setMediaController(new MediaController(this));
				videoView.setVideoURI(uri);
				videoView.requestFocus();
			}
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
