package com.itpoints.njmetro.ui.gn;

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
import com.itpoints.njmetro.adapter.ExamineListAdapter;
import com.itpoints.njmetro.bean.ExamineBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 调查列表
 * 
 * @author peidongxu
 * 
 */
public class ExamineListUI extends BaseUI implements OnItemClickListener {

	private ListView mListView;
	private List<ExamineBean> listExamineBean;
	private ExamineListAdapter adapter;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.examine_list);
	}

	@Override
	protected void findView_AddListener() {
		mListView = (ListView) findViewById(R.id.lv_examine_list);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("调查");

		listExamineBean = new ArrayList<ExamineBean>();

		adapter = new ExamineListAdapter(this, listExamineBean);
		mListView.setAdapter(adapter);

		getQuestion();
	}

	/**
	 * 获取调查问卷列表
	 */
	private void getQuestion() {
		String url = HttpUtil.getUrl(UrlConstants.QUESTION_GET_QUESTION_URL);
		HttpUtil.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisGetQuestion(response.toString());
				if (HttpUtil.checkHttpSuccess(ExamineListUI.this, returnBean.getCode())) {
					listExamineBean = returnBean.getListObject();
					adapter.setData(listExamineBean);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ExamineBean examineBean = listExamineBean.get(position);
		
		SeeBean seeBean = new SeeBean();
		seeBean.setConflictId(examineBean.getConflictId());
		DbHelper.getInstance(this).save(seeBean);
		
		Intent intent = new Intent(this, ExamineUI.class);
		intent.putExtra("bean", examineBean);
		startActivity(intent);
	}

	@Override
	protected void back() {
		finish();
	}

}
