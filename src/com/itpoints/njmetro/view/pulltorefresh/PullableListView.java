package com.itpoints.njmetro.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {
	private boolean mPullRefresh = true;
	private boolean mPullLoading = true;

	public PullableListView(Context context) {
		super(context);
	}

	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
