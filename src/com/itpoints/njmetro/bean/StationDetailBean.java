package com.itpoints.njmetro.bean;

/**
 * 站点基本信息Bean
 * 
 * @author peidongxu
 * 
 */
public class StationDetailBean extends BaseDBBean {

	private String line;// 线路
	private String station;// 站点名中文
	private String station_en;// 站点名英文
	private String img;// 站层图
	private String lat;// 纬度
	private String lon;// 经度
	private String desc;// 站点说明
	private String transfer_line;// 换乘路线

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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTransfer_line() {
		return transfer_line;
	}

	public void setTransfer_line(String transfer_line) {
		this.transfer_line = transfer_line;
	}

}
