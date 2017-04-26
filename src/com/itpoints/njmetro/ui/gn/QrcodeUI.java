package com.itpoints.njmetro.ui.gn;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 二维码
 * 
 * @author peidongxu
 * 
 */
public class QrcodeUI extends BaseUI {

	private ImageView iv_qrcode;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.qrcode);
	}

	@Override
	protected void findView_AddListener() {
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
	}

	@Override
	protected void prepareData() {

		Intent intent = getIntent();
		String type = intent.getStringExtra("type");

		if ("1".equals(type)) {
			setTitle("微博二维码");
			iv_qrcode.setBackgroundResource(R.drawable.weibo_qrcode);
		} else {
			setTitle("微信二维码");
			iv_qrcode.setBackgroundResource(R.drawable.weixin_qrcode);
		}
	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	protected void back() {
		finish();
	}

}
