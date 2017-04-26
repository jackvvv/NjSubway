package com.itpoints.njmetro.ui.img;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.TimeLineAdapter;
import com.itpoints.njmetro.bean.LinePlanBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseFragmentUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DialogUtil;
import com.itpoints.njmetro.utils.TimeUtils;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.utils.DialogUtil.DialogClickCallBack;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 检索结果轴线图
 * 
 * @author peidongxu
 * 
 */
public class ImgLineFrament extends BaseFragmentUI implements DialogClickCallBack {

	// 时间线
	private ListView timeLineListView;
	private List<StationDetailBean> listStationDetailBean;
	private TimeLineAdapter timeAdapter;
	private DialogUtil dialogUtil;
	// 起点、终点
	private String addr_start, addr_end, lineplan;
	// 起点最晚出发时间
	private TextView tv_last_time;
	// 时间。价格
	private String time, price;

	// 线路分割后数据
	private ArrayList<ArrayList<String>> listAll;

	@Override
	protected void loadViewLayout(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.img_line, null);
	}

	@Override
	protected void findView_AddListener() {

		timeLineListView = (ListView) view.findViewById(R.id.lv_img_line_time_line);

		LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_img_line_share);
		ll_share.setOnClickListener(this);

		tv_last_time = (TextView) view.findViewById(R.id.tv_img_line_last_time);

	}

	@Override
	protected void prepareData() {

		dialogUtil = new DialogUtil();
		dialogUtil.setCallBack(this);
		dialogUtil.setDialogWidth(Constants.width, true);

		// 接收参数
		addr_start = getArguments().getString("addr_start");
		addr_end = getArguments().getString("addr_end");
		lineplan = getArguments().getString("lineplan");
		time = getArguments().getString("time");
		price = getArguments().getString("price");

		// 设置途经线路
		timeAdapter = new TimeLineAdapter(getActivity(), listStationDetailBean, null);
		timeLineListView.setAdapter(timeAdapter);

		// 获取线路规划数据
		setData(lineplan);

		// 设置listv 高度
		setHeight();
	}

	/**
	 * 数据处理
	 */
	private void setData(String line) {
		if (Utils.isEmity(line)) {
			return;
		}
		String[] arrStation = line.split(",");
		// 线路分割后数据
		listAll = new ArrayList<ArrayList<String>>();

		String station;
		ArrayList<String> listTemp = new ArrayList<String>();
		// 将线路按照换乘站点分割
		StationDetailBean stationDetailBean;
		for (int i = 0; i < arrStation.length; i++) {
			station = arrStation[i];
			stationDetailBean = (StationDetailBean) DbHelper.getInstance(getActivity()).searchOne(StationDetailBean.class, "station", station);
			if (stationDetailBean != null && !Utils.isEmity(stationDetailBean.getTransfer_line())) {
				// 是换乘线
				listTemp.add(station);
				if (listTemp.size() > 1) {
					listAll.add(listTemp);
				}
				if (i != arrStation.length - 1) {
					// 不是最后一个
					listTemp = new ArrayList<String>();
					listTemp.add(station);
				}
			} else {
				// 不是换乘线
				listTemp.add(station);
				if (i == arrStation.length - 1) {
					// 最后一个
					if (listTemp.size() > 1) {
						listAll.add(listTemp);
					}
				}
			}
		}

		// 计算每一段线路数据
		ArrayList<String> arrayList;
		listStationDetailBean = new ArrayList<StationDetailBean>();
		List<StationDetailBean> listParam = new ArrayList<StationDetailBean>();
		StationDetailBean tempBean;
		int size = listAll.size();
		for (int i = 0; i < size; i++) {
			arrayList = listAll.get(i);
			listParam = DbHelper.getInstance(getActivity()).getStationLine(arrayList.get(0), arrayList.get(arrayList.size() - 1));

			if (size <= 1) {
				listStationDetailBean.addAll(listParam);
			} else {
				if (i == size - 1) {
					for (int j = 0; j < listParam.size(); j++) {
						tempBean = listParam.get(j);
						listStationDetailBean.add(tempBean);
					}
				} else {
					for (int j = 0; j < listParam.size() - 1; j++) {
						tempBean = listParam.get(j);
						listStationDetailBean.add(tempBean);
					}
				}
			}
		}

		// 计算时间
		int millisecond = 0;
		List<String> listTime = new ArrayList<String>();
		String currertData = "";
		String lastData = "";
		size = listStationDetailBean.size();
		for (int i = 0; i < listStationDetailBean.size(); i++) {
			if (i == 0) {
				currertData = TimeUtils.getCurrertData("HH:mm");
				lastData = currertData;
				listTime.add(currertData);
			} else if (i > 0 && i < size) {
				millisecond = DbHelper.getInstance(getActivity()).getMillisecond(listStationDetailBean.get(i - 1).getStation(), listStationDetailBean.get(i).getStation());
				currertData = TimeUtils.getDateAfter(lastData, millisecond);
				listTime.add(currertData);
				lastData = currertData;
			}
		}

		timeAdapter.setData(listStationDetailBean, listTime);

		// 设置出发站点最晚时间
		StationDetailBean lastStationBean = listStationDetailBean.get(0);
		StationTimeBean stationTime = DbHelper.getInstance(getActivity()).getStationTime(lastStationBean.getLine(), lastStationBean.getStation());
		StationDetailBean beforStationBean = listStationDetailBean.get(1);
//		if (lastStationBean.get_id() > beforStationBean.get_id()) {
//			// 上行
//			tv_last_time.setText( stationTime.getTime_up().split("-")[1]);
//		} else {
//			// 下行
//			tv_last_time.setText(stationTime.getTime_down().split("-")[1]);
//		}
		if (lastStationBean.get_id() > beforStationBean.get_id()) {
			// 上行
			tv_last_time.setText(TimeUtils.getFotmatData("HH:mm", stationTime.getTime_up().split("-")[1]));
		} else {
			// 下行
			tv_last_time.setText(TimeUtils.getFotmatData("HH:mm", stationTime.getTime_down().split("-")[1]));
		}
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.ll_img_line_share:
			// 分享
			dialogUtil.showShareDialog(getActivity());
			break;
		default:
			break;
		}
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
		// 拼接线路名
		buffer.append(addr_start + "~" + addr_end);
		buffer.append(" 乘车路线: ");
		// 拼接路线
		StationDetailBean stationDetailBean;
		int size = listStationDetailBean.size();
		List<StationDetailBean> searchCriteria;
		for (int i = 0; i < size; i++) {
			stationDetailBean = listStationDetailBean.get(i);

			if (i == 0) {
				// 起点
				buffer.append("从");
				buffer.append(stationDetailBean.getLine() + stationDetailBean.getStation());
				buffer.append("，乘坐");
				searchCriteria = DbHelper.getInstance(getActivity()).searchCriteria(StationDetailBean.class, "line", stationDetailBean.getLine());

				if (listStationDetailBean.get(i).get_id() > listStationDetailBean.get(i + 1).get_id()) {
					// 上行
					buffer.append(searchCriteria.get(0).getStation());
				} else {
					// 下行
					buffer.append(searchCriteria.get(searchCriteria.size() - 1).getStation());
				}
				buffer.append("方向列车");
			} else if (i == size - 1) {
				// 终点
				buffer.append("最后到达");
				buffer.append(stationDetailBean.getStation());
				buffer.append(",");
			} else {
				if (!Utils.isEmity(stationDetailBean.getTransfer_line())) {
					// 换乘点
					buffer.append("到达");
					buffer.append(stationDetailBean.getStation());
					buffer.append("后换乘");
					buffer.append(stationDetailBean.getLine());
					buffer.append("往");
					searchCriteria = DbHelper.getInstance(getActivity()).searchCriteria(StationDetailBean.class, "line", stationDetailBean.getLine());

					if (listStationDetailBean.get(i).get_id() > listStationDetailBean.get(i + 1).get_id()) {
						// 上行
						buffer.append(searchCriteria.get(0).getStation());
					} else {
						// 下行
						buffer.append(searchCriteria.get(searchCriteria.size() - 1).getStation());
					}
					buffer.append("方向列车,");
				}
			}
		}

		// 拼接时间
		buffer.append("车程约" + time + "分钟，");
		// 拼接价钱
		buffer.append("单程票价" + price + "元。");

		buffer.append(" [南京地铁]");

		new ShareAction(getActivity()).setPlatform(share_media).setCallback(umShareListener).withTitle("南京地铁线路分享").withText(buffer.toString()).withTargetUrl("http://www.njmetro.com").share();
	}

	/**
	 * 分享回调
	 */
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

	/**
	 * 设置listview 高度
	 */
	public void setHeight() {
		LayoutParams layoutParams = timeLineListView.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = Constants.height;
		timeLineListView.setLayoutParams(layoutParams);
	}

}
