package com.itpoints.njmetro.utils;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * 图片选择裁剪
 * 
 * @author peidongxu
 * 
 */
public class PicPickUtils {
	private final Activity activity;
	private final Fragment fragmentContext;
	private final OnPickedlistener onPicPickedlistener;
	// 是否裁剪
	private boolean doCrop;
	// 宽
	private int cropWidth;
	// 高
	private int cropHeight;
	// 保存图片本地路径
	public static final String ACCOUNT_DIR = Constants.path + Constants._image + "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;

	private String IMAGE_FILE_NAME = "";
	private String TMP_IMAGE_FILE_NAME = "";
	File fileone = null;
	File filetwo = null;
	// 常量定义
	/** 相机拍照 */
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	private String mAlbumPicturePath = null;

	// 版本比较：是否是4.4及以上版本
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	public PicPickUtils(Activity context, Fragment fragmentContext, OnPickedlistener onPicPickedlistener) {
		this.activity = context;
		this.fragmentContext = fragmentContext;
		this.onPicPickedlistener = onPicPickedlistener;
	}

	/**
	 * 设置是否裁剪
	 * 
	 * @param doCrop
	 *            true: 是 false:否
	 */
	public void setDoCrop(boolean doCrop) {
		this.doCrop = doCrop;
	}

	/**
	 * 初始化
	 * 
	 * @param cropImg
	 *            是否裁剪
	 * @param outWith
	 *            宽度
	 * @param outHeight
	 *            高度
	 */
	public void doPickPhotoAction(final int outWith, final int outHeight) {
		this.cropWidth = outWith;
		this.cropHeight = outHeight;

		File directory = new File(ACCOUNT_DIR);
		File imagepath = new File(IMGPATH);
		if (!directory.exists()) {
			Log.i("zou", "directory.mkdir()");
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}
		long currentTimeMillis = System.currentTimeMillis();
		IMAGE_FILE_NAME = currentTimeMillis + ".jpeg";
		TMP_IMAGE_FILE_NAME = "tmp_" + currentTimeMillis + ".jpeg";

		fileone = new File(IMGPATH, IMAGE_FILE_NAME);
		filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);

		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {
		}
	}

	public void pickResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_A_PICTURE) {
			if (resultCode == activity.RESULT_OK && null != data) {
				String picturePath = "";
				if (doCrop) {
					picturePath = new File(IMGPATH, TMP_IMAGE_FILE_NAME).getPath();
				} else {
					picturePath = getPath(activity.getApplicationContext(), data.getData());
				}
				if (onPicPickedlistener != null) {
					onPicPickedlistener.picPicked(picturePath, null);
				}
			} else if (resultCode == activity.RESULT_CANCELED) {
				return;
			}
		} else if (requestCode == SELECET_A_PICTURE_AFTER_KIKAT) {
			if (resultCode == activity.RESULT_OK && null != data) {
				mAlbumPicturePath = getPath(activity.getApplicationContext(), data.getData());
				if (doCrop) {
					cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
				} else {
					if (onPicPickedlistener != null) {
						onPicPickedlistener.picPicked(new File(mAlbumPicturePath).getPath(), null);
					}
				}
			} else if (resultCode == activity.RESULT_CANCELED) {
				return;
			}
		} else if (requestCode == SET_ALBUM_PICTURE_KITKAT) {
			if (onPicPickedlistener != null) {
				onPicPickedlistener.picPicked(new File(IMGPATH, TMP_IMAGE_FILE_NAME).getPath(), null);
			}
		} else if (requestCode == TAKE_A_PICTURE) {
			Log.i("zou", "TAKE_A_PICTURE-resultCode:" + resultCode);
			if (resultCode == activity.RESULT_OK) {
				if (doCrop) {
					cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
				} else {
					if (onPicPickedlistener != null) {
						onPicPickedlistener.picPicked(new File(IMGPATH, IMAGE_FILE_NAME).getPath(), null);
					}
				}
			} else {
				return;
			}
		} else if (requestCode == SET_PICTURE) {
			// 拍照的设置头像 不考虑版本
			if (resultCode == activity.RESULT_OK && null != data) {
				if (onPicPickedlistener != null) {
					onPicPickedlistener.picPicked(new File(IMGPATH, IMAGE_FILE_NAME).getPath(), null);
				}
			} else if (resultCode == activity.RESULT_CANCELED) {
				return;
			} else {
				return;
			}
		}
	}

	/**
	 * 调用拍照
	 */
	public void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, TAKE_A_PICTURE);
		} else {
			activity.startActivityForResult(intent, TAKE_A_PICTURE);
		}
	}

	/**
	 * 调用相册
	 */
	public void doPickPhotoFromGallery() {
		if (mIsKitKat) {
			selectImageUriAfterKikat();
		} else {
			if (doCrop) {
				cropImageUri();
			} else {
				cropImageUriNo();
			}
		}
	}

	/**
	 * <br>
	 * 功能简述:不裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void cropImageUriNo() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, SELECT_A_PICTURE);
		} else {
			activity.startActivityForResult(intent, SELECT_A_PICTURE);
		}
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", String.valueOf(doCrop));
		intent.putExtra("aspectX", cropWidth);
		intent.putExtra("aspectY", cropHeight);
		intent.putExtra("outputX", cropWidth);
		intent.putExtra("outputY", cropHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, SELECT_A_PICTURE);
		} else {
			activity.startActivityForResult(intent, SELECT_A_PICTURE);
		}
	}

	/**
	 * <br>
	 * 功能简述:4.4以上裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
		} else {
			activity.startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
		}
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现----------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	private void cameraCropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", String.valueOf(doCrop));
		intent.putExtra("aspectX", cropWidth);
		intent.putExtra("aspectY", cropHeight);
		intent.putExtra("outputX", cropWidth);
		intent.putExtra("outputY", cropHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, SET_PICTURE);
		} else {
			activity.startActivityForResult(intent, SET_PICTURE);
		}
	}

	/**
	 * <br>
	 * 功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", String.valueOf(doCrop));
		intent.putExtra("aspectX", cropWidth);
		intent.putExtra("aspectY", cropHeight);
		intent.putExtra("outputX", cropWidth);
		intent.putExtra("outputY", cropHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		if (fragmentContext != null) {
			fragmentContext.startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
		} else {
			activity.startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
		}
	}

	/**
	 * <br>
	 * 功能简述:4.4及以上获取图片的方法 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public interface OnPickedlistener {
		public void picPicked(String path, Bitmap bmp);
	}

}
