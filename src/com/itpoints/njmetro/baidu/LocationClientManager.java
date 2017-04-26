package com.itpoints.njmetro.baidu;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.itpoints.njmetro.utils.LogUtils;
import com.itpoints.njmetro.utils.MyConfig;

/**
 * 定位
 * 
 * @author peidongxu
 * 
 */
public class LocationClientManager {
	private Context context;
	private LocationClient locationClient;
	private LocationClientOption option;
	public BDLocation location;

	public LocationClientManager(final Context context) {
		this.context = context;
		locationClient = new LocationClient(context);
		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				LocationClientManager.this.location = location;
				StringBuffer sb = new StringBuffer();
				sb.append("time : ");
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
					sb.append("\naddr : ");
					sb.append(location.getAddrStr());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					// sb.append("\naddr : ");
					// sb.append(location.getAddrStr());
					// 运营商信息
					// sb.append("\noperationers : ");
					// sb.append(location.getOperators());
				}
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndirection ： " + location.getDirection());
				sb.append("\nprovince ： " + location.getProvince());
				sb.append("\ncityCode ： " + location.getCityCode());
				sb.append("\ncity ： " + location.getCity());
				sb.append("\ndistrict ： " + location.getDistrict());
				sb.append("\nfloor ： " + location.getFloor());
				sb.append("\noperators ： " + location.getOperators());
				sb.append("\nstreet ： " + location.getStreet());
				sb.append("\nstreetNumber ： " + location.getStreetNumber());
				LogUtils.d("MyLog", sb.toString());
				MyConfig.setConfig(context, "config", "city", location.getCity());
				MyConfig.setConfig(context, "config", "lon", String.valueOf(location.getLongitude()));
				MyConfig.setConfig(context, "config", "lat", String.valueOf(location.getLatitude()));
			}
		});
		initLocationClientOption();
		start();
	}

	private void initLocationClientOption() {
		option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setProdName(context.getPackageName());
		locationClient.setLocOption(option);
	}

	public void start() {
		if (!locationClient.isStarted()) {
			locationClient.start();
		}
	}

	public void stop() {
		if (locationClient.isStarted()) {
			locationClient.stop();
		}
	}

	public int requestLocation() {
		if (locationClient != null && locationClient.isStarted()) {
			if (option == null) {
				initLocationClientOption();
			}
			return locationClient.requestLocation();
		}
		initLocationClientOption();
		start();
		return -1;
	}

}