package com.itpoints.njmetro.bean;

/**
 * 站点景点Bean
 * 
 * @author peidongxu
 * 
 */
public class StationScenicSpotBean extends BaseDBBean {

	private String station;// 站点名称
	private String name;// 景点名称
	private String address;// 景点位置
	private String desc;// 景点简介
	private String price;// 门票
	private String preferential;// 优待政策
	private String time;// 开放时间

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
