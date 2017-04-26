package com.itpoints.njmetro.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.itpoints.njmetro.R;

/**
 * 弹出框工具类
 * 
 * @author peidongxu
 * 
 */
public class DialogUtil {
	private Dialog mDialog;
	private DialogClickCallBack dialogClick;
	private int width;
	private int hight;

	/**
	 * 设置弹出框宽度
	 * 
	 * @param width
	 *            宽度
	 * @param isFull
	 *            宽度是否满屏
	 */
	public void setDialogWidth(int width, boolean isFull) {
		if (isFull) {
			this.width = width;
		} else {
			this.width = width - width / 10 * 2;
		}
	}

	/**
	 * 设置弹出框高度
	 * 
	 * @param hight
	 *            高度
	 */
	public void setDialogHight(int hight) {
		this.hight = hight - hight / 10 * 3;
	}

	/**
	 * 设置回调函数
	 */
	public void setCallBack(DialogClickCallBack dialogClick) {
		this.dialogClick = dialogClick;
	}

	/**
	 * 选择图片弹出框
	 */
	public void showImgDialog(Context context) {
		mDialog = new Dialog(context, R.style.MyDialogStyle);
		View view = View.inflate(context, R.layout.dialog_choose_img, null);
		view.findViewById(R.id.other_view).setOnClickListener(cancleClickListener);
		view.findViewById(R.id.dialog_cancel).setOnClickListener(cancleClickListener);
		// 拍照上传
		view.findViewById(R.id.choose_by_camera).setOnClickListener(onClickListener);
		// 本地上传
		view.findViewById(R.id.choose_by_local).setOnClickListener(onClickListener);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.show();
		setDialogAttributes(mDialog.getWindow(), width);
	}

	/**
	 * 拨打电话弹出框
	 */
	public void showPhoneCallDialog(Context context) {
		mDialog = new Dialog(context, R.style.MyDialogStyle);
		View view = View.inflate(context, R.layout.dialog_phone_call, null);
		view.findViewById(R.id.other_view).setOnClickListener(cancleClickListener);
		view.findViewById(R.id.dialog_cancel).setOnClickListener(cancleClickListener);
		// 拨打电话
		view.findViewById(R.id.dialog_phone_call_call).setOnClickListener(onClickListener);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.show();
		setDialogAttributes(mDialog.getWindow(), width);
	}
	
	
	/**
	 *  分享弹出框
	 */
	public void showShareDialog(Context context) {
		mDialog = new Dialog(context, R.style.MyDialogStyle);
		View view = View.inflate(context, R.layout.dialog_share, null);
		view.findViewById(R.id.other_view).setOnClickListener(cancleClickListener);
		view.findViewById(R.id.dialog_cancel).setOnClickListener(cancleClickListener);
		// 
		view.findViewById(R.id.ll_dialog_share_email).setOnClickListener(onClickListener);
		view.findViewById(R.id.ll_dialog_share_sms).setOnClickListener(onClickListener);
		view.findViewById(R.id.ll_dialog_share_sina).setOnClickListener(onClickListener);
		view.findViewById(R.id.ll_dialog_share_wechat).setOnClickListener(onClickListener);
		
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.show();
		setDialogAttributes(mDialog.getWindow(), width);
	}

	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (dialogClick != null) {
				dialogClick.callBack(v);
			}
		}
	};
	/**
	 * 返回点击事件
	 */
	OnClickListener cancleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dismissDialog();
		}
	};

	/**
	 * 设置对话框的左右边距
	 * 
	 * @param window
	 *            窗口对象
	 * @param width
	 *            宽度
	 */
	public void setDialogAttributes(Window window, int width) {
		LayoutParams attributes = window.getAttributes();
		attributes.width = width;
		window.setAttributes(attributes);

		attributes = null;
	}

	/**
	 * 设置对话框的左右、上下边距
	 * 
	 * @param window
	 *            窗口对象
	 * @param width
	 *            宽度
	 */
	public void setDialogAttributes(Window window, int width, int height) {
		LayoutParams attributes = window.getAttributes();
		attributes.width = width;
		attributes.height = height;
		window.setAttributes(attributes);

		attributes = null;
	}

	/**
	 * 关闭弹出框
	 */
	public void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	/**
	 * 对话框回调接口
	 * 
	 * @author peidongxu
	 */
	public interface DialogClickCallBack {
		public void callBack(View v);
	}

}
