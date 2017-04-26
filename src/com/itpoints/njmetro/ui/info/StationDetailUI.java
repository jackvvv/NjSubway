package com.itpoints.njmetro.ui.info;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.StationDetailAdapter;
import com.itpoints.njmetro.bean.StationCollectionBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DialogUtil;
import com.itpoints.njmetro.utils.DialogUtil.DialogClickCallBack;
import com.itpoints.njmetro.view.MyListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 站点详情
 * 
 * @author peidongxu
 * 
 */
public class StationDetailUI extends BaseUI implements DialogClickCallBack {
	// 弹出框工具
	private DialogUtil dialogUtil;
	// 站点详情数据
	private StationDetailBean stationDetailBean;

	// 站点名、站点英文名
	private String station;
	private TextView tv_station, tv_station_en;

	// 站层图
	private ImageView iv_img;

	// 是否可以设置起点、终点
	private boolean isSet;
	private LinearLayout ll_set, ll_set_addr_start, ll_set_addr_end;

	// 站点时间表
	private MyListView myListView;
	private List<StationTimeBean> listStationTimeBean;
	private StationDetailAdapter adapter;

	// 收藏
	private LinearLayout ll_collection;
	private TextView tv_collection;
	private StationCollectionBean collectionBean;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_detail);
	}

	@Override
	protected void findView_AddListener() {
		tv_station = (TextView) findViewById(R.id.tv_station_detail_station);
		tv_station_en = (TextView) findViewById(R.id.tv_station_detail_station_en);

		ll_set = (LinearLayout) findViewById(R.id.ll_station_degtail_set);

		ll_set_addr_start = (LinearLayout) findViewById(R.id.ll_station_degtail_set_addr_start);
		ll_set_addr_start.setOnClickListener(this);
		ll_set_addr_end = (LinearLayout) findViewById(R.id.ll_station_degtail_set_addr_end);
		ll_set_addr_end.setOnClickListener(this);

		//
		myListView = (MyListView) findViewById(R.id.lv_station_detail);

		// 出口信息，服务设施，站层图
		ImageView iv_infomation = (ImageView) findViewById(R.id.iv_station_detail_infomation);
		iv_infomation.setOnClickListener(this);
		ImageView iv_facility = (ImageView) findViewById(R.id.iv_station_detail_facility);
		iv_facility.setOnClickListener(this);
		iv_img = (ImageView) findViewById(R.id.iv_station_detail_img);
		iv_img.setOnClickListener(this);

		ll_collection = (LinearLayout) findViewById(R.id.ll_img_line_collection);
		ll_collection.setOnClickListener(this);
		LinearLayout ll_share = (LinearLayout) findViewById(R.id.ll_img_line_share);
		ll_share.setOnClickListener(this);

		tv_collection = (TextView) findViewById(R.id.tv_station_detail_collection);
	}

	@Override
	protected void prepareData() {
		setTitle("站点信息");

		// 初始化对象
		dialogUtil = new DialogUtil();
		dialogUtil.setCallBack(this);
		dialogUtil.setDialogWidth(Constants.width, true);

		// 接收参数
		Intent intent = getIntent();
		station = intent.getStringExtra("station");
		isSet = intent.getBooleanExtra("isSet", false);

		// 获取收藏状态
		collectionBean = (StationCollectionBean) DbHelper.getInstance(this).searchOne(StationCollectionBean.class, "station", station);
		if (collectionBean != null) {
			tv_collection.setText("取消收藏");
			ll_collection.setSelected(true);
		} else {
			tv_collection.setText("收藏");
			ll_collection.setSelected(false);
		}

		// 查询站点基本信息
		stationDetailBean = (StationDetailBean) DbHelper.getInstance(this).searchOne(StationDetailBean.class, "station", station);
		if (stationDetailBean == null) {
			return;
		}

		tv_station.setText(station);
		StationEnBean stationEnBean = (StationEnBean) DbHelper.getInstance(this).searchOne(StationEnBean.class, "station", stationDetailBean.getStation());
		if (stationEnBean != null) {
			tv_station_en.setText(stationEnBean.getStation_en());
		}

		if (isSet) {
			ll_set.setVisibility(View.VISIBLE);
		}

		String imgUrl = Constants.path + Constants._anex + File.separator + "station_img" + File.separator + stationEnBean.getStation_en() + ".jpg";
		if (new File(imgUrl).exists()) {
			iv_img.setVisibility(View.VISIBLE);
		}

		// 设置站点所属线路
		setStationLogo();

		listStationTimeBean = DbHelper.getInstance(this).getStationTime(station);
		adapter = new StationDetailAdapter(this, station, listStationTimeBean);
		myListView.setAdapter(adapter);
	}

	@Override
	protected void onMyClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_station_degtail_set_addr_start:
			// 设置起点
			intent = new Intent();
			intent.putExtra("station", station);
			intent.putExtra("type", "start");
			setResult(1, intent);
			back();
			break;
		case R.id.ll_station_degtail_set_addr_end:
			// 设置终点
			intent = new Intent();
			intent.putExtra("station", station);
			intent.putExtra("type", "end");
			setResult(1, intent);
			back();
			break;
		case R.id.iv_station_detail_infomation:
			// 出口信息
			intent = new Intent(this, StationInfoMationUI.class);
			intent.putExtra("station", station);
			startActivity(intent);
			break;
		case R.id.iv_station_detail_facility:
			// 服务设施
			intent = new Intent(this, StationFacilitiesTypeUI.class);
			intent.putExtra("station", station);
			startActivity(intent);
			break;
		case R.id.iv_station_detail_img:
			// 站层图
			intent = new Intent(this, StationImgUI.class);
			intent.putExtra("station", stationDetailBean.getStation());
			startActivity(intent);
			break;
		case R.id.ll_img_line_collection:
			// 收藏
			collectionBean = (StationCollectionBean) DbHelper.getInstance(this).searchOne(StationCollectionBean.class, "station", station);
			if (collectionBean != null) {
				// 取消收藏
				DbHelper.getInstance(this).deleteCriteria(StationCollectionBean.class, "station", station);
				tv_collection.setText("收藏");
				ll_collection.setSelected(false);
			} else {
				// 收藏
				StationCollectionBean tempBean = new StationCollectionBean();
				tempBean.setStation(station);
				DbHelper.getInstance(this).save(tempBean);
				tv_collection.setText("取消收藏");
				ll_collection.setSelected(true);
			}
			break;
		case R.id.ll_img_line_share:
			// 分享
			dialogUtil.showShareDialog(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置站点logo
	 */
	private void setStationLogo() {

		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		listStationDetailBean = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "station", station);
		List<String> listLine = new ArrayList<String>();
		StationDetailBean stationDetailBean;
		for (int i = 0; i < listStationDetailBean.size(); i++) {
			stationDetailBean = listStationDetailBean.get(i);
			if (!listLine.contains(stationDetailBean.getLine())) {
				listLine.add(stationDetailBean.getLine());
			}
		}
		TextView tv;
		String line;
		int lineIndex;
		int[] logoResId = getLogoResId();
		for (int i = 0; i < listLine.size(); i++) {
			line = listLine.get(i);
			lineIndex = getLineIndex(line);
			tv = (TextView) findViewById(logoResId[i]);
			tv.setBackgroundResource(getIds()[lineIndex]);
			tv.setVisibility(View.VISIBLE);
		}
	}

	private int getLineIndex(String line) {
		String[] arrLineName = getLineName();
		int index = 0;
		for (int i = 0; i < arrLineName.length; i++) {
			if (line.equals(arrLineName[i])) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 获取图片资源ID
	 * 
	 * @return
	 */
	private int[] getIds() {
		return new int[] { R.drawable.station_detail_logo_1, R.drawable.station_detail_logo_2, R.drawable.station_detail_logo_3, R.drawable.station_detail_logo_10, R.drawable.station_detail_logo_s1, R.drawable.station_detail_logo_s8 };
	}

	/**
	 * 线路名称
	 * 
	 * @return
	 */
	private String[] getLineName() {
		return new String[] { "1号线", "2号线", "3号线", "10号线", "S1机场线", "S8宁天城际" };
	}

	/**
	 * logo 布局ID
	 * 
	 * @return
	 */
	private int[] getLogoResId() {
		return new int[] { R.id.tv_line_logo_1, R.id.tv_line_logo_2, R.id.tv_line_logo_3, R.id.tv_line_logo_4, R.id.tv_line_logo_5 };
	}

	@Override
	public void callBack(View v) {
		dialogUtil.dismissDialog();
		switch (v.getId()) {
		case R.id.ll_dialog_share_email:
			// 邮件
			showShare(SHARE_MEDIA.EMAIL);
			break;
		case R.id.ll_dialog_share_sms:
			// 短信
			showShare(SHARE_MEDIA.SMS);
			break;
		case R.id.ll_dialog_share_sina:
			// 新浪
			showShare(SHARE_MEDIA.SINA);
			break;
		case R.id.ll_dialog_share_wechat:
			// 微信
			showShare(SHARE_MEDIA.WEIXIN);
			break;
		default:
			break;
		}
	}

	/**
	 * 分享
	 */
	private void showShare(SHARE_MEDIA share_media) {

		// 分享内容设置
		StringBuffer buffer = new StringBuffer();
		// 拼接线路、站点
		List<String> listLine = new ArrayList<String>();
		StationTimeBean stationTimeBean;
		boolean isHas = false;
		for (int i = 0; i < listStationTimeBean.size(); i++) {
			stationTimeBean = listStationTimeBean.get(i);
			if (!listLine.contains(stationTimeBean.getLine())) {
				listLine.add(stationTimeBean.getLine());
				if (isHas) {
					buffer.append(",");
				}
				buffer.append(stationTimeBean.getLine());
				isHas = true;
			}
		}
		buffer.append(station);

		// 拼接时间
		buffer.append(" 首末班车时间:");
		for (int i = 0; i < listStationTimeBean.size(); i++) {
			stationTimeBean = listStationTimeBean.get(i);
			String line = stationTimeBean.getLine();

			List<StationDetailBean> listStationDetailBean = DbHelper.getInstance(this).searchCriteria(StationDetailBean.class, "line", line);

			if (!station.equals(listStationDetailBean.get(0).getStation())) {
				buffer.append("往 ");
				buffer.append(listStationDetailBean.get(0).getStation());
				buffer.append(" 方向,");
				buffer.append(stationTimeBean.getTime_up());
				buffer.append(",");
			}

			if (!station.equals(listStationDetailBean.get(listStationDetailBean.size() - 1).getStation())) {
				buffer.append("往 ");
				buffer.append(listStationDetailBean.get(listStationDetailBean.size() - 1).getStation());
				buffer.append(" 方向,");
				buffer.append(stationTimeBean.getTime_down());
				buffer.append(",");
			}
		}
		// 拼接出口信息
		List<StationInfoMationBean> listStationInfoMationBean = DbHelper.getInstance(this).searchCriteria(StationInfoMationBean.class, "station", station);

		if (listStationInfoMationBean != null && listStationInfoMationBean.size() > 0) {
			buffer.append("出口信息:");
			for (int i = 0; i < listStationInfoMationBean.size(); i++) {
				StationInfoMationBean stationInfoMationBean = listStationInfoMationBean.get(i);
				buffer.append(stationInfoMationBean.getName());
				buffer.append(":");
				buffer.append(stationInfoMationBean.getLandmark());
				buffer.append("	");
			}
		}

		buffer.append(" [南京地铁]");

		new ShareAction(this).setPlatform(share_media).setCallback(umShareListener).withTitle("南京地铁站点分享").withText(buffer.toString()).withTargetUrl("http://www.njmetro.com").share();

	}

	private UMShareListener umShareListener = new UMShareListener() {

		@Override
		public void onResult(SHARE_MEDIA arg0) {
			MyApplication.getInstance().showToast("分享成功");
		}

		@Override
		public void onError(SHARE_MEDIA arg0, Throwable arg1) {
			MyApplication.getInstance().showToast("分享失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA arg0) {
			MyApplication.getInstance().showToast("分享取消");
		}
	};

	@Override
	protected void back() {
		finish();
	}

}
