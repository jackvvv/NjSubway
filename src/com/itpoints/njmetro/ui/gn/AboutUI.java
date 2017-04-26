package com.itpoints.njmetro.ui.gn;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.BaseUI;

/**
 * 关于
 * 
 * @author peidongxu
 * 
 */
public class AboutUI extends BaseUI {
	private TextView tv_about_desc;
	private String desc;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.about);
	}

	@Override
	protected void findView_AddListener() {
		tv_about_desc = (TextView) findViewById(R.id.tv_about_desc);
	}

	@Override
	protected void prepareData() {
		setTitle("关于");
		
		desc = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1999年5月28日，经市委、市政府研究决定，撤销地铁筹建处，成立南京市地下铁道工程建设指挥部，为正局级事业单位，南京市地下铁道总公司与其合署，一个机构、两块牌子，主要负责南京市地铁工程的规划、设计、筹资、建设、运营及与地铁相关的物业开发等。出于经济活动的需要，2000年又成立了南京地下铁道有限责任公司，与指挥部、总公司合署。2012年6月，根据市委、市政府要求，在原南京地下铁道有限责任公司的基础上改组成立南京地铁集团有限公司。";
		
		tv_about_desc.setText(Html.fromHtml(desc));
	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	protected void back() {
		finish();
	}

}
