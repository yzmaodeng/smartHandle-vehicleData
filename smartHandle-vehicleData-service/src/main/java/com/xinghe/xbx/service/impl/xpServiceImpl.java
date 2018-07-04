/**  
* @Title: xpServiceImpl.java  
* @Package com.xinghe.xbx.auto.chains.service.impl  
* @Description: TODO(用一句话描述该文件做什么)  
* @author xbx 
* @date 2018年5月14日下午4:42:43  
* @version V1.0  
*/
package com.xinghe.xbx.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xinghe.xbx.dao.xpDao;
import com.xinghe.xbx.service.xpService;
import com.xinghe.xbx.vo.xp;

/**
 * @ClassName: xpServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author xupei
 * @date 2018年5月14日下午4:42:43
 * 
 */
@Service("xpService")
public class xpServiceImpl implements xpService {
	@Resource(name = "xpDao1")
	xpDao xpdao;

	@Override
	public List<xp> selectxp() {
		List<xp> list = xpdao.getxpList();
		return list;
	}

}
