package com.itpoints.njmetro.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itpoints.njmetro.bean.AdListBean;
import com.itpoints.njmetro.bean.AdTypeBean;
import com.itpoints.njmetro.bean.AppVersion;
import com.itpoints.njmetro.bean.ArticleListBean;
import com.itpoints.njmetro.bean.ArticleTypeBean;
import com.itpoints.njmetro.bean.ExamineBean;
import com.itpoints.njmetro.bean.FeedBackBean;
import com.itpoints.njmetro.bean.FeedBackDetailBean;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.WeatherBean;
import com.itpoints.njmetro.bean.WeiboListBean;
import com.itpoints.njmetro.bean.ZhiyuzheReportTypeBean;

/**
 * 数据解析共通类
 * 
 * @author peidongxu
 * 
 */
public class DataAnalysisUtil {
	private static String TAG = "DataAnalysisUtil";

	/**
	 * 无返回值解析，只判断状态
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysis(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// returnBean.setCode("200");
		// returnBean.setMessage("success");
		return returnBean;
	}

	/**
	 * 返回值单独值解析，只判断状态
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysisData(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("smscode")) {
				// 验证码
				returnBean.setObject(jsonObject.getString("smscode"));
			}
			if (jsonObject.has("requestId")) {
				// 请求ID
				returnBean.setObject(jsonObject.getString("requestId"));
			}
			if (jsonObject.has("fileUpload1")) {
				// 文件链接
				returnBean.setObject(jsonObject.getString("fileUpload1"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 返回值单独值解析，只判断状态
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysisFileUpload(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			String resourceServer = "";
			String avatar = "";
			if (jsonObject.has("constant")) {
				JSONObject jsonObject2 = jsonObject.getJSONObject("constant");
				resourceServer = jsonObject2.getString("resourceServer");
			}
			if (jsonObject.has("datum")) {
				JSONObject jsonObject2 = jsonObject.getJSONObject("datum");
				avatar = jsonObject2.getString("avatar");
			}
			// 文件链接
			returnBean.setObject(avatar);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取首起页广告
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getAd(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", jsonObject.getString("code"));
			map.put("url", jsonObject.getString("url"));
			returnBean.setObject(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取广告分类
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getAdType(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<AdTypeBean> listAdTypeBean = new ArrayList<AdTypeBean>();
				AdTypeBean adTypeBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					adTypeBean = new AdTypeBean();
					adTypeBean.setCustomid(jsonObject2.getString("customid"));
					adTypeBean.setCodename(jsonObject2.getString("codename"));
					listAdTypeBean.add(adTypeBean);
				}
				returnBean.setListObject(listAdTypeBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取广告列表
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getAdList(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("totalPage")) {
				returnBean.setTotalPage(jsonObject.getString("totalPage"));
			}
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<AdListBean> listAdListBean = new ArrayList<AdListBean>();
				AdListBean adListBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					adListBean = new AdListBean();
					adListBean.setAttrib03(jsonObject2.getString("attrib03"));
					adListBean.setAttrib37(jsonObject2.getString("attrib37"));
					adListBean.setConflictId(jsonObject2.getString("conflictId"));
					adListBean.setRownum(jsonObject2.getString("rownum"));
					adListBean.setCreated(jsonObject2.getString("created"));
					adListBean.setUrl(jsonObject2.getString("url"));
					listAdListBean.add(adListBean);
				}
				returnBean.setListObject(listAdListBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取文章分类
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getArticleType(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<ArticleTypeBean> listArticleTypeBean = new ArrayList<ArticleTypeBean>();
				ArticleTypeBean articleTypeBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					articleTypeBean = new ArticleTypeBean();
					articleTypeBean.setCustomid(jsonObject2.getString("customid"));
					articleTypeBean.setCodename(jsonObject2.getString("codename"));
					if (jsonObject2.has("icon")) {
						articleTypeBean.setIcon(jsonObject2.getString("icon"));
					}
					listArticleTypeBean.add(articleTypeBean);
				}
				returnBean.setListObject(listArticleTypeBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取文章列表
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getArticleList(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("totalPage")) {
				returnBean.setTotalPage(jsonObject.getString("totalPage"));
			}
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<ArticleListBean> listArticleListBean = new ArrayList<ArticleListBean>();
				ArticleListBean articleListBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					articleListBean = new ArticleListBean();
					articleListBean.setAttrib03(jsonObject2.getString("attrib03"));
					articleListBean.setAttrib37(jsonObject2.getString("attrib37"));
					articleListBean.setConflictId(jsonObject2.getString("conflictId"));
					articleListBean.setRownum(jsonObject2.getString("rownum"));
					articleListBean.setCreated(jsonObject2.getString("created"));
					articleListBean.setUrl(jsonObject2.getString("url"));
					listArticleListBean.add(articleListBean);
				}
				returnBean.setListObject(listArticleListBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取天气
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getWeather(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				WeatherBean weatherBean = new WeatherBean();
				JSONObject jsonObject2 = jsonObject.getJSONObject("datum");
				weatherBean.setAttrib12(jsonObject2.getString("attrib12"));
				weatherBean.setAttrib04(jsonObject2.getString("attrib04"));
				weatherBean.setAttrib09(jsonObject2.getString("attrib09"));
				returnBean.setObject(weatherBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取微博list
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getWeiboList(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("totalPage")) {
				returnBean.setTotalPage(jsonObject.getString("totalPage"));
			}
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<WeiboListBean> listWeiboListBean = new ArrayList<WeiboListBean>();
				WeiboListBean weiboListBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					weiboListBean = new WeiboListBean();
					weiboListBean.setAttrib03(jsonObject2.getString("attrib03"));
					weiboListBean.setAttrib19(jsonObject2.getString("attrib19"));
					weiboListBean.setConflictId(jsonObject2.getString("conflictId"));
					weiboListBean.setCreated(jsonObject2.getString("created"));
					listWeiboListBean.add(weiboListBean);
				}
				returnBean.setListObject(listWeiboListBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取上报分类
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getVolunteerType(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<ZhiyuzheReportTypeBean> listZhiyuzheReportTypeBean = new ArrayList<ZhiyuzheReportTypeBean>();
				ZhiyuzheReportTypeBean zhiyuzheReportTypeBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					zhiyuzheReportTypeBean = new ZhiyuzheReportTypeBean();
					zhiyuzheReportTypeBean.setName(jsonObject2.getString("codename"));
					zhiyuzheReportTypeBean.setAttrib15(jsonObject2.getString("attrib15"));
					listZhiyuzheReportTypeBean.add(zhiyuzheReportTypeBean);
				}
				returnBean.setListObject(listZhiyuzheReportTypeBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取志愿者状态
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getVolunteerState(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				if (!Utils.isEmity(jsonObject.getString("datum"))) {
					JSONObject jsonObject2 = jsonObject.getJSONObject("datum");
					String state = jsonObject2.getString("attrib15");
					returnBean.setObject(state);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取反馈列表
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getFeedBackList(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<FeedBackBean> listFeedBackBean = new ArrayList<FeedBackBean>();
				FeedBackBean feedBackBean;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					feedBackBean = new FeedBackBean();
					feedBackBean.setAttrib15(jsonObject2.getString("attrib15"));
					feedBackBean.setAttrib16(jsonObject2.getString("attrib16"));
					feedBackBean.setAttrib20(jsonObject2.getString("attrib20"));
					feedBackBean.setConflictId(jsonObject2.getString("conflictId"));
					feedBackBean.setCreated(jsonObject2.getString("created"));
					feedBackBean.setName(jsonObject2.getString("name"));
					feedBackBean.setRowId(jsonObject2.getString("rowId"));
					listFeedBackBean.add(feedBackBean);
				}
				returnBean.setListObject(listFeedBackBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 获取反馈详情
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean getFeedBackDetail(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				if (!Utils.isEmity(jsonObject.getString("datum"))) {
					JSONObject jsonObject2 = jsonObject.getJSONObject("datum");
					FeedBackDetailBean feedBackDetailBean = new FeedBackDetailBean();
					feedBackDetailBean.setAttrib20(jsonObject2.getString("attrib20"));
					feedBackDetailBean.setCreated(jsonObject2.getString("created"));
					returnBean.setObject(feedBackDetailBean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 版本更新数据解析
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysisVersion(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			String resourceServer = "";
			String avatar = "";
			if (jsonObject.has("constant")) {
				JSONObject jsonObject2 = jsonObject.getJSONObject("constant");
				resourceServer = jsonObject2.getString("resourceServer");
			}
			AppVersion appVersion = new AppVersion();
			if (jsonObject.has("datum")) {
				JSONObject jsonObject2 = jsonObject.getJSONObject("datum");
				appVersion.setAttrib01(jsonObject2.getString("attrib01"));
				appVersion.setAttrib02(jsonObject2.getString("attrib02"));
				appVersion.setAttrib03(jsonObject2.getString("attrib03"));
				appVersion.setAttrib16(jsonObject2.getString("attrib16"));
				avatar = jsonObject2.getString("attrib13");
				appVersion.setAttrib13(resourceServer + avatar);
			}
			returnBean.setObject(appVersion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 问卷调查数据解析
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysisGetQuestion(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			String code = jsonObject.getString("code");
			returnBean.setCode(code);
			returnBean.setMessage(jsonObject.getString("message"));
			if (jsonObject.has("datum")) {
				JSONArray jsonArray = jsonObject.getJSONArray("datum");
				JSONObject jsonObject2;
				List<ExamineBean> listExamineBean = new ArrayList<ExamineBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject2 = jsonArray.getJSONObject(i);
					ExamineBean questionBean = new ExamineBean();
					questionBean.setAttrib12(jsonObject2.getString("attrib12"));
					questionBean.setConflictId(jsonObject2.getString("conflictId"));
					questionBean.setCreated(jsonObject2.getString("created"));
					questionBean.setUrl(jsonObject2.getString("url"));
					listExamineBean.add(questionBean);
				}
				returnBean.setListObject(listExamineBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

	/**
	 * 数据更新-解析
	 * 
	 * @param result
	 * @return RequestReturnBean
	 */
	public static RequestReturnBean analysisCheckVerstion(String result) {
		LogUtils.i(TAG, result);
		RequestReturnBean returnBean = new RequestReturnBean();
		try {
			JSONObject jsonObject = new JSONObject(result);
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", jsonObject.getString("code"));
			map.put("url", jsonObject.getString("url"));
			returnBean.setObject(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnBean;
	}

}
