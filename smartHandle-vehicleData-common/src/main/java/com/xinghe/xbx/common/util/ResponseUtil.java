/**
 *
 */
package com.xinghe.xbx.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jing
 */
public class ResponseUtil {

    private static Logger logger = Logger.getLogger(ResponseUtil.class);
    /**请求成功*/
    public static final int STATUS_SUCCESS = 200;
    /**缺少参数*/
    public static final int STATUS_LACK_PARAM = 100;
    
    public static final int STATUS_UID_TOKEN_EMPTY = 101;

    public static final int STATUS_USER_INVALID = 102;

    public static final int STATUS_AUTH_TOKEN_INVALID = 103;
    
    /**系统错误*/
    public static final int STATUS_FAIL = 104;
    /**没有权限*/
    public static final int STATUS_NO_PRIVILEGE = 105;
    /**参数错误*/
    public static final int STATUS_ERROR_PARAM = 106;

    public static final int STATUS_LOGIN_ERROR = 107;

    public static final int STATUS_LOGIN_USERPWD_ERROR = 108;

    public static final int STATUS_CODE_INVALID = 109;
    /**调用时间必须在5分钟之内*/
    public static final int STATUS_TIME_OUT = 110;  
    /**用户名信息缺少。*/
    public static final int STATUS_USER_INFO_lACK = 111;  
    
    /**=================第三方请求权限验证========================*/
    /**Apikey无效或过期*/
    public static final int STATUS_APIKEY_INVALID = 201;
    /**Apikey无权访问当前接口*/
    public static final int STATUS_APIKEY_NO_PRIVILEGE = 202;
    /**接口访问次数已用完*/
    public static final int STATUS_INTERFACE_NO_NUMBER = 203; 
    /**签名错误*/
    public static final int STATUS_SIGN_ERROR = 204;
    /**接口当天访问次数已用完*/
    public static final int STATUS_INTERFACE_NO_NUMBER_BY_DAY = 205; 
    /**=================提交报告验证========================*/   
    
    
    
    /**=================第三方请求参数验证========================*/
    /**vin前三位不可识别*/
    public static final int STATUS_VIN_TOP_THREE_ERROR = 401;
    /**vin错误*/
    public static final int STATUS_VIN_ERROR = 402;
    /**订单号错误*/
    public static final int STATUS_ORDERID_ERROR = 403;  
    /**一分钟之内不能使用同一个vin下单*/
    public static final int STATUS_ONE_MINUTE_NOT_USE_SAME_VIN = 404;
    /**报告生成中，请等待*/
    public static final int STATUS_REPOET_TREATMENT = 405;
    /**area code 错误*/
    public static final int STATUS_AREA_ERROR = 406;
    /**汽车简历回调 order id 错误*/
    public static final int STATUS_QCJL_ORDERID_ERROR = 407;
    /**汽车简历回调 save callBack data 错误*/
    public static final int STATUS_QCJL_SAVE_CALLBACK_DATA_ERROR = 408;
    /**一分钟之内不能使用同一个plateNo下单*/
    public static final int STATUS_ONE_MINUTE_NOT_USE_SAME_PLATE_NO = 409;
    /**一分钟之内不能使用相同参数下单*/
    public static final int STATUS_ONE_MINUTE_NOT_USE_SAME_PARAMSTR = 410;
    
    /**=================第三方请求结果========================*/
    /**调第三方接口失败*/
    public static final int THIRD_PARTY_RETURN_ERROR = 500;
    /**返回数据为空*/
	public static final int RETURN_DATA_IS_EMPTY = 501;
    /**返回数据无效*/
	public static final int RETURN_DATA_IS_INVALID = 502;
	/**车辆有报点,无运行数据*/
	public static final int RETURN_DATA_IS_NULL = 503;
    
    
    /**=================提交报告验证========================*/
    /**报告已经生成*/
    public static final int STATUS_ERROR_REPORT_ALREADY_FINISH = 410;
    /**车款不能为空*/
    public static final int STATUS_ERROR_REPORT_FLDID = 411;
    /**公里数不能为空mileage*/
    public static final int STATUS_ERROR_REPORT_MILEAGE = 412;
    /**请选择是否正常保养isMaintenance*/
    public static final int STATUS_ERROR_REPORT_ISMAINTENANCE = 413;
    /**请填写鉴定结果appraisalResults*/
    public static final int STATUS_ERROR_REPORT_APPRAISALRESULTS = 414;
    /**请填写评估等级carLevel*/
    public static final int STATUS_ERROR_REPORT_CARLEVEL = 415;
    /**评估价钱assessPrice为空或者不是数字*/
    public static final int STATUS_ERROR_REPORT_ASSESSPRICE = 416;
    /**=================取消报告验证========================*/
    /**报告不允许取消*/
    public static final int STATUS_REPORT_UNALLOWED_CANCEL = 417;
    /**报告取消失败*/
    public static final int STATUS_REPORT_CANCEL_FAIL = 418;
    /**报告下载失败**/
    public static final int STATUS_REPORT_DOWNLOAD_FAIL = 419;
    /**=================调第三方接口下单========================*/
    /**保险数据查询失败*/
    public static final int STATUS_GET_INSRUANCE_FAILED = 0;
    /**未下单*/
    public static final int STATUS_NO_BUY = 431;
    /**下单失败*/
    public static final int STATUS_BUY_FAILED = 432;
    /**未回调*/
    public static final int STATUS_NO_CALLBACK = 433;
    /**回调失败*/
    public static final int STATUS_CALLBACK_FAILED = 434;
    /**未获取订单详细信息*/
    public static final int STATUS_NO_GET_DETAIL = 435;
    /**获取订单详情失败*/
    public static final int STATUS_GET_DETAIL_FAILED = 436;
    /**不需要获取数据*/
    public static final int STATUS_NOT_NEED_DATA = 437;
    /**未获取订单当前状态*/
    public static final int STATUS_NO_GET_ORDERSTATUS = 438;
    /**获取订单当前状态失败*/
    public static final int STATUS_GET_ORDERSTATUS_FAILED = 439;
    /**订单状态异常*/
    public static final int STATUS_ORDERSTATUS_ERROR = 440;
    /**=================重新下单结果========================*/
    /**重新下单成功*/
    public static final int STATUS_BUY_AGAIN_SUCCESS = 441;
    /**重新下单失败*/
    public static final int STATUS_BUY_AGAIN_FAIL = 442;
  

    
    
    private static SerializerFeature[] features = {
//    		SerializerFeature.WriteNullNumberAsZero,
//    		SerializerFeature.WriteNullStringAsEmpty,
    		SerializerFeature.DisableCircularReferenceDetect,
//    		SerializerFeature.WriteMapNullValue,是否输出值为null的字段,默认为false
//    		SerializerFeature.WriteDateUseDateFormat,日期格式输出
//    		SerializerFeature.WriteNullListAsEmpty
    };
    /**
     * json response
     *
     * @param response
     * @param json
     */
    public static void printJson(HttpServletResponse response, String json) {
        printJson(response, json, "");
    }

    /**
     * jsonp response
     *
     * @param response
     * @param json
     * @param callback
     */
    public static void printJson(HttpServletResponse response, String json,
            String callback) {
        OutputStream out = null;
        if (StringUtils.isEmpty(json)) {
            logger.warn("response json is empty");
            return;
        }

        if (StringUtils.isNotBlank(callback)) {
            StringBuilder result = new StringBuilder(json.length() + 16);
            result.append(callback);
            result.append('(');
            result.append(json);
            result.append(')');
            json = result.toString();
        }

        try {
            out = response.getOutputStream();
            byte[] bytes = json.getBytes("utf-8");
            response.setStatus(200);
            response.setContentLength(bytes.length);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "no-cache");
            // HTTP 1.1
            response.setHeader("Cache-Control", "No-cache");
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            logger.error("ResponseUtil--printJson--error ", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static final String RESPONSE_LACK_PARAM;

    public static final String RESPONSE_ERROR_PARAM;

    public static final String RESPONSE_SYSTEM_ERROR;

    public static final String RESPONSE_NO_PRIVILEGE;

    public static final String RESPONSE_OK;

    public static final String RESPONSE_HAS_SIGN;



    static {

        JSONObject obj = new JSONObject();
        obj.put("status", STATUS_LACK_PARAM);
        obj.put("message", "lack required parameter");
        RESPONSE_LACK_PARAM = obj.toString();

        obj = new JSONObject();
        obj.put("status", STATUS_ERROR_PARAM);
        obj.put("message", "param error");
        RESPONSE_ERROR_PARAM = obj.toString();

        obj = new JSONObject();
        obj.put("status", STATUS_FAIL);
        obj.put("message", "system error");
        RESPONSE_SYSTEM_ERROR = obj.toString();

        obj = new JSONObject();
        obj.put("status", STATUS_NO_PRIVILEGE);
        obj.put("message", "this user has no privilege");
        RESPONSE_NO_PRIVILEGE = obj.toString();

        obj = new JSONObject();
        obj.put("status", STATUS_SUCCESS);
        obj.put("message", "operate succ");
        RESPONSE_OK = obj.toString();

        obj = new JSONObject();
        obj.put("message", "has sign today.");
        RESPONSE_HAS_SIGN = obj.toString();

    }

    public static void responseLackParam(HttpServletResponse response) {
        printJson(response, RESPONSE_LACK_PARAM);
    }

    public static void responseLackParam(HttpServletResponse response,
            String callback) {
        printJson(response, RESPONSE_LACK_PARAM, callback);
    }

    public static void responseErrorParam(HttpServletResponse response) {
        printJson(response, RESPONSE_ERROR_PARAM);
    }

    public static void responseErrorParam(HttpServletResponse response,String callback) {
        printJson(response, RESPONSE_ERROR_PARAM, callback);
    }

    public static void responseSystemError(HttpServletResponse response) {
        printJson(response, RESPONSE_SYSTEM_ERROR);
    }

    public static void responseSystemError(HttpServletResponse response,
            String callback) {
        printJson(response, RESPONSE_SYSTEM_ERROR, callback);
    }

    public static void responseNoPrivilege(HttpServletResponse response) {
        printJson(response, RESPONSE_NO_PRIVILEGE);
    }

    public static void responseNoPrivilege(HttpServletResponse response,
            String callback) {
        printJson(response, RESPONSE_NO_PRIVILEGE, callback);
    }

    public static void responseOK(HttpServletResponse response) {
        printJson(response, RESPONSE_OK);
    }

    public static void responseOK(HttpServletResponse response, String callback) {
        printJson(response, RESPONSE_OK, callback);
    }

    public static void responseObject(HttpServletResponse response,
            Object object) {
        printJson(response, getOkResponse(object));
    }

    public static void responseObject(HttpServletResponse response,
            Object object, String callback) {
        printJson(response, getOkResponse(object), callback);
    }

    public static void responseObjectWithContentType(HttpServletResponse response,
            Object object, String callback, String contentType) {
        printJsonWithContentType(response, getOkResponse(object), callback, contentType);
    }

    public static String getOkResponse(Object object) {
        JSONObject obj = new JSONObject();
        obj.put("status", STATUS_SUCCESS);
        obj.put("message", object);
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static void responseObject(HttpServletResponse response, Object object, int status) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("message", object);
        printJson(response, obj.toJSONString());
    }

    public static void responseObject(HttpServletResponse response, Object object, int status, String callback) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("message", object);
        printJson(response, obj.toJSONString(), callback);
    }

    public static void responseObject2App(HttpServletResponse response, Object object, int status, String desc,
            String callback) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("statusText", desc);
        obj.put("data", object);
        printJson(response, JSON.toJSONString(obj, features), callback);
    }
    public static void responseJson2App(HttpServletResponse response, String json, int status, String desc,
            String callback) {
        printJson(response, json, callback);
    }
    //汽车简历回调使用
    public static void responseObject2QCJL(HttpServletResponse response,  int code, 
            String callback) {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        printJson(response, JSON.toJSONString(obj, features), callback);
    }
    
    public static void responseHasSign(HttpServletResponse response,
            String callback) {
        printJson(response, RESPONSE_HAS_SIGN, callback);
    }

    public static Map<String, Object> convertToMap(Object bean, String[] columns) throws Exception {
        if ((bean != null) && (columns != null) && (columns.length > 0)) {
            Map<String, Object> map = new HashMap<String, Object>();
            Class beanCla = bean.getClass();
            for (String colName : columns) {
                Field field = beanCla.getDeclaredField(colName);
                if (field != null) {
                    field.setAccessible(true); // 设置些属性是可以访问的
                    Object val = field.get(bean); // 得到此属性的值
                    String name = field.getName();
                    map.put(name, val);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * <p>
     * Description: 复制部分属性，用于返回json结果
     * </p>
     *
     * @param <T>
     * @param list 原对象列表
     * @param columns 想要复制的属性值
     * @return
     */
    public static <T> List<Map<String, Object>> convertToMapList(List<T> list, String[] columns) {
        try {
            List<Map<String, Object>> results = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(list)) {
                for (T object : list) {
                    Map<String, Object> map = convertToMap(object, columns);
                    if (map != null) {
                        results.add(map);
                    }
                    else {
                        return null;
                    }
                }
            }
            return results;
        } catch (Exception e) {
            logger.error("convertToMapList error", e);
        }
        return null;
    }

    public static <T> NewPage<Map<String, Object>> convertToMapPage(NewPage<T> page, String[] columns) {
        try {
            List<Map<String, Object>> results = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(page.getList())) {
                for (T object : page.getList()) {
                    Map<String, Object> map = convertToMap(object, columns);
                    if (map != null) {
                        results.add(map);
                    }
                    else {
                        return null;
                    }
                }
            }
            return new NewPage<Map<String, Object>>(page.getPage(), page.getPageSize(), page.getAllCount(), results);
        } catch (Exception e) {
            logger.error("convertToMapPage error", e);
        }
        return null;
    }

    /**
     * json response
     *
     * @param response
     * @param json
     */
    public static void printJsonWithContentType(HttpServletResponse response, String json, String contentType) {
        printJsonWithContentType(response, json, "", contentType);
    }

    /**
     * jsonp response
     *
     * @param response
     * @param json
     * @param callback
     */
    public static void printJsonWithContentType(HttpServletResponse response, String json,
            String callback, String contentType) {
        OutputStream out = null;
        if (StringUtils.isEmpty(json)) {
            logger.warn("response json is empty");
            return;
        }

        if (StringUtils.isNotBlank(callback)) {
            StringBuilder result = new StringBuilder(json.length() + 16);
            result.append(callback);
            result.append('(');
            result.append(json);
            result.append(')');
            json = result.toString();
        }

        try {
            out = response.getOutputStream();
            byte[] bytes = json.getBytes("utf-8");
            response.setStatus(200);
            response.setContentLength(bytes.length);
            response.setCharacterEncoding("utf-8");
//            response.setHeader("Access-Control-Allow-Origin", "*");
            if (contentType == null) {
                response.setContentType("application/json; charset=UTF-8");
            } else {
                response.setContentType(contentType);
            }
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "no-cache");
            // HTTP 1.1
            response.setHeader("Cache-Control", "No-cache");
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            logger.error("ResponseUtil--printJson--error ", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
