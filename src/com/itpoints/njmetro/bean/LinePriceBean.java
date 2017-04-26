package com.itpoints.njmetro.bean;

/**
 * 线路票价表
 * 
 * @author peidongxu
 * 
 */
public class LinePriceBean extends BaseDBBean {

	private String start_station;// 起点车站号
	private String end_station;// 终点车站号
	private String distance;// 最短里程
	private String fee_level;// 费率等级
	private String fee;// 票价

	public String getStart_station() {
		return start_station;
	}

	public void setStart_station(String start_station) {
		this.start_station = start_station;
	}

	public String getEnd_station() {
		return end_station;
	}

	public void setEnd_station(String end_station) {
		this.end_station = end_station;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getFee_level() {
		return fee_level;
	}

	public void setFee_level(String fee_level) {
		this.fee_level = fee_level;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

}
