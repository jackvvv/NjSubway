package com.itpoints.njmetro.ui.img.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import android.content.Context;

/**
 * 最短路径计算
 * 
 * @author peidongxu
 * 
 */
public class SubwayShortPath {
	// 记录已经分析过的站点
	private static List<Station> outList = new ArrayList<Station>();
	private static String linePlan;

	private static SubwayShortPath instance;

	/**
	 * 单一实例
	 */
	public static SubwayShortPath getInstance(Context context) {
		if (instance == null) {
			instance = new SubwayShortPath();
			DataBuilder.init(context);
		}
		linePlan = "";
		outList = new ArrayList<Station>();
		return instance;
	}

	public String getLinePlan(String start, String end) {
		calculate(new Station(start), new Station(end));
		return linePlan;
	}

	/**
	 * 计算从start站到end站的最短经过路径
	 * 
	 * @param start
	 *            起点
	 * @param end
	 *            终点
	 */
	private void calculate(Station start, Station end) {
		if (outList.size() == DataBuilder.totalStaion) {
			StringBuffer buffer = new StringBuffer();
			for (Station station : start.getAllPassedStations(end)) {
				buffer.append(station.getName() + ",");
			}
			linePlan = buffer.toString();
			if (linePlan.length()>1) {
				linePlan = linePlan.substring(0, linePlan.length()-1);
			}
			return;
		}
		if (!outList.contains(start)) {
			outList.add(start);
		}
		// 如果起点站的OrderSetMap为空，则第一次用起点站的前后站点初始化之
		if (start.getOrderSetMap().isEmpty()) {
			List<Station> Linkedstations = getAllLinkedStations(start);
			for (Station s : Linkedstations) {
				start.getAllPassedStations(s).add(s);
			}
		}
		Station parent = getShortestPath(start);// 获取距离起点站s1最近的一个站（有多个的话，随意取一个）
		if (parent == end) {
			StringBuffer buffer = new StringBuffer();
			for (Station station : start.getAllPassedStations(end)) {
				buffer.append(station.getName() + ",");
			}
			linePlan = buffer.toString();
			if (linePlan.length()>1) {
				linePlan = linePlan.substring(0, linePlan.length()-1);
			}
			return;
		}
		for (Station child : getAllLinkedStations(parent)) {
			if (outList.contains(child)) {
				continue;
			}
			int shortestPath = (start.getAllPassedStations(parent).size() - 1) + 1;// 前面这个1表示计算路径需要去除自身站点，后面这个1表示增加了1站距离
			if (start.getAllPassedStations(child).contains(child)) {
				// 如果s1已经计算过到此child的经过距离，那么比较出最小的距离
				if ((start.getAllPassedStations(child).size() - 1) > shortestPath) {
					// 重置S1到周围各站的最小路径
					start.getAllPassedStations(child).clear();
					start.getAllPassedStations(child).addAll(start.getAllPassedStations(parent));
					start.getAllPassedStations(child).add(child);
				}
			} else {
				// 如果s1还没有计算过到此child的经过距离
				start.getAllPassedStations(child).addAll(start.getAllPassedStations(parent));
				start.getAllPassedStations(child).add(child);
			}
		}
		outList.add(parent);
		// 重复计算，往外面站点扩展
		calculate(start, end);
	}

	// 取参数station到各个站的最短距离，相隔1站，距离为1，依次类推
	private Station getShortestPath(Station station) {
		int minPatn = Integer.MAX_VALUE;
		Station rets = null;
		for (Station s : station.getOrderSetMap().keySet()) {
			if (outList.contains(s)) {
				continue;
			}
			LinkedHashSet<Station> set = station.getAllPassedStations(s);// 参数station到s所经过的所有站点的集合
			if (set.size() < minPatn) {
				minPatn = set.size();
				rets = s;
			}
		}
		return rets;
	}

	// 获取参数station直接相连的所有站，包括交叉线上面的站
	private List<Station> getAllLinkedStations(Station station) {
		List<Station> linkedStaions = new ArrayList<Station>();
		for (List<Station> line : DataBuilder.lineSet) {
			if (line.contains(station)) {// 如果某一条线包含了此站，注意由于重写了hashcode方法，只有name相同，即认为是同一个对象
				Station s = line.get(line.indexOf(station));
				if (s.prev != null) {
					linkedStaions.add(s.prev);
				}
				if (s.next != null) {
					linkedStaions.add(s.next);
				}
			}
		}
		return linkedStaions;
	}

}
