package com.itpoints.njmetro.bean;

/**
 * 地铁时刻表
 * 
 * @author peidongxu
 * 
 */
public class StationTimeBean extends BaseDBBean {

	private String line;// 线路
	private String station;// 站点名
	private String time_up;// 上行时间
	private String time_down;// 下行时间

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

	public String getTime_up() {
		return time_up;
	}

	public void setTime_up(String time_up) {
		this.time_up = time_up;
	}

	public String getTime_down() {
		return time_down;
	}

	public void setTime_down(String time_down) {
		this.time_down = time_down;
	}

}
