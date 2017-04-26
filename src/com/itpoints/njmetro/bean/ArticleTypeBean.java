package com.itpoints.njmetro.bean;

import java.io.Serializable;

/**
 * 文章分类
 * 
 * @author peidongxu
 * 
 */
public class ArticleTypeBean implements Serializable {

	private String customid;
	private String codename;
	private String icon;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
