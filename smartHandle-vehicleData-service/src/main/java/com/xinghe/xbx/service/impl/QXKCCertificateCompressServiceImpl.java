package com.xinghe.xbx.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.xinghe.xbx.dao.QXKCCertificateCompressDao;

@Service("qXKCCertificateCompressServiceImpl")
public class QXKCCertificateCompressServiceImpl {
	private static final Log logger = LogFactory.getLog(QXKCCertificateCompressServiceImpl.class);
	private static final String String = null;
	@Resource(name = "qXKCCertificateCompressDao")
	private QXKCCertificateCompressDao qXKCCertificateCompressDao;

	public void createTableBatchInsertData(String s, HSSFSheet sheetAt) {
		logger.info("第一步正在执行创建表格并导入数据。。。。。。。。。存在临时表就删掉");
		// 表格存在就刪除
		qXKCCertificateCompressDao.checkTable();
		logger.info("第一步、存在临时表就删掉执行完毕。。。。。。。。。根据excel表头创建表格");
		// 根据excel表头创建表格
		qXKCCertificateCompressDao.createTable(s);
		logger.info("第一步、根据excel表头创建表格执行完毕。。。。。。。。。批量导入数据");
		// 批量导入数据
		qXKCCertificateCompressDao.batchInsertData(sheetAt);
		logger.info("第一步、批量导入数据执行完毕。。。。。。。。。更新时间字段");
		// 更新时间字段
		qXKCCertificateCompressDao.updateData();
		qXKCCertificateCompressDao.deleteTop();
		logger.info("更新时间字段完成第一步执行完毕。。。。。。。。。开始执行第二步标准化排放标准");
		// 排放标准的标准化

	}

	public void createHGZID() {

		qXKCCertificateCompressDao.createHGZID();
	}

	public void emission_standard() {
		logger.info("排放标准的标准化正在进行》》》》》》》》》》");
		qXKCCertificateCompressDao.emission_standard();
	}

	public void updateCompressData() {
		List<Map<String, Object>> idmap = qXKCCertificateCompressDao.getIdCompressIds();
		if (idmap == null)
			return;
		logger.info("压缩完成正在更新合格证id》》》》》》》》》》");
		qXKCCertificateCompressDao.updateCatarc(idmap);
		logger.info("压缩完成&&更新合格证id完成》》》》》》》》》》");
	}

	public void updateCompressDataJCCX() {

		List<Map<String, Object>> idmap = qXKCCertificateCompressDao.getIdCompressIdsJCCX();
		qXKCCertificateCompressDao.updateCatarc(idmap);
	}

	public void updateCompressDataPK() {

		List<Map<String, Object>> idmap = qXKCCertificateCompressDao.updateCompressDataPK();
		qXKCCertificateCompressDao.updateCatarc(idmap);
	}

	public void importCertificate(int i) {
		qXKCCertificateCompressDao.importCertificate(i);

	}

	public void changeCertificateDefautValue() {
		qXKCCertificateCompressDao.changeCertificateDefautValue();
	}

	public void checkTable() {
		// 表格存在就刪除
		qXKCCertificateCompressDao.checkTable();

	}

	/**
	 * @Title: exportExcel
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 上午11:01:52
	 * @version V1.0
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void exportExcel() {
		List mfgrbrandList = qXKCCertificateCompressDao.getMfgrBrand();
		Iterator<Map> it = mfgrbrandList.iterator();
		while (it.hasNext()) {
			Map next = it.next();

			String mfgr_name_full = (String) next.get("mfgr_name_full");
			String brand_name = (String) next.get("brand_name");
			int i = qXKCCertificateCompressDao.checkMfgr(mfgr_name_full);
			int j = qXKCCertificateCompressDao.checkBrand(
					brand_name.endsWith("牌") ? brand_name.substring(0, brand_name.length() - 1) : brand_name);

			if (i >= 1 && j >= 1) {
				it.remove();
				continue;
			}
			// 删除元素
			Iterator<Map.Entry<String, String>> itmap = next.entrySet().iterator();
			while (itmap.hasNext()) {
				Map.Entry<String, String> entry = itmap.next();
				if (i >= 1 && "mfgr_name_full".equals(entry.getKey())) {
					entry.setValue("@" + entry.getValue());
				}
				if (j >= 1 && "brand_name".equals(entry.getKey())) {
					entry.setValue("@" + entry.getValue());
				}
			}

		}
		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");
		String timeStr = sdf.format(new Date());
		listWriteExcel("QXKCnewBrandMfgr"+timeStr, new String[] { "mfgr_id", "brand_id", "catarc_card_id" }, mfgrbrandList);

	}

	/**
	 * @Title: listWriteExcel
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            string2
	 * @param @param
	 *            strings
	 * @param @param
	 *            mfgrbrandList 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午2:10:23
	 * @version V1.0
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	private void listWriteExcel(java.lang.String fileName, java.lang.String[] coloumItems, List mfgrbrandList) {
		String path = "C:/smartVehicleOrigionData/qxkc/";
		// 写文件
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		// 文件输出流
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(path + fileName + ".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFCellStyle createCellStyle = workbook.createCellStyle();
			HSSFSheet sheet = workbook.createSheet();
			createTag(coloumItems, sheet);// 写表格的列名
			createList_map_Value(mfgrbrandList, sheet, createCellStyle);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @Title: createList_map_Value
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            mfgrbrandList 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午2:42:39
	 * @version V1.0
	 * @param style
	 * @param sheet
	 */
	private void createList_map_Value(List mfgrbrandList, HSSFSheet s, HSSFCellStyle style) {

		try {
			int flag = 1;
			// int count = mfgrbrandList.size();
			HSSFRow row = null;
			HSSFCell cell = null;
			@SuppressWarnings("rawtypes")
			Iterator iterator = mfgrbrandList.iterator();
			while (iterator.hasNext()) {
				row = s.createRow(flag);
				@SuppressWarnings("rawtypes")
				Map map = (Map) iterator.next();
				Iterator<Map.Entry<String, String>> itmap = map.entrySet().iterator();
				int j = 0;
				while (itmap.hasNext()) {
					Entry<java.lang.String, java.lang.String> nextEn = itmap.next();
					cell = row.createCell(j);
					cell.setCellValue(nextEn.getValue() + "");

					if (!(nextEn.getValue().contains("@")) && !"catarc_card_id".equals(nextEn.getKey())) {
						style.setFillForegroundColor(IndexedColors.RED.getIndex());
						style.setFillPattern(CellStyle.SOLID_FOREGROUND);
						cell.setCellStyle(style);
					}
					j++;
				}

				flag++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建表格表头
	 * 
	 * @param tags
	 * @param s
	 */
	private static void createTag(String[] tags, HSSFSheet s) {
		HSSFRow row = s.createRow(0);
		HSSFCell cell = null;
		for (int i = 0; i < tags.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(tags[i]);
		}
	}

	private static void createValue(SqlRowSet queryForRowSet, HSSFSheet s) {
		try {
			int flag = 1;
			int count = queryForRowSet.getMetaData().getColumnCount();
			HSSFRow row = null;
			HSSFCell cell = null;
			while (queryForRowSet.next()) {
				row = s.createRow(flag);
				for (int i = 1; i <= count; i++) {
					cell = row.createCell(i - 1);
					Object obj = queryForRowSet.getObject(i);
					cell.setCellValue(obj + "");
				}
				flag++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void updateHGZ() {
		// 将所有的数据都查询处理啊
		List<Map<String, Object>> listData = qXKCCertificateCompressDao.queyCompressClumns();
		int num=0;
		for (Map<String, Object> map : listData) {
			Long catarc_card_id = (Long) map.get("catarc_card_id");
			List<Map<String, Object>> resuLIST = qXKCCertificateCompressDao.getNumCompressColum(map);
			int j = resuLIST.size();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("model_ID是：：");
			if (j >= 1) {
				num++;
				for (Map<String, Object> map2 : resuLIST) {
					Long model_id = (Long) map2.get("model_id");
					stringBuilder.append(model_id).append("和");
					String catarc_card_ref_ids_new = (String) map.get("catarc_card_ref_ids");
					qXKCCertificateCompressDao.updateOldCatarcIds(model_id, catarc_card_ref_ids_new);
				}
				stringBuilder.append("重复");
				logger.info("新增数据第"+num+"条catarc_card_id是" + catarc_card_id + "的与老的数据库里面的" + (stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)));
				qXKCCertificateCompressDao.cancleCertificate(catarc_card_id, stringBuilder.toString());
			} else if (j == 0) {
				continue;
			} else {
				logger.error("catarc_card_id" + catarc_card_id + "是和老的数据库做对比的时候" + "::方法error！！！");
				throw new RuntimeException("和老的数据库做对比的时候" + "QXKCCertificateCompressServiceImpl——》updateHGZ::方法error！！！");

			}
		}

	}

	// 遍历这些数据看看有没有重复的
	// 有就要更新合格证id
	// 没有就跳过

	/**
	 * @Title: test
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 上午9:55:05
	 * @version V1.0
	 */
	public void test() {
		qXKCCertificateCompressDao.test();
	}

	/**
	 * @Title: erportErrorEmissionStandard
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 下午7:12:33
	 * @version V1.0
	 */
	public void exportErrorEmissionStandard() {
		logger.info("排放标准的标准化result》》》》》》》》》》");
		List<String> str = qXKCCertificateCompressDao.erportErrorEmissionStandard();
		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("yyyy年MM月dd日_HH时mm分ss秒");
		String timeStr = sdf.format(new Date());
		File file = new File("C:\\smartVehicleOrigionData\\qxkc\\排放标准标准化说明"+timeStr+".txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (str != null && str.size() != 0) {

				BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
				for (String s : str) {
					bw.write(s);
					bw.newLine();
				}
				bw.write("以上存在问题的值请改成标准格式：国Ⅳ，国Ⅴ，欧Ⅳ，欧Ⅴ，欧Ⅲ，国Ⅰ，国Ⅱ，国Ⅲ，国Ⅲ带OBD，化油器");
				if (bw != null) {
					bw.close();
					bw = null;
				}
				logger.info("排放标准存在问题的已经导出请检查C:\\smartVehicleOrigionData\\qxkc");
			} else {

				BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
				bw.write("排放标准已经没有问题请检查其他的压缩字段！！");
				if (bw != null) {
					bw.close();
					bw = null;
				}
				logger.info("排放标准已经没有问题请检查其他的压缩字段");
			}
			logger.info("排放标准的标准化result处理完成》》》》》》》》》》");
		} catch (IOException e) {
			logger.error("C:\\smartVehicleOrigionData\\qxkc\\排放标准标准化说明.txt" + "::方法error！！！");
			throw new RuntimeException("C:\\smartVehicleOrigionData\\qxkc\\排放标准标准化说明.txt" + "::方法error！！！");
		}
	qXKCCertificateCompressDao.duplicateStaData();

	}

	/** 
	* @Title: restoreStaData 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     入参
	* @return void    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月9日 上午11:31:52 
	* @version V1.0   
	*/
	public void restoreStaData() {
		qXKCCertificateCompressDao.restoreStaData();
	}

	/** 
	* @Title: updategbCodeFn 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     入参
	* @return void    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月13日 下午6:20:54 
	* @version V1.0   
	*/
	public void updategbCodeFn() {
		qXKCCertificateCompressDao.updategbCodeFn();
		
	}

}
