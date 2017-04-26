package com.itpoints.njmetro.bean;

public class MsgBean extends BaseDBBean {

	// {
	// type:"1",
	// url:"http----"
	// content:"这是一个紧急公告，字数限制";
	// }
	// 说明 1：url 2：紧急公告

	private String type;
	private String url;
	private String content;
	private String time;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
