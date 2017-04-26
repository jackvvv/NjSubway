package com.itpoints.njmetro.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.itpoints.njmetro.bean.LinePriceBean;
import com.itpoints.njmetro.bean.MsgBean;
import com.itpoints.njmetro.bean.SearchHistoryBean;
import com.itpoints.njmetro.bean.StationDetailBean;
import com.itpoints.njmetro.bean.StationFacilityBean;
import com.itpoints.njmetro.bean.StationIdBean;
import com.itpoints.njmetro.bean.StationTimeBean;
import com.itpoints.njmetro.utils.Constants;
import com.itpoints.njmetro.utils.TimeUtils;
import com.itpoints.njmetro.utils.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

/**
 * 描述：数据库的操作
 */
public class DbHelper implements DbUtils.DbUpgradeListener {
	private static DbHelper dbHelper;
	private final String dbname = "njmetro";// 数据库名称
	private int version;// 数据库版本
	private DbUtils mDBClient;
	private Context context;

	private DbHelper(Context context) {
		this.context = context;
		version = Utils.getVersionCode(context);
		// mDBClient = DbUtils.create(context, dbname, version, this);
		mDBClient = DbUtils.create(context, Constants.path + Constants._path, dbname, version, this);
		mDBClient.configAllowTransaction(true);
		mDBClient.configDebug(true);
	}

	public static DbHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DbHelper(context);
		}
		return dbHelper;
	}

	@Override
	public void onUpgrade(DbUtils dbUtils, int i, int i1) {
		try {
			// 数据库的更新
			if (i < i1) {
				// 旧的版本小于新的版本
				// TODO
			}
		} catch (Exception e) {
			Log.d("DbHelper", "失败了");
		}
	}

	/******************* 增、删、改、查 ******************************/

	/**
	 * 获取最新消息数据
	 */
	public synchronized List<MsgBean> getMsg() {
		List<MsgBean> listMsgBean = new ArrayList<MsgBean>();
		try {
			String time = TimeUtils.getCurrertData("yyyy-MM-dd");
			listMsgBean = mDBClient.findAll(Selector.from(MsgBean.class).where(WhereBuilder.b("type", "=", "2").and("time", "=", time)).orderBy("_id", true).limit(1));
		} catch (Exception e) {
			return null;
		}

		return listMsgBean;
	}

	/**
	 * 倒叙查找所有数据 模糊搜索
	 * 
	 * @param entityClass
	 *            实体类
	 * @param column
	 *            定义的查询条件
	 * @param value
	 *            定义的查询值
	 * @return 返回数据库中所有的表数据
	 */
	public synchronized List<StationFacilityBean> getFacility(String station, String name_type) {
		List<StationFacilityBean> listStationFacilityBean = new ArrayList<StationFacilityBean>();
		try {
			if (Utils.isEmity(station)) {
				if (name_type.contains(",")) {
					String[] arrSplit = name_type.split(",");
					List<StationFacilityBean> listTemp1 = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("name", "like", "%" + arrSplit[0] + "%")));
					List<StationFacilityBean> listTemp2 = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("name", "like", "%" + arrSplit[1] + "%")));
					listStationFacilityBean.addAll(listTemp1);
					listStationFacilityBean.addAll(listTemp2);
				} else {
					listStationFacilityBean = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("name", "like", "%" + name_type + "%")));
				}
			} else {
				if (name_type.contains(",")) {
					String[] arrSplit = name_type.split(",");
					List<StationFacilityBean> listTemp1 = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("station", "=", station).and("name", "like", "%" + arrSplit[0] + "%")));
					List<StationFacilityBean> listTemp2 = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("station", "=", station).and("name", "like", "%" + arrSplit[1] + "%")));
					listStationFacilityBean.addAll(listTemp1);
					listStationFacilityBean.addAll(listTemp2);
				} else {
					listStationFacilityBean = mDBClient.findAll(Selector.from(StationFacilityBean.class).where(WhereBuilder.b("station", "=", station).and("name", "like", "%" + name_type + "%")));
				}
			}
		} catch (Exception e) {
			return null;
		}

		return listStationFacilityBean;
	}

	/**
	 * 获取线路价格
	 */
	public synchronized String getLinePrice(String addr_start, String addr_end) {
		StationIdBean startStationIdBean;
		StationIdBean endStationIdBean;
		LinePriceBean linePriceBean = null;
		int price = 0;
		try {
			startStationIdBean = mDBClient.findFirst(Selector.from(StationIdBean.class).where("station_name", "=", addr_start));
			endStationIdBean = mDBClient.findFirst(Selector.from(StationIdBean.class).where("station_name", "=", addr_end));
			linePriceBean = mDBClient.findFirst(Selector.from(LinePriceBean.class).where("start_station", "=", startStationIdBean.getId()).and("end_station", "=", endStationIdBean.getId()));
			if (linePriceBean != null) {
				price = Integer.parseInt(linePriceBean.getFee().replace(",", "")) / 100;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(price);
	}

	/**
	 * 判断是否有历史搜索
	 */
	public synchronized boolean hasSearchHistory(String name) {
		SearchHistoryBean searchHistoryBean = new SearchHistoryBean();
		try {
			searchHistoryBean = mDBClient.findFirst(Selector.from(SearchHistoryBean.class).where("name", "=", name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchHistoryBean == null ? true : false;
	}

	/**
	 * 查询历史搜索
	 * 
	 */
	public synchronized List<SearchHistoryBean> getSearchHistory(String name) {
		List<SearchHistoryBean> listSearchHistoryBean = new ArrayList<SearchHistoryBean>();
		try {
			listSearchHistoryBean = mDBClient.findAll(Selector.from(SearchHistoryBean.class).where("name", "like", "%" + name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listSearchHistoryBean;
	}

	/**
	 * 根据起点、终点计算相差时间（分）
	 * 
	 * @param addr_start
	 *            起点
	 * @param addr_end
	 *            终点
	 * @return int 相差天数
	 */
	public synchronized int getMillisecond(String addr_start, String addr_end) {
		List<Integer> listTime = new ArrayList<Integer>();
		try {
			List<StationTimeBean> listStartStationTimeBean = mDBClient.findAll(Selector.from(StationTimeBean.class).where(WhereBuilder.b("station", "=", addr_start)));
			List<StationTimeBean> listEndStationTimeBean = mDBClient.findAll(Selector.from(StationTimeBean.class).where(WhereBuilder.b("station", "=", addr_end)));

			StationTimeBean startBean;
			StationTimeBean endBean;
			ArrayList<ArrayList<StationTimeBean>> listAll = new ArrayList<ArrayList<StationTimeBean>>();
			ArrayList<StationTimeBean> listTemp;
			boolean isT = false;
			for (int i = 0; i < listStartStationTimeBean.size(); i++) {
				startBean = listStartStationTimeBean.get(i);
				listTemp = new ArrayList<StationTimeBean>();
				listTemp.add(startBean);
				isT = false;
				for (int j = 0; j < listEndStationTimeBean.size(); j++) {
					endBean = listEndStationTimeBean.get(j);
					if (startBean.getLine().equals(endBean.getLine())) {
						// 线路相同
						isT = true;
						listTemp.add(endBean);
					}
				}
				if (isT) {
					listAll.add(listTemp);
				}
			}

			// 所有起点和终点的集合
			int time = 0;
			ArrayList<StationTimeBean> arrayList;
			for (int i = 0; i < listAll.size(); i++) {
				arrayList = listAll.get(i);
				StationTimeBean startData = arrayList.get(0);
				StationTimeBean endData = arrayList.get(1);

				if (startData.get_id() > endData.get_id()) {
					// 上行
					// 上行时间
					time = TimeUtils.getDateMillisecond(endData.getTime_up().split("-")[0], startData.getTime_up().split("-")[0]);
				} else {
					// 下行
					// 下行时间
					time = TimeUtils.getDateMillisecond(startData.getTime_down().split("-")[0], endData.getTime_down().split("-")[0]);
				}
				listTime.add(Math.abs(time));

				Collections.sort(listTime);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (listTime != null && listTime.size() > 0) {
			return listTime.get(0);
		} else {
			return 0;
		}
	}

	/**
	 * 获取起点站点数据
	 * 
	 * @param addr_start
	 *            起点
	 * @param addr_end
	 *            终点
	 */
	public synchronized List<StationDetailBean> getListStartStation(String line, String addr_start) {
		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		try {
			listStationDetailBean = mDBClient.findAll(Selector.from(StationDetailBean.class).where("line", "!=", line).and("station", "=", addr_start));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listStationDetailBean;
	}

	/**
	 * 获取起点、终点经过的线路数据
	 * 
	 * @param addr_start
	 *            起点
	 * @param addr_end
	 *            终点
	 */
	public synchronized List<StationDetailBean> getStationLine(String addr_start, String addr_end) {
		List<StationDetailBean> listStationDetailBean = new ArrayList<StationDetailBean>();
		try {
			List<StationDetailBean> listStartBean = searchCriteria(StationDetailBean.class, "station", addr_start);
			List<StationDetailBean> listEndBean = searchCriteria(StationDetailBean.class, "station", addr_end);

			StationDetailBean startBean;
			StationDetailBean endBean;
			int[] arrWhere = null;
			boolean isSort = false;
			for (int i = 0; i < listStartBean.size(); i++) {
				startBean = listStartBean.get(i);

				for (int j = 0; j < listEndBean.size(); j++) {
					endBean = listEndBean.get(j);

					if (startBean.getLine().equals(endBean.getLine())) {
						// 起点、终点的线路相同
						if (startBean.get_id() > endBean.get_id()) {
							isSort = true;
							arrWhere = new int[] { endBean.get_id(), startBean.get_id() };
						} else {
							isSort = false;
							arrWhere = new int[] { startBean.get_id(), endBean.get_id() };
						}
					}
				}
			}
			listStationDetailBean = mDBClient.findAll(Selector.from(StationDetailBean.class).where(WhereBuilder.b("_id", "between", arrWhere)));
			if (isSort) {
				Collections.reverse(listStationDetailBean);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return listStationDetailBean;
	}

	/**
	 * 获取站点所属线路、时间
	 * 
	 * @param station
	 *            站点名
	 */
	public synchronized List<StationTimeBean> getStationTime(String station) {
		List<StationTimeBean> listStationTimeBean = new ArrayList<StationTimeBean>();
		try {
			listStationTimeBean = mDBClient.findAll(Selector.from(StationTimeBean.class).where(WhereBuilder.b("station", "=", station)));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return listStationTimeBean;
	}

	/**
	 * 获取站点所属线路、时间
	 * 
	 * @param station
	 *            站点名
	 */
	public synchronized StationTimeBean getStationTime(String line, String station) {
		StationTimeBean tempBean = new StationTimeBean();
		try {
			List<StationTimeBean> listStationTimeBean = mDBClient.findAll(Selector.from(StationTimeBean.class).where(WhereBuilder.b("line", "=", line).and("station", "=", station)));
			for (int i = 0; i < listStationTimeBean.size(); i++) {
				tempBean = listStationTimeBean.get(i);
				return tempBean;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return tempBean;
	}

	/**
	 * 插入单个对象
	 * 
	 * @param entity
	 *            实体类的对象
	 * @return true:插入成功 false:插入失败
	 */
	public synchronized boolean save(Object entity) {
		try {
			mDBClient.save(entity);
		} catch (DbException e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 插入全部对象
	 * 
	 * @param entity
	 *            实体类的对象
	 * @return true:插入成功 false:插入失败
	 */
	public synchronized boolean saveAll(List<?> entity) {
		try {
			mDBClient.saveAll(entity);
		} catch (DbException e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 保存、更新全部对象
	 * 
	 * @param entity
	 *            实体类的对象
	 * @return true:插入成功 false:插入失败
	 */
	public synchronized boolean saveOrUpdateAll(List<?> entity) {
		try {
			mDBClient.saveOrUpdateAll(entity);
		} catch (DbException e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除这个表中的所有数据
	 * 
	 * @param entity
	 *            实体类的对象
	 * @return true:成功 false:失败
	 */
	public synchronized boolean delete(Object entity) {
		try {
			mDBClient.delete(entity);
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据条件删除表
	 * 
	 * @param entity
	 *            表名称
	 * @param colun
	 *            列名
	 * @param value
	 *            值
	 * @return true:成功 false:失败
	 */
	public synchronized boolean deleteCriteria(Class<?> entity, String colun, String value) {
		try {
			mDBClient.delete(entity, WhereBuilder.b(colun, "=", value));
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 更新这张表中的所有数据
	 * 
	 * @param entity
	 *            实体类的对象
	 * @return true:更新成功 false:更新失败
	 */
	public synchronized boolean update(Object entity) {
		try {
			mDBClient.saveOrUpdate(entity);// 先去查这个条数据 根据id来判断是存储还是更新 如果存在更新
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据参数更新表中的数据
	 * 
	 * @param entity
	 *            实体类的对象
	 * @param value
	 *            要更新的字段
	 * @return true:更新成功 false:更新失败
	 */
	public synchronized boolean update(Object entity, String... value) {
		try {
			mDBClient.update(entity, value);
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 查找 根据id
	 * 
	 * @param cla
	 *            要查询的类
	 * @param id
	 *            要查询的id
	 * @return 查询到的数据
	 */
	public synchronized <T> Object searchOne(Class<T> cla, String column, String value) {
		try {
			return mDBClient.findFirst(Selector.from(cla).where(WhereBuilder.b(column, "=", value)));
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
		}
		return null;
	}

	/**
	 * 正叙查找 没有条件的
	 * 
	 * @param entity
	 *            要查询的类
	 * @param <T>
	 *            要查询的类
	 * @return 查询到的数据
	 */
	public synchronized <T> List<T> search(Class<T> entity) {
		try {
			return mDBClient.findAll(Selector.from(entity));
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
		}
		return null;
	}

	/**
	 * 倒叙查找所有数据 没有条件的
	 * 
	 * @param entityClass
	 * @return 返回数据库中所有的表数据
	 */
	public synchronized <T> List<T> searchDesc(Class<T> entityClass) {
		try {
			return mDBClient.findAll(Selector.from(entityClass).orderBy("_id", true));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 倒叙查找所有数据 没有条件的
	 * 
	 * @param entityClass
	 *            实体类
	 * @param column
	 *            定义的查询条件
	 * @param value
	 *            定义的查询值
	 * @return 返回数据库中所有的表数据
	 */
	public synchronized <T> List<T> searchCriteria(Class<T> entityClass, String column, String value) {
		try {
			return mDBClient.findAll(Selector.from(entityClass).where(WhereBuilder.b(column, "=", value)));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 倒叙查找所有数据 模糊搜索
	 * 
	 * @param entityClass
	 *            实体类
	 * @param column
	 *            定义的查询条件
	 * @param value
	 *            定义的查询值
	 * @return 返回数据库中所有的表数据
	 */
	public synchronized <T> List<T> searchLikeCriteria(Class<T> entityClass, String column, String value) {
		try {
			return mDBClient.findAll(Selector.from(entityClass).where(WhereBuilder.b(column, "like", "%" + value + "%")));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 删除表格
	 * 
	 * @param entityClass
	 *            实体类
	 * @return 返回数据库中所有的表数据
	 */
	public synchronized <T> boolean drop(Class<T> entityClass) {
		try {
			mDBClient.dropTable(entityClass);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
