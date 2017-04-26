package com.itpoints.njmetro.bean;

import java.io.Serializable;

public class ExamineBean implements Serializable {
	private String created;// 创建时间
	private String attrib12;// 调查问卷名称
	private String conflictId;// 调查问卷ID
	private String url;// url

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getAttrib12() {
		return attrib12;
	}

	public void setAttrib12(String attrib12) {
		this.attrib12 = attrib12;
	}

	public String getConflictId() {
		return conflictId;
	}

	public void setConflictId(String conflictId) {
		this.conflictId = conflictId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
