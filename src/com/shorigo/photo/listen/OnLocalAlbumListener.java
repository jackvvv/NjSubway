package com.shorigo.photo.listen;

import java.util.List;

import com.shorigo.photo.model.AlbumModel;

public interface OnLocalAlbumListener {
	public void onAlbumLoaded(List<AlbumModel> albums);
}
