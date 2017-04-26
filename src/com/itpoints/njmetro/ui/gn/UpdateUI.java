package com.itpoints.njmetro.ui.gn;

import java.io.File;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.LineBean;
import com.itpoints.njmetro.bean.LinePriceBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.StationCollectionBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.bean.StationIdBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.bean.StationScenicSpotBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.ReadFile;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.utils.ZIP;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 数据更新
 * 
 * @author peidongxu
 * 
 */
public class UpdateUI extends BaseUI {

	private TextView tv_no;
	private RelativeLayout rl_yes;

	private TextView tv_update_version, tv_update_loading;

	private String tempPath;

	private Map<String, String> map;

	private ProgressDialog progressDialog;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.update);
	}

	@Override
	protected void findView_AddListener() {
		tv_no = (TextView) findViewById(R.id.tv_update_no);
		rl_yes = (RelativeLayout) findViewById(R.id.rl_update_yes);

		tv_update_version = (TextView) findViewById(R.id.tv_update_version);
		tv_update_loading = (TextView) findViewById(R.id.tv_update_loading);
		tv_update_loading.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("数据更新");

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在更新中, 请稍后...");
		progressDialog.setCancelable(false);

		checkVersionData();
	}

	/**
	 * 检测数据版本
	 */
	private void checkVersionData() {
		String url = HttpUtil.getUrl(UrlConstants.COMM_V1_CHECK_DATA_URL);
		String data_vertion = MyConfig.getConfig(UpdateUI.this, "config", "data_vertion", "1");
		RequestParams params = new RequestParams();
		params.put("code", data_vertion);
		params.put("type", "android");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisCheckVerstion(response.toString());
				map = (Map<String, String>) returnBean.getObject();
				if (map != null && !Utils.isEmity(map.get("url"))) {
					// 有更新
					rl_yes.setVisibility(View.VISIBLE);
					tv_no.setVisibility(View.GONE);
					tv_update_version.setText(map.get("code"));
				} else {
					// 没有更新
					rl_yes.setVisibility(View.GONE);
					tv_no.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 更新数据
	 */
	private void updateData() {
		tempPath = Constants.path + Constants._anex + File.separator + "njmetro.zip";
		HttpUtils http = new HttpUtils();
		http.download(map.get("url"), tempPath, true, true, new RequestCallBack<File>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				progressDialog.dismiss();
				FileUtils.deleteFile(map.get("url"));
				MyApplication.getInstance().showToast("数据更新失败，请稍后尝试");
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// 下载成功
				// 新建线程
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							// 解压
							ZIP.unZipFolder(tempPath, Constants.path + Constants._anex + File.separator);
							FileUtils.deleteFile(tempPath);
							// 删除表
							DbHelper.getInstance(UpdateUI.this).drop(StationEnBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationIdBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(LinePriceBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationCollectionBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(LineBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationTimeBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationFacilityBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationScenicSpotBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationInfoMationBean.class);
							DbHelper.getInstance(UpdateUI.this).drop(StationDetailBean.class);
							// 加载数据
							ReadFile.loadData(UpdateUI.this, Constants.path + Constants._anex + File.separator + "line_detail");
							// 保存数据版本
							MyConfig.setConfig(UpdateUI.this, "config", "data_vertion", map.get("code"));
							// 向handler发消息
							mHandler.sendEmptyMessage(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			MyApplication.getInstance().showToast("数据更新成功");
			back();
		};
	};

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_update_loading:
			// 立即更新
			progressDialog.show();
			updateData();
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
