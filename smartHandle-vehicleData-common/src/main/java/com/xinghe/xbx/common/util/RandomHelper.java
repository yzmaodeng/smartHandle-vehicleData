package com.xinghe.xbx.common.util;

import java.util.Random;

public class RandomHelper {
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String allChar2 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String numberChar = "12345";
	public static final String numberChar2 = "01234567890123456789";
	
	private static RandomHelper instance = null;

	public static RandomHelper getInstance(){
		if (instance == null) {
			synchronized (RandomHelper.class) {
				if (instance == null) {
					instance = new RandomHelper();
				}
			}
		}
		return instance;
	}
	
	/** 
	 * 返回一个定长的随机字符串(只包含大小写字母、数字) 
	 * 
	 * @param length 随机字符串长度 
	 * @return 随机字符串 
	 */ 
	public static String generateString(int length) { 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) {
			sb.append(allChar2.charAt(random.nextInt(allChar2.length()))); 
		} 
		return sb.toString().toUpperCase(); 
	}
	
	/** 
	 * 返回一个定长的随机字符串(只包含大小写字母、数字) 
	 * 
	 * @param length 随机字符串长度 
	 * @return 随机字符串 
	 */ 
	public static String generateNum(int length) { 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) {
			sb.append(numberChar2.charAt(random.nextInt(numberChar2.length()))); 
		} 
		return sb.toString(); 
	}
	
	public static int generateNum1(int length) { 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) {
			sb.append(numberChar.charAt(random.nextInt(numberChar.length()))); 
		}
		
		return getInt(sb.toString(), 1);
	}
	
    public static int getInt(String stringNumber, int defaultValue) {
        if (stringNumber == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(stringNumber);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
	public static void main(String[] args) { 
		System.out.println(generateString(32).toUpperCase()); 
		System.out.println(generateNum(4)); 
	} 
}
