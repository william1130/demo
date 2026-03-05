package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edstw.lang.DateString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetCallback;
import com.edstw.sql.ResultSetTool;
import com.edstw.util.DateUtil;
import com.edstw.util.NotImplementedException;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.ProjPersistable;
import proj.nccc.logsearch.vo.MerchantVO;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author Stephen Lin
 * @version $Revision: 1.3 $
 */
public class MerchantQSImpl extends AbstractJdbcPersistableQueryService implements MerchantQS {
	private static final Logger log = LogManager.getLogger(MerchantQSImpl.class);
	private String contractDatebyU;
	private String contractDatebyA;

	/** Creates a new instance of MerchantQSImpl */
	public MerchantQSImpl() {
	}

	public String getServiceName() {
		return "MerchantQSImpl Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public List queryByPrimaryKeys(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	public List queryAll() throws QueryServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public MerchantVO queryMerchantInfo(String mchtNo) throws QueryServiceException {

		MerchantVO storeVo = queryMerchantBase(mchtNo);

		// to query extra data
		if (storeVo != null && storeVo.getMchtNo() != null) {
			// bizType003Count
			// bizType002Count
			String s002 = getBizTypeCount(mchtNo, "002");
			String s003 = getBizTypeCount(mchtNo, "003");
			try {
				Integer.parseInt(s002);
				storeVo.setBizType002Count("" + Integer.parseInt(s002));
			} catch (Exception x) {
				storeVo.setBizType002Count("0");
			}
			try {
				Integer.parseInt(s003);
				storeVo.setBizType003Count("" + Integer.parseInt(s003));
			} catch (Exception x) {
				storeVo.setBizType003Count("0");
			}
			// mchtType
			storeVo.setMchtType(getMchtType(mchtNo));
			storeVo.setContractDateByA(this.getContractDatebyA());
			storeVo.setContractDateByU(this.getContractDatebyU());

			return storeVo;
		} else
			return null;
	}

	public MerchantVO queryMerchantBase(String mchtNo, String cardType) throws QueryServiceException {
		final MerchantVO merchantVo = queryMerchantBase(mchtNo);
		if (cardType != null && merchantVo != null) {
			String today = MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD);
			List<String> params = new LinkedList<String>();
			params.add(mchtNo);
			params.add("C");
			params.add(cardType);
			params.add(today);
			params.add(today);
			StringBuffer cmd = new StringBuffer();
			cmd.append("SELECT MERCHANT_NO,CONTRACT_DATE, ");
			cmd.append("              CONTRACT_END_DATE,TRUST_CLASS ");
			cmd.append("FROM MERCHANT_CARD_DATA ");
			cmd.append("WHERE MERCHANT_NO = ? ");
			cmd.append("AND CARD_KIND = ? ");
			cmd.append("AND CARD_TYPE = ? ");
			cmd.append("AND CONTRACT_DATE <= ? ");
			cmd.append("AND ( CONTRACT_END_DATE > ? OR  CONTRACT_END_DATE is null )");
			super.query(cmd.toString(), params, new ResultSetCallback() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					ResultSetTool rst = new ResultSetTool(rs);
					merchantVo.setContractDate(rst.getString("CONTRACT_DATE"));
					merchantVo.setContractEndDate(rst.getString("CONTRACT_END_DATE"));
					merchantVo.setTrustClass(rst.getString("TRUST_CLASS"));
					return null;
				}

				public boolean isStopProcess() {
					return false;
				}
			});
			merchantVo.setContractType(querySignedCards(mchtNo));
		}
		return merchantVo;

	}

	public MerchantVO queryMerchantBase(String mchtNo) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		params.add(mchtNo);
		params.add("03");

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT A.MCC_CODE,A.INDUSTRY_CODE,A.CURRENT_CODE,A.TEL_NO_1,A.CLASS,A.EDC_FLAG,");
		cmd.append("A.TRUST_FILE_DATE,A.FILE_DATE,B.CHIN_NAME,B.ZIP_CODE_3,             ");
		cmd.append("B.ZIP_CODE_2,B.CHIN_ZIP_CITY,B.CHIN_ADDR,A.BELONG_BANK,'0' as IMPRINTER_CNT, ");
		cmd.append("A.IC_FLAG,A.POS_FLAG, ");
		cmd.append("A.SPECIAL_TREATY_LIMIT, ");
		cmd.append("A.ENG_CITY_NAME,   ");
		cmd.append("A.MERCHANT_NO,   ");
		cmd.append("'' as INSTALLMENT_ONLY  ");
		cmd.append("FROM MERCHANT_DATA A, MERCHANT_CHINESE_DATA B                     ");
		cmd.append("WHERE A.MERCHANT_NO = B.MERCHANT_NO                                 ");
		cmd.append("AND A.MERCHANT_NO = ?                                               ");
		cmd.append("AND B.CARD_CODE = ?                                              ");
		final MerchantVO merchantVo = new MerchantVO();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				merchantVo.setMchtNo(rst.getString("MERCHANT_NO"));
				merchantVo.setMchtName(rst.getString("CHIN_NAME"));
				merchantVo.setEngCityName(rst.getString("ENG_CITY_NAME"));
				merchantVo.setIndustryCode(rst.getString("INDUSTRY_CODE"));
				merchantVo.setCurrentCode(rst.getString("CURRENT_CODE"));
				merchantVo.setTelNo1(rst.getString("TEL_NO_1"));
				merchantVo.setArticleClass(rst.getString("CLASS"));
				merchantVo.setEdcFlag(rst.getString("EDC_FLAG"));
				merchantVo.setTrustFileDate(rst.getString("TRUST_FILE_DATE"));
				merchantVo.setFileDate(rst.getString("FILE_DATE"));
				merchantVo.setZipCode3(rst.getString("ZIP_CODE_3"));
				merchantVo.setZipCode2(rst.getString("ZIP_CODE_2"));
				merchantVo.setChinZipCity(rst.getString("CHIN_ZIP_CITY"));
				merchantVo.setChinAddr(rst.getString("CHIN_ADDR"));
				merchantVo.setBelongBank(rst.getString("BELONG_BANK"));
				merchantVo.setImprinterCnt(rst.getString("IMPRINTER_CNT"));
				merchantVo.setIcFlag(rst.getString("IC_FLAG"));
				merchantVo.setPosFlag(rst.getString("POS_FLAG"));
				merchantVo.setMccCode(rst.getString("MCC_CODE"));
				merchantVo.setSepcialTreatyLimit(rst.getLongString("SPECIAL_TREATY_LIMIT"));
				merchantVo.setInstallmentOnly(rst.getString("INSTALLMENT_ONLY"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (merchantVo != null && merchantVo.getMchtNo() != null)
			return merchantVo;
		else
			return null;
	}

	public String merchantEngName(String mchtNo) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		params.add(mchtNo);
		params.add("06");

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT CHIN_NAME FROM MERCHANT_CHINESE_DATA ");
		cmd.append("WHERE MERCHANT_NO = ? ");
		cmd.append("AND CARD_CODE = ?   ");
		List list = super.queryValueList(cmd.toString(), params, "CHIN_NAME");
		if (list != null && list.size() > 0) {
			return (String) list.get(0);
		} else
			return null;
	}

	public MerchantVO queryQrMerchantBase(String mchtNo) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		params.add(mchtNo);
		params.add("03");

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT A.MCC_CODE,A.INDUSTRY_CODE,A.CURRENT_CODE,A.TEL_NO_1,A.CLASS,A.EDC_FLAG,");
		cmd.append("A.TRUST_FILE_DATE,A.FILE_DATE,B.CHIN_NAME,B.ZIP_CODE_3,             ");
		cmd.append("B.ZIP_CODE_2,B.CHIN_ZIP_CITY,B.CHIN_ADDR,A.BELONG_BANK,'0' as IMPRINTER_CNT, ");
		cmd.append("A.IC_FLAG,A.POS_FLAG, ");
		cmd.append("A.SPECIAL_TREATY_LIMIT, ");
		cmd.append("A.ENG_CITY_NAME,   ");
		cmd.append("A.MERCHANT_NO,   ");
		cmd.append("'' as INSTALLMENT_ONLY  ");
		cmd.append("FROM MERCHANT_DATA A, MERCHANT_CHINESE_DATA B                     ");
		cmd.append("WHERE A.MERCHANT_NO = B.MERCHANT_NO                                 ");
		cmd.append(
				"AND A.MERCHANT_NO =  (select MERCHANT_NO from merchant_card_data where merchant_pan = ? AND ROWNUM =1)");
		cmd.append("AND B.CARD_CODE = ?                                              ");
		log.info("cmd====" + cmd + "====");
		final MerchantVO merchantVo = new MerchantVO();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				merchantVo.setMchtNo(rst.getString("MERCHANT_NO"));
				merchantVo.setMchtName(rst.getString("CHIN_NAME"));
				merchantVo.setEngCityName(rst.getString("ENG_CITY_NAME"));
				merchantVo.setIndustryCode(rst.getString("INDUSTRY_CODE"));
				merchantVo.setCurrentCode(rst.getString("CURRENT_CODE"));
				merchantVo.setTelNo1(rst.getString("TEL_NO_1"));
				merchantVo.setArticleClass(rst.getString("CLASS"));
				merchantVo.setEdcFlag(rst.getString("EDC_FLAG"));
				merchantVo.setTrustFileDate(rst.getString("TRUST_FILE_DATE"));
				merchantVo.setFileDate(rst.getString("FILE_DATE"));
				merchantVo.setZipCode3(rst.getString("ZIP_CODE_3"));
				merchantVo.setZipCode2(rst.getString("ZIP_CODE_2"));
				merchantVo.setChinZipCity(rst.getString("CHIN_ZIP_CITY"));
				merchantVo.setChinAddr(rst.getString("CHIN_ADDR"));
				merchantVo.setBelongBank(rst.getString("BELONG_BANK"));
				merchantVo.setImprinterCnt(rst.getString("IMPRINTER_CNT"));
				merchantVo.setIcFlag(rst.getString("IC_FLAG"));
				merchantVo.setPosFlag(rst.getString("POS_FLAG"));
				merchantVo.setMccCode(rst.getString("MCC_CODE"));
				merchantVo.setSepcialTreatyLimit(rst.getLongString("SPECIAL_TREATY_LIMIT"));
				merchantVo.setInstallmentOnly(rst.getString("INSTALLMENT_ONLY"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (merchantVo != null && merchantVo.getMchtNo() != null)
			return merchantVo;
		else
			return null;
	}

	public MerchantVO queryMerchantCardDataBase(String mchtNo, String cardType, String SystemYYYYmmdd)
			throws QueryServiceException {
		List<String> params = new LinkedList<String>();
		params.add(mchtNo);
		params.add("C");
		params.add(cardType);
		params.add(SystemYYYYmmdd);
		params.add(SystemYYYYmmdd);

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT MERCHANT_NO,CONTRACT_DATE, ");
		cmd.append("              CONTRACT_END_DATE,TRUST_CLASS ");
		cmd.append("FROM MERCHANT_CARD_DATA ");
		cmd.append("WHERE MERCHANT_NO = ? ");
		cmd.append("AND CARD_KIND = ? ");
		cmd.append("AND CARD_TYPE = ? ");
		cmd.append("AND CONTRACT_DATE <= ? ");
		cmd.append("AND ( CONTRACT_END_DATE > ? OR CONTRACT_END_DATE is null )");
		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				map.put(rst.getString("MERCHANT_NO"), rst.getString("MERCHANT_NO"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (map.size() != 0) {
			MerchantVO merchantVo = this.queryMerchantBase(mchtNo);
			if (merchantVo != null && merchantVo.getMchtNo() != null)
				return merchantVo;
		}
		return null;
	}

	public String querySignedCards(String mchtNo) throws QueryServiceException {
		List<String> params = new LinkedList<String>();
		params.add(mchtNo);
		params.add(DateUtil.getInstance().getSysDateString().toString());
		params.add(DateUtil.getInstance().getSysDateString().toString());

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT CARD_TYPE  ");
		cmd.append("FROM MERCHANT_CARD_DATA ");
		cmd.append("WHERE MERCHANT_NO = ? ");
		cmd.append("AND CONTRACT_DATE <= ?");
		cmd.append("AND ( CONTRACT_END_DATE > ? OR CONTRACT_END_DATE is null )");
		cmd.append("ORDER BY CARD_TYPE DESC ");
		final List<String> cardTypes = new LinkedList<String>();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				cardTypes.add(rst.getString("CARD_TYPE"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (cardTypes.size() != 0) {
			StringBuffer cardTypeString = new StringBuffer();
			for (Iterator<String> iter = cardTypes.iterator(); iter.hasNext();) {
				cardTypeString.append(iter.next()).append(", ");
			}
			return cardTypeString.toString().substring(0, cardTypeString.length() - 2);
		} else
			return null;
	}

	public String getMchtType(String mchtNo) throws QueryServiceException {
		String sMchtType = null;
		List params = new LinkedList();

		params.add(mchtNo);
		params.add(DateUtil.getInstance().getSysDateString().toString());
		params.add(DateUtil.getInstance().getSysDateString().toString());
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT CONTRACT_DATE,CARD_TYPE ");
		cmd.append(" FROM MERCHANT_CARD_DATA");
		cmd.append("    WHERE MERCHANT_NO = ? ");
		cmd.append(" AND CARD_KIND = 'C'");
		cmd.append(" AND ( CARD_TYPE =  'U' OR CARD_TYPE =  'A') ");
		cmd.append(" AND CONTRACT_DATE <= ? ");
		cmd.append(" AND ( CONTRACT_END_DATE > ? OR CONTRACT_END_DATE is null ) ");
		// log.info("getMchtType="+cmd.toString());

		final Map map = new HashMap();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				map.put(rst.getString("CARD_TYPE"), rst.getString("CONTRACT_DATE"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (map.size() != 0) {
			// AEonly(AEONLY)：有簽AE卡，但沒有簽U卡
			// 四卡加簽(FOURADD)：U卡簽約日 > AE卡簽約日
			// AE加簽(AEADD)：U卡簽約日 < AE卡簽約日
			// 四卡(FOUR)：只有簽U卡
			// 五卡(FIVE)：U卡簽約日 = AE卡簽約日
			if (map.get("U") != null)
				this.setContractDatebyU((String) map.get("U"));
			if (map.get("A") != null)
				this.setContractDatebyA((String) map.get("A"));

			String sDateU = (String) map.get("U");
			String sDateA = (String) map.get("A");
			if (map.get("U") != null && map.get("A") != null) {
				// U卡簽約日 > AE卡簽約日
				if (sDateU.compareTo(sDateA) > 0)
					sMchtType = "FOURADD";
				else if (sDateU.compareTo(sDateA) < 0)
					sMchtType = "AEADD";
				else if (sDateU.compareTo(sDateA) == 0)
					sMchtType = "FIVE";
			} else if (map.get("A") != null && map.get("U") == null)
				sMchtType = "AEONLY";
			else if (map.get("A") == null && map.get("U") != null)
				sMchtType = "FOUR";
		}
		return sMchtType;
	}

	public String queryMerchantName(String mchtNo) throws QueryServiceException {
		MerchantVO vo = queryMerchantBase(mchtNo);
		if (vo != null)
			return vo.getMchtName();
		else
			return null;
	}

	public String getBizTypeCount(String mchtNo, String sType) throws QueryServiceException {
		List params = new LinkedList();
		// log.info("Data Source ="+this.getDataSourceName());
		// 紅利扣抵–BUSINESS_TYPE = ‘003’
		// 分期付款–BUSINESS_TYPE = ‘002’
		params.add(mchtNo);
		params.add(new DateString(new Date()).toString());
		params.add(sType);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) as COUNTX ");
		cmd.append("FROM MERCHANT_BUSINESS_DATA ");
		cmd.append("WHERE MERCHANT_NO =? ");
		cmd.append("AND BUSINESS_TYPE = ? ");
		cmd.append("AND START_DATE <= ? ");
		cmd.append("AND END_DATE IS NULL");
		// log.info("getBizTypeCount="+cmd.toString());
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

	public String getMchtManager(String mchtNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);

		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT INFO_NAME ");
		cmd.append(" FROM MERCHANT_INFO_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND INFO_CODE = '01' ");

		log.info("getMchtManager=" + cmd.toString());
		List list = super.queryValueList(cmd.toString(), params, "INFO_NAME");
		if (list != null && list.size() > 0) {
			return (String) list.get(0);
		} else
			return null;
	}

	public String getContractDatebyA() {
		return contractDatebyA;
	}

	public void setContractDatebyA(String contractDatebyA) {
		this.contractDatebyA = contractDatebyA;
	}

	public String getContractDatebyU() {
		return contractDatebyU;
	}

	public void setContractDatebyU(String contractDatebyU) {
		this.contractDatebyU = contractDatebyU;
	}

	public ProjPersistable queryByPrimaryKey(Object id) throws QueryServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMerchantBusinessDataExitsLoyaltyBy(String mchtNo, String businessType, String systemDate)
			throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);
		params.add(businessType);
		params.add(systemDate);
		params.add(systemDate);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) AS cnt");
		cmd.append(" FROM MERCHANT_BUSINESS_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND BUSINESS_TYPE = ? ");
		cmd.append(" AND START_DATE <= ? ");
		cmd.append(" AND ( END_DATE > ?  OR END_DATE is null )");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) == 1)
				return true;
		}
		return false;

	}

	public boolean isMerchantBusinessDataExitsInstallmentBy(String mchtNo, String businessType, String systemDate)
			throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);
		params.add(businessType);
		params.add(systemDate);
		params.add(systemDate);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) AS cnt");
		cmd.append(" FROM MERCHANT_BUSINESS_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND BUSINESS_TYPE = ? ");
		cmd.append(" AND START_DATE <= ? ");
		cmd.append(" AND ( END_DATE > ?  OR END_DATE is null ) ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) == 1)
				return true;
		}
		return false;

	}

	public boolean isFace2FaceTx(String mchtNo) throws QueryServiceException {
		List params = new LinkedList();
		boolean bRet = false;
		if (mchtNo != null && mchtNo.length() >= 5) {
			String s = mchtNo.substring(2, 5);
			// log.info("mchtNo="+mchtNo+","+mchtNo.substring(2, 5));
			params.add(s);
			StringBuffer cmd = new StringBuffer();
			cmd.append("SELECT COUNT(*) AS cnt");
			cmd.append(" FROM AUTH_SYSTEM_PARM ");
//	        cmd.append(" WHERE PARM_TYPE ='NON_FACE' ");   -- 20090121 BUG修正
			cmd.append(" WHERE PARM_TYPE ='non_face' ");
			cmd.append(" AND PARM_CODE = ? ");
			List list = super.queryValueList(cmd.toString(), params, "cnt");
			if (list != null) {
				// log.info("isFace2FaceTx = "+list.get(0));
				if (Integer.parseInt(list.get(0).toString()) >= 1)
					bRet = true;
			}
		}
		// log.info("bRet = "+bRet);
		return bRet;
	}

	public boolean isSuitableCardForMcht(String mchtNo, String cardType) throws QueryServiceException {
		List params = new LinkedList();
		boolean bRet = false;
		params.add(mchtNo);
		params.add(cardType);
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());

		StringBuffer cmd = new StringBuffer();

		cmd.append("SELECT COUNT(*) AS cnt ");
		cmd.append(" FROM MERCHANT_CARD_DATA");
		cmd.append("    WHERE MERCHANT_NO = ? ");
		cmd.append(" AND CARD_TYPE = ?");
		cmd.append(
				" AND (( CARD_TYPE='T' and (   ( nvl(nt_flag,'N')='Y' AND CONTRACT_DATE <= ?)  or(nvl(nt_flag,'N')='N' AND nvl(u_trans_date,'29991231') <= ?)))");
		cmd.append(" OR  ( CARD_TYPE<>'T' AND CONTRACT_DATE <= ? ");
		cmd.append(" AND ( CONTRACT_END_DATE > ? OR CONTRACT_END_DATE is null )))");
		log.info("merchant_command = " + cmd);
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			// log.info("isFace2FaceTx = "+list.get(0));
			if (Integer.parseInt(list.get(0).toString()) >= 1)
				bRet = true;
		}
		return bRet;
	}

	public boolean isAcEcMcht(String mchtNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);
		params.add("016");
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) AS cnt");
		cmd.append(" FROM MERCHANT_BUSINESS_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND BUSINESS_TYPE = ? ");
		cmd.append(" AND START_DATE <= ? ");
		cmd.append(" AND ( END_DATE > ?  OR END_DATE is null ) ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) == 1)
				return true;
		}
		return false;
	}

	// 特店是否可以集中授權交易
	public boolean isAuthByCentralize(String mchtNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);
		params.add("013");
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) AS cnt");
		cmd.append(" FROM MERCHANT_BUSINESS_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND BUSINESS_TYPE = ? ");
		cmd.append(" AND START_DATE <= ? ");
		cmd.append(" AND ( END_DATE > ?  OR END_DATE is null ) ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) == 1)
				return true;
		}
		return false;
	}

	// 特店是否為定期繳款特店
	public boolean isRecurring(String mchtNo) throws QueryServiceException {
		List params = new LinkedList();
		params.add(mchtNo);
		params.add("015");
		params.add(new DateString(new Date()).toString());
		params.add(new DateString(new Date()).toString());
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT COUNT(*) AS cnt");
		cmd.append(" FROM MERCHANT_BUSINESS_DATA");
		cmd.append(" WHERE MERCHANT_NO =? ");
		cmd.append(" AND BUSINESS_TYPE = ? ");
		cmd.append(" AND START_DATE <= ? ");
		cmd.append(" AND ( END_DATE > ?  OR END_DATE is null ) ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) == 1)
				return true;
		}
		return false;
	}

	// 特店是否為銀聯卡特店
	public boolean isCupMcht(String mchtNo) {
		String signedCard = querySignedCards(mchtNo);
		if (signedCard == null || signedCard.indexOf("C") < 0)
			return false;
		return true;
	}

	// 特店是否為DFS特店
	public boolean isDfsMcht(String mchtNo) {
		String signedCard = querySignedCards(mchtNo);
		if (signedCard == null || signedCard.indexOf("D") < 0)
			return false;
		return true;
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByExample(ProjPersistableArg example) throws QueryServiceException {
		throw new NotImplementedException();
	}

}
