package com.itpoints.njmetro.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.adapter.LookMorePicAdapter;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.view.ViewPagerFixed;
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
public class LookMorePicUI extends BaseUI implements OnPageChangeListener {
	private ProgressBar progressBar;
	private LinearLayout llDot;
	// ViewPager用于显示图片作品的控件
	private ViewPagerFixed vp;
	// 记录前一个点的位置
	private int prePosition = 0;
	// 存放数据源的集合
	private List<ImageView> mList;
	private int position;

	private ArrayList<String> imgList;
	private LookMorePicAdapter lookMorePicAdapter;
	private PhotoViewAttacher mAttacher;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.look_more_pic);
	}

	@Override
	protected void findView_AddListener() {
		progressBar = (ProgressBar) findViewById(R.id.pb_look_more_pic);
		llDot = (LinearLayout) findViewById(R.id.lldot_look_more_pic);
		vp = (ViewPagerFixed) findViewById(R.id.vp_look_more_pic);
	}

	@Override
	protected void prepareData() {
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 1);
		imgList = intent.getStringArrayListExtra("img");

		initView();
	}

	private void initView() {
		if (imgList == null || imgList.size() <= 0) {
			return;
		}
		mList = new ArrayList<ImageView>();
		for (int i = 0; i < imgList.size(); i++) {
			// 创建一个ImageView对象
			final ImageView iv = new ImageView(this);
			progressBar.setVisibility(View.VISIBLE);
			BitmapHelp.getInstance(this).display(iv, imgList.get(i), new BitmapLoadCallBack<View>() {
				@Override
				public void onLoadCompleted(View arg0, String arg1, Bitmap bm, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
					progressBar.setVisibility(View.GONE);
					iv.setImageBitmap(bm);
					mAttacher = new PhotoViewAttacher(iv);
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
				}
			});
			mList.add(iv);

			// 给线性布局加小圆点
			View view = new View(this);
			// 设置小圆点的宽高
			LayoutParams params = new LayoutParams(15, 15);
			// 设置圆点的间距
			params.leftMargin = 5;
			view.setLayoutParams(params);
			// 设置控件的默认状态
			view.setEnabled(false);
			// 利用背景选择器来作为背景图片
			view.setBackgroundResource(R.drawable.dots_selector);
			// 将小圆点加到线性布局中
			llDot.addView(view);
		}
		lookMorePicAdapter = new LookMorePicAdapter();
		lookMorePicAdapter.setData(mList, imgList);
		vp.setAdapter(lookMorePicAdapter);
		// 设置第一个小圆点为选中状态
		llDot.getChildAt(position).setEnabled(true);
		prePosition = position;
		vp.setCurrentItem(position);
		// ViewPager改变的监听
		vp.setOnPageChangeListener(this);
	}

	/**************************** ViewPager页面的监听 ************************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		// 更改当前点和前一个点的状态
		llDot.getChildAt(prePosition).setEnabled(false);
		llDot.getChildAt(position).setEnabled(true);
		// 改变前一个点的索引值
		prePosition = position;
	}

	@Override
	protected void onMyClick(View v) {
	}

	@Override
	protected void back() {
		finish();
	}

}
