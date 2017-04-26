package com.itpoints.njmetro.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {
	private boolean mPullRefresh = true;
	private boolean mPullLoading = true;
	private ScrollViewListener scrollViewListener = null;

	public PullableScrollView(Context context) {
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);

		if (Math.abs(y - oldy) > 5) {
			if (oldy > y) {
				// 上滑动
				if (scrollViewListener != null) {
					scrollViewListener.onUpSlide();
				}
			} else {
				// 下滑动
				if (scrollViewListener != null) {
					scrollViewListener.onDownSlide();
				}
			}
		}
		if (y == 0) {
			// 上滑动
			if (scrollViewListener != null) {
				scrollViewListener.onUpSlide();
			}
		}
	}

	public interface ScrollViewListener {
		void onUpSlide();

		void onDownSlide();
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return mPullRefresh;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return mPullLoading;
		else
			return false;
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
