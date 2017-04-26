package com.itpoints.njmetro.ui;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.UserBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.LogUtils;
import com.itpoints.njmetro.utils.MyConfig;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 登录页
 * 
 * @author peidongxu
 * 
 */
public class LoginUI extends BaseUI {
	// 手机号、密码
	private EditText et_phone, et_pass;
	private String phone, pass;
	private UMShareAPI shareAPI;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.login);
	}

	@Override
	protected void findView_AddListener() {

		et_phone = (EditText) findViewById(R.id.et_login_user);
		et_pass = (EditText) findViewById(R.id.et_login_password);

		TextView tv_login_forget_pass = (TextView) findViewById(R.id.tv_login_forget_pass);
		tv_login_forget_pass.setOnClickListener(this);
		TextView tv_login_register = (TextView) findViewById(R.id.tv_login_register);
		tv_login_register.setOnClickListener(this);
		TextView btn_login_login = (TextView) findViewById(R.id.btn_login_login);
		btn_login_login.setOnClickListener(this);

		// 第三方图标:
		ImageView iv_login_qq = (ImageView) findViewById(R.id.iv_login_qq);
		iv_login_qq.setOnClickListener(this);
		ImageView iv_login_sina = (ImageView) findViewById(R.id.iv_login_sina);
		iv_login_sina.setOnClickListener(this);
		ImageView iv_login_wechat = (ImageView) findViewById(R.id.iv_login_wechat);
		iv_login_wechat.setOnClickListener(this);

	}

	@Override
	protected void prepareData() {
		setTitle("登录");
		// 初始化登录
		shareAPI = UMShareAPI.get(this);
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login_forget_pass:
			//
			startActivity(new Intent(this, ForgetPassUI.class));
			break;
		case R.id.btn_login_login:
			//
			phone = et_phone.getText().toString();
			pass = et_pass.getText().toString();

			if (Utils.isEmity(phone)) {
				MyApplication.getInstance().showToast("手机号不能为空");
				return;
			}
			if (!Utils.isMobileNO(phone)) {
				MyApplication.getInstance().showToast("手机号格式有误");
				return;
			}
			if (Utils.isEmity(pass)) {
				MyApplication.getInstance().showToast("密码不能为空");
				return;
			}
			userLogin();
			break;
		case R.id.tv_login_register:
			startActivity(new Intent(this, RegisterUI.class));
			break;
		case R.id.iv_login_qq:
			// QQ登录
			authorize(SHARE_MEDIA.QQ);
			break;
		case R.id.iv_login_sina:
			// 新浪登录
			authorize(SHARE_MEDIA.SINA);
			break;
		case R.id.iv_login_wechat:
			// 微信登录
			authorize(SHARE_MEDIA.WEIXIN);
			break;
		default:
			break;
		}
	}

	/**
	 * 用户登录
	 */
	private void userLogin() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_LOGIN_URL);
		RequestParams params = new RequestParams();
		params.put("loginId", phone);
		params.put("password", pass);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(response.toString(), UserBean.class);

				if (HttpUtil.checkHttpSuccess(LoginUI.this, userBean.getCode())) {
					// 登录成功
					MyApplication.token = userBean.getToken();
					MyApplication.userBean = userBean;
					MyConfig.saveToken(LoginUI.this, userBean.getToken());
					MyConfig.saveUserInfo(LoginUI.this, userBean);
					MyApplication.getInstance().showToast("登录成功");
					back();
				} else {
					MyApplication.getInstance().showToast("用户名密码错误");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 第三方登录
	 */
	private void otherLogin(String openId, String avatar, String nick, String sex, String code) {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_OTHER_LOGIN_URL);
		RequestParams params = new RequestParams();
		params.put("openId", openId);// 第三方ID
		params.put("avatar", avatar);// 头像
		params.put("attrib02", nick);// 昵称
		params.put("sex", sex);// 性别 0.未知 1.男 2.女
		params.put("code", code);// 第三方类型 Q. QQ W.微信 X.新浪微博
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				LogUtils.e("otherlogin", response.toString());
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(response.toString(), UserBean.class);

				if (HttpUtil.checkHttpSuccess(LoginUI.this, userBean.getCode())) {
					// 登录成功
					MyApplication.token = userBean.getToken();
					MyApplication.userBean = userBean;
					MyConfig.saveToken(LoginUI.this, userBean.getToken());
					MyConfig.saveUserInfo(LoginUI.this, userBean);
					MyApplication.getInstance().showToast("登录成功");
					back();
				} else {
					MyApplication.getInstance().showToast("登录失败");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	// 执行授权,获取用户信息
	private void authorize(SHARE_MEDIA platform) {
		shareAPI.deleteOauth(this, platform, null);
		shareAPI.doOauthVerify(this, platform, umAuthListener);
	}

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			getUserInfo(platform);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			MyApplication.getInstance().showToast("登录失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			MyApplication.getInstance().showToast("登录取消");
		}
	};

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		shareAPI.getPlatformInfo(this, platform, new UMAuthListener() {

			@Override
			public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int status, Map<String, String> info) {
				if (info != null) {
					String openId = "";
					String avatar = "";
					String nick = "";
					String sex = "";
					String code = "";
					if (platform == SHARE_MEDIA.QQ) {
						openId = info.get("openid");
						avatar = info.get("profile_image_url");
						nick = info.get("screen_name");
						String gender = info.get("gender");
						if ("男".equals(gender)) {
							sex = "1";
						} else if ("女".equals(gender)) {
							sex = "2";
						} else {
							sex = "0";
						}
						code = "Q";
					} else if (platform == SHARE_MEDIA.SINA) {
						try {
							JSONObject jsonObject = new JSONObject(info.get("result"));
							openId = jsonObject.getString("id");
							avatar = jsonObject.getString("profile_image_url");
							nick = jsonObject.getString("screen_name");
							String gender = jsonObject.getString("gender");
							if ("m".equals(gender)) {
								sex = "1";
							} else if ("f".equals(gender)) {
								sex = "2";
							} else {
								sex = "0";
							}
							code = "X";
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (platform == SHARE_MEDIA.WEIXIN) {
						openId = info.get("openid");
						avatar = info.get("headimgurl");
						nick = info.get("nickname");
						sex = info.get("sex");
						code = "W";
					}

					otherLogin(openId, avatar, nick, sex, code);
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA arg0, int arg1) {
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		shareAPI.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void back() {
		finish();
	}

}
