package com.itpoints.njmetro.bean;

import java.io.Serializable;

/**
 * 反馈Bean
 * 
 * @author peidongxu
 * 
 */
public class FeedBackBean implements Serializable {
	private String attrib20;// 反馈内容
	private String created;// 投诉时间
	private String name;// 用户姓名
	private String rowId;// 用户ID
	private String conflictId;// 反馈ID
	private String attrib15;// 反馈类型
	private String attrib16;// 处理状态

	public String getAttrib20() {
		return attrib20;
	}

	public void setAttrib20(String attrib20) {
		this.attrib20 = attrib20;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getConflictId() {
		return conflictId;
	}

	public void setConflictId(String conflictId) {
		this.conflictId = conflictId;
	}

	public String getAttrib15() {
		return attrib15;
	}

	public void setAttrib15(String attrib15) {
		this.attrib15 = attrib15;
	}

	public String getAttrib16() {
		return attrib16;
	}

	public void setAttrib16(String attrib16) {
		this.attrib16 = attrib16;
	}

}
