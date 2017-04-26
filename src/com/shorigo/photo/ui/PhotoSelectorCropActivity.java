package com.shorigo.photo.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.AnimationUtil;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.shorigo.photo.domain.PhotoSelectorDomain;
import com.shorigo.photo.listen.OnLocalAlbumListener;
import com.shorigo.photo.listen.OnLocalReccentListener;
import com.shorigo.photo.model.AlbumModel;
import com.shorigo.photo.model.PhotoModel;
import com.shorigo.photo.ui.PhotoItem.onItemClickListener;
import com.shorigo.photo.ui.PhotoItem.onPhotoItemCheckedListener;

/**
 * 选择本地图片并裁剪
 * 
 * @author peidongxu
 * 
 */
public class PhotoSelectorCropActivity extends Activity implements onItemClickListener, onPhotoItemCheckedListener, OnItemClickListener, OnClickListener, OnLocalAlbumListener, OnLocalReccentListener {

	private static final int REQUEST_CAMERA = 1;
	private static final int SET_PICTURE = 2;

	public static String RECCENT_PHOTO = null;

	private GridView gvPhotos;
	private ListView lvAblum;
	private TextView tvAlbum;
	private PhotoSelectorDomain photoSelectorDomain;
	private PhotoSelectorAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	/** 1：头像 2：背景 */
	private int type;

	private String path;
	private String fileName;
	private File file;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RECCENT_PHOTO = "最近照片";
		setContentView(R.layout.activity_photoselector);

		type = getIntent().getIntExtra("type", 1);

		path = Constants.path + Constants._image;
		long currentTimeMillis = System.currentTimeMillis();
		fileName = currentTimeMillis + ".jpeg";
		file = new File(path, fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
		}
		initImageLoader();

		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
		// 返回
		findViewById(R.id.bv_back_lh).setOnClickListener(this);
		gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
		lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
		tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
		tvAlbum.setOnClickListener(this);
		layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);

		TextView tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
		tvPreview.setVisibility(View.GONE);
		Button btnOk = (Button) findViewById(R.id.btn_right_lh);
		btnOk.setVisibility(View.GONE);
		TextView tvNumber = (TextView) findViewById(R.id.tv_number);
		tvNumber.setVisibility(View.GONE);
		TextView tv_line_ar = (TextView) findViewById(R.id.tv_line_ar);
		tv_line_ar.setVisibility(View.GONE);

		photoAdapter = new PhotoSelectorAdapter(getApplicationContext(), new ArrayList<PhotoModel>(), Utils.getWidthPixels(this), this, this, 1);
		gvPhotos.setAdapter(photoAdapter);

		albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
		lvAblum.setAdapter(albumAdapter);
		lvAblum.setOnItemClickListener(this);

		photoSelectorDomain.getReccent(this); // 更新最近照片
		photoSelectorDomain.updateAlbum(this); // 跟新相册信息
	}

	private void initImageLoader() {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_picture_loading).showImageOnFail(R.drawable.ic_picture_loadfailed).cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).considerExifParams(false).bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(400, 400)
		// default = device screen dimensions
				.diskCacheExtraOptions(400, 400, null).threadPoolSize(5)
				// default Thread.NORM_PRIORITY - 1
				.threadPriority(Thread.NORM_PRIORITY)
				// default FIFO
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// default
				.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.diskCache(new UnlimitedDiscCache(StorageUtils.getCacheDirectory(this, true)))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// default
				.imageDownloader(new BaseImageDownloader(this))
				// default
				.imageDecoder(new BaseImageDecoder(false))
				// default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// default
				.defaultDisplayImageOptions(imageOptions).build();

		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_camera_vc)
			catchPicture();
		else if (v.getId() == R.id.bv_back_lh)
			finish();
	}

	/** 拍照 */
	private void catchPicture() {
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SET_PICTURE && data != null) {
			// 拿到剪切数据
			String path = file.getPath();
			if (!Utils.isEmity(path)) {
				// 保存裁剪图片
				Intent intent = new Intent();
				intent.putExtra("data", path);
				setResult(100, intent);
				finish();
			}
		}
	}

	private void album() {
		if (layoutAlbum.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	/** 弹出相册列表 */
	private void popAlbum() {
		layoutAlbum.setVisibility(View.VISIBLE);
		new AnimationUtil(getApplicationContext(), R.anim.translate_up_current).setLinearInterpolator().startAnimation(layoutAlbum);
	}

	/** 隐藏相册列表 */
	private void hideAlbum() {
		new AnimationUtil(getApplicationContext(), R.anim.translate_down).setLinearInterpolator().startAnimation(layoutAlbum);
		layoutAlbum.setVisibility(View.GONE);
	}

	@Override
	/** 点击查看照片 */
	public void onItemClick(int position) {
		PhotoModel model = (PhotoModel) photoAdapter.getItem(position);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(new File(model.getOriginalPath())), "image/jpeg");
		intent.putExtra("crop", "true");
		if (1 == type) {
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 900);
			intent.putExtra("outputY", 900);
		} else if (2 == type) {
			intent.putExtra("aspectX", 16);
			intent.putExtra("aspectY", 9);
			intent.putExtra("outputX", 900);
			intent.putExtra("outputY", 600);
		}
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_PICTURE);

	}

	/** 照片选中状态改变之后 */
	@Override
	public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
	}

	@Override
	public void onBackPressed() {
		if (layoutAlbum.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	@Override
	/** 相册列表点击事件 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position)
				album.setCheck(true);
			else
				album.setCheck(false);
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		tvAlbum.setText(current.getName());

		// 更新照片列表
		if (current.getName().equals(RECCENT_PHOTO))
			photoSelectorDomain.getReccent(this);
		else {
			// 获取选中相册的照片
			photoSelectorDomain.getAlbum(current.getName(), this);
		}
	}

	@Override
	public void onAlbumLoaded(List<AlbumModel> albums) {
		albumAdapter.update(albums);
	}

	@Override
	public void onPhotoLoaded(List<PhotoModel> photos) {
		photoAdapter.update(photos);
		// 滚动到顶端
		gvPhotos.smoothScrollToPosition(0);
	}

}
