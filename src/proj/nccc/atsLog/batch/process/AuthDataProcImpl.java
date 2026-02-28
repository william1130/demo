package proj.nccc.atsLog.batch.process;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.AuthDataProc;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.util.DecodeUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthDataProcImpl {

	private String jobId;

	private static int[] colSize = { 3, 19, 11, 6, 15, 8, 8, 6, 15, 8, 6, 4, 2, 1, 1, 2, 6, 9, 2, 6, 2, 4, 2, 1, 3, 2,
			2, 1, 1, 4, 2, 1, 1, 1, 1, 1, 1, 1, 10, 10, 15, 15, 2, 15, 15, 15, 15, 1, 1, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, 15, 15, 15, 1, 8, 30, 16, 40, 2, 3, 11, 1, 10, 1, 3, 1, 2, 5, 5, 1, 1, 6, 15, 2, 15, 19,
			3 };

	private static String[] colType = { "str", "str", "str", "str", "str", "str", "str", "str", "int", "str", "str",
			"str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str",
			"str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "int", "int", "int",
			"int", "int", "int", "int", "int", "int", "str", "str", "int", "int", "int", "int", "int", "int", "int",
			"int", "int", "int", "int", "int", "int", "int", "int", "int", "str", "str", "str", "str", "str", "str",
			"str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "str", "int", "str",
			"int", "int", "str" };

	private static String insertAuthDataSql = "INSERT INTO ATSLOGAP.AUTH_LOG_DATA "
			+ "(BANK_NO, CARD_NO, MAJOR_CARD_ID, APPROVAL_NO, MERCHANT_NO, TERMINAL_NO, "
			+ "PURCHASE_DATE, PURCHASE_TIME, PURCHASE_AMT, PROCESS_DATE, PROCESS_TIME, "
			+ "EXPIRE_DATE, TRANS_TYPE, CONDITION_CODE, IC_FLAG, AUTHORIZE_REASON, "
			+ "TERM_TRACE_NO, ACQUIRE_ID, ACQUIRE_BK, CANCEL_APPROVAL_NO, COUNTRY_NAME, "
			+ "MCC_CODE, ENTRY_MODE, TWO_SCREEN_AP_TX, RETURN_TANDEM_CODE, RETURN_ISO_CODE, "
			+ "RETURN_HOST_CODE, ORIGINATOR, RESPONDER, MESSAGE_TYPE, EDC_TX_TYPE, "
			+ "CVV2_CHECK_RESULT, MPAS_FLAG, MPAS_MEMBER, INSTALLMENT_FLAG, INST_INDICATOR, "
			+ "LOYALTY_FLAG, LYLT_INDICATOR, LOYALTY_ORIGINAL_POINT, LOYALTY_REDEMPTION_POINT, "
			+ "LOYALTY_REDEMPTION_AMT, LOYALTY_PAID_CREDIT_AMT, INSTALL_PERIOD_NUM, "
			+ "INSTALL_FIRST_PAYMENT, INSTALL_STAGES_PAYMENT, INSTALL_FORMALITY_FEE, "
			+ "MCC_AMT_LIMIT, CARD_CLASS, LIMIT_TYPE, NORMAL_LIMIT_AMT_MONTH, NORMAL_AMT_LAST_MONTH, "
			+ "NORMAL_AMT_MONTH, CASH_LIMIT_AMT_MONTH, CASH_BILL_AMT, CASH_AMT_LAST_MONTH, "
			+ "CASH_AMT_MONTH, TOTAL_LIMIT_AMT_MONTH, AMT_CURRENT_MONTH, AMT_LAST_MONTH, "
			+ "ID_BILL_AMT, MAX_LIMIT_AMT_MONTH, CASH_AMT_CURRENT_MONTH, ID_CASH_AMT_LAST_MONTH, "
			+ "ID_CASH_BILL_AMT, ID_CREDIT_LIMIT, DATA_SOURCE, FILE_DATE, MOD_PROGRAM_ID, UPDATE_ID, "
			+ "REMARK, BANK_NO_2, BUSINESS_TYPE, CARDHOLDER_ID, ACTION_CODE, ACTION_CODE_MEM, "
			+ "AUTH_LINK_TYPE, SERVICE_CODE, CRDH_ACTVT_TERM_IND, POS_CONDITION_CODE, CARD_TYPE, "
			+ "CARD_KIND, MANUAL_ENTRY_RESULT, MANUAL_FORCE_POST_FLAG, BIN_NO, PRE_AUTH_AMT, "
			+ "SEQ_NO, TEMP_CREDIT_LIMIT, SURCHG_AMT, DEPT_ID) VALUES ( ?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,? )";

	private static String deleteAuthDataSql = "DELETE FROM ATSLOGAP.AUTH_LOG_DATA WHERE PURCHASE_DATE BETWEEN ? AND ? ";

	public AuthDataProcImpl(String jobId) {
		this.jobId = jobId;
	}

	public ReturnVO process() throws Exception {
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {} ", jobId);

		// -- 取得待處理的資料
		ProjDao projDao = new ProjDao();
		DecodeUtil decodeUtil = new DecodeUtil();

		try {
//			String authFile = decodeUtil.execDecode(ProjInit.nmipLocalOutPath + AuthDataProc.fileName + ".Z",
//					ProjInit.nmipLocalOutPath + AuthDataProc.fileName);
			String authFile = ProjInit.nmipLocalOutPath + AuthDataProc.fileName;

			File file = new File(authFile);

			if (file.exists()) {

				// Step 1. 先依PURCHASE_DATE(消費日期)區間清檔
				// List<Object> delParms = new LinkedList<Object>();
				// delParms.add(AuthDataProc.startDate);
				// delParms.add(AuthDataProc.endDate);
				// projDao.execSqlByObj(deleteAuthDataSql, delParms);

				int count = 0;
				int totalCount = 0;

				// Step 2. 讀檔寫入AUTH_LOG_DATA
				projDao.setAutoCommit(false);
				List<String> dataList = Files.readAllLines(Paths.get(file.getAbsolutePath()),
						StandardCharsets.ISO_8859_1);
				int dataListSize = 0;
				if (dataList != null && dataList.size() > 0) {
					dataListSize = dataList.size();
					for (int i = 0; i < dataList.size(); i++) {
						String purchaseDate = dataList.get(i).substring(62, colSize[6] + 62).trim();
						if (!StringUtils.equals(purchaseDate, AuthDataProc.startDate)
								|| !StringUtils.equals(purchaseDate, AuthDataProc.startDate)) {
							if (!MyDateUtil.isInRange(MyDateUtil.toDateYYYYMMDD(purchaseDate),
									MyDateUtil.toDateYYYYMMDD(AuthDataProc.startDate),
									MyDateUtil.toDateYYYYMMDD(AuthDataProc.endDate))) {
								continue;
							}
						}
						count++;
						totalCount++;

						List<Object> parms = new LinkedList<Object>();
						int cursor = 0;
						for (int j = 0; j < colSize.length; j++) {
							String val = "";
							if (j == colSize.length - 1) {
								val = "CSD"; // DEPT_ID
							} else if (j == 69) {
								val = dataList.get(i).substring(cursor, colSize[j] + cursor).trim();
								val = new String(val.getBytes("ISO-8859-1"), "BIG5");
							} else {
								val = dataList.get(i).substring(cursor, colSize[j] + cursor).trim();
							}
							
							if (StringUtils.isBlank(val)) {
								parms.add(null);
							} else if (colType[j].equals("str")) {
								parms.add(val);
							} else {
								parms.add(Integer.valueOf(val));
							}
							cursor += colSize[j];
						}

						projDao.execSqlByObj(insertAuthDataSql, parms);
						if (count >= 3000) {
							projDao.commit();
							log.info("commit merchant biz data " + totalCount + " records");
							count = 0;
						}
					}
					projDao.commit();
				} else {
					log.info("File context is empty : " + file.getName());
				}
//				log.info("delete local File: " + file.getName());
				if (file.exists()) {
//					file.delete();
				}

				log.info("Job : {} finish, total {}, insert {} ", jobId, dataListSize, totalCount);
				returnVo.setSuccess(true);
				returnVo.setMessage("insert " + totalCount + " , total " + dataListSize);
			} else {
				log.warn(String.format("無法取得檔案 [%s].", authFile));
				returnVo.setSuccess(false);
				returnVo.setMessage(String.format("無法取得檔案 [%s].", authFile));
			}
		} finally {
			projDao.close();
//			try {
//				decodeUtil.deleteFile();
//			} catch (Exception x) {
//				log.error(x);
//			}
		}

		return returnVo;
	}
}
