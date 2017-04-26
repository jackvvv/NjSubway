package com.itpoints.njmetro.ui;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.baidu.LocationClientManager;
import com.itpoints.njmetro.bean.LineBean;
import com.itpoints.njmetro.bean.LinePriceBean;
import com.itpoints.njmetro.bean.StationCollectionBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.bean.StationIdBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.bean.StationScenicSpotBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.ReadFile;
import com.itpoints.njmetro.utils.ZIP;

/**
 * 欢迎页
 * 
 * @author peidongxu
 * 
 */
public class WelcomeUI extends BaseUI {
	// 是否显示引导页
	private boolean showLead;
	private ImageView iv_welcome;

	private TextView tv_tip;
	private boolean first;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.welcome);
		setTranslucentStatus();
	}

	@Override
	protected void findView_AddListener() {
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		tv_tip = (TextView) findViewById(R.id.tv_welcome_tip);
	}

	@Override
	protected void prepareData() {

		// 创建缓存文件
		FileUtils.createCacheFolder();

		MyApplication.token = MyConfig.getToken(this);
		MyApplication.userBean = MyConfig.getUserInfo(this);

		// 开启定位服务
		LocationClientManager locationClientManager = new LocationClientManager(this);

		// 屏幕宽度，高度
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		Constants.width = width;
		Constants.height = height;

		// 加载启动动画
		AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
		animation.setDuration(300);
		iv_welcome.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				first = MyConfig.getConfig(WelcomeUI.this, "config", "first", true);
				if (first) {
					// 显示ProgressDialog
					tv_tip.setVisibility(View.VISIBLE);
					// 新建线程
					new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								ReadFile.copyBigDataToSD(WelcomeUI.this, "njmetro.zip", Constants.path + Constants._anex + File.separator + "njmetro.zip");
								ZIP.unZipFolder(Constants.path + Constants._anex + File.separator + "njmetro.zip", Constants.path + Constants._anex + File.separator);
								FileUtils.deleteFile(Constants.path + Constants._anex + File.separator + "njmetro.zip");
								// 删除表
								DbHelper.getInstance(WelcomeUI.this).drop(StationEnBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationIdBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(LinePriceBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationCollectionBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(LineBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationTimeBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationFacilityBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationScenicSpotBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationInfoMationBean.class);
								DbHelper.getInstance(WelcomeUI.this).drop(StationDetailBean.class);

								ReadFile.loadData(WelcomeUI.this, Constants.path + Constants._anex + File.separator + "line_detail");
								// 第一次
								MyConfig.setConfig(WelcomeUI.this, "config", "first", false);
								// 向handler发消息
								mHandler.sendEmptyMessage(0);

								// 生成json文件
//								List<StationEnBean> search1 = DbHelper.getInstance(WelcomeUI.this).search(StationEnBean.class);
//								List<StationIdBean> search2 = DbHelper.getInstance(WelcomeUI.this).search(StationIdBean.class);
//								List<LinePriceBean> search3 = DbHelper.getInstance(WelcomeUI.this).search(LinePriceBean.class);
//								List<LineBean> search4 = DbHelper.getInstance(WelcomeUI.this).search(LineBean.class);
//								List<StationTimeBean> search5 = DbHelper.getInstance(WelcomeUI.this).search(StationTimeBean.class);
//								List<StationFacilityBean> search6 = DbHelper.getInstance(WelcomeUI.this).search(StationFacilityBean.class);
//								List<StationScenicSpotBean> search7 = DbHelper.getInstance(WelcomeUI.this).search(StationScenicSpotBean.class);
//								List<StationInfoMationBean> search8 = DbHelper.getInstance(WelcomeUI.this).search(StationInfoMationBean.class);
//								List<StationDetailBean> search9 = DbHelper.getInstance(WelcomeUI.this).search(StationDetailBean.class);
//								Gson gson = new Gson();
//								FileUtils.saveFile(gson.toJson(search1), "station_en.json");
//								FileUtils.saveFile(gson.toJson(search2), "station_id.json");
//								FileUtils.saveFile(gson.toJson(search3), "station_price.json");
//								FileUtils.saveFile(gson.toJson(search4), "station_line_all.json");
//								FileUtils.saveFile(gson.toJson(search5), "station_time.json");
//								FileUtils.saveFile(gson.toJson(search6), "station_facility.json");
//								FileUtils.saveFile(gson.toJson(search7), "station_scenicsport.json");
//								FileUtils.saveFile(gson.toJson(search8), "station_infomation.json");
//								FileUtils.saveFile(gson.toJson(search9), "station_detail.json");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				} else {
					// 向handler发消息
					mHandler.sendEmptyMessage(0);
				}
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 关闭ProgressDialog
			tv_tip.setVisibility(View.GONE);
			showLead = MyConfig.getConfig(WelcomeUI.this, Constants.CONFIG_NAME, "showLead", true);
			if (showLead) {
				// 跳转到引导页
				Intent intent = new Intent(WelcomeUI.this, LeadUI.class);
				startActivity(intent);
				back();
				return;
			}
			// 根据token判断是否已经登录
			Intent intent = new Intent(WelcomeUI.this, MainUI.class);
			startActivity(intent);
			back();
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	protected void back() {
		finish();
	}

}