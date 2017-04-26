package com.itpoints.njmetro.ui;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.AppManager;
import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.AppVersion;
import com.itpoints.njmetro.bean.MsgBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.WeatherBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.ad.AdTypeUI;
import com.itpoints.njmetro.ui.gn.FunctionUI;
import com.itpoints.njmetro.ui.img.ImgMainUI;
import com.itpoints.njmetro.ui.info.InfoMainUI;
import com.itpoints.njmetro.ui.life.ArticleTypeUI;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.DrawLineView;
import com.itpoints.njmetro.view.ForcedTextView;
import com.itpoints.njmetro.view.MarqueeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 首页
 * 
 * @author peidongxu
 * 
 */
public class MainUI extends BaseUI {
	/** 首页广播 */
	public final static String MAIN_ACTION = "com.itpoints.njmetro.ui.mianBroadcastReceiver";

	private LinearLayout ll_main_ad_bottom;
//	private MarqueeView ll_marquee;
	private ForcedTextView tv_ad;
	private List<MsgBean> listMsgBean;

	private RelativeLayout rl_line;
	private ImageView iv_main_tab_1, iv_main_tab_2, iv_main_tab_3, iv_main_tab_4, iv_main_tab_5;

	/* 版本更新start */
	private Dialog mDialog;
	private String tempPath;
	private ProgressBar pbProgress;
	private TextView tvProgress;
	private AppVersion appVersion;
	/* 版本更新end */

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.main);
		setTranslucentStatus();
	}

	@Override
	protected void findView_AddListener() {
		iv_main_tab_1 = (ImageView) findViewById(R.id.iv_main_tab_1);
		iv_main_tab_1.setOnClickListener(this);
		iv_main_tab_2 = (ImageView) findViewById(R.id.iv_main_tab_2);
		iv_main_tab_2.setOnClickListener(this);
		iv_main_tab_3 = (ImageView) findViewById(R.id.iv_main_tab_3);
		iv_main_tab_3.setOnClickListener(this);
		iv_main_tab_4 = (ImageView) findViewById(R.id.iv_main_tab_4);
		iv_main_tab_4.setOnClickListener(this);
		iv_main_tab_5 = (ImageView) findViewById(R.id.iv_main_tab_5);
		iv_main_tab_5.setOnClickListener(this);

		ll_main_ad_bottom = (LinearLayout) findViewById(R.id.ll_main_ad_bottom);
//		ll_marquee = (MarqueeView) findViewById(R.id.ll_marquee);
		tv_ad = (ForcedTextView) findViewById(R.id.tv_main_ad);

		rl_line = (RelativeLayout) findViewById(R.id.rl_main_menu);

	}

	@Override
	protected void prepareData() {

		// 注册消息广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(MAIN_ACTION);
		registerReceiver(mainBroadcastReceiver, filter);

		DrawLineView drawView = new DrawLineView(this, iv_main_tab_2, iv_main_tab_1, -30);
//		setFlickerAnimation(drawView);
		rl_line.addView(drawView);
		drawView = new DrawLineView(this, iv_main_tab_2, iv_main_tab_3, 30);
//		setFlickerAnimation(drawView);
		rl_line.addView(drawView);
		drawView = new DrawLineView(this, iv_main_tab_4, iv_main_tab_3, -30);
//		setFlickerAnimation(drawView);
		rl_line.addView(drawView);
		drawView = new DrawLineView(this, iv_main_tab_4, iv_main_tab_5, 30);
//		setFlickerAnimation(drawView);
		rl_line.addView(drawView);

//		setFlickerAnimation(iv_main_tab_1);
//		setFlickerAnimation(iv_main_tab_2);
//		setFlickerAnimation(iv_main_tab_3);
//		setFlickerAnimation(iv_main_tab_4);
//		setFlickerAnimation(iv_main_tab_5);

		// 版本更新
		checkVersion();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(MainUI.this, AdUI.class);
				startActivity(intent);
			}
		}, 1000);
	}

	private void setFlickerAnimation(View iv_chat_head) {
		final Animation animation = new AlphaAnimation(1, (float) 0.1); 
		animation.setDuration(800); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation
		animation.setRepeatMode(Animation.REVERSE); //
		iv_chat_head.startAnimation(animation);
	}

	@Override
	protected void onResume() {
		super.onResume();
		listMsgBean = DbHelper.getInstance(this).getMsg();
		if (listMsgBean != null && listMsgBean.size() > 0) {
			MsgBean msgBean;
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < listMsgBean.size(); i++) {
				msgBean = listMsgBean.get(i);
				stringBuffer.append(msgBean.getContent());
			}

			tv_ad.setText(stringBuffer.toString());
//			new Handler().postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					ll_marquee.startMarquee();
//				}
//			}, 1000);
			ll_main_ad_bottom.setVisibility(View.VISIBLE);
		}

		getWeather();
	}

	/**
	 * 获取天气
	 */
	private void getWeather() {
		String url = HttpUtil.getUrl(UrlConstants.COMM_V1_GET_WEATHER_URL);
		HttpUtil.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.getWeather(response.toString());
				if (HttpUtil.checkHttpSuccess(MainUI.this, returnBean.getCode())) {
					WeatherBean weatherBean = (WeatherBean) returnBean.getObject();
					setWeatherValue(weatherBean);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 设置天气数据
	 * 
	 * @param weatherBean
	 */
	private void setWeatherValue(WeatherBean weatherBean) {
		if (weatherBean == null) {
			return;
		}
		ImageView iv_weather = (ImageView) findViewById(R.id.iv_main_weather);
		TextView tv_1 = (TextView) findViewById(R.id.tv_main_weather_1);
		TextView tv_2 = (TextView) findViewById(R.id.tv_main_weather_2);
		BitmapHelp.getInstance(this).display(iv_weather, weatherBean.getAttrib12());
		tv_1.setText(weatherBean.getAttrib04());
		tv_2.setText(weatherBean.getAttrib09() + "℃");
	}

	/**
	 * 检查版本更新
	 */
	private void checkVersion() {
		String versionName = Utils.getVersionName(this);
		String url = HttpUtil.getUrl(UrlConstants.COMM_V1_CHECK_VERSION_URL);
		RequestParams params = new RequestParams();
		params.put("attrib02", versionName);
		params.put("attrib03", "android");
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisVersion(response.toString());
				if (HttpUtil.checkHttpSuccess(MainUI.this, returnBean.getCode())) {
					appVersion = (AppVersion) returnBean.getObject();
					if (appVersion == null) {
						return;
					}
					// 弹出版本升级提示框
					showUpdateVersionDialog();
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
		case R.id.iv_main_tab_1:
			// 线网图
			startActivity(new Intent(this, ImgMainUI.class));
			break;
		case R.id.iv_main_tab_2:
			// 站点信息
			startActivity(new Intent(this, InfoMainUI.class));
			break;
		case R.id.iv_main_tab_3:
			// 地铁生活
			startActivity(new Intent(this, ArticleTypeUI.class));
			break;
		case R.id.iv_main_tab_4:
			// 运营公告
			if (Utils.isEmity(MyApplication.token)) {
				startActivity(new Intent(this, LoginUI.class));
			} else {
				startActivity(new Intent(this, AdTypeUI.class));
			}
			break;
		case R.id.iv_main_tab_5:
			// 功能表
			startActivity(new Intent(this, FunctionUI.class));
			break;
		case R.id.tv_update_version_dialog_ok:
			// 立即升级
			mDialog.dismiss();
			// 弹出下载框
			showDownLoadingDialog();
			new Thread(new Runnable() {

				@Override
				public void run() {
					downLoadAPK();
				}
			}).start();
			break;
		case R.id.tv_update_version_dialog_cancel:
			// 取消升级
			if (appVersion != null && "1".equals(appVersion.getAttrib13())) {
				// 强制更新
				AppManager.getAppManager().AppExit(getApplicationContext());
			}
			if (mDialog != null) {
				mDialog.dismiss();
			}
			break;
		default:
			break;
		}
	}

	BroadcastReceiver mainBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			listMsgBean = DbHelper.getInstance(MainUI.this).getMsg();
			if (listMsgBean != null && listMsgBean.size() > 0) {
				MsgBean msgBean;
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < listMsgBean.size(); i++) {
					msgBean = listMsgBean.get(i);
					stringBuffer.append(msgBean.getContent());
					stringBuffer.append("          ");
				}

				tv_ad.setText(stringBuffer.toString());
//				new Handler().postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						ll_marquee.startMarquee();
//					}
//				}, 1000);
				ll_main_ad_bottom.setVisibility(View.VISIBLE);
			}
		}
	};

	/**
	 * 版本升级对话框
	 */
	private void showUpdateVersionDialog() {
		mDialog = new Dialog(this, R.style.custom_dialog_style);
		View dialogView = View.inflate(this, R.layout.dialog_update_version, null);
		dialogView.findViewById(R.id.tv_update_version_dialog_ok).setOnClickListener(this);
		dialogView.findViewById(R.id.tv_update_version_dialog_cancel).setOnClickListener(this);
		mDialog.setContentView(dialogView);
		mDialog.setCancelable(false);
		mDialog.show();

		LayoutParams attributes = mDialog.getWindow().getAttributes();
		attributes.width = Constants.width - Constants.width / 10 * 2;
		mDialog.getWindow().setAttributes(attributes);

		attributes = null;
	}

	/**
	 * 版本下载框
	 */
	private void showDownLoadingDialog() {
		mDialog = new Dialog(this, R.style.custom_dialog_style);
		View view = View.inflate(this, R.layout.dialog_down_load_apk, null);
		pbProgress = (ProgressBar) view.findViewById(R.id.pb_down_load_apk_dialog);
		tvProgress = (TextView) view.findViewById(R.id.tv_down_load_apk_dialog);
		mDialog.setContentView(view);
		mDialog.setCancelable(false);
		mDialog.show();

		LayoutParams attributes = mDialog.getWindow().getAttributes();
		attributes.width = Constants.width - Constants.width / 10 * 2;
		mDialog.getWindow().setAttributes(attributes);

		attributes = null;
	}

	/**
	 * 下载新版本
	 */
	private void downLoadAPK() {
		tempPath = Constants.path + Constants._video + File.separator + appVersion.getAttrib13().replaceAll("[^\\w.]", "");
		HttpUtils http = new HttpUtils();
		http.download(appVersion.getAttrib13(), tempPath, true, true, new RequestCallBack<File>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				pbProgress.setMax((int) total);
				pbProgress.setProgress((int) current);
				tvProgress.setText(transformProgress((int) current) + "/" + transformProgress((int) total));
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mDialog.dismiss();
				FileUtils.deleteFile(appVersion.getAttrib13());
				MyApplication.getInstance().showToast("下载失败");
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// 下载成功
				mDialog.dismiss();
				try {
					String command[] = { "chmod", "777", responseInfo.result.getPath() };
					ProcessBuilder builder = new ProcessBuilder(command);
					builder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
				startActivity(installIntent);
				finish();
			}
		});
	}

	/**
	 * 转换进度
	 * 
	 * @param progress
	 * @return
	 */
	public String transformProgress(int progress) {
		String result = null;
		if (progress / 1048576 > 0) { // 单位为MB
			result = divide(progress, 1048576) + "MB";
		} else if (progress / 1024 > 0) { // 单位为KB
			result = divide(progress, 1024) + "KB";
		} else { // 单位为B
			result = progress + "B";
		}
		return result;
	}

	/**
	 * 除法运算,保留两位小数点,并四舍五入
	 * 
	 * @param m
	 *            被除数
	 * @param n
	 *            除数
	 * @return
	 */
	public double divide(int m, int n) {
		BigDecimal bd1 = new BigDecimal(m);
		BigDecimal bd2 = new BigDecimal(n);
		return bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	protected void back() {
		exit();
	}

}
