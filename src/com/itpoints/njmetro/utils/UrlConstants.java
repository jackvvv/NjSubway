package com.itpoints.njmetro.utils;

/**
 * 请求URL常量类
 * 
 * @author peidongxu
 * 
 */
public class UrlConstants {

	
	/** 服务器url */
	public static String SERVICE_HOST = "http://consumer.njmetro.com.cn:8288/api";
//	public static String SERVICE_HOST = "http://202.102.92.19:28288/api";
	
	
	// 用户相关
	/** 检查用户是否注册 */
	public static String USER_V1_CHECKREG_URL = "/user/v1/checkReg";
	/** 发送手机验证码 */
	public static String USER_V1_SENDODE_URL = "/user/v1/sendCode";
	/** 用户注册 */
	public static String USER_V1_REGISTER_URL = "/user/v1/register";
	/** 登录接口 */
	public static String USER_V1_LOGIN_URL = "/user/v1/login";
	/** 第三方登录接口 */
	public static String USER_V1_OTHER_LOGIN_URL = "/user/v1/thirdPartyLogin";
	/** 查询用户信息 */
	public static String USER_V1_SELPROFILE_URL = "/user/v1/getProfile";
	/** 修改用户信息*/
	public static String USER_V1_CHGPROFILE_URL = "/user/v1/setProfile";
	/** 绑定或修改手机号码*/
	public static String USER_V1_RESETPHONE_URL = "/user/v1/resetPhone";
	/** 修改用户密码 */
	public static String USER_V1_CHGPASS_URL = "/user/v1/changePass";
	/** 找回用户密码 */
	public static String USER_V1_RESETPASS_URL = "/user/v1/resetPass";
	/** 修改用户头像 */
	public static String USER_V1_CHGACATAR_URL = "/user/v1/changeAvatar";
	/** 用户反馈新增初始化 */
	public static String USER_V1_COMPLAINTS_INIT_URL = "/user/v1/complaintsInit";
	/** 用户反馈保存 */
	public static String USER_V1_COMPLAINTS_SAVE_URL = "/user/v1/complaintsSave";
	/** 获取用户反馈列表 */
	public static String USER_V1_COMPLAINTS_LIST_URL = "/user/v1/complaintsList";
	/** 获取用户反馈详情 */
	public static String USER_V1_COMPLAINTS_INFO_URL = "/user/v1/complaintsInfo";
	
	
	
	// 志愿者相关
	/** 志愿者申请初始化 */
	public static String USER_V1_VOLUNTEER_INIT_URL = "/user/v1/volunteerInit";
	/** 志愿者申请保存 */
	public static String USER_V1_VOLUNTEER_SAVE_URL = "/user/v1/volunteerSave";
	/** 获取志愿者申请信息 */
	public static String USER_V1_VOLUNTEER_VIEW_URL = "/user/v1/volunteerView";
	/** 志愿者信息上报初始化 */
	public static String USER_V1_VOLUNTEER_SENDINIT_URL = "/user/v1/volunteerSendInit";
	/** 志愿者信息上报保存*/
	public static String USER_V1_VOLUNTEER_SENDSAVE_URL = "/user/v1/volunteerSendSave";
	
	
	
	// 文章相关
	/** 获取文章分类 */
	public static String ARTICLE_GET_ARTICLE_TYPE_URL = "/article/v1/listArticleType";
	/** 获取(分类\标签)文章列表 */
	public static String ARTICLE_GET_ARTICLE_LIST_URL = "/article/v1/listArticles";
	/** 获取文章标签 */
	public static String ARTICLE_GET_ARTICLE_TAGS_URL = "/article/v1/getArticleTags";
	/** 获取地铁生活分类 */
	public static String ARTICLE_GET_LIFE_TYPE_URL = "/article/v1/listMetroLifeType";
	/** 获取新浪微博列表 */
	public static String ARTICLE_GET_WEIBO_LIST_URL = "/article/v1/getWeiboArticles";
	/** 获取收藏列表 */
	public static String ARTICLE_GET_MYMARKS_LIST_URL = "/article/v1/getMyMarks";
	
	
	// 问卷调查 
	/** 获取调查问卷列表 */
	public static String QUESTION_GET_QUESTION_URL = "/question/v1/getQuestions";
	
	
	/** 用于用户上传图片、头像等 */
	public static String FS_V1_FSUPLOAD_URL = "/fs/v1/fileUpload";

	
	// 公共模块
	/** 版本更新检查*/
	public static String COMM_V1_CHECK_VERSION_URL = "/common/v1/checkVersion";
	/** 天气预报 */
	public static String COMM_V1_GET_WEATHER_URL = "/common/v1/getWeather";
	/** 线路数据更新检查 */
	public static String COMM_V1_CHECK_DATA_URL = "/common/v1/checkData";
	/** 线路数据更新检查 */
	public static String COMM_V1_GET_FISRT_AD_URL = "/common/v1/getFirstAD";

}
