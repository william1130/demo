package proj.nccc.atsLog.batch.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.other.MchtUtilDao;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;

@Log4j2
public class SyncMerchantService {
	private MchtUtilDao mchtDao;
	private ProjDao projDao;
	private Date updateDate;
	private String updateBatchNum;
	private String today;
	
	public SyncMerchantService(MchtUtilDao mchtDao, ProjDao projDao, String startDate, String updateBatchNum) {
		this.mchtDao = mchtDao;
		this.projDao = projDao;
		Date date = DateUtil.stringToDate(startDate, "yyyyMMdd");
		// -----------------------------------------------------------
		// -- incase 抓取更新7天內的資料
		this.updateDate = MyDateUtil.nextTime(date, 7, "BEFORE", "DAY");
		this.today = startDate;
				
		this.updateBatchNum = updateBatchNum;
		
	}
	
	public int syncMerchantMaster() throws Exception {
		boolean isFirstRun = projDao.count("MERCHANT_DATA", "1=1") <= 0;
		
		Connection mchtCon = mchtDao.getConnection();
		String sql = "select * from MERCHANT_MASTER where UPDATE_DATE >= ? ";
		if (isFirstRun) {
			sql = "select * from MERCHANT_MASTER ";
		}
		int count = 0;
		int totalCount = 0;
		
		PreparedStatement mchtPs = mchtCon.prepareStatement(sql);
		if (!isFirstRun) {
			mchtPs.setTimestamp(1, new Timestamp(updateDate.getTime()));
			//mchtPs.setString(2, today);
		}
		
		ResultSet mchtRs = mchtPs.executeQuery();
		try {
			projDao.setAutoCommit(false);
			
			while (mchtRs.next()) {
				count ++;
				totalCount ++;
				List<Object> parms = new LinkedList<Object>();
				// ------------------------------------------
				// -- catch from resultSet
				parms.add(mchtRs.getString("AREA_CODE"));
				parms.add(mchtRs.getString("INDUSTRY_CODE"));
				parms.add(mchtRs.getString("UPPER_MERCHANT_NO"));
				parms.add(mchtRs.getString("MCC_CODE"));
				parms.add(mchtRs.getString("CURRENT_CODE"));
				
				parms.add(mchtRs.getString("BELONG_BANK"));
				parms.add(mchtRs.getString("TEL_NO_1"));
				parms.add(mchtRs.getString("CLASS"));
				parms.add(mchtRs.getString("ENG_CITY_NAME"));
				parms.add(mchtRs.getString("IC_FLAG"));
				
				parms.add(mchtRs.getString("EDC_FLAG"));
				parms.add(mchtRs.getString("POS_FLAG"));
				parms.add(mchtRs.getString("MPOS_FLAG"));
				parms.add(mchtRs.getString("VPOS_FLAG"));
				parms.add(mchtRs.getString("TRUST_FILE_DATE"));
				
				parms.add(mchtRs.getString("FILE_DATE"));
				parms.add(mchtRs.getString("SPECIAL_TREATY_FLAG"));
				int sLimit = mchtRs.getInt("SPECIAL_TREATY_LIMIT");
				if (mchtRs.wasNull()) {
					parms.add(999999); // 單筆交易金額不得超過1百萬元(含)
				} else {
					parms.add(sLimit);
				}
				parms.add(this.updateBatchNum);
				
				String mchtNo = mchtRs.getString("MERCHANT_NO");
				parms.add(mchtNo);
				
				int updateRecord = projDao.execSqlByObj(updateMarchantMasterSql, parms);
				if (updateRecord <= 0) {
					// -------------------------------------
					// -- 更新失敗改用 insert
					projDao.execSqlByObj(insertMarchantMasterSql, parms);
				}
				if (count >= 3000) {
					projDao.commit();
					log.info("commit merchant data " + totalCount + " records");
					count = 0;
				}
			}
			projDao.commit();
			
			if (totalCount > 0) {
				// -----------------------------------------
				// -- 刪除舊資料
				
				// -- 不能刪除, 因為batch num 有可能是上一次的正常
				// -- 若依據 update_date 部分新增, 則會刪到這次沒新增的資料
				// String delSql = "delete MERCHANT_DATA where UPDATE_BATCHNUM != ?";
				// List<String> dparms = new LinkedList<String>();
				// dparms.add(this.updateBatchNum);
				// projDao.execSql(delSql, dparms);
				// projDao.commit();
			}
			log.info("Sync Merchant Data finished with " + totalCount + " records.");
		}catch(Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		finally {
			mchtRs.close();
			mchtPs.close();
		}
		return totalCount;
	}
	
	
	public int syncCardData() throws Exception {
		boolean isFirstRun = projDao.count("MERCHANT_CARD_DATA", "1=1") <= 0;
		
		Connection mchtCon = mchtDao.getConnection();
		String sql = "select * from MERCHANT_CARD_DATA where UPDATE_DATE >= ? "
				+ " and ( CONTRACT_END_DATE > ? OR CONTRACT_END_DATE is null ) ";
		if (isFirstRun) {
			sql = "select * from MERCHANT_CARD_DATA ";
		}
		int count = 0;
		int totalCount = 0;
		
		PreparedStatement mchtPs = mchtCon.prepareStatement(sql);
		if (!isFirstRun) {
			mchtPs.setTimestamp(1, new Timestamp(updateDate.getTime()));
			mchtPs.setString(2, today);
		}
		ResultSet mchtRs = mchtPs.executeQuery();
		try {
			projDao.setAutoCommit(false);
			
			while (mchtRs.next()) {
				count ++;
				totalCount ++;
				List<Object> parms = new LinkedList<Object>();
				// ------------------------------------------
				// -- catch from resultSet
				parms.add(mchtRs.getString("CONTRACT_DATE"));
				parms.add(mchtRs.getString("CONTRACT_END_DATE"));
				parms.add(mchtRs.getString("TRUST_CLASS"));
				parms.add(mchtRs.getDate("UPDATE_DATE"));
				parms.add(mchtRs.getDate("CONFIRM_DATE"));
				
				parms.add(mchtRs.getString("MERCHANT_PAN"));
				parms.add(mchtRs.getString("U_TRANS_DATE"));
				parms.add(mchtRs.getString("NT_FLAG"));
				parms.add(this.updateBatchNum);
				
				String mchtNo = mchtRs.getString("MERCHANT_NO");
				parms.add(mchtNo);
				
				parms.add(mchtRs.getString("CARD_KIND"));
				parms.add(mchtRs.getString("CARD_TYPE"));
				
				int updateRecord = projDao.execSqlByObj(updateCardDataSql, parms);
				if (updateRecord <= 0) {
					// -------------------------------------
					// -- 更新失敗改用 insert
					projDao.execSqlByObj(insertCardDataSql, parms);
				}
				if (count >= 3000) {
					projDao.commit();
					log.info("commit merchant card data " + totalCount + " records");
					count = 0;
				}
			}
			projDao.commit();
			
			if (totalCount > 0) {
				// -----------------------------------------
				// -- 刪除舊資料
				
				// -- 不能刪除, 因為batch num 有可能是上一次的正常
				// -- 若依據 update_date 部分新增, 則會刪到這次沒新增的資料
				// String delSql = "delete MERCHANT_CARD_DATA where UPDATE_BATCHNUM != ?";
				// List<String> dparms = new LinkedList<String>();
				// dparms.add(this.updateBatchNum);
				// projDao.execSql(delSql, dparms);
				// projDao.commit();
			}
			log.info("Sync Merchant card data finished with " + totalCount + " records.");
		}catch(Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		finally {
			mchtRs.close();
			mchtPs.close();
		}
		return totalCount;
	}
	
	
	public int syncBusinessData() throws Exception {
		boolean isFirstRun = projDao.count("MERCHANT_BUSINESS_DATA", "1=1") <= 0;
		
		Connection mchtCon = mchtDao.getConnection();
		String sql = "select * from MERCHANT_BUSINESS_DATA where UPDATE_DATE >= ? ";
		if (isFirstRun) {
			sql = "select * from MERCHANT_BUSINESS_DATA ";
		}
		int count = 0;
		int totalCount = 0;
		
		PreparedStatement mchtPs = mchtCon.prepareStatement(sql);
		if (!isFirstRun) {
			mchtPs.setTimestamp(1, new Timestamp(updateDate.getTime()));
		}
		ResultSet mchtRs = mchtPs.executeQuery();
		try {
			projDao.setAutoCommit(false);
			
			while (mchtRs.next()) {
				count ++;
				totalCount ++;
				List<Object> parms = new LinkedList<Object>();
				// ------------------------------------------
				// -- catch from resultSet
				parms.add(mchtRs.getString("START_DATE"));
				parms.add(mchtRs.getString("END_DATE"));
				parms.add(mchtRs.getDate("UPDATE_DATE"));
				parms.add(mchtRs.getDate("CONFIRM_DATE"));
				parms.add(mchtRs.getString("ID_CHECK_FLAG"));
				
				parms.add(this.updateBatchNum);
				
				String mchtNo = mchtRs.getString("MERCHANT_NO");
				parms.add(mchtNo);
				
				parms.add(mchtRs.getString("BUSINESS_TYPE"));
				
				int updateRecord = projDao.execSqlByObj(updateBizDataSql, parms);
				if (updateRecord <= 0) {
					// -------------------------------------
					// -- 更新失敗改用 insert
					projDao.execSqlByObj(insertBizDataSql, parms);
				}
				if (count >= 3000) {
					projDao.commit();
					log.info("commit merchant biz data " + totalCount + " records");
					count = 0;
				}
			}
			projDao.commit();
			
			if (totalCount > 0) {
				// -----------------------------------------
				// -- 刪除舊資料
				
				// -- 不能刪除, 因為batch num 有可能是上一次的正常
				// -- 若依據 update_date 部分新增, 則會刪到這次沒新增的資料
				// String delSql = "delete MERCHANT_BUSINESS_DATA where UPDATE_BATCHNUM != ?";
				// List<String> dparms = new LinkedList<String>();
				// dparms.add(this.updateBatchNum);
				// projDao.execSql(delSql, dparms);
				// projDao.commit();
			}
			log.info("Sync Merchant biz data finished with " + totalCount + " records.");
		}catch(Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		finally {
			mchtRs.close();
			mchtPs.close();
		}
		return totalCount;
	}
	
	public int syncChineseData() throws Exception {
		boolean isFirstRun = projDao.count("MERCHANT_CHINESE_DATA", "1=1") <= 0;
		
		Connection mchtCon = mchtDao.getConnection();
		String sql = "select * from MERCHANT_CHINESE_DATA where UPDATE_DATE >= ? ";
		if (isFirstRun) {
			sql = "select * from MERCHANT_CHINESE_DATA ";
		}
		int count = 0;
		int totalCount = 0;
		
		PreparedStatement mchtPs = mchtCon.prepareStatement(sql);
		if (!isFirstRun) {
			mchtPs.setTimestamp(1, new Timestamp(updateDate.getTime()));
		}
		ResultSet mchtRs = mchtPs.executeQuery();
		try {
			projDao.setAutoCommit(false);
			
			while (mchtRs.next()) {
				count ++;
				totalCount ++;
				List<Object> parms = new LinkedList<Object>();
				// ------------------------------------------
				// -- catch from resultSet
				parms.add(mchtRs.getString("ZIP_CODE_3"));
				parms.add(mchtRs.getString("ZIP_CODE_2"));
				parms.add(mchtRs.getString("CHIN_NAME"));
				parms.add(mchtRs.getString("CHIN_ZIP_CODE"));
				parms.add(mchtRs.getString("CHIN_ZIP_CITY"));
				parms.add(mchtRs.getString("CHIN_ADDR"));
				
				parms.add(mchtRs.getDate("UPDATE_DATE"));
				parms.add(mchtRs.getDate("CONFIRM_DATE"));
				
				parms.add(this.updateBatchNum);
				
				String mchtNo = mchtRs.getString("MERCHANT_NO");
				parms.add(mchtNo);
				
				parms.add(mchtRs.getString("CARD_CODE"));
				
				int updateRecord = projDao.execSqlByObj(updateChineseDataSql, parms);
				if (updateRecord <= 0) {
					// -------------------------------------
					// -- 更新失敗改用 insert
					projDao.execSqlByObj(insertChineseDataSql, parms);
				}
				if (count >= 3000) {
					projDao.commit();
					log.info("commit merchant Chinese data " + totalCount + " records");
					count = 0;
				}
			}
			projDao.commit();
			
			if (totalCount > 0) {
				// -----------------------------------------
				// -- 刪除舊資料
				
				// -- 不能刪除, 因為batch num 有可能是上一次的正常
				// -- 若依據 update_date 部分新增, 則會刪到這次沒新增的資料
				// String delSql = "delete MERCHANT_CHINESE_DATA where UPDATE_BATCHNUM != ?";
				// List<String> dparms = new LinkedList<String>();
				// dparms.add(this.updateBatchNum);
				// projDao.execSql(delSql, dparms);
				// projDao.commit();
			}
			log.info("Sync Merchant Chinese data finished with " + totalCount + " records.");
		}catch(Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		finally {
			mchtRs.close();
			mchtPs.close();
		}
		return totalCount;
	}

	
	
	
	
	
	
	
	private final String insertChineseDataSql = "insert into MERCHANT_CHINESE_DATA( "
			+ "ZIP_CODE_3, ZIP_CODE_2, CHIN_NAME, CHIN_ZIP_CODE, CHIN_ZIP_CITY, "
			+ "CHIN_ADDR, UPDATE_DATE, CONFIRM_DATE, UPDATE_BATCHNUM, "
			+ "MERCHANT_NO, CARD_CODE "
			+ ") Values ("
			+ "?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?,"
			+ "?, ?"
			+ ")";
	
	private final String updateChineseDataSql = "update MERCHANT_CHINESE_DATA set "
			+ "ZIP_CODE_3 = ? , ZIP_CODE_2 = ? , CHIN_NAME = ? , CHIN_ZIP_CODE = ? , CHIN_ZIP_CITY = ? , "
			+ "CHIN_ADDR = ? , UPDATE_DATE = ? , CONFIRM_DATE = ? , UPDATE_BATCHNUM = ? "
			+ " where "
			+ " MERCHANT_NO = ? AND CARD_CODE = ? ";
	
	
	private final String insertBizDataSql = "insert into MERCHANT_BUSINESS_DATA( "
			+ "START_DATE, END_DATE, UPDATE_DATE, CONFIRM_DATE, ID_CHECK_FLAG, "
			+ "UPDATE_BATCHNUM, "
			+ "MERCHANT_NO, BUSINESS_TYPE "
			+ ") Values ("
			+ "?, ?, ?, ?, ?, "
			+ "?, "
			+ "?, ?"
			+ ")";
	
	private final String updateBizDataSql = "update MERCHANT_BUSINESS_DATA set "
			+ "START_DATE = ? , END_DATE = ? , UPDATE_DATE = ? , CONFIRM_DATE = ? , ID_CHECK_FLAG = ? , "
			+ "UPDATE_BATCHNUM = ? "
			+ " where "
			+ " MERCHANT_NO = ? AND BUSINESS_TYPE = ? ";
	
	
	
	private final String insertCardDataSql = "insert into MERCHANT_CARD_DATA( "
			+ "CONTRACT_DATE, CONTRACT_END_DATE, TRUST_CLASS, UPDATE_DATE, CONFIRM_DATE, "
			+ "MERCHANT_PAN, U_TRANS_DATE, NT_FLAG, UPDATE_BATCHNUM, "
			+ "MERCHANT_NO, CARD_KIND, CARD_TYPE "
			+ ") Values ("
			+ "?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?,"
			+ "?, ?, ?"
			+ ")";
	
	private final String updateCardDataSql = "update MERCHANT_CARD_DATA set "
			+ "CONTRACT_DATE = ? , CONTRACT_END_DATE = ? , TRUST_CLASS = ? , UPDATE_DATE = ? , CONFIRM_DATE = ? , "
			+ "MERCHANT_PAN = ? , U_TRANS_DATE = ? , NT_FLAG = ? , UPDATE_BATCHNUM = ? "
			+ " where "
			+ " MERCHANT_NO = ? AND CARD_KIND = ? AND CARD_TYPE = ? ";
	
	private final String insertMarchantMasterSql = "insert into MERCHANT_DATA( "
			+ "AREA_CODE, INDUSTRY_CODE, UPPER_MERCHANT_NO, MCC_CODE, CURRENT_CODE, "
			+ "BELONG_BANK, TEL_NO_1, CLASS, ENG_CITY_NAME, IC_FLAG, "
			+ "EDC_FLAG, POS_FLAG, MPOS_FLAG, VPOS_FLAG, TRUST_FILE_DATE, "
			+ "FILE_DATE, SPECIAL_TREATY_FLAG, SPECIAL_TREATY_LIMIT, UPDATE_BATCHNUM, "
			+ "MERCHANT_NO "
			+ ") Values ("
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ "?"
			+ ")";
	
	private final String updateMarchantMasterSql = "update MERCHANT_DATA set "
			+ " AREA_CODE = ? , INDUSTRY_CODE = ? , UPPER_MERCHANT_NO = ? , MCC_CODE = ? , CURRENT_CODE = ? , "
			+ " BELONG_BANK = ? , TEL_NO_1 = ? , CLASS = ? , ENG_CITY_NAME = ? , IC_FLAG = ? , "
			+ " EDC_FLAG = ? , POS_FLAG = ? , MPOS_FLAG = ? , VPOS_FLAG = ? , TRUST_FILE_DATE = ? , "
			+ " FILE_DATE = ? , SPECIAL_TREATY_FLAG = ? , SPECIAL_TREATY_LIMIT = ? , UPDATE_BATCHNUM = ? "
			+ " where "
			+ " MERCHANT_NO = ? ";
	
	
}
