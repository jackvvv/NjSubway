package com.itpoints.njmetro.bean;

/**
 * 站点设施表
 * 
 * @author peidongxu
 * 
 */
public class StationFacilityBean extends BaseDBBean {

	private String line;// 线路
	private String station;// 车站
	private String station_en;// 车站英文
	private String name;// 服务设施名称
	private String desc;// 简介
	private String explain;// 站点说明

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}
