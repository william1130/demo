package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edstw.lang.LongString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetCallback;
import com.edstw.sql.ResultSetTool;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.persist.AuthAccessLog;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.AuthAccessLogArg;
import proj.nccc.logsearch.vo.AuthLogDataArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 * 
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class AuthLogDataQSImpl extends AbstractJdbcPersistableQueryService implements AuthLogDataQS {

	private static final Logger log = LogManager.getLogger(AuthLogDataQSImpl.class);
	private String qsCriteria;

	public String getServiceName() {

		return "LogData Query Service";
	}

	public void setServiceParams(Map arg0) throws ServiceException {

		// do nothing
	}

	public AuthLogData queryByPrimaryKey() throws QueryServiceException {

		throw new UnsupportedOperationException("Not supported yet.");
	}

	public AuthLogData queryByPrimaryKey(String id) throws QueryServiceException {

		List params = new LinkedList();
		params.add(id);
		StringBuffer cmd = new StringBuffer(
				"SELECT rowid as X , CARD_NO, AUTH_LOG_DATA.* FROM AUTH_LOG_DATA WHERE ROWID=? ");

		List<AuthLogData> list = super.query(cmd.toString(), params, LogDataBuilder);
		return list.size() > 0 ? list.get(0) : null;

	}

	public List<AuthLogData> queryByPrimaryKeys(List<String> ids) throws QueryServiceException {

		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List queryByExample(ProjPersistableArg arg) throws QueryServiceException {
		AuthLogDataArg obj = (AuthLogDataArg) arg;
		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate1())) {
			criteria.append("PURCHASE_DATE >=? AND ");
			params.add(obj.getPurchaseDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate2())) {
			criteria.append("PURCHASE_DATE <=? AND ");
			params.add(obj.getPurchaseDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}

		StringBuffer cmd = new StringBuffer("SELECT rowid as X , AUTH_LOG_DATA.* FROM AUTH_LOG_DATA ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));
		}
		cmd.append(" ORDER BY AUTH_LOG_DATA.PURCHASE_DATE DESC , AUTH_LOG_DATA.PURCHASE_TIME DESC ");
		// log.info("authLogData.Qs="+cmd.toString());
		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, LogDataBuilder, obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, LogDataBuilder);
	}

	public List<AuthLogData> queryByExampleByBank(AuthLogDataArg obj) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		StringBuffer cmd = new StringBuffer("SELECT * FROM ( ");
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate1())) {
			criteria.append("PURCHASE_DATE >=? AND ");
			params.add(obj.getPurchaseDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate2())) {
			criteria.append("PURCHASE_DATE <=? AND ");
			params.add(obj.getPurchaseDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}
		criteria.append("MOD_PROGRAM_ID <> 'MGOA800'  ");

		cmd.append("SELECT rowid as X ,CARD_NO, ");
		cmd.append(getSql());
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length()));
		}
		cmd.append("UNION ALL ");
		StringBuffer criteria2 = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria2.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria2.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria2.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate1())) {
			criteria2.append("PURCHASE_DATE >=? AND ");
			params.add(obj.getPurchaseDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getPurchaseDate2())) {
			criteria2.append("PURCHASE_DATE <=? AND ");
			params.add(obj.getPurchaseDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria2.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria2.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria2.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria2.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria2.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria2.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria2.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}
		cmd.append("SELECT rowid as X , CARD_NO, ");
		cmd.append(getSql());
		criteria2.append("MOD_PROGRAM_ID = 'MGOA800' AND TRANS_TYPE = 'IP' ");
		if (criteria2.length() > 0) {
			cmd.append("WHERE ").append(criteria2.substring(0, criteria2.length()));
		}
		cmd.append(") ");
		cmd.append(" ORDER BY PURCHASE_DATE DESC , PURCHASE_TIME DESC ");
		// log.info("authLogData.Qs="+cmd.toString());
		if (obj.getPagingInfo().isEnablePaging())
			return query(cmd.toString(), params, LogDataBuilder, obj.getPagingInfo());
		else
			return query(cmd.toString(), params, LogDataBuilder);
	}

	private String getSql() {

		StringBuffer cmd = new StringBuffer();
		cmd.append(
				"BANK_NO, MAJOR_CARD_ID, APPROVAL_NO, MERCHANT_NO, TERMINAL_NO, PURCHASE_DATE, PURCHASE_TIME, PURCHASE_AMT, ")
				.append("PROCESS_DATE, PROCESS_TIME, EXPIRE_DATE, TRANS_TYPE, CONDITION_CODE, IC_FLAG, AUTHORIZE_REASON, TERM_TRACE_NO, ACQUIRE_ID, ")
				.append("ACQUIRE_BK, CANCEL_APPROVAL_NO, COUNTRY_NAME, MCC_CODE, ENTRY_MODE, TWO_SCREEN_AP_TX, RETURN_TANDEM_CODE, RETURN_ISO_CODE, RETURN_HOST_CODE, ")
				.append("ORIGINATOR, RESPONDER, MESSAGE_TYPE, EDC_TX_TYPE, CVV2_CHECK_RESULT, MPAS_FLAG, MPAS_MEMBER, INSTALLMENT_FLAG, INST_INDICATOR, ")
				.append("LOYALTY_FLAG, LYLT_INDICATOR, LOYALTY_ORIGINAL_POINT,LOYALTY_REDEMPTION_POINT, LOYALTY_REDEMPTION_AMT, LOYALTY_PAID_CREDIT_AMT, ")
				.append("INSTALL_PERIOD_NUM, INSTALL_FIRST_PAYMENT, INSTALL_STAGES_PAYMENT,INSTALL_FORMALITY_FEE, MCC_AMT_LIMIT, CARD_CLASS,LIMIT_TYPE, NORMAL_LIMIT_AMT_MONTH, NORMAL_AMT_LAST_MONTH, ")
				.append("NORMAL_AMT_MONTH, CASH_LIMIT_AMT_MONTH, CASH_BILL_AMT,CASH_AMT_LAST_MONTH, CASH_AMT_MONTH, TOTAL_LIMIT_AMT_MONTH,AMT_CURRENT_MONTH, AMT_LAST_MONTH, ID_BILL_AMT, ")
				.append("MAX_LIMIT_AMT_MONTH, CASH_AMT_CURRENT_MONTH, ID_CASH_AMT_LAST_MONTH, ID_CASH_BILL_AMT, ID_CREDIT_LIMIT, DATA_SOURCE,FILE_DATE, MOD_PROGRAM_ID, UPDATE_ID,")
				.append("REMARK, BANK_NO_2, BUSINESS_TYPE,CARDHOLDER_ID, ACTION_CODE, ACTION_CODE_MEM,AUTH_LINK_TYPE, SERVICE_CODE, CRDH_ACTVT_TERM_IND,")
				.append("POS_CONDITION_CODE, CARD_TYPE, CARD_KIND,MANUAL_ENTRY_RESULT, MANUAL_FORCE_POST_FLAG, BIN_NO,PRE_AUTH_AMT, SEQ_NO,TEMP_CREDIT_LIMIT,SURCHG_AMT  FROM AUTH_LOG_DATA  ");
		return cmd.toString();
	}

	public List<AuthLogData> queryAll() throws QueryServiceException {

		StringBuffer cmd = new StringBuffer("SELECT rowid as X ,  CARD_NO,AUTH_LOG_DATA.* FROM AUTH_LOG_DATA ");
		return super.query(cmd.toString(), LogDataBuilder);
	}

	public String getTotalRecord(AuthLogDataArg obj) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}

		StringBuffer cmd = new StringBuffer("SELECT COUNT(*) as COUNTX FROM AUTH_LOG_DATA ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));
			this.setQsCriteria(criteria.substring(0, criteria.length() - 4));
		}
		// log.info(new Date()+"authLogData.QsCount="+cmd.toString());
		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {

			public Object processResultSet(ResultSet rs) throws SQLException {

				ResultSetTool rst = new ResultSetTool(rs);
				map.put("COUNTX", rst.getString("COUNTX"));
				return null;
			}

			public boolean isStopProcess() {

				return false;
			}
		});
		// log.info(new Date()+" ==>finish");
		return (String) map.get("COUNTX");
	}

	public String getTotalRecordByBank(AuthLogDataArg obj) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		StringBuffer cmd = new StringBuffer("SELECT COUNT(*) AS COUNTX FROM ( ");
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}
		criteria.append("MOD_PROGRAM_ID <> 'MGOA800'  ");
		cmd.append("SELECT * FROM AUTH_LOG_DATA ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length()));
		}
		cmd.append("UNION ALL ");
		StringBuffer criteria2 = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getBankNo())) {
			criteria2.append("BANK_NO=? AND ");
			params.add(obj.getBankNo());
		}
		if (ValidateUtil.isNotBlank(obj.getCardNo())) {
			criteria2.append(" CARD_NO =? AND ");
			params.add(obj.getCardNo());
		}
		if (ValidateUtil.isNotBlank(obj.getMerchantNo())) {
			criteria2.append("MERCHANT_NO=? AND ");
			params.add(obj.getMerchantNo());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate1())) {
			criteria2.append("PROCESS_DATE >=? AND ");
			params.add(obj.getProcessDate1());
		}
		if (ValidateUtil.isNotBlank(obj.getProcessDate2())) {
			criteria2.append("PROCESS_DATE <=? AND ");
			params.add(obj.getProcessDate2());
		}
		if (ValidateUtil.isNotBlank(obj.getUpdateId())) {
			criteria2.append("UPDATE_ID=? AND ");
			params.add(obj.getUpdateId());
		}
		if (ValidateUtil.isNotBlank(obj.getApprovalNo())) {
			criteria2.append("APPROVAL_NO=? AND ");
			params.add(obj.getApprovalNo());
		}
		if (ValidateUtil.isNotBlank(obj.getDataSource())) {
			criteria2.append("DATA_SOURCE=? AND ");
			params.add(obj.getDataSource());
		}
		if (ValidateUtil.isNotBlank(obj.getCardType())) {
			criteria2.append("CARD_TYPE=? AND ");
			params.add(obj.getCardType());
		}
		if (ValidateUtil.isNotBlank(obj.getCardKind())) {
			criteria2.append("CARD_KIND=? AND ");
			params.add(obj.getCardKind());
		}
		criteria2.append("MOD_PROGRAM_ID = 'MGOA800' AND TRANS_TYPE='IP'  ");
		cmd.append("SELECT * FROM AUTH_LOG_DATA ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria2.substring(0, criteria2.length()));
		}
		cmd.append(") ");
		// log.info(new Date()+"authLogData.QsCount="+cmd.toString());
		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {

			public Object processResultSet(ResultSet rs) throws SQLException {

				ResultSetTool rst = new ResultSetTool(rs);
				map.put("COUNTX", rst.getString("COUNTX"));
				return null;
			}

			public boolean isStopProcess() {

				return false;
			}
		});
		log.info((String) map.get("COUNTX"));
		return (String) map.get("COUNTX");
	}

	public boolean isExist(String cardNo, String merchantNo, String approveNo, String purchaseDate,
			LongString purchaseAmt) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();

		criteria.append(" CARD_NO =? AND ");
		params.add(cardNo);

		criteria.append("MERCHANT_NO=? AND ");
		params.add(merchantNo);
		criteria.append("APPROVAL_NO=? AND ");
		params.add(approveNo);
		criteria.append("PURCHASE_DATE=? AND ");
		params.add(purchaseDate);
		criteria.append("PURCHASE_AMT=? AND ");
		params.add(purchaseAmt.toString());

		StringBuffer cmd = new StringBuffer("SELECT COUNT(*) as COUNTX FROM AUTH_LOG_DATA ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));
			this.setQsCriteria(criteria.substring(0, criteria.length() - 4));
		}
		// log.info("cardNo=["+cardNo+"]");
		// log.info("merchantNo=["+merchantNo+"]");
		// log.info("approveNo=["+approveNo+"]");
		// log.info("purchaseDate=["+purchaseDate+"]");
		// log.info("purchaseAmt=["+purchaseAmt+"]");
		// log.info("authLogData.QsCount="+cmd.toString());
		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {

			public Object processResultSet(ResultSet rs) throws SQLException {

				ResultSetTool rst = new ResultSetTool(rs);
				map.put("COUNTX", rst.getString("COUNTX"));
				return null;
			}

			public boolean isStopProcess() {

				return false;
			}
		});
		String r = (String) map.get("COUNTX");
		try {
			if (Integer.parseInt(r) > 0)
				return true;
			else
				return false;
		} catch (Exception gg) {
			return false;
		}
	}

	public String getQsCriteria() {

		return qsCriteria;
	}

	public void setQsCriteria(String qsCriteria) {

		this.qsCriteria = qsCriteria;
	}

	/**
	 * M2014070
	 * 
	 * @param arg
	 * @return
	 * @throws QueryServiceException
	 */
	public String getAuthAccessLogTotalRecord(AuthAccessLogArg arg) throws QueryServiceException {

		List<String> params = new LinkedList<String>();

		String updateDept = arg.getUpdateDept();
		String orgId = arg.getOrgId();
		String teamId = arg.getTeamId();
		String processDate1 = arg.getOperateDate1();
		String processDate2 = arg.getOperateDate2();

		if (processDate2 == null || processDate2.trim().length() <= 0)
			processDate2 = processDate1;
		if (processDate1 == null || processDate1.trim().length() <= 0)
			throw new QueryServiceException("Criteria procDate cannot be NULL!");

		StringBuffer criteria = new StringBuffer();

		if (ValidateUtil.isNotBlank(updateDept)) {

			criteria.append("UPDATE_DEPT = ? AND ");
			params.add(updateDept);
		}
		if (ValidateUtil.isNotBlank(orgId)) {

			criteria.append("ORG_ID = ? AND ");
			params.add(orgId);
		}
		if (ValidateUtil.isNotBlank(teamId)) {

			criteria.append("TEAM_ID = ? AND ");
			params.add(teamId);
		}

		criteria.append(" PROCESS_DATE BETWEEN ? AND ? ");
		params.add(processDate1);
		params.add(processDate2);

		StringBuffer cmd = new StringBuffer("SELECT COUNT(*) as COUNTX FROM AUTH_ACCESS_LOG WHERE ");
		cmd.append(criteria);
		log.info("" + cmd.toString() + ">>>>" + orgId + "," + updateDept + "," + teamId + "," + processDate1
				+ "," + processDate2);
		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {

			public Object processResultSet(ResultSet rs) throws SQLException {

				ResultSetTool rst = new ResultSetTool(rs);
				map.put("COUNTX", rst.getString("COUNTX"));
				return null;
			}

			public boolean isStopProcess() {

				return false;
			}
		});
		return (String) map.get("COUNTX");
	}

	/**
	 * M2014070
	 * 
	 * @param arg
	 * @return
	 * @throws QueryServiceException
	 */
	public List queryAuthAccessLog(AuthAccessLogArg arg) throws QueryServiceException {

		List<String> params = new LinkedList<String>();

		String updateDept = arg.getUpdateDept();
		String orgId = arg.getOrgId();
		String teamId = arg.getTeamId();
		String processDate1 = arg.getOperateDate1();
		String processDate2 = arg.getOperateDate2();

		if (processDate2 == null || processDate2.trim().length() <= 0)
			processDate2 = processDate1;
		if (processDate1 == null || processDate1.trim().length() <= 0)
			throw new QueryServiceException("Criteria procDate cannot be NULL!");

		StringBuffer criteria = new StringBuffer();

		if (ValidateUtil.isNotBlank(updateDept)) {

			criteria.append("UPDATE_DEPT = ? AND ");
			params.add(updateDept);
		}
		if (ValidateUtil.isNotBlank(orgId)) {

			criteria.append("ORG_ID = ? AND ");
			params.add(orgId);
		}
		if (ValidateUtil.isNotBlank(teamId)) {

			criteria.append("TEAM_ID = ? AND ");
			params.add(teamId);
		}

		criteria.append(" PROCESS_DATE BETWEEN ? AND ? ");
		params.add(processDate1);
		params.add(processDate2);

		StringBuffer cmd = new StringBuffer("SELECT * FROM AUTH_ACCESS_LOG  WHERE ");
		cmd.append(criteria);
		cmd.append(" order by PROCESS_DATE,BANK_NO");

		AbstractJdbcPersistableBuilder authAccessLogBuilder = new AbstractJdbcPersistableBuilder() {

			@Override
			protected JdbcPersistable build(ResultSet rs) throws SQLException {

				ResultSetTool rst = new ResultSetTool(rs);
				AuthAccessLog obj = new AuthAccessLog();
				obj.setBankNo(rst.getString("BANK_NO"));
				obj.setCardData(rst.getString("CARD_DATA"));
				obj.setUpdateDept(rst.getString("UPDATE_DEPT"));
				obj.setUpdateId(rst.getString("UPDATE_ID"));
				obj.setModProgramId(rst.getString("MOD_PROGRAM_ID"));
				obj.setProcessDate(rst.getString("PROCESS_DATE"));
				obj.setProcessTime(rst.getString("PROCESS_TIME"));
				return obj;
			}
		};

		return super.query(cmd.toString(), params, authAccessLogBuilder, arg.getPagingInfo());
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

}
