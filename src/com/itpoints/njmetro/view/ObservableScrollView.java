package com.itpoints.njmetro.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.itpoints.njmetro.view.pulltorefresh.Pullable;
import com.nineoldandroids.view.ViewHelper;

public class ObservableScrollView extends ScrollView implements Pullable {
	int PARALLAX_SPEED = 100;
	private ScrollViewListener scrollViewListener = null;

	protected List<ImageView> imageViewList = new ArrayList<ImageView>();
	View firstVisibleView = null;
	float recyclerviewCenterY = -1;
	Rect rect = new Rect();
	View currentImageView;

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void imageParallax(ImageView imageView) {
		if (!imageViewList.contains(imageView))
			imageViewList.add(imageView);
	}

	public static float limit(float min, float value, float max) {
		return Math.max(Math.min(value, max), min);
	}

	int lastDirection = 0;

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

	public interface ScrollViewListener {
		void onUpSlide();

		void onDownSlide();
	}
	
	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

}
