package com.itpoints.njmetro.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class ImageDisplayer implements BitmapDisplayer {

	private int targetWidth;

	public ImageDisplayer(int targetWidth) {
		this.targetWidth = targetWidth;
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,LoadedFrom loadedFrom) {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			bitmap = ImageBimp.resizeImageByWidth(bitmap, targetWidth);
		}
		imageAware.setImageBitmap(bitmap);
	}

}
