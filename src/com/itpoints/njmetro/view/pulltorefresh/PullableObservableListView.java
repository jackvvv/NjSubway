package com.itpoints.njmetro.view.pulltorefresh;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;

public class PullableObservableListView extends ListView implements Pullable {
	int PARALLAX_SPEED = 150;

	protected List<ImageView> imageViewList = new ArrayList<ImageView>();
	View firstVisibleView = null;
	float recyclerviewCenterY = -1;
	Rect rect = new Rect();
	View currentImageView;

	private boolean mPullRefresh = true;
	private boolean mPullLoading = true;

	public PullableObservableListView(Context context) {
		super(context);
	}

	public PullableObservableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableObservableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void imageParallax(ImageView imageView) {
		if (!imageViewList.contains(imageView))
			imageViewList.add(imageView);
	}

	public static float limit(float min, float value, float max) {
		return Math.max(Math.min(value, max), min);
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			// 没有item的时候也可以下拉刷新
			return mPullRefresh;
		} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
			// 滑到ListView的顶部了
			return mPullRefresh;
		} else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			// 没有item的时候也可以上拉加载
			return mPullLoading;
		} else if (getLastVisiblePosition() == (getCount() - 1)) {
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return mPullLoading;
		}
		return false;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (getChildCount() > 0) {
			if (firstVisibleView == null)
				firstVisibleView = getChildAt(0);
			if (recyclerviewCenterY == -1)
				recyclerviewCenterY = getMeasuredHeight() / 2 + getTop();

			for (int i = 0, count = imageViewList.size(); i < count; ++i) {
				currentImageView = imageViewList.get(i);
				currentImageView.getGlobalVisibleRect(rect);

				float yOffset = limit(-1, (recyclerviewCenterY - rect.top) / currentImageView.getHeight(), 1);
				ViewHelper.setTranslationY(currentImageView, (-1f + yOffset) * PARALLAX_SPEED);
			}
		}
	}

	/**
	 * enable or disable pull down refresh feature
	 */
	public void setPullRefreshEnable(boolean enable) {
		mPullRefresh = enable;
	}

	/**
	 * enable or disable pull up load more feature.
	 */
	public void setPullLoadEnable(boolean enable) {
		mPullLoading = enable;
	}

}
