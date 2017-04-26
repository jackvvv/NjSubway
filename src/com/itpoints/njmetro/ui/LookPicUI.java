package com.itpoints.njmetro.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.photoview.PhotoViewAttacher;
import com.itpoints.njmetro.view.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

/**
 * 查看大图
 * 
 * @author peidongxu
 * 
 */
public class LookPicUI extends BaseUI {
	private ImageView ivPictrue;
	private String imgUrl;
	private PhotoViewAttacher mAttacher;
	private ProgressBar progressBar;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.look_pic);
	}

	@Override
	protected void findView_AddListener() {
		ivPictrue = (ImageView) findViewById(R.id.iv_look_pic);
		progressBar = (ProgressBar) findViewById(R.id.pb_look_pic);
	}

	@Override
	protected void prepareData() {
		Intent intent = getIntent();
		imgUrl = intent.getStringExtra(Constants.IMG_PATH);

		if (!Utils.isEmity(imgUrl)) {
			progressBar.setVisibility(View.VISIBLE);
			BitmapHelp.getInstance(this).display(ivPictrue, imgUrl, new BitmapLoadCallBack<View>() {
				@Override
				public void onLoadCompleted(View arg0, String arg1, Bitmap bm, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
					progressBar.setVisibility(View.GONE);
					ivPictrue.setImageBitmap(bm);
					mAttacher = new PhotoViewAttacher(ivPictrue);
					mAttacher.setScaleType(ScaleType.FIT_CENTER);
					mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
						@Override
						public void onPhotoTap(View view, float x, float y) {
							back();
						}
					});
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					progressBar.setVisibility(View.GONE);
				}
			});
		}
	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	protected void back() {
		finish();
	}

}
