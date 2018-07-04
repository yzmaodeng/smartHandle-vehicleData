package com.xinghe.xbx.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 地图工具类
 * @author hzh
 * @time 2016/06/07 PM 15:00
 *
 */
public class MapHelper{
	private static final Logger logger = Logger.getLogger(MapHelper.class);
	
	/** 获取精确坐标值url后缀  */
	private static final String LOCATION_URL_SUBFIX = "place/text?key=%s&children=1&offset=2&page=1&types=02&citylimit=true&city=%s&keywords=%s";
	/** 获取两坐标间距离url后缀 */
	private static final String DISTANCE_URL_SUBFIX = "distance?key=%s&origins=%s&destination=%s";
	
	/** 地图接口地址 */
	private String mapUrl;
	/** 地图用户key */
	private String userKey;
	
	private static MapHelper instance = null;

	public static MapHelper getInstance(){
		if (instance == null) {
			synchronized (MapHelper.class) {
				if (instance == null) {
					instance = new MapHelper();
				}
			}
		}
		return instance;
	}
  
	/**
	 * 通过省份地址获取精确坐标值 
	 * 地图API
	 * @param provinceId
	 * @param name
	 * @param addr
	 * @return
	 */
	public String getLocation(Integer provinceId, String name, String addr){
		String location = "";
		String url = mapUrl + LOCATION_URL_SUBFIX;
		if(StringUtils.isBlank(name) || StringUtils.isBlank(addr)){
			return "";
		}
		try{
			// 拼接地址，进一步精确位置坐标
			String accurateAddr = this.getAccurateAddr(name + addr);
			if(StringUtils.isNotBlank(accurateAddr)){
				// url = String.format(url, userKey,"110000","北京名威致远北京市朝阳区来广营西路甲8号");
				// 默认：北京
//				provinceId = StringUtils.isBlank(provinceId)?"110000":provinceId;
				provinceId = provinceId == null ? 110000:provinceId;
				// 格式化url参数
				url = String.format(url, userKey, provinceId, accurateAddr);
				// 调用map，并返回结果json串
				String json = HttpUtil.doInnerGet(url, 3, "utf8");
				JSONObject obj = JSONObject.parseObject(json);
				if(obj != null){
					JSONArray array = (JSONArray) obj.get("pois");
					if(array != null && array.size() > 0){
						JSONObject o = (JSONObject) array.get(0);
						// 坐标临时值
						String locationTemp = (String) o.get("location");
						// 返回结果中的店名
						String returnName = "";
						try{
							returnName = (String) o.get("name");
						}catch(Exception e){
							returnName = "";
						}
						// 返回结果中的地址
						String address = "";
						try{
							address = (String) o.get("address");
						}catch(Exception e){
							address = "";
						}
						// 若查出一条数据，则直接返回结果
						if(array.size() == 1){
							return locationTemp;
						}
						/*
						 *  若查出多条数据
						 *  【第一种判断方式】：查看第一条数据与第二条的location值是否相等
						 *  1.1 ：若相等，则返回结果
						 *  1.2 ：若不相等，采取下面：[第二种方式]
						 *  【第二种判断方式】：
						 *  2.1 ：查看第一条数据的[店名]与参数shop's name是否匹配 , 若匹配，则返回结果
						 *  2.2 ：查看第一条数据的[地址]与参数shop's addr是否匹配, 若匹配，则返回结果
						 *  【第三种判断方式】：
						 *    采取重试机制：
						 *      [第一步]：拼接地址，进一步精确位置坐标时， 直接用addr做关键字查找 ，若成功找到，则返回结果，否则进入下一步
						 *      [第二步]：拼接地址，进一步精确位置坐标时， 直接用name做关键字查找 ，若成功找到，则返回结果，否则返回空值
						 */
						else{
							JSONObject o2 = (JSONObject) array.get(1);
							String location2 = (String) o2.get("location");
							// 第一条数据电话号
//							String tel1 = (String) o.get("tel");
							// 第二条数据电话号
//							String tel2 = (String) o2.get("tel");
							if(StringUtils.isNotBlank(locationTemp)){
								// 【第一种判断方式】
								if(locationTemp.equals(location2)){
									return locationTemp;
								}
								// 【第二种判断方式】
								// 2.1:查看第一条数据的[店名]与参数shop's name是否匹配
								if(StringUtils.isNotBlank(returnName) && name.indexOf(returnName) > -1){
									return locationTemp;
								}
								// 2.2:[地址]与参数addr是否匹配
								if(StringUtils.isNotBlank(address) && addr.indexOf(address) > -1){
									return locationTemp;
								}
								// 【第三种判断方式】
								/*
								 * getLocation方法参数：
								 * 布尔类型flag( true:  直接用addr做关键字查找)
								 *           ( false: 直接用name做关键字查找)
								 */
								// 重试机制：[第一步]：拼接地址，进一步精确位置坐标时， 直接用addr做关键字查找
								location = this.getLocation(provinceId, name, addr, true);
								// 重试机制：[第二步]：拼接地址，进一步精确位置坐标时， 直接用name做关键字查找
								if(StringUtils.isBlank(location)){
									location = this.getLocation(provinceId, name, addr, false);
								}
							}
						}
					}
					/*
					 * 当返回数据为空时，分两种情况
					 * 【1】.请求url错误（如：参数，用户key失效，数字签名）
					 * 【2】.请求url成功，但是通过当前地址没有查到其坐标信息，则说明当前地址不准确
					 *      采取重试机制：
					 *      [第一步]：拼接地址，进一步精确位置坐标时， 直接用name做关键字查找 ，若成功找到，则返回结果，否则进入下一步
					 *      [第二步]：拼接地址，进一步精确位置坐标时， 直接用addr做关键字查找 ，若成功找到，则返回结果，否则返回空值
					 */
					else{
						// 返回状态   1:成功  ; 0:失败
						String status = (String) obj.get("status");
						// 若url请求失败，打印错误信息
						if("0".equals(status)){
							// 返回状态错误详细码
							String infoCode = (String) obj.get("infocode");
							// 返回状态错误详细信息
							String info = (String) obj.get("info");
							logger.info("[MapHelper]-->getLocation(" + provinceId + "," + name + "," + addr + ") url_error -->"
									+ "errorCode:" + infoCode + "-->errorInfo:" + info);
						}
						/*
						 *  若url请求成功，但是返回数据为空，
						 *  记录日志
						 */
						else{
							logger.info("[MapHelper]-->getLocation(" + provinceId + "," + name + "," + addr + ") url_success -->"
									+ " but: No Data found !");
							/*
							 * getLocation方法参数：
							 * 布尔类型flag( true:  直接用addr做关键字查找)
							 *           ( false: 直接用name做关键字查找)
							 */
							// 重试机制：[第一步]：拼接地址，进一步精确位置坐标时， 直接用name查找
							location = this.getLocation(provinceId, name, addr, false);
							// 重试机制：[第二步]：若还是未找到坐标值，则进行最后一次重试机制：
							//    拼接地址，进一步精确位置坐标时， 直接用addr查找
							if(StringUtils.isBlank(location)){
								location = this.getLocation(provinceId, name, addr, true);
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception->MapHelper-->getLocation(" + provinceId + "," + name + "," + addr + ") :" 
					+ e.getMessage());
		}
		return location;
	}
	
	/**
	 * 获取两坐标点之间的距离 
	 * 地图API
	 * @param origins
	 * @param destination
	 * @return
	 */
	public Long getDistance(String origins,String destination){
		Long distance = null;
		String url = mapUrl + DISTANCE_URL_SUBFIX;
		if(StringUtils.isBlank(origins) || StringUtils.isBlank(destination)){
			return null;
		}
		try{
			// 格式化url参数
			url = String.format(url, userKey, origins, destination);
			// 调用map，并返回结果json串
			String json = HttpUtil.doInnerGet(url, 3, "utf8");
			JSONObject obj = JSONObject.parseObject(json);
			JSONArray array = (JSONArray) obj.get("results");
			if(obj != null){
				if(array != null && array.size() > 0){
					JSONObject o = (JSONObject) array.get(0);
					distance = o.getLong("distance");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception->MapHelper-->getDistance(" + origins + "," + destination + ") :" 
					+ e.getMessage());
		}
		
//		System.out.println(distance);
		return distance;
	}
	
	/**
	 * 获取多个起始点与一个终点之间的距离列表
	 * 地图API
	 * @param destination
	 * @param origins List<String>
	 * @return
	 */
	public List<Long> getDistanceList(String destination,List<String> origins){
		Long distance = null;
		String url = mapUrl + DISTANCE_URL_SUBFIX;
		if(CollectionUtils.isEmpty(origins) || StringUtils.isBlank(destination)){
			return null;
		}
		List<Long> distanceList = null;
		try{
			StringBuffer originsStr = new StringBuffer();
			// 支持100个坐标对，坐标对见用“| ”分隔；经度和纬度用","分隔 , 
			// 如：116.481028,39.989643|114.481028,39.989643|115.481028,39.989643
			for(String coor : origins){
				if(!StringUtils.isBlank(coor)){
					originsStr.append(coor).append("|");
				}
			}
			String coorValue = "";
			if(originsStr.charAt(originsStr.length()-1) == '|'){
				coorValue = originsStr.substring(0, originsStr.length()-1);
			}
			// 格式化url参数
			url = String.format(url, userKey, coorValue, destination);
			// 调用map，并返回结果json串
			String json = HttpUtil.doInnerGet(url, 3, "utf8");
			JSONObject obj = JSONObject.parseObject(json);
			JSONArray array = (JSONArray) obj.get("results");
			if(obj != null){
				if(array != null && array.size() > 0){
					distanceList = new ArrayList<Long>();
					for(int i=0;i<array.size();i++){
						JSONObject o = (JSONObject) array.get(i);
						if(o != null){
							distance = o.getLong("distance");
							distanceList.add(distance);
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception->MapHelper-->getDistanceList(" + destination + "," + origins + ") :" 
					+ e.getMessage());
		}
		return distanceList;
	}
	
	/**
	 * 获取两坐标点之间的距离 (带单位)
	 * km , m
	 * 地图API
	 * @param origins
	 * @param destination
	 * @return
	 */
//	public String getDistanceForUnit(String origins,String destination){
	public Long getDistanceForUnit(String origins,String destination){
		Long distanceValue = 0L;
		if(StringUtils.isBlank(origins) || StringUtils.isBlank(destination)){
			return null;
		}
		try{
			distanceValue = this.getDistance(origins, destination);
//			distance = this.getDistanceForUnit(distanceValue);
		}catch(Exception e){
			logger.error("Exception->MapHelper-->getDistance(" + origins + "," + destination + ") :" 
					+ e.getMessage());
		}
		
//		System.out.println(distanceValue);
		return distanceValue;
	}
	
	/**
	 * 获取两坐标点之间的距离 (带单位)
	 * km , m
	 * 地图API
	 * @param distanceValue
	 * @return
	 */
	public String getDistanceForUnit(Long distanceValue){
		String distance = "";
		try{
			if(distanceValue != null){
				distance = distanceValue.toString();
				if(StringUtils.isNotBlank(distance)){
					BigDecimal b = new BigDecimal(distance);
					int index = b.compareTo(new BigDecimal("1000"));
					// index < 0 , 表示距离小于1000米，则distance直接显示，否则单位换算
					if(index >= 0){
						// 获取距离单位：米 ，转化为千米，除以1000，保留1位小数
						double f = b.divide(new BigDecimal("1000"),1,BigDecimal.ROUND_HALF_UP).doubleValue();
						distance = String.valueOf(f);
						// 由于保留1位小数，若距离以.0结尾 ， 则去掉.0
						if(distance.endsWith(".0")){
							distance = distance.substring(0, distance.length()-2);
						}
						distance += "km";
					}else{
						distance += "m";
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception->MapHelper-->getDistance(" + distanceValue + ") :" 
					+ e.getMessage());
		}
		
//		System.out.println(distance);
		return distance;
	}
	
	/**
	 * 通过省份地址获取精确坐标值 
	 * 重载方法，防止方法递归stack过深
	 * 地图API
	 * @param provinceId
	 * @param name
	 * @param addr
	 * @param flag( true:  拼接地址，进一步精确位置坐标时， 直接用addr做关键字查找)
	 *            ( false: 拼接地址，进一步精确位置坐标时， 直接用name做关键字查找)
	 * @return
	 */
	public String getLocation(Integer provinceId, String name,String addr, boolean flag){
		String location = "";
		String url = mapUrl + LOCATION_URL_SUBFIX;
		if(StringUtils.isBlank(addr)){
			return "";
		}
		try{
			// 拼接地址，进一步精确位置坐标
			String accurateAddr = this.getAccurateAddr(flag?addr:name);
			if(StringUtils.isNotBlank(accurateAddr)){
				// 默认：北京
//				provinceId = StringUtils.isBlank(provinceId)?"110000":provinceId;
				provinceId = provinceId == null ? 110000 : provinceId;
				// 格式化url参数
				url = String.format(url, userKey, provinceId, accurateAddr);
				// 调用map，并返回结果json串
				String json = HttpUtil.doInnerGet(url, 3, "utf8");
				JSONObject obj = JSONObject.parseObject(json);
				if(obj != null){
					JSONArray array = (JSONArray) obj.get("pois");
					if(array != null && array.size() > 0){
						JSONObject o = (JSONObject) array.get(0);
						String locationTemp = (String) o.get("location");
						// 返回结果中的店名
						String returnName = "";
						try{
							returnName = (String) o.get("name");
						}catch(Exception e){
							returnName = "";
						}
						// 返回结果中的地址
						String address = "";
						try{
							address = (String) o.get("address");
						}catch(Exception e){
							address = "";
						}
						// 若查出一条数据，则直接返回结果
						if(array.size() == 1){
							logger.info("[MapHelper]-->getLocation:retry(" + provinceId + "," + name + "," + addr + ") Data found ！");
							return locationTemp;
						}/*
						 *  若查出多条数据
						 *  【第一种判断方式】：查看第一条数据与第二条的location值是否相等
						 *  1.1 ：若相等，则返回结果
						 *  1.2 ：若不相等，采取下面：[第二种方式]
						 *  【第二种判断方式】：
						 *  2.1 ：查看第一条数据的[店名]与参数shop's name是否匹配 , 若匹配，则返回结果
						 *  2.2 ：查看第一条数据的[地址]与参数shop's addr是否匹配, 若匹配，则返回结果
						 */
						else{
							JSONObject o2 = (JSONObject) array.get(1);
							String location2 = (String) o2.get("location");
							
							if(StringUtils.isNotBlank(locationTemp)){
								// 【第一种判断方式】
								if(locationTemp.equals(location2) || 
										// 【第二种判断方式】
										// 2.1:查看第一条数据的[店名]与参数shop's name是否匹配
										(StringUtils.isNotBlank(returnName) && (name.indexOf(returnName) > -1 || 
												returnName.indexOf(
														name.replaceAll("(\\[.*\\])|(\u5317\u4EAC)", "").replaceAll("\\s", "")) > -1)) || 
										// 2.2:[地址]与参数addr是否匹配
										(StringUtils.isNotBlank(address) && addr.indexOf(address) > -1)){
									logger.info("[MapHelper]-->getLocation:retry(" + provinceId + "," + name + "," + addr + ") Data found ！");
									return locationTemp;
								}
							}
						}
						
					}
					/*
					 * 当返回数据为空时，分两种情况
					 * 【1】.请求url错误（如：参数，用户key失效，数字签名）
					 * 【2】.请求url成功，但是通过当前地址没有查到其坐标信息，则说明当前地址不准确
					 */
					else{
						// 返回状态   1:成功  ; 0:失败
						String status = (String) obj.get("status");
						// 若url请求失败，打印错误信息
						if("0".equals(status)){
							// 返回状态错误详细码
							String infoCode = (String) obj.get("infocode");
							// 返回状态错误详细信息
							String info = (String) obj.get("info");
							logger.info("[MapHelper]-->getLocation:retry(" + provinceId + "," + name + "," + addr + ") url_error -->"
									+ "errorCode:" + infoCode + "-->errorInfo:" + info);
						}
						/*
						 *  若url请求成功，但是返回数据为空，
						 *  记录日志
						 */
						else{
							logger.info("[MapHelper]-->getLocation:retry(" + provinceId + "," + name + "," + addr + ") url_success -->"
									+ " but: No Data found !");
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception->MapHelper-->getLocation:retry(" + provinceId + "," + name + "," + addr + ") :" 
					+ e.getMessage());
		}
		return location;
	}
	
    /**
     * 拼接地址，进一步精确位置坐标
     * @param addr
     * @return
     */
	public String getAccurateAddr(String addr){
		// name与addr都为空时，记录日志并返回
		if(StringUtils.isBlank(addr)){
		    logger.info("[MapHelper] : getAccurateAddr this method param : addr is nil !");
		    return null;
   	    }
		// 当地址里出现两个以上地址时，则视为地址不准确，返回null
		if(addr.indexOf("：") != addr.lastIndexOf("：")){
			logger.info("[MapHelper] : getAccurateAddr -->"
					+ "Address in the address of two or more, so the address is not accurate");
			return null;
		}
		String regex = "(\\[.*\\])|(\\（.*\\）)|(\\(.*\\))|(\\/.*)|(\u5317\u4EAC\u5E02)|(\u5317\u4EAC)";
   	    addr = addr.replaceAll(regex, "").replaceAll("\\s", "");
   	    return  addr;
    }
	public static void main(String[] args) {
		//http://restapi.amap.com/v3/;
		//map.user.key=5638bb7e07badab9996d520dc8a723da;
		Long distance = null;
//		String url = mapUrl + DISTANCE_URL_SUBFIX;
//		if(StringUtils.isBlank(origins) || StringUtils.isBlank(destination)){
//			return null;
//		}
		try{
			// 格式化url参数
			String url = String.format("http://restapi.amap.com/v3/distance?key=%s&origins=%s&destination=%s", "5638bb7e07badab9996d520dc8a723da", "40.03887641059028,116.3094875759549","116.349205,39.830157");
			// 调用map，并返回结果json串
			String json = HttpUtil.doInnerGet(url, 3, "utf8");
			JSONObject obj = JSONObject.parseObject(json);
			JSONArray array = (JSONArray) obj.get("results");
			if(obj != null){
				if(array != null && array.size() > 0){
					JSONObject o = (JSONObject) array.get(0);
					distance = o.getLong("distance");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
//			logger.error("Exception->MapHelper-->getDistance(" + origins + "," + destination + ") :" 
//					+ e.getMessage());
		}
		
//		System.out.println(distance);
		System.out.println(distance);
	} 
    public void setMapUrl(String mapUrl) {
	    this.mapUrl = mapUrl;
    }

    public void setUserKey(String userKey) {
	    this.userKey = userKey;
    }
	
}