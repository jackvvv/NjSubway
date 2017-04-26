package com.shorigo.photo.listen;

import java.util.List;

import com.shorigo.photo.model.PhotoModel;

public interface OnLocalReccentListener {
	public void onPhotoLoaded(List<PhotoModel> photos);
}
