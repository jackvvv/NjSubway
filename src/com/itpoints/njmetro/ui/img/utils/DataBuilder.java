package com.itpoints.njmetro.ui.img.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.db.DbHelper;

public class DataBuilder {
	public static List<Station> line1 = new ArrayList<Station>();// 1号线
	public static List<Station> line2 = new ArrayList<Station>();// 2号线
	public static List<Station> line3 = new ArrayList<Station>();// 3号线
	public static List<Station> line10 = new ArrayList<Station>();// 10号线
	public static List<Station> lineS1 = new ArrayList<Station>();// s1号线
	public static List<Station> lineS8 = new ArrayList<Station>();// s8号线
	public static Set<List<Station>> lineSet = new HashSet<List<Station>>();// 所有线集合
	public static int totalStaion = 0;// 总的站点数量
	
	public static void init(Context context) {
		// 1号线
		List<StationDetailBean> listLine1 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "1号线");
		for (StationDetailBean stationDetailBean : listLine1) {
			line1.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < line1.size(); i++) {
			if (i < line1.size() - 1) {
				line1.get(i).next = line1.get(i + 1);
				line1.get(i + 1).prev = line1.get(i);
			}
		}

		/*******************************************************************************/
		// 2号线
		List<StationDetailBean> listLine2 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "2号线");
		for (StationDetailBean stationDetailBean : listLine2) {
			line2.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < line2.size(); i++) {
			if (i < line2.size() - 1) {
				line2.get(i).next = line2.get(i + 1);
				line2.get(i + 1).prev = line2.get(i);
			}
		}

		/*******************************************************************************/
		// 3号线
		List<StationDetailBean> listLine3 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "3号线");
		for (StationDetailBean stationDetailBean : listLine3) {
			line3.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < line3.size(); i++) {
			if (i < line3.size() - 1) {
				line3.get(i).next = line3.get(i + 1);
				line3.get(i + 1).prev = line3.get(i);
			}
		}

		/*******************************************************************************/
		// 10号线
		List<StationDetailBean> listLine10 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "10号线");
		for (StationDetailBean stationDetailBean : listLine10) {
			line10.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < line10.size(); i++) {
			if (i < line10.size() - 1) {
				line10.get(i).next = line10.get(i + 1);
				line10.get(i + 1).prev = line10.get(i);
			}
		}

		/*******************************************************************************/
		// s1号线
		List<StationDetailBean> listLineS1 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "S1机场线");
		for (StationDetailBean stationDetailBean : listLineS1) {
			lineS1.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < lineS1.size(); i++) {
			if (i < lineS1.size() - 1) {
				lineS1.get(i).next = lineS1.get(i + 1);
				lineS1.get(i + 1).prev = lineS1.get(i);
			}
		}

		/*******************************************************************************/
		// s8号线
		List<StationDetailBean> listLineS8 = DbHelper.getInstance(context).searchCriteria(StationDetailBean.class, "line", "S8宁天城际");
		for (StationDetailBean stationDetailBean : listLineS8) {
			lineS8.add(new Station(stationDetailBean.getStation()));
		}
		for (int i = 0; i < lineS8.size(); i++) {
			if (i < lineS8.size() - 1) {
				lineS8.get(i).next = lineS8.get(i + 1);
				lineS8.get(i + 1).prev = lineS8.get(i);
			}
		}

		lineSet.add(line1);
		lineSet.add(line2);
		lineSet.add(line3);
		lineSet.add(line10);
		lineSet.add(lineS1);
		lineSet.add(lineS8);
		totalStaion = line1.size() + line2.size() + line3.size() + line10.size() + lineS1.size() + lineS8.size();
	}

}
