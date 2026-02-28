package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.AuthLogData;

/**
 * @author slin29
 *
 */
@Log4j2
public class AuthLogDataDao extends ProjDao {
	
	@Override
	public AuthLogData buildObj(ResultSet resultSet) throws Exception {
		AuthLogData o = new AuthLogData();
		o.setMerchantNo(rs.getString("merchant_no"));
		o.setCardNo(rs.getString("card_no"));
		o.setPurchaseDate(rs.getString("purchase_date"));
		o.setPurchaseTime(rs.getString("purchase_time"));
		o.setApprovalNo(rs.getString("approval_no"));
		o.setCancelApprovalNo(rs.getString("cancel_approval_no"));
		o.setDataSource(rs.getString("data_source"));
		o.setIcFlag(rs.getString("ic_flag"));
		o.setPurchaseAmt(rs.getLong("purchase_amt"));
		o.setConditionCode(rs.getString("condition_code"));
		o.setBankNo2(rs.getString("bank_no_2"));
		o.setExpireDate(rs.getString("expire_date"));
		
		return o;
	}
	

	
	/**
	 * - 依狀態碼取得資料
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<AuthLogData> queryAuthData(String processDate, boolean idFlag) throws Exception{
		String sql = "select merchant_no, "
				+ " card_no, purchase_date, purchase_time, "
				+ " nvl(approval_no,'      ') as approval_no, nvl(cancel_approval_no,'      ') as cancel_approval_no, "
				+ " data_source, ic_flag, purchase_amt, condition_code, bank_no_2, "
				+ " nvl(expire_date,'    ') as expire_date "
				+ " from AUTH_LOG_DATA "
				+ " where process_date = ? "
				+ " and ( "
				+ "      ( data_source = '4' and trans_type <> 'IP') "
				+ "      or (data_source = '3' and trans_type = 'DP') "
				+ "     )"
				+ " and not ( mod_program_id='MGOA800' and nvl(condition_code,' ')='R') "
				+ " and (condition_code <> 'U' or condition_code is null) ";
		if (idFlag) {
			sql += " AND UPDATE_ID LIKE '[%' ";
		}
		
		List<String> params = new LinkedList<String>();
		params.add(processDate);
		
		@SuppressWarnings("unchecked")
		List<AuthLogData> list = (List<AuthLogData>) queryList(sql, params);
		log.info("total " + list.size() + " records");
		return list;
	}
	
	
	/**
	 * 取得 authLogData for Dayrpt004
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDayrpt004(String date1, String date2) throws Exception{
		
		String sql = "select purchase_date, purchase_time, merchant_no,"
				+ " (substr(card_no, 1, 6) || '******' || substr(card_no, 13, 4)) as card_no, "
				+ " purchase_amt, approval_no, update_id, remark "
				+ " from auth_log_data "
				+ " where purchase_date between ? and ? "
				+ " and data_source = '4' "
				+ " and manual_force_post_flag = 'Y' "
				+ " and upper(dept_id) like 'CSD%' "
				+ " order by purchase_date, purchase_time";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	/**
	 * 取得 authLogData for Monrpt0501
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp501Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE ,CARD_TYPE, CONDITION_CODE ,COUNT(*) as COUNT "
				+ " FROM "
				+ "  (SELECT BANK_NO_2, PURCHASE_DATE, CARD_TYPE, CONDITION_CODE"
				+ "     FROM AUTH_LOG_DATA "
				+ "     WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "       AND DATA_SOURCE = '4' "
				+ "       AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "       AND TRANS_TYPE LIKE 'A%' "
				+ "       AND CARD_TYPE <> 'A' "
				+ "       AND UPPER(DEPT_ID) LIKE 'CSD%' ) A, "
				+ "  ( select b.main_id AS BANK_NO_2 "
				+ "  FROM (SELECT DISTINCT(fid) "
				+ "  FROM SETL_BIN_NEW_BINO_PARM "
				+ "  WHERE acq_iss = 'I' ) a, "
				+ "  SETL_BIN_BANK_PARM b "
				+ "  where a.fid=b.fid "
				+ "  and MAN_ID_NCCC NOT LIKE '50%' ) B"
				+ " WHERE A.BANK_NO_2 = B.BANK_NO_2 "
				+ " GROUP BY PURCHASE_DATE,CARD_TYPE,CONDITION_CODE "
				+ " ORDER BY PURCHASE_DATE,CARD_TYPE,CONDITION_CODE ";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	/**
	 * 取得 authLogData for Monrpt0501 summary
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMonrtp501Summary(String date1, String date2) throws Exception{
		String sql = "SELECT CARD_TYPE, CONDITION_CODE, COUNT(*) as COUNT "
				+ " FROM "
				+ "  (SELECT BANK_NO_2, CARD_TYPE, CONDITION_CODE "
				+ "     FROM AUTH_LOG_DATA "
				+ "    WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "      AND DATA_SOURCE = '4' "
				+ "      AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "      AND TRANS_TYPE LIKE 'A%' "
				+ "      AND CARD_TYPE <> 'A' "
				+ "      AND UPPER(DEPT_ID) LIKE 'CSD%' ) A,"
				+ "      ( "
				+ "      select b.main_id AS BANK_NO_2 "
				+ "        FROM (SELECT DISTINCT(fid) "
				+ "                FROM SETL_BIN_NEW_BINO_PARM "
				+ "              WHERE acq_iss = 'I' ) a, "
				+ "        SETL_BIN_BANK_PARM b "
				+ "      where a.fid=b.fid "
				+ "        and MAN_ID_NCCC NOT LIKE '50%' "
				+ "      ) B "
				+ " WHERE A.BANK_NO_2 = B.BANK_NO_2"
				+ " GROUP BY CARD_TYPE,CONDITION_CODE "
				+ " ORDER BY CARD_TYPE,CONDITION_CODE ";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
		
	}
	
	
	/**
	 * 取得 authLogData for Monrpt0502
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp502Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE, BUSINESS_TYPE, CARD_TYPE, CONDITION_CODE, COUNT(*) as COUNT "
				+ " FROM ( "
				+ "    SELECT PURCHASE_DATE, "
				+ "      NVL(BUSINESS_TYPE, '一般特店') BUSINESS_TYPE, "
				+ "      CARD_TYPE, "
				+ "      CONDITION_CODE "
				+ "    FROM ( "
				+ "         SELECT BANK_NO_2, PURCHASE_DATE, CARD_TYPE, CONDITION_CODE, MERCHANT_NO "
				+ "          FROM AUTH_LOG_DATA "
				+ "         WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "           AND DATA_SOURCE = '4' "
				+ "           AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "           AND TRANS_TYPE LIKE 'A%' "
				+ "           AND CARD_TYPE <> 'A' "
				+ "           AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ "           AND ACQUIRE_BK = '99' "
				+ "           AND LENGTH(MERCHANT_NO) = 10 "
				+ "           AND MERCHANT_NO <> '0000000000'"
				+ "         ) A, "
				+ "         ( "
				+ "         select b.main_id AS BANK_NO_2 "
				+ "           FROM (SELECT DISTINCT(fid) "
				+ "                   FROM SETL_BIN_NEW_BINO_PARM "
				+ "                 WHERE acq_iss = 'I' ) a, "
				+ "           SETL_BIN_BANK_PARM b "
				+ "         where a.fid=b.fid "
				+ "           and MAN_ID_NCCC LIKE '50%' "
				+ "         ) B, "
				+ "         ( "
				+ "         SELECT MERCHANT_NO, "
				+ "                DECODE (SUM(BUSINESS_TYPE),1,'集中授權特店','一般特店') BUSINESS_TYPE "
				+ "           FROM ( "
				+ "                SELECT MERCHANT_NO , "
				+ "                DECODE (BUSINESS_TYPE,'013',1,0) BUSINESS_TYPE "
				+ "                  FROM MERCHANT_BUSINESS_DATA "
				+ "                 WHERE START_DATE <= ? "
				+ "                   AND NVL(END_DATE,'99999999') > ? "
				+ "                ) "
				+ "          GROUP BY MERCHANT_NO "
				+ "         ) C "
				+ "     WHERE A.BANK_NO_2 = B.BANK_NO_2 "
				+ "       AND A.MERCHANT_NO = C.MERCHANT_NO(+) "
				+ " ) "
				+ " GROUP BY PURCHASE_DATE,BUSINESS_TYPE,CARD_TYPE,CONDITION_CODE "
				+ " ORDER BY PURCHASE_DATE,BUSINESS_TYPE,CARD_TYPE,CONDITION_CODE ";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	/**
	 * 取得 authLogData for Monrpt0502 summary
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMonrtp502Summary(String date1, String date2) throws Exception{
		String sql = "SELECT BUSINESS_TYPE, CARD_TYPE, CONDITION_CODE, COUNT(*) as COUNT "
				+ " FROM ("
				+ "   SELECT NVL(BUSINESS_TYPE,'一般特店') BUSINESS_TYPE, "
				+ "          CARD_TYPE, CONDITION_CODE "
				+ "     FROM ( "
				+ "          SELECT BANK_NO_2, CARD_TYPE, CONDITION_CODE, MERCHANT_NO "
				+ "            FROM AUTH_LOG_DATA "
				+ "           WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "             AND DATA_SOURCE = '4' "
				+ "             AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "             AND TRANS_TYPE LIKE 'A%' "
				+ "             AND CARD_TYPE <> 'A' "
				+ "             AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ "             AND ACQUIRE_BK = '99' "
				+ "             AND LENGTH(MERCHANT_NO) = 10 "
				+ "             AND MERCHANT_NO <> '0000000000'"
				+ "          )A, "
				+ "          ( "
				+ "          select b.main_id AS BANK_NO_2 "
				+ "          FROM (SELECT DISTINCT(fid) "
				+ "                   FROM SETL_BIN_NEW_BINO_PARM "
				+ "                   WHERE acq_iss = 'I' ) a, "
				+ "            SETL_BIN_BANK_PARM b "
				+ "          where a.fid=b.fid "
				+ "            and MAN_ID_NCCC LIKE '50%' "
				+ "          ) B, "
				+ "          ( "
				+ "          SELECT MERCHANT_NO,DECODE (SUM(BUSINESS_TYPE),1,'集中授權特店','一般特店') BUSINESS_TYPE "
				+ "            FROM ( "
				+ "                SELECT MERCHANT_NO, DECODE(BUSINESS_TYPE,'013',1,0) BUSINESS_TYPE "
				+ "                  FROM MERCHANT_BUSINESS_DATA "
				+ "                 WHERE START_DATE <= ? "
				+ "                   AND NVL(END_DATE,'99999999') > ? "
				+ "                 ) "
				+ "           GROUP BY MERCHANT_NO "
				+ "          ) C "
				+ "    WHERE A.BANK_NO_2 = B.BANK_NO_2 "
				+ "      AND A.MERCHANT_NO = C.MERCHANT_NO(+) "
				+ "  ) "
				+ " GROUP BY BUSINESS_TYPE,CARD_TYPE,CONDITION_CODE "
				+ " ORDER BY BUSINESS_TYPE,CARD_TYPE,CONDITION_CODE ";
 		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
		
	}
	
	/**
	 * 取得 authLogData for Monrpt0503
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp503Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE, CONDITION_CODE, COUNT(*) as COUNT "
				+ "  FROM AUTH_LOG_DATA "
				+ "  WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "    AND DATA_SOURCE = '4' "
				+ "    AND CARD_TYPE = 'A' "
				+ "    AND TRANS_TYPE LIKE 'A%' "
				+ "    AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ "  GROUP BY PURCHASE_DATE,CONDITION_CODE "
				+ "  ORDER BY PURCHASE_DATE,CONDITION_CODE ";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	
	/**
	 * 取得 authLogData for Monrpt0503 summary
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMonrtp503Summary(String date1, String date2) throws Exception{
		String sql = "SELECT CONDITION_CODE ,COUNT(*) as COUNT "
				+ " FROM AUTH_LOG_DATA "
				+ " WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "   AND DATA_SOURCE = '4' "
				+ "   AND CARD_TYPE = 'A' "
				+ "   AND TRANS_TYPE LIKE 'A%' "
				+ "   AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ " GROUP BY CONDITION_CODE "
				+ " ORDER BY CONDITION_CODE ";
 		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
		
	}
		
	
	/**
	 * 取得 authLogData for Monrpt0511
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp511Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE, PURCHASE_TIME, MERCHANT_NO, MCC_CODE, "
				+ " PURCHASE_AMT, A.BANK_NO_2, CARD_TYPE, CONDITION_CODE "
				+ " FROM ("
				+ "     SELECT PURCHASE_DATE, PURCHASE_TIME, MERCHANT_NO, MCC_CODE, "
				+ "            PURCHASE_AMT, BANK_NO_2, CARD_TYPE, CONDITION_CODE "
				+ "       FROM AUTH_LOG_DATA "
				+ "      WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "        AND DATA_SOURCE = '4' "
				+ "        AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "        AND TRANS_TYPE LIKE 'A%' "
				+ "        AND CARD_TYPE <> 'A' "
				+ "        AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ "      ) A, "
				+ "      ( "
				+ "      select b.main_id AS BANK_NO_2 "
				+ "        FROM (SELECT DISTINCT(fid) "
				+ "                FROM SETL_BIN_NEW_BINO_PARM "
				+ "              WHERE acq_iss = 'I' ) a, "
				+ "        SETL_BIN_BANK_PARM b "
				+ "      where a.fid=b.fid "
				+ "        and MAN_ID_NCCC NOT LIKE '50%' "
				+ "      ) B "
				+ "      WHERE A.BANK_NO_2 = B.BANK_NO_2";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	/**
	 * 取得 authLogData for Monrpt0512
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp512Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE, PURCHASE_TIME, A.MERCHANT_NO, "
				+ " NVL(BUSINESS_TYPE,'一般特店') BUSINESS_TYPE, "
				+ " MCC_CODE, PURCHASE_AMT, A.BANK_NO_2, CARD_TYPE, CONDITION_CODE "
				+ " FROM("
				+ "     SELECT PURCHASE_DATE, PURCHASE_TIME, MERCHANT_NO, MCC_CODE, "
				+ "            PURCHASE_AMT, BANK_NO_2, CARD_TYPE, CONDITION_CODE "
				+ "       FROM AUTH_LOG_DATA "
				+ "      WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "        AND DATA_SOURCE = '4' "
				+ "        AND MOD_PROGRAM_ID = 'MGOA800' "
				+ "        AND TRANS_TYPE LIKE 'A%' "
				+ "        AND CARD_TYPE <> 'A' "
				+ "        AND UPPER(DEPT_ID) LIKE 'CSD%' "
				+ "        AND ACQUIRE_BK = '99' "
				+ "        AND LENGTH(MERCHANT_NO) = 10 "
				+ "        AND MERCHANT_NO <> '0000000000'"
				+ "     )A, "
				+ "     ( "
				+ "     select b.main_id AS BANK_NO_2 "
				+ "       FROM (SELECT DISTINCT(fid) "
				+ "               FROM SETL_BIN_NEW_BINO_PARM "
				+ "             WHERE acq_iss = 'I' ) a, "
				+ "       SETL_BIN_BANK_PARM b "
				+ "     where a.fid=b.fid "
				+ "       and MAN_ID_NCCC LIKE '50%' "
				+ "     ) B, "
				+ "     ( "
				+ "     SELECT MERCHANT_NO,DECODE (SUM(BUSINESS_TYPE),1,'集中授權特店','一般特店') BUSINESS_TYPE "
				+ "       FROM ("
				+ "            SELECT MERCHANT_NO, DECODE(BUSINESS_TYPE,'013',1,0) BUSINESS_TYPE "
				+ "              FROM MERCHANT_BUSINESS_DATA "
				+ "             WHERE START_DATE <= ? AND NVL(END_DATE,'99999999') > ? "
				+ "            )"
				+ "      GROUP  BY MERCHANT_NO"
				+ "     ) C"
				+ "  WHERE A.BANK_NO_2 = B.BANK_NO_2 "
				+ "    AND A.MERCHANT_NO = C.MERCHANT_NO(+) ";
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
	/**
	 * 取得 authLogData for Monrpt0513
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> queryMonrtp513Data(String date1, String date2) throws Exception{
		
		String sql = "SELECT PURCHASE_DATE, PURCHASE_TIME, MERCHANT_NO, "
				+ " MCC_CODE, PURCHASE_AMT, BANK_NO_2, CONDITION_CODE "
				+ " FROM AUTH_LOG_DATA "
				+ " WHERE PURCHASE_DATE BETWEEN ? AND ? "
				+ "   AND DATA_SOURCE = '4' "
				+ "   AND CARD_TYPE = 'A' "
				+ "   AND TRANS_TYPE LIKE 'A%' "
				+ "   AND UPPER(DEPT_ID) LIKE 'CSD%' ";
		
		List<Object> params = new LinkedList<Object>();
		params.add(date1);
		params.add(date2);
		
		return super.queryListMap(sql, params);
	}
	
}
