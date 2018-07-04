/*
 * Copyright (c) 2012 Sohu. All Rights Reserved
 */
package com.xinghe.xbx.common.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author jing
 * @version 1.0
 * @Date 2015-1-13
 */
public class DateHelper {

    public static final String sf = "yyyyMMdd";
    public static final String sf1 = "yyyy-MM-dd HH:mm:ss";
    public static final String sf2 = "yyyy-MM-dd";
    public static final String sf3 = "yyyyMMdd HH:mm:ss";
    public static final String sf4 = "yyyyMMdd-HH:mm:ss";
    public static final String sf5 = "yyyy-MM-dd HH:mm";
    public static final String sf6 = "yyyyMMddHH";
    public static final String sf7 = "yyyyMMddHHmmss";
    public static final String sf8 = "MMddHHmmssS";
    public static final String sf9 = "HH";
    public static final String sf10 = "yyyyMMddHH";
    public static final String sf11 = "yyyyMM";
    public static final String sf12 = "HHmmss";
    public static final String sf13 = "yyyyMMddHHmm";
    public static final String sf14 = "HH/mm/ss";
    public static final String sf15 = "dd";

    /**
     * <p>
     * Description:
     * </p>
     *
     * @param args
     * @author jing
     * @date 2015-1-13
     */
    public static void main(String[] args) {
    	
    	System.out.println(new Date(1487315972000L));
//    	System.out.println(getNowString());
//    	
//    	getStart("2016-10-16");
//    	
//    	
//    	String yesterday = DateFormatUtils.format(DateUtils.addDays(getStart("2016-10-16"), 0), sf2);
//        String yesterdayStart = yesterday.trim() + " 00:00:00";
//        System.out.println(yesterdayStart);
//    	
//    	
//    //	String time=getOtherTimeString(new Date(),1);
//    	System.out.println(getYesterdayStart());
//    	System.out.println(getYesterdayEnd());
//    	
//    	
//    	String sd =getTodayDate(sf8);
//    	System.out.println(sd);
//    	
//    	 String ss = getDateFormatter();
//    	    System.out.println(ss);	
//    	
//    	System.out.println(getDays(3));
//    	System.out.println(getTodayDate(sf1));
////        System.out.println(getYesterdayStart());
////        Date end = getYesterdayEnd();
////        System.out.println(DateFormatUtils.format(end, sf1));
//    	Date d=new Date();
//    	System.out.println(d);
//    	Integer start =120000;
//    	Integer end = 180000;
//    	SimpleDateFormat sdf = new SimpleDateFormat(sf12);
//    	String sss=sdf.format(new Date());
//    	Integer now =Integer.parseInt(sss);
//    	
//    	System.out.println(start);
//    	System.out.println(end);
//    	System.out.println(now);
//    	
//    	System.out.println(now>start);
//    	System.out.println(now<end);
    }
    
    public static String getNowTime() {
    	String nowTime="000000";
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat(sf12);
        	nowTime=sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowTime;
    }
 
    public static int compareDays(String oldFeedTime, String feedTime, String format) {
        try {
            if (StringUtils.isBlank(format)) {
                format = sf;
            }
            if (StringUtils.isNotBlank(oldFeedTime) && StringUtils.isNotBlank(feedTime)) {
                Date oldDate = DateUtils.parseDate(oldFeedTime, new String[] {format});
                Date date = DateUtils.parseDate(feedTime, new String[] {format});
                return compareDays(oldDate, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int compareDays(Date oldFeedTime, Date feedTime) {
        try {
            if (oldFeedTime != null && feedTime != null) {
                Long between_days = (feedTime.getTime() - oldFeedTime.getTime()) / (1000 * 3600 * 24);
                return between_days.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int compareMins(String oldTime, String newTime, String format) {
        try {
            if (StringUtils.isBlank(format)) {
                format = sf1;
            }
            if (StringUtils.isNotBlank(oldTime) && StringUtils.isNotBlank(newTime)) {
                Date oldDate = DateUtils.parseDate(oldTime, new String[] {format});
                Date date = DateUtils.parseDate(newTime, new String[] {format});
                return compareSecs(oldDate, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int compareSecs(Date oldTime, Date newTime) {
        try {
            if (oldTime != null && newTime != null) {
                Long between_mins = (newTime.getTime() - oldTime.getTime()) / (1000);
                return between_mins.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
    public static String getDateFormatter(){
    	return getTodayDate(sf1);
    }
    public static String getTodayDate() {
        return getTodayDate(sf2);
    }
    /**
     * 获取系统当前日期(以月开始精确到毫秒)，格式：MMddHHmmssS
     * @return
     */
    public static String monthDateFormatter(){
    	return getTodayDate(sf8);
    }

    public static String getTodayDate(String format) {
        try {
            Date today = new Date();
            return DateFormatUtils.format(today, format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date getYesterdayStart() {
        try {
            Date today = new Date();
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), sf2);
            String yesterdayStart = yesterday.trim() + " 00:00:00";
            return DateUtils.parseDate(yesterdayStart, new String[] {sf1});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getYesterdayEnd() {
        try {
            Date today = new Date();
            // Date yesDate = DateUtils.addDays(today, -1);
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), sf2);
            String yesterdayEnd = yesterday.trim() + " 23:59:59";
            System.out.println("" + yesterdayEnd);
            return DateUtils.parseDate(yesterdayEnd, new String[] {sf1});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getEnd(String date) {
        try {
            // Date yesDate = DateUtils.addDays(today, -1);
            String end = date.trim() + " 23:59:59";
            System.out.println("" + end);
            return DateUtils.parseDate(end, new String[] {sf1});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getStart(String date) {
        try {
            // Date yesDate = DateUtils.addDays(today, -1);
            String start = date.trim() + " 00:00:00";
            System.out.println("" + start);
            return DateUtils.parseDate(start, new String[] {sf1});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYesterdayStartString() {
        try {
            Date today = new Date();
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), sf2);
            String yesterdayStart = yesterday.trim() + " 00:00:00";
            return yesterdayStart;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYesterdayEndString() {
        try {
            Date today = new Date();
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), sf2);
            String yesterdayEnd = yesterday.trim() + " 23:59:59";
            return yesterdayEnd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getTodayStartString() {
    	try {
    		String today = DateFormatUtils.format(new Date(), sf2);
    		String todayStart = today.trim() + " 00:00:00";
    		return todayStart;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static String getTodayEndString() {
    	try {
    		String today = DateFormatUtils.format(new Date(), sf2);
    		String todayEnd = today.trim() + " 23:59:59";
    		return todayEnd;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public static String getYesterday() {
        try {
            Date today = new Date();
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), sf);
            return yesterday;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //**********************************************
    public static Date getDays(int days) {
        try {
            return DateUtils.addDays(new Date(), days);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNowString() {
        try {
            Date now = new Date();
            String nows = DateFormatUtils.format(now, sf1);
            return nows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNowTimestampString() {
        try {
            Date now = new Date();
            String nows = DateFormatUtils.format(now, sf7);
            return nows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMonthString() {
        try {
            Date now = new Date();
            String nows = DateFormatUtils.format(now, sf11);
            return nows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }   
    public static String getTodayString() {
    	String nows = "00000000";
        try {
            Date now = new Date();
            nows = DateFormatUtils.format(now, sf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nows;
    }
    
    public static String getYearMonString(Date date) {
    	String nows = "000000";
        try {
            nows = DateFormatUtils.format(date, sf11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nows;
    }
    
    public static String getDayString(Date date) {
    	String nows = "00";
        try {
            nows = DateFormatUtils.format(date, sf15);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nows;
    }
    
    public static String getYearMonDayString() {
    	Date now = new Date();
    	String YearMon = getYearMonString(now);
    	String day = getDayString(now);
    	StringBuffer str = new StringBuffer(YearMon);
        return str.append(File.separator).append(day).toString();
    }
    
    /**
     * 返回指定格式的字符串格式时间戳
     * @param format
     * @return
     */
    public static String getDateFormat(String format){
    	 try {
             Date now = new Date();
             String nows = DateFormatUtils.format(now, format);
             return nows;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
    }
    public static String getDateFormat(Date date,String format){
   	 try {
            String nows = DateFormatUtils.format(date, format);
            return nows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
   }
    /**
     * 取当前时间的前一段时间(正数)或后一段时间(负数) 时间单位是小时
     * @param date
     * @param hours
     * @return
     */
    public static String getOtherTimeString(Date date,int hours) {
        try {
            String othertime = DateFormatUtils.format(DateUtils.addHours(date, hours), sf1);
            return othertime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 取当前时间的前一段时间(正数)或后一段时间(负数) 时间单位是秒
     * @param date
     * @param  second
     * @return
     */
    public static String getOtherTimeStringBySecond(Date date,int second) {
        try {
            String othertime = DateFormatUtils.format(DateUtils.addSeconds(date, second), sf1);
            return othertime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 取当前时间的前一段时间(正数)或后一段时间(负数) 时间单位是分钟
     * @param date
     * @param  min
     * @return
     */
    public static String getOtherTimeStringByMin(Date date,int min) {
        try {
            String othertime = DateFormatUtils.format(DateUtils.addMinutes(date, min), sf1);
            return othertime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 取当前时间的前一段时间(正数)或后一段时间(负数) 时间单位是分钟
     * @param date  date类型
     * @param min   前移或后移的（分钟--值）
     * @param format  返回指定的格式
     * @return
     */
    public static String getOtherTimeStringByMinFormat(Date date,int min,String format) {
        try {
            String othertime = DateFormatUtils.format(DateUtils.addMinutes(date, min), format);
            return othertime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTwoDaysAgo() {
        try {
            Date today = new Date();
            String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -2), sf);
            return yesterday;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getNumOfOneDaySecsLeft() {
    	GregorianCalendar calendar1 = new GregorianCalendar();
    	calendar1.add(GregorianCalendar.DATE, 1);
		calendar1.set(calendar1.get(GregorianCalendar.YEAR), calendar1.get(GregorianCalendar.MONTH),
				calendar1.get(GregorianCalendar.DATE),0,0,0);
		return (calendar1.getTimeInMillis()-new GregorianCalendar().getTimeInMillis())/1000;
    }

    public static String getMonthAgo() {
        try {
            Date today = new Date();
            String monthAgo = DateFormatUtils.format(DateUtils.addMonths(today, -1), sf);
            return monthAgo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWeekAgo() {
        try {
            Date today = new Date();
            String weekAgo = DateFormatUtils.format(DateUtils.addWeeks(today, -1), sf);
            return weekAgo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWeekAgoByDay(String day) {
        try {
            Date today = DateUtils.parseDate(day, sf);
            String weekAgo = DateFormatUtils.format(DateUtils.addWeeks(today, -1), sf);
            return weekAgo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 系统当前时间
     * @return
     */
    public static Long currentTime(){
    	try {
    		 return  System.currentTimeMillis()/1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
  //获得当天0点时间 
  	public static int getTimesmorning(){ 
  	Calendar cal = Calendar.getInstance(); 
  	cal.set(Calendar.HOUR_OF_DAY, 0); 
  	cal.set(Calendar.SECOND, 0); 
  	cal.set(Calendar.MINUTE, 0); 
  	cal.set(Calendar.MILLISECOND, 0); 
  	return (int) (cal.getTimeInMillis()/1000); 
  	} 
  	//获得当天24点时间 
  	public static int getTimesnight(){ 
  	Calendar cal = Calendar.getInstance(); 
  	cal.set(Calendar.HOUR_OF_DAY, 24); 
  	cal.set(Calendar.SECOND, 0); 
  	cal.set(Calendar.MINUTE, 0); 
  	cal.set(Calendar.MILLISECOND, 0); 
  	return (int) (cal.getTimeInMillis()/1000); 
  	} 
  	
	/**
	 * 比较是否当前时间与参数时间
	 * @param expireTimestamp 字符串型的时间戳  s
	 * @return 当前时间小于参数时间 返回 false 为不过期
	 */
  	public static boolean isExpire(String expireTimestamp){
		long current = System.currentTimeMillis()/1000;
		long expire = 0l;
		boolean retu = true;
		if (StringUtils.isNotBlank(expireTimestamp)) {
			try {
	        	expire = Long.parseLong(expireTimestamp);
	        } catch (Exception e) {}
		}
		
		if(current < expire){
			retu = false;
		}
		
		return retu;
	}
  	/**
  	 * 把支付宝返回的时间字符串转换为Date类型
  	 * @param date
  	 * @return
  	 */
  	public static Date stringToDateAli(String date){
		if(StringUtils.isBlank(date)){
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(sf1);
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
  		return null;
  	}
  	/**
  	 * 把微信返回的时间字符串转换为Date类型
  	 * @param date
  	 * @return
  	 */
  	public static Date stringToDateWX(String date){
  		if(StringUtils.isBlank(date)){
  			return null;
  		}
  		try {
      		SimpleDateFormat sdf = new SimpleDateFormat(sf7);
      		return sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
  		return null;
  	}
  	/**
  	 * 把yyyy-MM-dd HH:mm:ss格式的时间字符串转换为Date类型
  	 * @param date
  	 * @return
  	 */
  	public static Date stringToDateSf1(String date){
		if(StringUtils.isBlank(date)){
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(sf1);
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
  		return null;
  	}
  	/**
  	 * 获取任意天的开始时间
  	 * @param days --》参数是天(正数代表在当前时间上加一天--负数代表在当前时间上减一天)
  	 * @param num  --》0 或 null 显示2016-10-18 00:00:00 这种格式  1显示20161018这种格式
  	 * @return 
  	 */
  	public static String getAnyDayStart(Integer days,Integer num) {
        try {
            Date today = new Date();
            String anyDay = null;
            String anyDayStart = null;
            if(num == null || num.intValue() == 0){
            	 anyDay = DateFormatUtils.format(DateUtils.addDays(today, days.intValue()), sf2);
                 anyDayStart = anyDay.trim() + " 00:00:00";
            }else if (num != null && num.intValue() == 1){
            	anyDay = DateFormatUtils.format(DateUtils.addDays(today, days.intValue()), sf);
                anyDayStart = anyDay.trim();
            }
            return anyDayStart;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
  	/**
  	 * 获取任意天的结束时间
  	 * @param days --》参数是天(正数代表在当前时间上加一天--负数代表在当前时间上减一天)
  	 * @param num  --》0 或 null 显示2016-10-18 23:59:59 这种格式  1显示20161018这种格式
  	 * @return 2016-10-18 23:59:59
  	 */
    public static String getAnyDayEnd(Integer days,Integer num) {
        try {
            Date today = new Date();
            String anyDay = null;
            String anyDayStart = null;
            if(num == null || num.intValue() == 0){
            	 anyDay = DateFormatUtils.format(DateUtils.addDays(today, days.intValue()), sf2);
                 anyDayStart = anyDay.trim() + " 23:59:59";
            }else if (num != null && num.intValue() == 1){
            	anyDay = DateFormatUtils.format(DateUtils.addDays(today, days.intValue()), sf);
                anyDayStart = anyDay.trim();
            }
            return anyDayStart;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 输入任何一天返回这一天的零点
     * @param data  2016-10-18
     * @return  2016-10-18 00:00:00
     */
    public static String getAnyDayStart(String data){
    	try {
    		String yesterday = DateFormatUtils.format(DateUtils.addDays(getStart(data), 0), sf2);
            String yesterdayStart = yesterday.trim() + " 00:00:00";
            return yesterdayStart;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 输入任何一天返回这一天的零点
     * @param data  2016-10-18
     * @return  2016-10-18 23:59:59
     */
    public static String getAnyDayEnd(String data){
    	try {
    		String yesterday = DateFormatUtils.format(DateUtils.addDays(getStart(data), 0), sf2);
            String yesterdayStart = yesterday.trim() + " 23:59:59";
            return yesterdayStart;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 把date类型转换为指定的字符串格式
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date,String format){
    	if(date == null || StringUtils.isBlank(format)){
    		return null;
    	}
    	try {
    		String formatStr = DateFormatUtils.format(date, format);
    		formatStr = formatStr.trim();
    		return formatStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 获取到今天结束还剩多少秒
     * @param date
     * @param format
     * @return
     */
    public static Integer getTodayEndInt(){
    	try {
    		String today = DateFormatUtils.format(new Date(), sf2);
    		String todayEnd = today.trim() + " 23:59:59";
    		Long todayEndDate=new SimpleDateFormat(sf1).parse(todayEnd).getTime();
    		
    		Long nowDate=new Date().getTime();
    		
    		Long difference=todayEndDate-nowDate;
    		
    		Integer differenceInt=difference.intValue();
    		return differenceInt/1000;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    } 
    
    public static String getNowTimeToDb() {
    	String nowTime="000000";
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat(sf1);
        	nowTime=sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowTime;
    }
    /**
     * 把date类型转换为指定的字符串格式
     * @param date
     * @param format
     * @return
     */
    public static String timestampToString(Long timestamp,String format){
    	if(timestamp == null || StringUtils.isBlank(format)){
    		return null;
    	}
    	try {
    		Long timestampTmp = timestamp * 1000;
    		String date = new java.text.SimpleDateFormat(format).format(new java.util.Date(timestampTmp));  
    		return date;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
}
