package com.itpoints.njmetro.ui.gn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.ImgAdapter;
import com.itpoints.njmetro.bean.RequestReturnBean;
import com.itpoints.njmetro.bean.ZhiyuzheReportTypeBean;
import com.itpoints.njmetro.http.HttpUtil;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.ui.LookMorePicUI;
import com.itpoints.njmetro.ui.LookPicUI;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.DataAnalysisUtil;
import com.itpoints.njmetro.utils.DialogUtil;
import com.itpoints.njmetro.utils.DialogUtil.DialogClickCallBack;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.UrlConstants;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.MyGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shorigo.photo.model.PhotoModel;
import com.shorigo.photo.ui.PhotoSelectorActivity;

/**
 * 志愿者上报
 * 
 * @author peidongxu
 * 
 */
public class ZhiyuzheReportUI extends BaseUI implements OnItemClickListener, DialogClickCallBack {

	private String requestId;

	private ZhiyuzheReportTypeBean zhiyuzheReportTypeBean;
	private TextView tv_type;

	private EditText et_content;
	private String content;

	private MyGridView gv_img;
	private ImgAdapter adapter;
	private final int SELECT_IMAGE_CODE = 1001;
	private final int CAMERA_PHOTO = 1002;
	// 选择图片
	private String str_choosed_img = "";
	private List<PhotoModel> selected;
	private final int MAX_PHOTOS = 5;
	private DialogUtil dialogUtil;
	public static File cameraFile;
	private List<String> listFilePath;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.zhiyuzhe_report);
	}

	@Override
	protected void findView_AddListener() {
		LinearLayout ll_type = (LinearLayout) findViewById(R.id.ll_zheyuzhe_report_type_select);
		ll_type.setOnClickListener(this);
		tv_type = (TextView) findViewById(R.id.tv_zheyuzhe_report_type);

		et_content = (EditText) findViewById(R.id.et_zheyuzhe_report_content);

		gv_img = (MyGridView) findViewById(R.id.gv_zheyuzhe_report);
		gv_img.setOnItemClickListener(this);

		TextView tv_submit = (TextView) findViewById(R.id.tv_feed_zheyuzhe_report_launch);
		tv_submit.setOnClickListener(this);

	}

	@Override
	protected void prepareData() {
		setTitle("红领巾上报");

		dialogUtil = new DialogUtil();
		dialogUtil.setDialogHight(Constants.height);
		dialogUtil.setCallBack(this);

		listFilePath = new ArrayList<String>();
		selected = new ArrayList<PhotoModel>();
		PhotoModel photoModel = new PhotoModel();
		photoModel.setOriginalPath("default");
		selected.add(photoModel);
		adapter = new ImgAdapter(this, selected, handler);
		gv_img.setAdapter(adapter);

		getRequestId();
	}

	/**
	 * 获取请求ID
	 */
	private void getRequestId() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_SENDINIT_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysisData(response.toString());
				if (HttpUtil.checkHttpSuccess(ZhiyuzheReportUI.this, returnBean.getCode())) {
					requestId = (String) returnBean.getObject();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 获取请求IDPOST
	 */
	private void save() {
		String url = HttpUtil.getUrl(UrlConstants.USER_V1_VOLUNTEER_SENDSAVE_URL);
		RequestParams params = new RequestParams();
		params.put("accessToken", MyApplication.token);
		params.put("requestId", requestId);
		params.put("attrib15", zhiyuzheReportTypeBean.getAttrib15());
		params.put("attrib20", content);

		String path;
		for (int i = 0; i < listFilePath.size(); i++) {
			path = listFilePath.get(i);
			params.put("attrib0" + String.valueOf(7 + i), path);
		}
		HttpUtil.post(this, url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				RequestReturnBean returnBean = DataAnalysisUtil.analysis(response.toString());
				if (HttpUtil.checkHttpSuccess(ZhiyuzheReportUI.this, returnBean.getCode())) {
					MyApplication.getInstance().showToast("信息上报成功");
					back();
				} else {
					MyApplication.getInstance().showToast("信息上报失败");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 文件上传
	 */
	private void fsUpload(String tempPath) {
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
				if (HttpUtil.checkHttpSuccess(ZhiyuzheReportUI.this, returnBean.getCode())) {
					//
					listFilePath.add(returnBean.getObject().toString());
					mHandler.sendEmptyMessage(0);
				}
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
		case R.id.ll_zheyuzhe_report_type_select:
			// 选择上报分类
			Intent intent = new Intent(this, ZhiyuzheReportTypeUI.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.tv_feed_zheyuzhe_report_launch:
			//
			if (zhiyuzheReportTypeBean == null) {
				MyApplication.getInstance().showToast("请选择上报类型");
				return;
			}
			content = et_content.getText().toString();
			listFilePath = new ArrayList<String>();
			if (selected != null && selected.size() > 1) {
				count = 0;
				for (int i = 0; i < selected.size() - 1; i++) {
					if (!"default".equals(selected.get(i).getOriginalPath())) {
						fsUpload(selected.get(i).getOriginalPath());
					}
				}
			} else {
				save();
			}
			break;
		default:
			break;
		}
	}

	private int count = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			count++;
			if (count == selected.size() - 1) {
				save();
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == selected.size() - 1) {// 如果是最后一个
			if (selected != null && selected.size() >= MAX_PHOTOS) {
				MyApplication.getInstance().showToast("最多选择5张");
			} else {
				dialogUtil.setDialogWidth(Constants.width, true);
				dialogUtil.showImgDialog(this);
			}
		} else {
			List<PhotoModel> lists = new ArrayList<PhotoModel>();
			lists.addAll(selected);
			lists.remove(lists.size() - 1);

			ArrayList<String> listImg = new ArrayList<String>();

			for (int i = 0; i < lists.size(); i++) {
				listImg.add(lists.get(i).getOriginalPath());
			}

			if (listImg.size() == 1) {// 单张图片
				Intent intent = new Intent(this, LookPicUI.class);
				intent.putExtra("imgUrl", listImg.get(0));
				this.startActivity(intent);
			} else if (listImg.size() > 1) {// 多张图片
				Intent intent = new Intent(this, LookMorePicUI.class);
				intent.putExtra("position", position);
				intent.putStringArrayListExtra("img", listImg);
				this.startActivity(intent);

			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 100:
				//
				zhiyuzheReportTypeBean = (ZhiyuzheReportTypeBean) data.getSerializableExtra("bean");
				if (zhiyuzheReportTypeBean != null) {
					tv_type.setText(zhiyuzheReportTypeBean.getName());
				}
				break;
			case SELECT_IMAGE_CODE:
				if (data != null) {
					List<PhotoModel> photos = (List<PhotoModel>) data.getExtras().getSerializable("photos");
					selected.clear();
					adapter.notifyDataSetChanged();
					selected.addAll(photos);
					PhotoModel addModel = new PhotoModel();
					addModel.setOriginalPath("default");
					selected.add(addModel);
					adapter.notifyDataSetChanged();
				}
				break;
			case CAMERA_PHOTO:// 拍照上传
				if (cameraFile != null && cameraFile.exists()) {
					str_choosed_img = cameraFile.getAbsolutePath();
					PhotoModel cameraPhotoModel = new PhotoModel();
					cameraPhotoModel.setChecked(true);
					cameraPhotoModel.setOriginalPath(str_choosed_img);
					if (selected.size() > 0) {// 如果原来有图片
						selected.remove(selected.size() - 1);
					}
					selected.add(cameraPhotoModel);
					PhotoModel addModel1 = new PhotoModel();
					addModel1.setChecked(false);
					addModel1.setOriginalPath("default");
					selected.add(addModel1);
					adapter.notifyDataSetChanged();
					MediaScannerConnection.scanFile(this, new String[] { str_choosed_img }, null, null);
				}
				break;
			default:
				break;
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 从选的照片中删除其中一张照片
				if (selected != null && selected.size() > msg.arg1) {
					selected.remove(msg.arg1);
				}
				adapter.setData(selected);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 弹出框回调
	 */
	@Override
	public void callBack(View v) {
		switch (v.getId()) {
		case R.id.ll_zheyuzhe_report_type_select:
			// 选择类型
			Intent intent = new Intent(this, ZhiyuzheReportTypeUI.class);
			startActivity(intent);
			break;
		case R.id.choose_by_camera:
			// 拍照上传
			dialogUtil.dismissDialog();
			if (selected.size() > MAX_PHOTOS) {
			} else {
				selectPicFromCamera(this);
			}
			break;
		case R.id.choose_by_local:
			// 本地上传
			dialogUtil.dismissDialog();
			enterChoosePhoto();
			break;
		default:
			break;
		}
	}

	/**
	 * 照相获取图片
	 * 
	 * @param mContext
	 * @param cameraFile
	 * @return 图片路径
	 */
	private void selectPicFromCamera(Context mContext) {
		if (!Utils.getSDState()) {
			Toast.makeText(mContext, "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
			return;
		}
		cameraFile = new File(Constants.path + Constants._image + File.separator + System.currentTimeMillis() + ".png");
		try {
			FileUtils.deleteFile(cameraFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cameraFile.getParentFile().mkdirs();
			((Activity) mContext).startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), CAMERA_PHOTO);
		}
	}

	private void enterChoosePhoto() {
		List<PhotoModel> choosed = new ArrayList<PhotoModel>();
		if (selected.size() > 0) {
			choosed.addAll(selected);
			choosed.remove(choosed.size() - 1);
		}
		Intent intent = new Intent(this, PhotoSelectorActivity.class);
		intent.putExtra(PhotoSelectorActivity.KEY_MAX, MAX_PHOTOS);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("selected", (ArrayList<? extends Parcelable>) choosed);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, SELECT_IMAGE_CODE);
	}

	@Override
	protected void back() {
		finish();
	}

}
