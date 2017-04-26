package com.itpoints.njmetro.bean;

/**
 * 站点ID、名称对应表
 * 
 * @author peidongxu
 * 
 */
public class StationIdBean extends BaseDBBean {

	private String id;
	private String station_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}


}
