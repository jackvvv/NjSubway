package com.itpoints.njmetro.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

/**
 * 
 ****************************************** 
 * @文件名称 : FileUtils.java
 * @文件描述 : 文件工具类
 ****************************************** 
 */
public class FileUtils {

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void SaveBitmap(Bitmap bitmap, String path) {
		if (bitmap == null) {
			return;
		}
		File file = new File(path);
		if (file.exists())
			file.delete();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// 将Bitmap对象写入本地路径中，Unity在去相同的路径来读取这个文件
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		bitmap.compress(format, 80, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            文件夹路径
	 * @return
	 */
	public static double getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}

	/**
	 * 创建缓存文件夹
	 */
	public static void createCacheFolder() {
		// 获取跟目录
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}

		if (sdDir != null) {
			Constants.path = sdDir.toString();
		} else {
			Constants.path = Environment.getExternalStorageState();
		}

		File file;

		file = new File(Constants.path, Constants._image);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(Constants.path, Constants._audio);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(Constants.path, Constants._video);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constants.path, Constants._anex);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(Constants.path, Constants._log);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 删除文件
	 */
	public static void deleteFile(String path) {
		// 删除缓存
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 
	 * @Title: deleteFile
	 * @Description: TODO(删除文件)
	 * @param @param file 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}

	/**
	 * 删除缓存文件
	 */
	public static void deleteCacheFile() {
		String[] arrPath = new String[] { Constants.path + Constants._image, Constants.path + Constants._audio, Constants.path + Constants._video };
		for (int i = 0; i < arrPath.length; i++) {
			// 删除缓存
			File file = new File(arrPath[i]);
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					File files_clild[] = file.listFiles();
					for (int j = 0; j < files_clild.length; j++) {
						files_clild[j].delete();
					}
				}
			}
		}
	}

	/**
	 * 保存json到文件
	 */
	public static String saveFile(String json, String fileName) {
		try {
			// 文件路径
			String file_dir = Constants.path + Constants._log;
			File dir = new File(file_dir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(file_dir + File.separator + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(json.getBytes());
			fos.close();
			return fileName;
		} catch (Exception e) {
		}
		return null;
	}

	public static String getFilePath(String path) {
		// "/storage/emulated/0/com.itpoints.njmetro/anex/station_img/line_10/Zhongsheng Station";
		// path =
		// "/storage/emulated/0/com.itpoints.njmetro/anex/station_img/line_10/ANDEMEN";
		File[] files = new File(path).listFiles();
		if (files != null) {
			for (File file : files) {
				return file.getPath();
			}
		}

		return null;
	}

}
