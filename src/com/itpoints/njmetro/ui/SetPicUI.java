package com.itpoints.njmetro.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.UserBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.DialogUtil;
import com.itpoints.njmetro.utils.DialogUtil.DialogClickCallBack;
import com.itpoints.njmetro.utils.PicPickUtils;
import com.itpoints.njmetro.utils.PicPickUtils.OnPickedlistener;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 设置头像
 * 
 * @author peidongxu
 * 
 */
public class SetPicUI extends BaseUI implements OnPickedlistener, DialogClickCallBack {
	private ImageView iv_set_pic;
	// 选择图片dialog
	private PicPickUtils picPickUtils;
	// 对话框
	private DialogUtil dialogUtil;

	private String tempPath;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.set_pic);
	}

	@Override
	protected void findView_AddListener() {
		iv_set_pic = (ImageView) findViewById(R.id.iv_set_pic);
		iv_set_pic.setOnClickListener(this);
	}

	@Override
	protected void prepareData() {
		setTitle("头像");
		setRightButton("保存");

		// 初始化对话框
		dialogUtil = new DialogUtil();
		dialogUtil.setCallBack(this);
		// 初始化图片选择器
		picPickUtils = new PicPickUtils(this, null, this);

		UserBean userInfo = MyApplication.userBean;
		Map<String, String> info = userInfo.getInfo();
		if (info == null) {
			return;
		}
		String img_url = "";
		if (userInfo.getConstant() != null) {
			img_url = userInfo.getConstant().get("resourceServer");
		}
		String mPic = info.get("avatar");
		// 头像
		if (!Utils.isEmity(mPic) && mPic.contains("http")) {
			BitmapHelp.getInstance(this).display(iv_set_pic, mPic);
		} else {
			BitmapHelp.getInstance(this).display(iv_set_pic, img_url + mPic);
		}

	}

	/**
	 * 文件上传
	 */
	private void fsUpload() {
		String url = HttpUtil.getUrl(UrlConstants.FS_V1_FSUPLOAD_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		try {
			params.put("avatar", new File(tempPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisFileUpload(response.toString());
				if (HttpUtil.checkHttpSuccess(SetPicUI.this, returnBean.getCode())) {
					//
					chgProfile(returnBean.getObject().toString());
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 修改用户信息
	 */
	private void chgProfile(final String filePath) {
		if (filePath == null) {
			return;
		}
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_CHGACATAR_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("avatar", filePath);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(SetPicUI.this, returnBean.getCode())) {
					// 修改成功
					Map<String, String> info = MyApplication.userBean.getInfo();
					info.put("avatar", filePath);
					MyApplication.userBean.setInfo(info);
					MyApplication.getInstance().showToast("修改成功");
				} else {
					MyApplication.getInstance().showToast("修改失败");
				}
				back();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	protected void onMyClick(View v) {
		switch (v.getId()) {
		case R.id.tv_common_headbar_right:
			// 保存
			if (!Utils.isEmity(tempPath)) {
				fsUpload();
			}
			break;
		case R.id.iv_set_pic:
			dialogUtil.setDialogWidth(Constants.width, true);
			dialogUtil.showImgDialog(this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		picPickUtils.pickResult(requestCode, resultCode, data);
	}

	@Override
	public void picPicked(String path, Bitmap bmp) {
		// 选择图片回调
		if (!Utils.isEmity(path)) {
			tempPath = path;
			BitmapHelp.getInstance(this).display(iv_set_pic, tempPath);
		}
	}

	@Override
	public void callBack(View v) {
		switch (v.getId()) {
		case R.id.choose_by_camera:
			// 拍照上传
			dialogUtil.dismissDialog();
			picPickUtils.doPickPhotoAction(600, 600);
			picPickUtils.setDoCrop(true);
			picPickUtils.doTakePhoto();
			break;
		case R.id.choose_by_local:
			// 本地上传
			dialogUtil.dismissDialog();
			picPickUtils.doPickPhotoAction(600, 600);
			picPickUtils.setDoCrop(true);
			picPickUtils.doPickPhotoFromGallery();
			break;
		default:
			break;
		}
	}

	@Override
	protected void back() {
		finish();
	}

}
