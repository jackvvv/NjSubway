package com.itpoints.njmetro.ui.gn;

import android.view.View;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 发起投诉
 * 
 * @author peidongxu
 * 
 */
public class FeedBackUI extends BaseUI {

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.feed_back);
	}

	@Override
	protected void findView_AddListener() {

	}

	@Override
	protected void prepareData() {
		setTitle("发起投诉");
		setRightButton("确定");
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_common_headbar_right:
			back();
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
