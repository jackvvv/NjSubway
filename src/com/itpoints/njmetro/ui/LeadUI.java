package com.itpoints.njmetro.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.MyConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 引导页
 * 
 * @author peidongxu
 * 
 */
public class LeadUI extends BaseUI implements OnPageChangeListener, OnTouchListener {
	private List<View> views; // 滑动的图片集合
//	private List<View> listDots; // 图片标题正文的那些点
	private int currentIndex = 0; // 当前图片的索引号
//	private LinearLayout lldot_circle;
	private ViewPager viewPager;

	private int lastX = 0;
	// 引导图片
	private int[] guidePics;

	private DisplayImageOptions.Builder displayImageOptionsBuilder;

	@Override
	public void loadViewLayout() {
		setContentView(R.layout.lead);
	}

	@Override
	public void findView_AddListener() {
		viewPager = (ViewPager) findViewById(R.id.vp_lead);
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager = (ViewPager) findViewById(R.id.vp_lead);
//		lldot_circle = (LinearLayout) findViewById(R.id.lldot_lead);
	}

	@Override
	public void prepareData() {
		displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		// 开启内存缓存
		displayImageOptionsBuilder.cacheInMemory(false);
		// 开启SDCard缓存
		displayImageOptionsBuilder.cacheOnDisc(false);
		// 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		displayImageOptionsBuilder.displayer(new FadeInBitmapDisplayer(100));

		guidePics = new int[] { R.drawable.lead_1, R.drawable.lead_2, R.drawable.lead_3, R.drawable.lead_4, R.drawable.lead_5 };
		LayoutInflater mInflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
//		listDots = new ArrayList<View>();
		View view;
		ImageView imageView;
		for (int i = 0; i < guidePics.length; i++) {
			view = mInflater.inflate(R.layout.lead_item, null);
			imageView = (ImageView) view.findViewById(R.id.iv_lead_item);

			ImageLoader.getInstance().displayImage("drawable://" + guidePics[i], imageView, displayImageOptionsBuilder.build());
			views.add(view);

			if (i == guidePics.length - 1) {
				// 最后一条数据按钮显示
				View v = view.findViewById(R.id.btn_lead_item);
				v.setVisibility(View.VISIBLE);
				v.setOnClickListener(this);
			}

//			View point = new View(this);
//			LayoutParams params = new LayoutParams(15, 15);
//			params.setMargins(10, 0, 0, 0);
//			point.setLayoutParams(params);
//			point.setBackgroundResource(R.drawable.dots_selector);
//			lldot_circle.addView(point);
//			listDots.add(point);
		}
		currentIndex = 0;
		viewPager.setOnPageChangeListener(this);
		viewPager.setOnTouchListener(this);
		// 设置Adapter
		viewPager.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View v = views.get(position);
				container.addView(v);
				return v;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
		});
	}

	/**
	 * 设置选中的View
	 * 
	 * @param position
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= guidePics.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	private int oldPosition = 0;

	@Override
	public void onPageSelected(int position) {
		currentIndex = position;
//		listDots.get(oldPosition % views.size()).setBackgroundResource(R.drawable.dot_enable_true);
//		listDots.get(position % views.size()).setBackgroundResource(R.drawable.dot_enable_false);
//		oldPosition = position;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if ((lastX - event.getX()) > 0 && (currentIndex == views.size() - 1)) {
				// 最后一个向右滑动跳转页面
				MyConfig.setConfig(this, Constants.CONFIG_NAME, "showLead", false);
				startActivity(new Intent(this, RuleSelectUI.class));
				back();
			}
			break;
		}
		return false;
	}
	
	@Override
	protected void onMyClick(View v) {
		if (v.getId() == R.id.btn_lead_item) {
			// 立即体验按钮
			MyConfig.setConfig(this, Constants.CONFIG_NAME, "showLead", false);
			startActivity(new Intent(this, RuleSelectUI.class));
			back();
		} else {
			int position = (Integer) v.getTag();
			setCurView(position);
		}
	}

	@Override
	protected void back() {
		finish();
	}


}
