package com.itpoints.njmetro.utils;

import java.util.ArrayList;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;

/**
 * 动画工具
 * 
 * @author peidongxu
 * 
 */
public class AnimatorUtil {
	static AnimatorSet hideAnimatorSet;// 这是隐藏头尾元素使用的动画
	static AnimatorSet backAnimatorSet;// 这是显示头尾元素使用的动画

	/**
	 * 隐藏头部和底部
	 * 
	 * @param view_top
	 * @param view_footer
	 */
	public static void animateHide(View view_top, View view_footer) {
		// 先清除其他动画
		if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
			backAnimatorSet.cancel();
		}
		if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
			// 如果这个动画已经在运行了，就不管它
		} else {
			hideAnimatorSet = new AnimatorSet();
			ArrayList<Animator> animators = new ArrayList<Animator>();
			if (view_top != null) {
				ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(view_top, "translationY", view_top.getTranslationY(), -view_top.getHeight());// 将top隐藏到上面
				animators.add(headerAnimator);
			}
			if (view_footer != null) {
				ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(view_footer, "translationY", view_footer.getTranslationY(), view_footer.getHeight());// 将footer隐藏到下面
				animators.add(footerAnimator);
			}
			hideAnimatorSet.setDuration(300);
			hideAnimatorSet.playTogether(animators);
			hideAnimatorSet.start();
		}
	}

	/**
	 * 显示头部和底部
	 * 
	 * @param view_top
	 * @param view_footer
	 */
	public static void animateBack(View view_top, View view_footer) {
		// 先清除其他动画
		if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
			hideAnimatorSet.cancel();
		}
		if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
			// 如果这个动画已经在运行了，就不管它
		} else {
			backAnimatorSet = new AnimatorSet();
			// 下面两句是将头尾元素放回初始位置。
			ArrayList<Animator> animators = new ArrayList<Animator>();
			if (view_top != null) {
				ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(view_top, "translationY", view_top.getTranslationY(), 0f);
				animators.add(headerAnimator);
			}
			if (view_footer != null) {
				ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(view_footer, "translationY", view_footer.getTranslationY(), 0f);
				animators.add(footerAnimator);
			}
			backAnimatorSet.setDuration(300);
			backAnimatorSet.playTogether(animators);
			backAnimatorSet.start();
		}
	}

}
