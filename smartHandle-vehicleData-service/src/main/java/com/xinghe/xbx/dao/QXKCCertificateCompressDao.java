package com.xinghe.xbx.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.xinghe.xbx.service.util.IdGeneratorHelper;

@Repository("qXKCCertificateCompressDao")
public class QXKCCertificateCompressDao {
	private static final Log logger = LogFactory.getLog(QXKCCertificateCompressDao.class);
	@Resource(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	public void createTable(String s) {

		try {
			jdbcTemplate.execute(s);
			logger.info(s);
		} catch (Exception e) {
			logger.info("创建表格失败");
			logger.info(e.getMessage());
			System.exit(0);
		}

	}

	public void batchInsertData(final HSSFSheet sheetAt) {
		String s = "    SELECT" + "      count(*)" + "      " + "    FROM" + "     INFORMATION_SCHEMA.COLUMNS"
				+ "    where" + "    table_schema ='commercail_zl'" + "    AND" + "    table_name  = 'cerTableQXKC'";

		Integer num = 0;
		try {
			num = jdbcTemplate.queryForObject(s, Integer.class);
		} catch (Exception e1) {
			logger.info("查询总个数失败");
			logger.info(e1.getMessage());
			System.out.println(e1.getMessage());
			System.exit(0);
		}

		String sql = "";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into cerTableQXKC values(");
		for (int i = 0; i < num - 1; i++) {
			stringBuilder.append("?,");
		}
		stringBuilder.append("?);");
		String strInsert = stringBuilder.toString();
		logger.info(strInsert);
		final Integer num1 = num;
		try {
			jdbcTemplate.batchUpdate(strInsert, new BatchPreparedStatementSetter() {
				public int getBatchSize() {
					return sheetAt.getPhysicalNumberOfRows() - 1;
				}

				@SuppressWarnings("unused")
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					HSSFRow row = sheetAt.getRow(i + 1);
					int rowNum = row.getRowNum();

					if (i % 100 == 0) {

						logger.info(i + "条数据插入临时表完成");
					}
					for (int k = 0; k < num1; k++) {
						HSSFCell cell = row.getCell(k);
						if (cell == null) {
							ps.setString(k + 1, "");
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							ps.setString(k + 1, cell.getStringCellValue());
						}
					}

				}
			});
		} catch (Exception e) {
			logger.info("数据批量插入失败");
			logger.info(e.getMessage());
			System.exit(0);
		}
		
		// 存在空白行的情况的处理

		String ss = "delete from cerTableQXKC where catarc_card_id='';";
		jdbcTemplate.execute(ss);

	}
	// public void createHGZID() {
	// Long HGZId=null;
	// String s="SELECT id from hgzIDS";
	//
	// List<Integer> queryForList = jdbcTemplate.queryForList(s, Integer.class);
	//
	// String ss=" update hgzIDS set RID=? where id=?";
	// for (Integer integer : queryForList)
	// {
	// HGZId= IdGeneratorHelper.getInstance().getHGZId(ConstantConfig.HGZ_TYPE);
	// Object[] params = new Object[]{HGZId,integer};
	// jdbcTemplate.update(ss,params);
	//
	// }

	// }

	public void createHGZID() {
		
		
	String s1="select count(*) from cerTableQXKC   WHERE xbx_seq_id=''";
//		Integer catarc_card_idnull = jdbcTemplate.queryForObject(s1, Integer.class);
//		
//		if(catarc_card_idnull.intValue()==0)
//		{
//			return;
//		}
		Long HGZId = null;
		String s = "SELECT catarc_card_id from cerTableQXKC";
		List<String> queryForList = jdbcTemplate.queryForList(s, String.class);
		String ss = " update cerTableQXKC set xbx_seq_id=? where catarc_card_id=?";
		int i = 0;
		for (String integer : queryForList) {
			HGZId = IdGeneratorHelper.getInstance().getHGZId(7);
			Object[] params = new Object[] { HGZId, integer };
			try {
				jdbcTemplate.update(ss, params);
			} catch (Exception e) {
				logger.info("聚合id是" + integer + "的数据添加业务逻辑id失败");
				logger.info(e.getMessage());
				System.exit(0);
			}
			if (++i % 100 == 0) {

				logger.info("第" + (++i) + "条数据业务逻辑添加成功");
			}

		}

	}

	public void emission_standard() {

		int i = 0;

		final String pmapsql = "select emission_standard,catarc_card_id from cerTableQXKC where emission_standard!=''";
		List<Map<String, Object>> PList = jdbcTemplate.queryForList(pmapsql);
		String updatePVsql = "";
		for (Map<String, Object> pmap : PList) {
			if (++i % 100 == 0) {
				logger.info("第" + (i) + "条排放标准处理完成！！！");
			}
			String pid = (String) pmap.get("catarc_card_id");
			String ppfbz = (String) pmap.get("emission_standard");
			Boolean pIV2 = ppfbz.indexOf("国IV") != -1;
			Boolean pIV3 = ppfbz.indexOf("国Ⅳ") != -1;
			Boolean pIV = pIV2 || pIV3;
			Boolean pV2 = ppfbz.indexOf("国Ⅴ") != -1;
			Boolean pV3 = ppfbz.indexOf("国V") != -1;
			Boolean pV = pV2 || pV3;
			if (pIV && pV)
				throw new RuntimeException("catarc_card_id is :::" + pid + "::is all V  and IV");
			if (pV) {
				updatePVsql = " update cerTableQXKC set emission_standard='国五' where catarc_card_id= '" + pid + "'";
			} else if (pIV) {
				updatePVsql = " update cerTableQXKC  set emission_standard='国四' where catarc_card_id=  '" + pid + "'";
			} else {
				continue;
			}
			try {
				jdbcTemplate.execute(updatePVsql);
			} catch (Exception e) {
				logger.info("聚合id是" + pid + "的排放标准处理失败");
				logger.info(e.getMessage());
				System.exit(0);

			}

		}

		// 漏掉的數據的修改

		String fixsqlIv = "update cerTableQXKC  set  emission_standard='国四' where  emission_standard  like '%Ⅳ%' or emission_standard  like '%IV%'  ;";
		jdbcTemplate.execute(fixsqlIv);
		logger.info("漏掉的國四已經處理");

		String fixsqlV = "update cerTableQXKC  set  emission_standard='国五' where  emission_standard  like '%v%' or emission_standard  like '%V%' or  emission_standard  like '%Ⅴ%';";
		jdbcTemplate.execute(fixsqlV);
		logger.info("漏掉的國五已經處理");

		;

		String fixsqe = "update cerTableQXKC set emission_standard='' where   fuel_type='电'";
		jdbcTemplate.execute(fixsqe);
		logger.info("将电车的排放标准都变成空串");

		String fixiv = "update cerTableQXKC set emission_standard='国Ⅴ'  where   emission_standard='国五'";
		jdbcTemplate.execute(fixiv);
		logger.info("将国v的按照字典标准化");

		String fixv = "update cerTableQXKC set  emission_standard='国Ⅳ' where  emission_standard='国四'";
		jdbcTemplate.execute(fixv);
		logger.info("将国iv的按照字典标准化");

	}

	public List<Map<String, Object>> getIdCompressIds() {

		String s1 = "select count(*) from cerTableQXKC   WHERE catarc_card_ref_ids=''";

		Integer catarc_card_idnull = jdbcTemplate.queryForObject(s1, Integer.class);

		if (catarc_card_idnull.intValue() == 0) {
			return null;
		}

		String s = "select max(catarc_card_id) as catarc_card_id,group_concat(catarc_card_id separator '|') as catarc_card_ref_ids from cerTableQXKC c GROUP BY c.gb_code,c.engine_type ,"
				+ "      c.emission_standard ,c.power_max ,c.displacement_Ml ,c.ex_factory_time ,"
				+ "      c.weight_curb ,c.vehicle_length ,c.vehicle_width ,c.vehicle_height ,"
				+ "      c.container_length ,c.container_width ,c.container_height ,"
				+ "      c.drive_form ,c.capacity_rated,c.wheelbase ";
		List<Map<String, Object>> queryForList = null;
		try {
			queryForList = jdbcTemplate.queryForList(s);
		} catch (Exception e) {
			logger.info(s + "::执行失败");
			logger.info(e.getMessage());
			System.exit(0);
		}
		return queryForList;
	}

	public List<Map<String, Object>> updateCompressDataPK() {

		String s = "select max(catarc_card_id) as catarc_card_id,group_concat(catarc_card_id separator '|') as catarc_card_ref_ids from cerTableQXKC c GROUP BY "
				+ " c.gb_code,c.engine_type ,"
				+ "      c.emission_standard ,c.power_max ,c.displacement_Ml ,c.ex_factory_time ,"
				+ "      c.weight_curb ,c.vehicle_length ,c.vehicle_width ,c.vehicle_height ,"
				+ "      c.container_length ,c.container_width ,c.container_height ,"
				+ "      c.drive_form ,c.wheelbase ";
		List<Map<String, Object>> queryForList = null;
		try {
			queryForList = jdbcTemplate.queryForList(s);
		} catch (Exception e) {
			logger.info(s + "::执行失败");
			logger.info(e.getMessage());
			System.exit(0);
		}
		return queryForList;
	}

	public List<Map<String, Object>> getIdCompressIdsJCCX() {

		String s = "select max(catarc_card_id) as catarc_card_id,group_concat(catarc_card_id separator '|') as catarc_card_ref_ids from cerTableQXKC c GROUP BY "
				+ " c.gb_code,c.engine_type ,"
				+ "      c.emission_standard ,c.power_max ,c.displacement_Ml ,c.ex_factory_time ,"
				+ "      c.weight_curb ,c.vehicle_length ,c.vehicle_width ,c.vehicle_height ,"
				+ "      c.container_length ,c.container_width ,c.container_height ,"
				+ "      c.drive_form ,c.capacity_rated,c.wheelbase ";
		List<Map<String, Object>> queryForList = null;
		try {
			queryForList = jdbcTemplate.queryForList(s);
		} catch (Exception e) {
			logger.info(s + "::执行失败");
			logger.info(e.getMessage());
			System.exit(0);
		}
		return queryForList;
	}

	public void updateCatarc(final List<Map<String, Object>> idmap) {
//
//		String s = "select count(*) from cerTableQXKC   WHERE catarc_card_id=''";
//
//		Integer catarc_card_idnull = jdbcTemplate.queryForObject(s, Integer.class);
//
//		if (catarc_card_idnull.intValue() == 0) {
//			return;
//		}
		String sql = "";
		sql = "update  cerTableQXKC set catarc_card_ref_ids=  ?  where catarc_card_id= ? ";

		try {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				public int getBatchSize() {
					return idmap.size();
				}

				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map<String, Object> map = idmap.get(i);
					if (i % 100 == 0) {

						logger.info("共有" + i + "条数据压缩了完成");
					}
					String id = (String) map.get("catarc_card_id");
					String catarc_card_ref_ids = (String) map.get("catarc_card_ref_ids");
					ps.setString(1, catarc_card_ref_ids);
					ps.setString(2, id);

				}
			});
		} catch (Exception e) {
			logger.info("数据压缩了批处理失败");
			logger.info(e.getMessage());
			System.exit(0);
		}
		String sql2 = "delete from cerTableQXKC where catarc_card_ref_ids='' ";
		jdbcTemplate.execute(sql2);

	}

	public void checkTable() {

		String s = "drop table if exists cerTableQXKC;";
		String s1 = "drop table if exists cerTableQXKC_;";
		try {
			jdbcTemplate.execute(s);
			jdbcTemplate.execute(s1);
		} catch (DataAccessException e) {
			logger.info("临时表删除失败！！！！");
			logger.info(e.getMessage());
			System.exit(0);

		}

	}

	public void importCertificate(int i) {

		jdbcTemplate.execute("Truncate Table certificate_qxkc");

		String importSql = "insert into certificate_qxkc "
				+ "	( xbx_seq_id, mfgr_name_full, vehicle_category, vehicle_category_certificate, brand_name, gb_code, "
				+ "	chassis_acm_model, engine_type, fuel_type, emission_standard, displacement_ML, power_max, wheelspan_front, wheelspan_rear, "
				+ "   tyre_specification_drive, spring_number, wheelbase, axle_load, axle_num, vehicle_length, vehicle_width, vehicle_height, "
				+ "	container_length, container_width, container_height, weight_total, weight_rated_load, weight_curb, coefficient, tow_weight, capacity_rated, "
				+ "	weight_semitrailer,passenger_num_max, speed_max, ex_factory_time, remarks, fuel_consumption, gb_code_batch_num, drive_form, catarc_card_id, "
				+ "	catarc_card_ref_ids, CLSBDH, quantity_section, cylinder_num, e_company, vehicle_category_use, source_brand, source_series, "
				+ "   certificate_status, " + "	create_time, " + "	update_time, " + "	status" + "	)"
				+ "	select 	 xbx_seq_id, mfgr_name_full, vehicle_category, vehicle_category_certificate, brand_name, gb_code, "
				+ "	chassis_acm_model, engine_type, fuel_type, emission_standard, displacement_ML, power_max, wheelspan_front, wheelspan_rear, "
				+ "	tyre_specification_drive, spring_number, wheelbase, axle_load, axle_num, vehicle_length, vehicle_width, vehicle_height, "
				+ "	container_length, container_width, container_height, weight_total, weight_rated_load, weight_curb, coefficient, tow_weight, capacity_rated, "
				+ "	weight_semitrailer, passenger_num_max, speed_max, ex_factory_time, remarks, fuel_consumption, gb_code_batch_num, drive_form,catarc_card_id,"
				+ "   catarc_card_ref_ids,CLSBDH, quantity_section,cylinder_num, e_company,vehicle_category_use,  source_brand, source_series,"
				+ "   certificate_status, " + "	create_time, " + "	update_time, " + "	status" + "	 "
				+ "	from cerTableQXKC";
		try {
			jdbcTemplate.execute(importSql);
		} catch (Exception e) {
			logger.info("数据向合格证录入失败");
			logger.info(e.getMessage());
			System.exit(0);
		}

		String updateCategory = "";

		if (i == 1) {

			updateCategory = "update certificate_qxkc set  vehicle_category='客车'";
		} else {
			updateCategory = "update certificate_qxkc set  vehicle_category='货车'";
		}

		jdbcTemplate.execute(updateCategory);

	}

	public void changeCertificateDefautValue() {

		String s = "SELECT" + "      COLUMN_NAME 字段名," + "      COLUMN_TYPE 数据类型" + "    FROM"
				+ "     INFORMATION_SCHEMA.COLUMNS" + "    where" + "    table_name  = 'certificate_qxkc'";

		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(s);
		String SQL = "";
		for (Map<String, Object> map : queryForList) {
			String zdm = (String) map.get("字段名");
			String sjlx = (String) map.get("数据类型");
			logger.info(zdm + "默认值修改完毕");
			if ("id".equals(zdm)) {

				continue;
			}
			if (zdm.indexOf("status") != -1) {
				SQL = "update  cerTableQXKC  set " + zdm + "='0' where  " + zdm + "='' or " + zdm + " is null or " + zdm
						+ "=''";
				jdbcTemplate.execute(SQL);
				continue;
			}
			if (sjlx.indexOf("int") != -1 || sjlx.startsWith("float")) {
				SQL = "update  cerTableQXKC  set " + zdm + "='-1' where  " + zdm + "='' or " + zdm + " is null or "
						+ zdm + "=''";
			} else if (sjlx.startsWith("varchar")) {
				SQL = "update  cerTableQXKC  set " + zdm + "='no' where  " + zdm + "='' or " + zdm + " is null or "
						+ zdm + "=''";

			} else {
				continue;
			}
			try {
				jdbcTemplate.execute(SQL);
			} catch (Exception e) {
				logger.info(zdm + "默认值更改失败！！！！");
				logger.info(e.getMessage());
				System.exit(0);
			}

		}

	}

	public void updateData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:HH:HH");
		String format = formatter.format(new Date());
		String s1 = "update cerTableQXKC  set create_time='" + format + "' , update_time ='" + format + "'";
		try {
			jdbcTemplate.execute(s1);
		} catch (Exception e) {
			logger.info("时间更新失败！！！！");
			logger.info(e.getMessage());
			System.exit(0);
		}

	}

	public List<Map<String, Object>> getMfgrBrand() {
		String sql = "select mfgr_name_full, brand_name, catarc_card_id from cerTableQXKC";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		return queryForList;
	}

	/**
	 * @Title: checkMfgr
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            mfgr_name_full
	 * @param @return
	 *            入参
	 * @return int 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午1:25:28
	 * @version V1.0
	 */
	public int checkMfgr(java.lang.String mfgr_name_full) {

		String sql = "select count(*) from mfgr_info where status=0 AND name_full like '%" + mfgr_name_full + "%';";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class);

		return queryForObject.intValue();
	}

	/**
	 * @Title: checkBrand
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            brand_name
	 * @param @return
	 *            入参
	 * @return int 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午1:25:40
	 * @version V1.0
	 */
	public int checkBrand(java.lang.String brand_name) {
		String sql = "select count(*) from brand_info where status=0 AND name='" + brand_name + "';";
		Integer queryForObject = jdbcTemplate.queryForObject(sql, Integer.class);
		return queryForObject.intValue();
	}

	/**
	 * @Title: queyCompressClumns
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return
	 *            入参
	 * @return List<Map<java.lang.String,Object>> 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午5:53:01
	 * @version V1.0
	 */
	public List<Map<java.lang.String, Object>> queyCompressClumns() {

		String sql = " select  gb_code,engine_type , (select id from dict_base_info where name=emission_standard and status=0 and type=33 )  emission_standard"
				+ ",power_max ,displacement_Ml ," + "weight_curb ,vehicle_length ,vehicle_width ,vehicle_height ,"
				+ "container_length ,container_width ,container_height ,"
				+ "ex_factory_time ,(select id from dict_base_info where name=drive_form and status=0 and type=26 ) drive_form ,"
				+ "if(capacity_rated = 'no','',capacity_rated)capacity_rated,wheelbase,catarc_card_id,catarc_card_ref_ids from certificate_qxkc ";

		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);

		return queryForList;
	}

	/**
	 * @Title: getNumCompressColum
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            listData
	 * @param @return
	 *            入参
	 * @return int 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年3月30日 下午6:59:03
	 * @version V1.0
	 * @param map
	 */
	public List<Map<String, Object>> getNumCompressColum(Map<String, Object> map) {
		List<Map<String, Object>> queryForList = null;
		try {

			// final String SQL = "select gb_code,e_type
			// ,emission_standard_id,power_max,displacement_ML,vehicle_length,vehicle_width,vehicle_height,container_length,"
			// +
			// "container_width,container_height,weight_curb,ex_factory_time,drive_form_id,wheelbase,capacity_rated,catarc_card_id,catarc_card_ref_ids
			// from "
			// + "vehicle_model m left join vehicle_detail d on
			// m.model_id=d.m_id left join engine_info e on m.model_id=e.m_id
			// where m.status=0 and d.status=0 and e.status=0"
			// + " AND gb_code= ? AND e_type= ? AND emission_standard_id= ? AND
			// power_max= ? AND displacement_ML= ? AND vehicle_length= ?"
			// + " AND vehicle_width= ? AND vehicle_height= ? AND
			// container_length= ? AND container_width= ? AND container_height=?
			// "
			// + " AND weight_curb= ? AND ex_factory_time= ? AND drive_form_id=
			// ? AND wheelbase= ? AND capacity_rated= ? ";
			final String SQL = "select model_id,catarc_card_id,catarc_card_ref_ids from "
					+ "vehicle_model m left join  vehicle_detail d on m.model_id=d.m_id left join engine_info e on m.model_id=e.m_id where m.status=0 and d.status=0 and e.status=0"
					+ " AND gb_code= ? AND e_type= ? AND emission_standard_id= ? AND power_max= ? AND displacement_ML= ? AND vehicle_length= ?"
					+ " AND vehicle_width= ? AND vehicle_height= ? AND container_length= ? AND container_width= ? AND container_height=? "
					+ " AND weight_curb= ? AND ex_factory_time= ? AND drive_form_id= ? AND wheelbase= ? AND capacity_rated= ? ";

			String gb_code = (String) map.get("gb_code");
			String engine_type = (String) map.get("engine_type");
			Long emission_standard = (Long) map.get("emission_standard");
			String power_max = (String) map.get("power_max");
			int displacement_ML = (int) map.get("displacement_ML");
			int vehicle_length = (int) map.get("vehicle_length");
			int vehicle_width = (int) map.get("vehicle_width");
			int vehicle_height = (int) map.get("vehicle_height");
			int container_length = (int) map.get("container_length");
			int container_width = (int) map.get("container_width");
			int container_height = (int) map.get("container_height");
			Float weight_curb = (Float) map.get("weight_curb");
			String ex_factory_time = (String) map.get("ex_factory_time");
			Long drive_form = (Long) map.get("drive_form");
			int wheelbase = (int) map.get("wheelbase");
			String capacity_rated = (String) map.get("capacity_rated");

			Object[] params = new Object[] { gb_code, engine_type, emission_standard, power_max, displacement_ML,
					vehicle_length, vehicle_width, vehicle_height, container_length, container_width, container_height,
					weight_curb, ex_factory_time, drive_form, wheelbase, capacity_rated };
			queryForList = jdbcTemplate.queryForList(SQL, params);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("QXKCCertificateCompressDao->getNumCompressColum！");
			logger.info(e.getMessage());
			System.exit(0);
		}
		return queryForList;
	}

	/**
	 * @Title: test
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 上午9:55:50
	 * @version V1.0
	 */
	public void test() {

		String s = "select *  from certificate where  mfgr_name_full='上汽通用五菱汽车股份有限公司'";
		SqlRowSet queryForRowSet = jdbcTemplate.queryForRowSet(s);

		int row = queryForRowSet.getRow();
		int columnCount = queryForRowSet.getMetaData().getColumnCount();

		int i = 0;

		while (queryForRowSet.next()) {
			i++;
		}

	}

	/**
	 * @Title: updateOldCatarcIds
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            catarc_card_id_old
	 * @param @param
	 *            catarc_card_ref_ids_new 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 上午11:28:01
	 * @version V1.0
	 */
	public void updateOldCatarcIds(Long model_id, java.lang.String catarc_card_ref_ids_new) {

		try {
			String s1 = "SELECT catarc_card_ref_ids FROM  vehicle_model WHERE  status=0  AND  model_id=" + model_id;
			String catarc_card_ref_idsold = jdbcTemplate.queryForObject(s1, String.class);

			boolean contains = catarc_card_ref_idsold.contains(catarc_card_ref_ids_new);

			if (contains) {
				return;

			} else {
				String s = "update vehicle_model SET  catarc_card_ref_ids=concat(catarc_card_ref_ids,concat('|',?)) where  model_id=  ? ";

				Object[] params = new Object[] { catarc_card_ref_ids_new, model_id };

				jdbcTemplate.update(s, params);

			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("QXKCCertificateCompressDao->updateOldCatarcIds！model_id是" + model_id);
			logger.info(e.getMessage());
			System.exit(0);

		}

	}

	/**
	 * @Title: cancleCertificate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            catarc_card_id 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 上午11:44:10
	 * @version V1.0
	 * @param string
	 */
	public void cancleCertificate(Long catarc_card_id, String string) {

		String s = "UPDATE  certificate_qxkc   SET  modelIDs = ? ,status =?  WHERE  catarc_card_id =  ?";

		Object[] params = new Object[] { string, -1, catarc_card_id };

		jdbcTemplate.update(s, params);

	}

	/**
	 * @Title: erportErrorEmissionStandard
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return
	 *            入参
	 * @return List<java.lang.String> 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月2日 下午7:14:21
	 * @version V1.0
	 */
	public List<java.lang.String> erportErrorEmissionStandard() {
		String s = "select  distinct(emission_standard)  from cerTableQXKC where  emission_standard!='国Ⅳ' and  emission_standard!='国Ⅴ' and  emission_standard!=''";
		List<String> queryForList = jdbcTemplate.queryForList(s, String.class);
		return queryForList;
	}

	/**
	 * @Title: deleteTop
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param 入参
	 * @return void 返回类型
	 * @author 臧亮
	 * @throws @date
	 *             2018年4月3日 下午5:10:57
	 * @version V1.0
	 */
	public void deleteTop() {
		
		String s = "delete   from cerTableQXKC  LIMIT 1;";
		jdbcTemplate.execute(s);

	}
	
	
	/** 
	* @Title: duplicateStaData 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     入参
	* @return void    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月9日 上午10:44:03 
	* @version V1.0   
	*/
	public void duplicateStaData() {
		String s="CREATE TABLE cerTableQXKC_ SELECT * FROM cerTableQXKC ";
		jdbcTemplate.execute(s);
		
	}
	
	
	/** 
	* @Title: restoreStaData 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     入参
	* @return void    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月9日 上午11:25:24 
	* @version V1.0   
	*/
	public void restoreStaData() {

		String s1 = "drop table if exists cerTableQXKC;";
		String s="CREATE TABLE cerTableQXKC SELECT * FROM cerTableQXKC_ ";
		jdbcTemplate.execute(s1);
		jdbcTemplate.execute(s);
		
	}

	/** 
	* @Title: updategbCodeFn 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     入参
	* @return void    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月13日 下午6:21:16 
	* @version V1.0   
	*/
	public void updategbCodeFn() {
		
		String s1="update certificate_qxkc   set gb_code_fn = gb_code;";
		jdbcTemplate.execute(s1);
		String s2="update certificate_qxkc  " +
		" " +
		"set gb_code_fn = (  " +
		"case   " +
		"  when SUBSTRING(gb_code_fn  , 1, 1) not between '0' and '9' then  SUBSTRING(gb_code_fn , 2)" +
		"ELSE gb_code_fn" +
		"end);";
		
		for(int i=0;i<=5;i++)
		{
			jdbcTemplate.execute(s2);
		}

		String s7="update certificate_qxkc set gb_code_fn = SUBSTRING(gb_code_fn  , 1, 1)";
		
		jdbcTemplate.execute(s7);
		
	}
}
