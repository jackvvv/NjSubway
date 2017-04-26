package com.itpoints.njmetro.bean;

import java.util.Map;

/**
 * 用户信息
 * 
 * @author peidongxu
 * 
 */
public class UserBean {
	// {
	// "message": "login success",
	// "constant": {
	// "resourceServer": "http://202.102.92.19:28288/upload"
	// },
	// "accessToken": "0a4ab074fe6f4905b522e3841a2d56e4a7QaIK",
	// "code": 1,
	// "info": {
	// "enabled": "1",
	// "createTime": "2016-06-29 09:56:16",
	// "loginId": "18810305902",
	// "sex": "1",
	// "updateTime": "2016-06-29 09:56:16",
	// "attrib33": null,
	// "attrib32": null,
	// "attrib31": null,
	// "credits": 0,
	// "userTyp": "4",
	// "avatar": "/images/defaultAvatar.jpg",
	// "attrib02": "小雨",
	// "golds": 0,
	// "attrib14": null,
	// "attrib07": null,
	// "id": "1000340",
	// "rank": null,
	// "name": null
	// }
	// }

	private String message;
	private Map<String, String> constant;
	private String accessToken;
	private String code;
	private Map<String, String> info;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getConstant() {
		return constant;
	}

	public void setConstant(Map<String, String> constant) {
		this.constant = constant;
	}

	public String getToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

}
