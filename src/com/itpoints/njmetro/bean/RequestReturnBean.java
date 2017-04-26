package com.itpoints.njmetro.bean;

import java.util.List;
import java.util.Map;

/**
 * 数据返回基类
 * 
 * @author peidongxu
 * 
 */
public class RequestReturnBean<T> {

	// 请求状态
	private String code;
	// 请求信息
	private String message;
	// 总页数
	private String totalPage;
	// 上传文件数据
	private Map<String, String> datum;

	private T object;
	private List<T> listObject;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public Map<String, String> getDatum() {
		return datum;
	}

	public void setDatum(Map<String, String> datum) {
		this.datum = datum;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public List<T> getListObject() {
		return listObject;
	}

	public void setListObject(List<T> listObject) {
		this.listObject = listObject;
	}

}
