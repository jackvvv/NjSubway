package com.itpoints.njmetro.bean;

/**
 * 站点出口信息bean
 * 
 * @author peidongxu
 * 
 */
public class StationInfoMationBean extends BaseDBBean {

	private String line;// 线路
	private String station;// 车站
	private String station_en;// 车站英文
	private String name;// 出口名称
	private String landmark;// 地标
	private String bus;// 公交

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStation_en() {
		return station_en;
	}

	public void setStation_en(String station_en) {
		this.station_en = station_en;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

}
