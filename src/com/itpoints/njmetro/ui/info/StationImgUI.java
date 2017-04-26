package com.itpoints.njmetro.ui.info;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.db.DbHelper;
import com.itpoints.njmetro.ui.BaseUI;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.FileUtils;
import com.itpoints.njmetro.utils.Utils;
import com.itpoints.njmetro.view.photoview.PhotoViewAttacher;
import com.itpoints.njmetro.view.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

/**
 * 车站---站层图
 * 
 * @author peidongxu
 * 
 */
public class StationImgUI extends BaseUI {
	private ImageView ivPictrue;
	private String imgUrl;
	private PhotoViewAttacher mAttacher;
	private ProgressBar progressBar;

	// 站点
	private String station;
	private String station_en;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.station_img);
	}

	@Override
	protected void findView_AddListener() {
		ivPictrue = (ImageView) findViewById(R.id.iv_station_img_pic);
		progressBar = (ProgressBar) findViewById(R.id.pb_station_img);

	}

	@Override
	protected void prepareData() {
		setTitle("站层图");
		Intent intent = getIntent();
		station = intent.getStringExtra("station");

		StationEnBean stationEnBean = (StationEnBean) DbHelper.getInstance(this).searchOne(StationEnBean.class, "station", station);
		if (stationEnBean != null) {
			station_en = stationEnBean.getStation_en();
		}

		imgUrl = Constants.path + Constants._anex + File.separator + "station_img" + File.separator + station_en + ".jpg";
//		imgUrl = FileUtils.getFilePath(Constants.path + Constants._anex + File.separator + "station_img" + File.separator + station_en + ".jpg");

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
