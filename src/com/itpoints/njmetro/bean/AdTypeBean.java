package com.itpoints.njmetro.bean;

import java.io.Serializable;

/**
 * 运营公告分类
 * 
 * @author peidongxu
 * 
 */
public class AdTypeBean implements Serializable{

	private String customid;
	private String codename;

	public String getCustomid() {
		return customid;
	}

	public void setCustomid(String customid) {
		this.customid = customid;
	}

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

}
