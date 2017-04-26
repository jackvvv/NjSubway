package com.itpoints.njmetro.bean;

/**
 * 搜索历史表
 * 
 * @author peidongxu
 * 
 */
public class SearchHistoryBean extends BaseDBBean {

	private String name;// 搜索名称

	private String type;// 1: 站点 2: 地标

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
