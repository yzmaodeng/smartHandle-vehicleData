package com.xinghe.xbx.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * VIN格式验证
 */
public class VinVerificationUtil{
	private static final Logger logger = Logger.getLogger(VinVerificationUtil.class);
	private static int vinIndexWeights[] = {8,7,6,5,4,3,2,10,0,9,8,7,6,5,4,3,2};
	/**
	 * 
	 * @Title: vinValidate
	 * @param  vin
	 * @return Integer
	 * 0 验证通过
	 * 1 VIN码前三位不可识别
	 * 2 VIN码格式错误
	 */
	public  static Integer vinValidate(String vin){
		logger.info("Info->CommonServiceImpl-->vinValidate|param:vin="+vin);

		String vinCode=vin.toUpperCase();
		
		//长度验证
        if (vinCode.length() == 17){
            //Vin不会存在IOQ三个字母 
            if (vinCode.indexOf("I") < 0 && vinCode.indexOf("O") < 0 && vinCode.indexOf("Q")<0){
                int vinValidate = 0;//验证结果
                int validateTotal = 0;//所有位数乘以权重后的和
                //最后四位必须是数字
                if (StringUtils.isNumeric(vinCode.substring(13, 17))){
                    //将VIN拆分成17个字母的数组
                    char[] vinCharArray = vinCode.toCharArray();
                    for (int i = 0; i < vinCharArray.length; i++){
                        //校验码计算
                        if (i == 8){
                            if ('X' == vinCharArray[i]){
                                vinValidate = 10;
                            }else{
                            	try {
                            		vinValidate = Integer.parseInt(vinCharArray[i]+"");
								} catch (Exception e) {
									logger.error("Exception->CommonServiceImpl-->vinValidate|Exception="+e.getMessage()+"|param:vinCharArray["+i+"]="+vinCharArray[i]+"|vin="+vin);
									return 2;
								}
                            }
                        }else{
                        	try {
                        		validateTotal += getCharValue(vinCharArray[i]+"") * vinIndexWeights[i];
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("Exception->CommonServiceImpl-->vinValidate|Exception="+e.getMessage()+"|param:vinCharArray["+i+"]="+vinCharArray[i]+"|vin="+vin);
								return 2;
							}
                        }
                    }
                    int vinValidateTmp = 0;
                	try {
                        vinValidateTmp = validateTotal % 11;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception->CommonServiceImpl-->vinValidate|Exception="+e.getMessage()+"|param:validateTotal="+validateTotal+"|vin="+vin);
						return 2;
					}
                    //计算规则 第九位的值等于其他16位乘以权重的和对11取余
                    if ( vinValidateTmp == vinValidate){
                    	return 0;
                    }else{
                    	return 2;
                    }	
                }else {
                	return 2;
                }
            }else{
            	return 2;
            }
        }else{
        	return 2;
        }
	}
	
	private static int getCharValue(String code){
		if(code.equals("0")){
			return 0;
		}
		if(code.equals("1") || code.equals("A") || code.equals("J")){
			return 1;
		}
		if(code.equals("2") || code.equals("B") || code.equals("K") || code.equals("S")){
			return 2;
		}
		if(code.equals("3") || code.equals("C") || code.equals("L") || code.equals("T")){
			return 3;
		}
		if(code.equals("4") || code.equals("D") || code.equals("M") || code.equals("U")){
			return 4;
		}
		if(code.equals("5") || code.equals("E") || code.equals("N") || code.equals("V")){
			return 5;
		}
		if(code.equals("6") || code.equals("F") || code.equals("W")){
			return 6;
		}
		if(code.equals("7") || code.equals("G") || code.equals("P") || code.equals("X")){
			return 7;
		}
		if(code.equals("8") || code.equals("H") || code.equals("Y")){
			return 8;
		}
		if(code.equals("9") || code.equals("R") || code.equals("Z")){
			return 9;
		}
		return 0;
	}
}