package com.itpoints.njmetro.ui.gn;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DialogUtil;
import com.itpoints.njmetro.utils.DialogUtil.DialogClickCallBack;

/**
 *联系我们
 * 
 * @author peidongxu
 * 
 */
public class ContactUsUI extends BaseUI implements DialogClickCallBack {
	// 对话框宽度
	private DialogUtil dialogUtil;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.contact_us);
	}

	@Override
	protected void findView_AddListener() {
		RelativeLayout rl_phone = (RelativeLayout) findViewById(R.id.rl_contact_us_phone);
		rl_phone.setOnClickListener(this);
		RelativeLayout rl_contact_us_weibo = (RelativeLayout) findViewById(R.id.rl_contact_us_weibo);
		rl_contact_us_weibo.setOnClickListener(this);
		RelativeLayout rl_contact_us_weixin = (RelativeLayout) findViewById(R.id.rl_contact_us_weixin);
		rl_contact_us_weixin.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("联系我们");

		dialogUtil = new DialogUtil();
		dialogUtil.setCallBack(this);

	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.rl_contact_us_phone:
			dialogUtil.setDialogWidth(Constants.width, true);
			dialogUtil.showPhoneCallDialog(this);
			break;
		case R.id.rl_contact_us_weixin:
			// 
			Intent intent = new Intent(this, QrcodeUI.class);
			intent.putExtra("type", "2");
			startActivity(intent);
			break;
		case R.id.rl_contact_us_weibo:
			// 
			Intent intent1 = new Intent(this, QrcodeUI.class);
			intent1.putExtra("type", "1");
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	/**
	 * 打电话
	 * 
	 * @param phoneno
	 */
	private void callPhone(String phoneno) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));
		startActivity(intent);
	}

	@Override
	public void callBack(View v) {
		switch (v.getId()) {
		case R.id.dialog_phone_call_call:
			// 立即拨打
			dialogUtil.dismissDialog();
			callPhone("025-51899999");
			break;
		default:
			break;
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
