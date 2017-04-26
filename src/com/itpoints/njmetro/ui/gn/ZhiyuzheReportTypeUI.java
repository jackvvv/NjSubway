package com.itpoints.njmetro.ui.gn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.ZhiyuzheReportTypeAdapter;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.ZhiyuzheReportTypeBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 志愿者上报类型选择
 * 
 * @author peidongxu
 * 
 */
public class ZhiyuzheReportTypeUI extends BaseUI implements OnItemClickListener {

	private int index;
	private List<ZhiyuzheReportTypeBean> listZhiyuzheReportTypeBean;
	private GridView gv_type;
	private ZhiyuzheReportTypeAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.zhiyuzhe_report_type);
	}

	@Override
	protected void findView_AddListener() {
		gv_type = (GridView) findViewById(R.id.gv_zhiyuzhe_report_type);
		gv_type.setOnItemClickListener(this);

		TextView tv_ok = (TextView) findViewById(R.id.tv_zhiyuzhe_report_type_ok);
		tv_ok.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("红领巾上报类别");

		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);

		listZhiyuzheReportTypeBean = new ArrayList<ZhiyuzheReportTypeBean>();

		adapter = new ZhiyuzheReportTypeAdapter(this, listZhiyuzheReportTypeBean, index);
		gv_type.setAdapter(adapter);

		getType();
	}

	/**
	 * 获取分类
	 */
	private void getType() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_SENDINIT_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getVolunteerType(response.toString());
				if (HttpUtil.checkHttpSuccess(ZhiyuzheReportTypeUI.this, returnBean.getCode())) {
					listZhiyuzheReportTypeBean = returnBean.getListObject();
					adapter.setData(listZhiyuzheReportTypeBean, index);
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
		switch (v.getId()) {
		case R.id.tv_zhiyuzhe_report_type_ok:
			ZhiyuzheReportTypeBean zhiyuzheReportTypeBean = listZhiyuzheReportTypeBean.get(index);
			Intent intent = new Intent();
			intent.putExtra("bean", zhiyuzheReportTypeBean);
			setResult(RESULT_OK, intent);
			back();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		index = position;
		adapter.setData(listZhiyuzheReportTypeBean, index);
	}

	@Override
	protected void back() {
		finish();
	}
}
