/*
 * Copyright (c) 2012 Sohu. All Rights Reserved
 */
package com.xinghe.xbx.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * Description: 字符串处理工具
 * </p>
 *
 * @author jing
 * @version 1.0
 * @Date 2015-10-8
 */
public class StringUtil {

    private static Logger logger = Logger.getLogger( StringUtil.class );

    // 过滤特殊字符
    public static String StringFilter( String str ) {
        try {
            // 清除掉所有特殊字符
            String regEx = "`~!@#$%^&*|\\<>！@#￥%……&*（）——+|【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile( regEx );
            Matcher m = p.matcher( str );
            if ( m.find() ) {
                logger.info( "this string has special string,str:" + str );
            }
            return m.replaceAll( "" ).trim();
        } catch ( Exception e ) {
            logger.error( "StringFilter error,str:" + str, e );
        }
        return null;
    }

    private static String htmlEscape( char c ) {
        switch ( c ) {
            case '&':
                return "&amp;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '"':
                return "&quot;";
            case ' ':
                return "&nbsp;";
            default:
                return c + "";
        }
    }

    /**
     * 对传入的字符串 str 进行 Html 代码转义
     * @param str
     * @return
     */
    public static String htmlEscape( String str ) {
        if ( ( str == null ) || str.trim().equals( "" ) ) {
            return str;
        }
        StringBuilder encodeStrBuilder = new StringBuilder();
        for ( int i = 0, len = str.length(); i < len; i++ ) {
            encodeStrBuilder.append( htmlEscape( str.charAt( i ) ) );
        }
        return encodeStrBuilder.toString();
    }

    public static String stripXSS( String str ) {
        if ( ( str == null ) || str.trim().equals( "" ) ) {
            return str;
        }
        Pattern scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        str = scriptPattern.matcher(str).replaceAll("");
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        str = scriptPattern.matcher(str).replaceAll("");
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        str = scriptPattern.matcher(str).replaceAll("");
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        str = scriptPattern.matcher(str).replaceAll("");
        return str;
    }


    public static String trimUnicode(String content) {
        try {
            if (StringUtils.isNotBlank(content)) {
                return content.replaceAll("(?:\u2028|\u2029)", "");
            }
        } catch (Exception e) {
            logger.error("trimUnicode error,content:" + content, e);
        }
        return null;
    }
    /**
     * 列表转为字符串(用逗号分隔)
     * @param list
     * @param separator
     * @return
     */
    public static String simpleListJoinToStrWithSeparator(List<String> list, String separator) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder finalEmailStr = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                finalEmailStr.append(separator);
            }
            finalEmailStr.append(list.get(i));
        }
        return finalEmailStr.toString();
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    /**
     * 字符串替换指定字符
     * @param str
     * @param src
     * @param desc
     * @return
     */
    public static String strReplace(String str,String src,String desc) {
    	str = str.replaceAll(src, desc);
        return str;
    }
    
    
    
    public static void main( String[] args ) {
    	System.out.println( StringFilter( "http://111=@=.jpg" ) );
    	System.out.println( htmlEscape( "</>" ) );
    }
}
