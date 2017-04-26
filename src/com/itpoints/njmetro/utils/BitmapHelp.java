package com.itpoints.njmetro.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {

	private static BitmapUtils bitmapUtils;

	private BitmapHelp() {
	}

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getInstance(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(appContext, Constants.path + Constants._image);
		}
		// 是否开启内存缓存
		bitmapUtils.configMemoryCacheEnabled(true);
		// 是否开启磁盘缓存
		bitmapUtils.configDiskCacheEnabled(true);

		return bitmapUtils;
	}

}
