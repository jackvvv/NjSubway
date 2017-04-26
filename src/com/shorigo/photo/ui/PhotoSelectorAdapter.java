package com.shorigo.photo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;

import com.itpoints.njmetro.R;
import com.shorigo.photo.model.PhotoModel;
import com.shorigo.photo.ui.PhotoItem.onItemClickListener;
import com.shorigo.photo.ui.PhotoItem.onPhotoItemCheckedListener;

public class PhotoSelectorAdapter extends MBaseAdapter<PhotoModel> {

	private int itemWidth;
	private int horizentalNum = 3;
	private onPhotoItemCheckedListener listener;
	private LayoutParams itemLayoutParams;
	private onItemClickListener mCallback;
	private int maxNum;

	private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
		super(context, models);
	}

	public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models, int screenWidth, onPhotoItemCheckedListener listener, onItemClickListener mCallback, int maxNum) {
		this(context, models);
		setItemWidth(screenWidth);
		this.listener = listener;
		this.mCallback = mCallback;
		this.maxNum = maxNum;
	}

	/** 设置每一个Item的宽高 */
	public void setItemWidth(int screenWidth) {
		int horizentalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
		this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
		this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = null;
		if (convertView == null || !(convertView instanceof PhotoItem)) {
			item = new PhotoItem(context, listener, maxNum);
			item.setLayoutParams(itemLayoutParams);
			convertView = item;
		} else {
			item = (PhotoItem) convertView;
		}
		item.setImageDrawable(models.get(position));
		item.setSelected(models.get(position).isChecked());
		item.setOnClickListener(mCallback, position);
		return convertView;
	}
}
