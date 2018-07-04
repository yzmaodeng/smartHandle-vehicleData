/**  
* @Title: xpTestServiceImpl.java  
* @Package com.xinghe.xbx.auto.chains.service.impl  
* @Description: TODO(用一句话描述该文件做什么)  
* @author xbx 
* @date 2018年5月14日下午4:50:16  
* @version V1.0  
*/ 
package com.xinghe.xbx.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xinghe.xbx.dao.xpDao;
import com.xinghe.xbx.vo.xpTest;
import com.xinghe.xbx.service.xpTestService;

/**  
* @ClassName: xpTestServiceImpl  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author xupei
* @date 2018年5月14日下午4:50:16  
*    
*/
@Service("xpTestService")
public class xpTestServiceImpl implements  xpTestService{
	@Resource(name="xpDao1")
	xpDao xpdao ;
	@Override
	public int saveXpTest(xpTest bean) {
		int flag=0;
		flag = xpdao.saveXpTest(bean);
		return flag;
	}
	

}
