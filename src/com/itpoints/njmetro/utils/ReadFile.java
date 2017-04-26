package com.itpoints.njmetro.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;

import com.itpoints.njmetro.bean.LineBean;
import com.itpoints.njmetro.bean.LinePlanBean;
import com.itpoints.njmetro.bean.LinePriceBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationEnBean;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.bean.StationIdBean;
import com.itpoints.njmetro.bean.StationInfoMationBean;
import com.itpoints.njmetro.bean.StationScenicSpotBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.db.DbHelper;

/**
 * 地铁数据模板导入数据库
 * 
 * @author peidongxu
 * 
 */
public class ReadFile {
	/**
	 * 拷贝文件到sd卡
	 * 
	 * @param context
	 * @param strOutFileName
	 * @throws IOException
	 */
	public static void copyBigDataToSD(Context context, String strInFileName, String strOutFileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = context.getAssets().open(strInFileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	/**
	 * 解析excel
	 * 
	 * @param context
	 * @param strInFileName
	 */
	public static void loadData(Context context, String filePath) {

		try {
			File file_dir = new File(filePath);
			if (file_dir.exists()) {
				File[] files = file_dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.getAbsolutePath().contains("njmetro_id")) {
							readStationID(context, file);
						} else if (file.getAbsolutePath().contains("njmetro_jingqu")) {
							readStationScenicSport(context, file);
						} else if (file.getAbsolutePath().contains("njmetro_line")) {
							readStationBaseDate(context, file);
						} else if (file.getAbsolutePath().contains("njmetro_price")) {
							readLinePrice(context, file);
						} else if (file.getAbsolutePath().contains("njmetro_time")) {
							readStationTime(context, file);
						} else if (file.getAbsolutePath().contains("njmetro_station_en")) {
							readStationEn(context, file);
						}else if (file.getAbsolutePath().contains("njmetro_all_line")) {
							readStationLine(context, file);
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 读取地铁站点时刻表
	 * 
	 * @param context
	 * @param file
	 *            文件
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationTime(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<StationTimeBean> listStationTimeBean = new ArrayList<StationTimeBean>();
			StationTimeBean stationTimeBean;
			// 线路名称
			String lineName;
			// 循环工作薄
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				// 线路名称
				lineName = sheets[i].getName();
				// 循环条数
				for (int j = 3; j < sheet.getRows(); j++) {
					stationTimeBean = new StationTimeBean();
					// 线路名称
					stationTimeBean.setLine(lineName);
					// 站点名称
					stationTimeBean.setStation(sheet.getCell(0, j).getContents());

					String[] fist_up_time = sheet.getCell(1, j).getContents().split("\n");
					String[] fist_down_time = sheet.getCell(2, j).getContents().split("\n");
					String[] last_up_time = sheet.getCell(3, j).getContents().split("\n");
					String[] last_down_time = sheet.getCell(4, j).getContents().split("\n");
					// 上行时间
					String up_time_befor = TimeUtils.getFotmatData("HH:mm:ss", fist_up_time[0].replace("\r", ""));
					String up_time_after = TimeUtils.getFotmatData("HH:mm:ss", last_up_time[0].replace("\r", ""));
					stationTimeBean.setTime_up(up_time_befor + "-" + up_time_after);
					// 下行时间
					String down_time_befor;
					String down_time_after;
					if (1 < fist_down_time.length) {
						down_time_befor = TimeUtils.getFotmatData("HH:mm:ss", fist_down_time[1].replace("\r", ""));
					} else {
						down_time_befor = TimeUtils.getFotmatData("HH:mm:ss", fist_down_time[0].replace("\r", ""));
					}
					if (1 < last_down_time.length) {
						down_time_after = TimeUtils.getFotmatData("HH:mm:ss", last_down_time[1].replace("\r", ""));
					} else {
						down_time_after = TimeUtils.getFotmatData("HH:mm:ss", last_down_time[0].replace("\r", ""));
					}
					stationTimeBean.setTime_down(down_time_befor + "-" + down_time_after);
					listStationTimeBean.add(stationTimeBean);
				}
			}
			DbHelper.getInstance(context).saveAll(listStationTimeBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁站点景点表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationScenicSport(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<StationScenicSpotBean> listStationScenicSpotBean = new ArrayList<StationScenicSpotBean>();
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				StationScenicSpotBean stationScenicSpotBean;
				for (int j = 1; j < sheet.getRows(); j++) {
					// 景点信息
					stationScenicSpotBean = new StationScenicSpotBean();
					stationScenicSpotBean.setStation(sheet.getCell(0, j).getContents());
					stationScenicSpotBean.setName(sheet.getCell(1, j).getContents());
					stationScenicSpotBean.setAddress(sheet.getCell(2, j).getContents());
					stationScenicSpotBean.setDesc(sheet.getCell(3, j).getContents());
					stationScenicSpotBean.setPrice(sheet.getCell(4, j).getContents());
					stationScenicSpotBean.setPreferential(sheet.getCell(5, j).getContents());
					stationScenicSpotBean.setTime(sheet.getCell(6, j).getContents());
					listStationScenicSpotBean.add(stationScenicSpotBean);
				}
			}

			DbHelper.getInstance(context).saveAll(listStationScenicSpotBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁线路表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationLine(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<LineBean> listLineBean = new ArrayList<LineBean>();
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				LineBean lineBean;
				for (int j = 1; j < sheet.getRows(); j++) {
					// 线路
					lineBean = new LineBean();
					lineBean.setLine(sheet.getCell(0, j).getContents());
					lineBean.setColor(sheet.getCell(1, j).getContents());
					listLineBean.add(lineBean);
				}
			}

			DbHelper.getInstance(context).saveAll(listLineBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁站点对应英文表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationEn(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<StationEnBean> listStationEnBean = new ArrayList<StationEnBean>();
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				StationEnBean stationEnBean;
				for (int j = 3; j < sheet.getRows(); j++) {
					// 站点英文
					stationEnBean = new StationEnBean();
					stationEnBean.setStation(sheet.getCell(1, j).getContents().trim());
					stationEnBean.setStation_en(sheet.getCell(2, j).getContents().trim());
					listStationEnBean.add(stationEnBean);
				}
			}

			DbHelper.getInstance(context).saveAll(listStationEnBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁站点ID表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationID(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<StationIdBean> listStationIdBean = new ArrayList<StationIdBean>();
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				StationIdBean stationIdBean;
				for (int j = 1; j < sheet.getRows(); j++) {
					// 线路
					stationIdBean = new StationIdBean();
					stationIdBean.setId(sheet.getCell(0, j).getContents());
					stationIdBean.setStation_name(sheet.getCell(1, j).getContents());
					listStationIdBean.add(stationIdBean);
				}
			}

			DbHelper.getInstance(context).saveAll(listStationIdBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁线路票价表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readLinePrice(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			List<LinePriceBean> listLinePriceBean = new ArrayList<LinePriceBean>();
			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				LinePriceBean linePriceBean;
				for (int j = 1; j < sheet.getRows(); j++) {
					// 价格
					linePriceBean = new LinePriceBean();
					linePriceBean.setStart_station(sheet.getCell(0, j).getContents());
					linePriceBean.setEnd_station(sheet.getCell(1, j).getContents());
					linePriceBean.setDistance(sheet.getCell(2, j).getContents());
					linePriceBean.setFee_level(sheet.getCell(3, j).getContents());
					linePriceBean.setFee(sheet.getCell(4, j).getContents());
					listLinePriceBean.add(linePriceBean);
				}
			}

			DbHelper.getInstance(context).saveAll(listLinePriceBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁站点基础数据(基本信息、设施、出口信息)
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	public static boolean readStationBaseDate(Context context, File file) {
		try {
			Workbook course = null;
			course = Workbook.getWorkbook(file);
			Sheet[] sheets = course.getSheets();
			Sheet sheet;
			// 线路
			String line = null;

			for (int i = 0; i < sheets.length; i++) {
				sheet = course.getSheet(sheets[i].getName());
				String sheetName = sheet.getName();

				if (sheetName.contains("1号线")) {
					line = "1号线";
				} else if (sheetName.contains("2号线")) {
					line = "2号线";
				} else if (sheetName.contains("3号线")) {
					line = "3号线";
				} else if (sheetName.contains("10号线")) {
					line = "10号线";
				} else if (sheetName.contains("机场线")) {
					line = "S1机场线";
				} else if (sheetName.contains("宁天城际")) {
					line = "S8宁天城际";
				}

				if (sheetName.contains("站点信息表")) {
					readStationInfo(context, sheet, line);
				} else if (sheetName.contains("设施表")) {
					readStationFacility(context, sheet, line);
				} else if (sheetName.contains("出口信息表")) {
					readStationInfoMation(context, sheet, line);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取地铁站点基础信息表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	private static List<StationDetailBean> readStationInfo(Context context, Sheet sheet, String line) {
		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		StationDetailBean stationDetailBean;
		for (int j = 2; j < sheet.getRows(); j++) {
			// 基础信息
			stationDetailBean = new StationDetailBean();
			stationDetailBean.setLine(line);
			stationDetailBean.setStation(sheet.getCell(0, j).getContents().replace("\n", ""));
			stationDetailBean.setStation_en(sheet.getCell(1, j).getContents().replace("\n", ""));
			stationDetailBean.setImg(sheet.getCell(2, j).getContents().replace("\n", ""));
			stationDetailBean.setLon(sheet.getCell(3, j).getContents().replace("\n", ""));
			stationDetailBean.setLat(sheet.getCell(4, j).getContents().replace("\n", ""));
			String transfer_line = sheet.getCell(5, j).getContents().replace("\n", "");
			if ("无".equals(transfer_line)) {
				transfer_line = "";
			}
			stationDetailBean.setTransfer_line(transfer_line);
			stationDetailBean.setDesc(sheet.getCell(6, j).getContents().replace("\n", ""));
			listStationDetailBean.add(stationDetailBean);
		}

		DbHelper.getInstance(context).saveAll(listStationDetailBean);

		return listStationDetailBean;
	}

	/**
	 * 读取地铁站点设施表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	private static List<StationFacilityBean> readStationFacility(Context context, Sheet sheet, String line) {
		List<StationFacilityBean> listStationFacilityBean = new ArrayList<StationFacilityBean>();
		StationFacilityBean stationFacilityBean;
		for (int j = 2; j < sheet.getRows(); j++) {
			// 设施信息
			stationFacilityBean = new StationFacilityBean();
			stationFacilityBean.setLine(line);
			stationFacilityBean.setStation(sheet.getCell(0, j).getContents().replace("\n", ""));
			stationFacilityBean.setStation_en(sheet.getCell(1, j).getContents().replace("\n", ""));
			stationFacilityBean.setName(sheet.getCell(2, j).getContents().replace("\n", ""));
			stationFacilityBean.setDesc(sheet.getCell(3, j).getContents().replace("\n", ""));
			stationFacilityBean.setExplain(sheet.getCell(4, j).getContents().replace("\n", ""));
			listStationFacilityBean.add(stationFacilityBean);
		}

		DbHelper.getInstance(context).saveAll(listStationFacilityBean);

		return listStationFacilityBean;
	}

	/**
	 * 读取地铁站点出口信息表
	 * 
	 * @param context
	 * @param file
	 * @return true : 成功 false: 失败
	 */
	private static List<StationInfoMationBean> readStationInfoMation(Context context, Sheet sheet, String line) {
		List<StationInfoMationBean> listStationInfoMationBean = new ArrayList<StationInfoMationBean>();
		StationInfoMationBean stationInfoMationBean;
		for (int j = 2; j < sheet.getRows(); j++) {
			// 出口信息
			stationInfoMationBean = new StationInfoMationBean();
			stationInfoMationBean.setLine(line);
			stationInfoMationBean.setStation(sheet.getCell(0, j).getContents().replace("\n", ""));
			stationInfoMationBean.setStation_en(sheet.getCell(1, j).getContents().replace("\n", ""));
			stationInfoMationBean.setName(sheet.getCell(2, j).getContents().replace("\n", ""));
			stationInfoMationBean.setLandmark(sheet.getCell(3, j).getContents().replace("\n", ""));
			stationInfoMationBean.setBus(sheet.getCell(4, j).getContents().replace("\n", ""));
			listStationInfoMationBean.add(stationInfoMationBean);
		}

		DbHelper.getInstance(context).saveAll(listStationInfoMationBean);

		return listStationInfoMationBean;
	}

	/**
	 * 读取地铁线路规划表
	 * 
	 * @param context
	 * @param file
	 */
	public static void readStationPlan(Context context, String fileName) {
		String line_json = getJson(context, fileName);

		List<LinePlanBean> listLinePlanBean = new ArrayList<LinePlanBean>();
		try {
			JSONArray array = new JSONArray(line_json);
			int len = array.length();
			LinePlanBean linePlanBean;
			for (int i = 0; i < len; i++) {
				JSONObject object = array.getJSONObject(i);
				linePlanBean = new LinePlanBean();
				linePlanBean.setId(object.getString("id"));
				linePlanBean.setName(object.getString("name"));
				linePlanBean.setLine(object.getString("line"));
				linePlanBean.setPrice(object.getString("price"));
				listLinePlanBean.add(linePlanBean);
			}
			DbHelper.getInstance(context).saveAll(listLinePlanBean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析地铁线路规划数据
	 * 
	 * @param context
	 * @param file
	 */
	private static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}
