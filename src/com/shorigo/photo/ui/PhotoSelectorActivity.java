package com.shorigo.photo.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itpoints.njmetro.MyApplication;
import com.itpoints.njmetro.R;
import com.itpoints.njmetro.ui.LookMorePicUI;
import com.itpoints.njmetro.ui.LookPicUI;
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

public class PhotoSelectorActivity extends Activity implements onItemClickListener, onPhotoItemCheckedListener, OnItemClickListener, OnClickListener, OnLocalAlbumListener, OnLocalReccentListener {

	public static final int SINGLE_IMAGE = 1;
	public static final String KEY_MAX = "key_max";
	private int MAX_IMAGE;

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;

	public static String RECCENT_PHOTO = null;

	private GridView gvPhotos;
	private ListView lvAblum;
	private Button btnOk;
	private TextView tvAlbum, tvPreview, tvTitle;
	private PhotoSelectorDomain photoSelectorDomain;
	private PhotoSelectorAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	private ArrayList<PhotoModel> selected;
	private TextView tvNumber;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RECCENT_PHOTO = "最近照片";
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_photoselector);
		selected = new ArrayList<PhotoModel>();
		if (getIntent().getExtras() != null) {
			MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 10);
			selected.addAll((Collection<? extends PhotoModel>) getIntent().getParcelableArrayListExtra("selected"));
		}

		initImageLoader();

		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

		tvTitle = (TextView) findViewById(R.id.tv_title_lh);
		gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
		lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
		btnOk = (Button) findViewById(R.id.btn_right_lh);
		tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
		tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
		layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		tvNumber.setText("(" + selected.size() + ")");

		btnOk.setOnClickListener(this);
		tvAlbum.setOnClickListener(this);
		tvPreview.setOnClickListener(this);

		if (selected.size() > 0) {
			tvPreview.setEnabled(true);
		} else {
			tvPreview.setEnabled(false);

		}

		photoAdapter = new PhotoSelectorAdapter(getApplicationContext(), new ArrayList<PhotoModel>(), Utils.getWidthPixels(this), this, this, MAX_IMAGE);
		gvPhotos.setAdapter(photoAdapter);

		albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
		lvAblum.setAdapter(albumAdapter);
		lvAblum.setOnItemClickListener(this);

		findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回

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
		if (v.getId() == R.id.btn_right_lh)
			ok(); // 选完照片
		else if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_preview_ar)
			priview();
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
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			PhotoModel photoModel = new PhotoModel(Utils.query(getApplicationContext(), data.getData()));
			// selected.clear();
			// //--keep all
			// selected photos
			// tvNumber.setText("(0)");
			// //--keep all
			// selected photos
			// ///////////////////////////////////////////////////////////////////////////////////////////
			if (selected.size() >= MAX_IMAGE) {
				MyApplication.getInstance().showToast("不能选择超过"+MAX_IMAGE+"个图像");
				photoModel.setChecked(false);
				photoAdapter.notifyDataSetChanged();
			} else {
				if (!selected.contains(photoModel)) {
					selected.add(photoModel);
				}
			}
			ok();
		}
	}

	/** 完成 */
	private void ok() {
		if (selected.size() > MAX_IMAGE) {
			MyApplication.getInstance().showToast("不能选择超过"+MAX_IMAGE+"个图像");
			return;
		}
		Intent data = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("photos", selected);
		data.putExtras(bundle);
		setResult(RESULT_OK, data);
		finish();
	}

	/** 预览照片 */
	private void priview() {
		if (selected != null && selected.size() > 0) {
			int size = selected.size();
			if (size == 1) {
				Intent intent = new Intent(this, LookPicUI.class);
				intent.putExtra(Constants.IMG_PATH, selected.get(0).getOriginalPath());
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			} else {
				ArrayList<String> imgList = new ArrayList<String>();
				for (int i = 0; i < size; i++) {
					imgList.add(selected.get(i).getOriginalPath());
				}
				Intent intent = new Intent(this, LookMorePicUI.class);
				intent.putExtra("position", 1);
				intent.putStringArrayListExtra("img", imgList);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
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

	/** 清空选中的图片 */
	private void reset() {
		selected.clear();
		tvNumber.setText("(0)");
		tvPreview.setEnabled(false);
	}

	@Override
	/** 点击查看照片 */
	public void onItemClick(int position) {
		// Bundle bundle = new Bundle();
		// if (tvAlbum.getText().toString().equals(RECCENT_PHOTO))
		// bundle.putInt("position", position - 1);
		// else
		// bundle.putInt("position", position);
		// bundle.putString("album", tvAlbum.getText().toString());
		// Intent intent = new Intent(this, PhotoPreviewActivity.class);
		// intent.putExtras(bundle);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// startActivity(intent);
	}

	/** 照片选中状态改变之后 */
	@Override
	public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (!selected.contains(photoModel))
				selected.add(photoModel);
			tvPreview.setEnabled(true);
		} else {
			selected.remove(photoModel);
		}
		tvNumber.setText("(" + selected.size() + ")");

		if (selected.isEmpty()) {
			tvPreview.setEnabled(false);
			tvPreview.setText("预览");
		}
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
		// tvTitle.setText(current.getName());

		// 更新照片列表
		if (current.getName().equals(RECCENT_PHOTO))
			photoSelectorDomain.getReccent(this);
		else
			photoSelectorDomain.getAlbum(current.getName(), this); // 获取选中相册的照片
	}

	@Override
	public void onAlbumLoaded(List<AlbumModel> albums) {
		albumAdapter.update(albums);
	}

	@Override
	public void onPhotoLoaded(List<PhotoModel> photos) {
		for (PhotoModel model : photos) {
			if (selected.contains(model)) {
				model.setChecked(true);
			}
		}
		photoAdapter.update(photos);
		// 滚动到顶端
		gvPhotos.smoothScrollToPosition(0);
	}
}
