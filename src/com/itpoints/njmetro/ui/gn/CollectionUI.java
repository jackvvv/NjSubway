package com.itpoints.njmetro.ui.gn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.ArticleCollectionAdapter;
import com.itpoints.njmetro.adapter.StationCollectionAdapter;
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.SeeBean;
import com.itpoints.njmetro.bean.StationCollectionBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.ui.LoginUI;
import com.itpoints.njmetro.ui.UserInfoUI;
import com.itpoints.njmetro.ui.info.StationDetailUI;
import com.itpoints.njmetro.ui.life.ArticleDetailUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 收藏
 * 
 * @author peidongxu
 * 
 */
public class CollectionUI extends BaseUI implements OnItemClickListener, OnItemLongClickListener {

	private TextView tv_station;// 站点收藏
	private TextView tv_article;// 文章收藏

	private ListView mListView;
	private List<StationCollectionBean> listCollectionBean;
	private StationCollectionAdapter stationAdapter;

	private List<ArticleListBean> listArticleListBean;
	private ArticleCollectionAdapter articleAdapter;

	/** 类型： 1： 站点收藏 2： 文章收藏 */
	private int type;
	private EditText et_content;
	private StationCollectionBean updateBean;
	private Dialog mDialog;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.collection);
	}

	@Override
	protected void findView_AddListener() {
		tv_station = (TextView) findViewById(R.id.tv_collection_station);
		tv_station.setOnClickListener(this);
		tv_article = (TextView) findViewById(R.id.tv_collection_article);
		tv_article.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.lv_collection);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

	}

	@Override
	protected void prepareData() {

		tv_station.setSelected(true);
		tv_article.setSelected(false);
		type = 1;

	}

	@Override
	protected void onResume() {
		super.onResume();
		setData();
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_collection_station:
			type = 1;
			tv_station.setSelected(true);
			tv_article.setSelected(false);
			setData();
			break;
		case R.id.tv_collection_article:
			if (Utils.isEmity(MyApplication.token)) {
				startActivity(new Intent(this, LoginUI.class));
				return;
			} 
			type = 2;
			tv_station.setSelected(false);
			tv_article.setSelected(true);
			setData();
			break;
		case R.id.tv_dialog_station_collection_ok:
			// 修改框--确定
			StationCollectionBean tempBean = (StationCollectionBean) DbHelper.getInstance(this).searchOne(StationCollectionBean.class, "station", updateBean.getStation());
			tempBean.setDesc(et_content.getText().toString());
			DbHelper.getInstance(this).update(tempBean);
			setData();
			if (mDialog != null) {
				mDialog.dismiss();
			}
			break;
		case R.id.tv_dialog_station_collection_cancle:
			// 修改框--取消
			if (mDialog != null) {
				mDialog.dismiss();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		if (1 == type) {
			listCollectionBean = DbHelper.getInstance(this).searchDesc(StationCollectionBean.class);
			stationAdapter = new StationCollectionAdapter(this, listCollectionBean);
			mListView.setAdapter(stationAdapter);
		} else if (2 == type) {
			if (listArticleListBean == null) {
				listArticleListBean = new ArrayList<ArticleListBean>();
				articleAdapter = new ArticleCollectionAdapter(this, listArticleListBean);
				mListView.setAdapter(articleAdapter);
				getCollectionList();
			} else {
				articleAdapter = new ArticleCollectionAdapter(this, listArticleListBean);
				mListView.setAdapter(articleAdapter);
			}
		}
	}

	/**
	 * 获取收藏列表
	 */
	private void getCollectionList() {
		String url = HttpUtil.getUrl(UrlConstants.ARTICLE_GET_MYMARKS_LIST_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("pageNumber", "1");
		params.put("pageSize", "50");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getArticleList(response.toString());
				if (HttpUtil.checkHttpSuccess(CollectionUI.this, returnBean.getCode())) {
					listArticleListBean = returnBean.getListObject();
					articleAdapter = new ArticleCollectionAdapter(CollectionUI.this, listArticleListBean);
					mListView.setAdapter(articleAdapter);
				} else {
					MyApplication.getInstance().showToast(returnBean.getMessage());
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent;
		if (1 == type) {
			intent = new Intent(this, StationDetailUI.class);
			StationCollectionBean collectionBean = listCollectionBean.get(position);
			intent.putExtra("station", collectionBean.getStation());
			startActivity(intent);
		} else if (2 == type) {
			intent = new Intent(this, ArticleDetailUI.class);
			ArticleListBean articleListBean = listArticleListBean.get(position);
			intent.putExtra("bean", articleListBean);
			startActivity(intent);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (1 == type) {
			updateBean = listCollectionBean.get(position);
			showDescDialog(this);
		}
		return true;
	}

	/**
	 * 修改备注弹出框
	 */
	public void showDescDialog(Context context) {
		mDialog = new Dialog(context, R.style.MyDialogStyle);
		View view = View.inflate(context, R.layout.dialog_station_collection, null);

		et_content = (EditText) view.findViewById(R.id.et_dialog_station_collection_content);
		et_content.setText(updateBean.getDesc());

		TextView tv_ok = (TextView) view.findViewById(R.id.tv_dialog_station_collection_ok);
		tv_ok.setOnClickListener(this);
		TextView tv_cancle = (TextView) view.findViewById(R.id.tv_dialog_station_collection_cancle);
		tv_cancle.setOnClickListener(this);

		mDialog.setContentView(view);
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		LayoutParams attributes = mDialog.getWindow().getAttributes();
		attributes.width = Constants.width - Constants.width / 10 * 2;
		mDialog.getWindow().setAttributes(attributes);
	}

	@Override
	protected void back() {
		finish();
	}

}
