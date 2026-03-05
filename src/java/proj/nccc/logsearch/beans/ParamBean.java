package proj.nccc.logsearch.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.edstw.bean.BeanException;
import com.edstw.bean.CacheableBean;
import com.edstw.process.ProcessException;
import com.edstw.util.LabelValueBean;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvCardType;
import proj.nccc.logsearch.persist.Parameter;
import proj.nccc.logsearch.qs.AtsResponseCodeSetupQSImpl;
import proj.nccc.logsearch.qs.AtsTransTypeQSImpl;
import proj.nccc.logsearch.qs.EmvCardTypeQSImpl;
import proj.nccc.logsearch.qs.EmvCardTypeSetupQSImpl;
import proj.nccc.logsearch.qs.IFESTransTypeMappingQSImpl;
import proj.nccc.logsearch.qs.LDSSTransTypeMappingQSImpl;
import proj.nccc.logsearch.qs.NssmbankQSImpl;
import proj.nccc.logsearch.qs.ParameterQSImpl;
import proj.nccc.logsearch.qs.SpswReasonCodeQSImpl;
import proj.nccc.logsearch.qs.SpswResponseCodeSetupQSImpl;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParamBean implements CacheableBean {

	private static final ParamBean instance = new ParamBean();
	// 機構
	private Map<String, String> bankNameMap;
	// 卡別
	private Map<String, String> cardNameMap;
	// 交易類別
	private Map<String, String> transTypeNameMap;
	// 回應碼
	private Map<String, String> responseCodeNameMap;
	// ISO EMV Chip Data
	private Map<String, String> emvChipDataNameMap;
	// ISO TABLE ID
	private Map<String, String> tableDataNameMap;
	// reason code
	private Map<String, String> logReasonCodeNameMap;
	// LDSS 主機
	private List ldssAccordOptions = new LinkedList();
	// LDSS交易類別
	private List ldssTransTypeNameList = new LinkedList();
	// LDSS交易類別Map
	private Map<String, String> ldssTransTypeNameMap;

	// IFES交易類別
	private Map<String, String> ifesTransTypeNameMap;
	private List ifesTransTypeNameList = new LinkedList();

	// resp code
	private Map<String, String> logRespCodeNameMap;

	/** 授權來源 */
	private List<LabelValueBean> authorizeOptions = new LinkedList<LabelValueBean>();
	private Map<String, String> authorizeNameMap = new HashMap<String, String>();

	private List<?> emvCardTypeList;
	private List emvCardTypeOptions = new LinkedList();
	private Map<String, String> emvCardTypeNameMap;

	private Map<String, String> sortMap = new HashedMap();
	// e-Wallet, M2022086_電子錢包NameMap
	private Map<String, String> eWalletNameMap;
	
	private String chesgUri;

	public ParamBean() {

		cardNameMap = new HashMap<String, String>();
		emvChipDataNameMap = new HashMap<String, String>();
		tableDataNameMap = new HashMap<String, String>();
		emvCardTypeList = new ArrayList<List<?>>();
		emvCardTypeOptions = new LinkedList();
		emvCardTypeNameMap = new HashMap<String, String>();
		ldssAccordOptions = new LinkedList();
		ldssTransTypeNameMap = new HashMap<String, String>();
		ldssTransTypeNameList = new LinkedList();
		ifesTransTypeNameMap = new HashMap<String, String>();
		ifesTransTypeNameList = new LinkedList();
		sortMap = new HashMap<String, String>();
		eWalletNameMap = new HashMap<String, String>();
	}

	public static ParamBean getInstance() {
		return instance;
	}

	public void init() throws BeanException {

		refresh();
	}

	public void refresh() throws BeanException {
		
		// LDSS 主機
		ldssAccordOptions.clear();
		ldssAccordOptions.add(new LabelValueBean("全部", "all"));
		ldssAccordOptions.add(new LabelValueBean("LDSS1", "LDSS1"));
		ldssAccordOptions.add(new LabelValueBean("LDSS2", "LDSS2"));
		ldssAccordOptions.add(new LabelValueBean("LDSS3", "LDSS3"));
		ldssAccordOptions.add(new LabelValueBean("LDSS4", "LDSS4"));

		// LDSS Trans Name
		LDSSTransTypeMappingQSImpl proc = (LDSSTransTypeMappingQSImpl) ProjServices.getLDSSTransTypeMappingQS();
		ldssTransTypeNameMap.clear();
		ldssTransTypeNameList.clear();
		Map<String, String> transName = proc.queryTransNameMap("", "all");
		ldssTransTypeNameMap = transName;
		if (transName != null) {
			ldssTransTypeNameList.add(new LabelValueBean("全部", "all"));
			for (Map.Entry<String, String> entry : transName.entrySet()) {
				ldssTransTypeNameList.add(new LabelValueBean(entry.getValue(), entry.getKey()));
			}
		}

		// IFES Trans Name
		IFESTransTypeMappingQSImpl ifesQS = (IFESTransTypeMappingQSImpl) ProjServices.getIFESTransTypeMappingQS();
		ifesTransTypeNameMap.clear();
		ifesTransTypeNameList.clear();
		ifesTransTypeNameMap = ifesQS.queryTransNameMap("all");

		if (ifesTransTypeNameMap != null) {
			ifesTransTypeNameList.add(new LabelValueBean("全部", "all"));
			for (Map.Entry<String, String> entry : ifesTransTypeNameMap.entrySet()) {
				ifesTransTypeNameList.add(new LabelValueBean(entry.getValue(), entry.getKey()));
			}
		}

		// 機構
		NssmbankQSImpl nqs = (NssmbankQSImpl) ProjServices.getNssmbankQS();

		try {
			bankNameMap = nqs.queryNameMap();
			bankNameMap.put("", "國外卡");
		} catch (Exception e) {

			bankNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		// 交易類別
		AtsTransTypeQSImpl attqs = (AtsTransTypeQSImpl) ProjServices.getAtsTransTypeQS();

		try {
			transTypeNameMap = attqs.queryNameMap();
			transTypeNameMap.put("TRUST001", "信託交易");
			transTypeNameMap.put("TRUST031", "信託交易取消");
			transTypeNameMap.put("TRUST401", "信託交易沖銷");
			transTypeNameMap.put("TRUST431", "信託交易沖銷取消");
		} catch (Exception e) {
			transTypeNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		// 回應碼
		AtsResponseCodeSetupQSImpl arcsqs = (AtsResponseCodeSetupQSImpl) ProjServices.getAtsResponseCodeSetupQS();

		try {
			responseCodeNameMap = arcsqs.queryNameMap();
		} catch (Exception e) {
			responseCodeNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		// ReasonCode
		SpswReasonCodeQSImpl rcodeqs = (SpswReasonCodeQSImpl) ProjServices.getSpswReasonCodeQS();

		try {
			logReasonCodeNameMap = rcodeqs.queryNameMap();
		} catch (Exception e) {

			logReasonCodeNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		// RespCode
		SpswResponseCodeSetupQSImpl respCodeqs = (SpswResponseCodeSetupQSImpl) ProjServices
				.getSpswResponseCodeSetupQS();

		try {
			logRespCodeNameMap = respCodeqs.queryNameMap();
		} catch (Exception e) {

			logRespCodeNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		// EmvCardType
		EmvCardTypeQSImpl emvCardType = (EmvCardTypeQSImpl) ProjServices.getEmvCardTypeQS();

		try {
			emvCardTypeOptions.clear();
			emvCardTypeList = emvCardType.queryAll();
			for (Iterator itr = emvCardTypeList.iterator(); itr.hasNext();) {
				EmvCardType emvCardTypeTemp = (EmvCardType) itr.next();
				emvCardTypeOptions
						.add(new LabelValueBean(emvCardTypeTemp.getCardType(), emvCardTypeTemp.getCardType()));
			}
		} catch (Exception e) {
			emvCardTypeList = new ArrayList<EmvCardType>();
			e.printStackTrace();
			throw new BeanException(e);
		}

		EmvCardTypeSetupQSImpl emvCardTypeSetup = (EmvCardTypeSetupQSImpl) ProjServices.getEmvCardTypeSetupQS();

		try {
			emvCardTypeNameMap = emvCardTypeSetup.queryNameMap();
		} catch (Exception e) {

			emvCardTypeNameMap = new HashMap<String, String>();
			e.printStackTrace();
			throw new BeanException(e);
		}

//		M2022086 改由DB讀取, SYS_PARA.LOOKUP_TYPE(CARD_TYPE)
		// EMV Chip Data
		emvChipDataNameMap.clear();
		emvChipDataNameMap.put("5F2A", "Transaction Currency Code");
		emvChipDataNameMap.put("5F34", "Application PAN Sequence Number");
		emvChipDataNameMap.put("71", "Issuer Script Template 1");
		emvChipDataNameMap.put("72", "Issuer Script Template 2");
		emvChipDataNameMap.put("82", "Application Interchange Profile");
		emvChipDataNameMap.put("84", "Dedicated file name(AID)");
		emvChipDataNameMap.put("91_1", "Issuer Authentication Data--ARPC");
		emvChipDataNameMap.put("91_2", "Issuer Authentication Data--ARC");
		emvChipDataNameMap.put("95", "Terminal Verification Results");
		emvChipDataNameMap.put("9A", "Transaction Date");
		emvChipDataNameMap.put("9B", "Transaction Status Information");
		emvChipDataNameMap.put("9C", "Transaction Type");
		emvChipDataNameMap.put("9F02", "Amount, Authorized");
		emvChipDataNameMap.put("9F03", "Amount, Other");
		emvChipDataNameMap.put("9F08", "Application version number in Card");
		emvChipDataNameMap.put("9F09", "Application version number in terminal");
		emvChipDataNameMap.put("9F10", "Issuer Application Data");
		emvChipDataNameMap.put("9F1A", "Terminal Country Code");
		emvChipDataNameMap.put("9F1E", "Interface Device (IFD) Serial Number");
		emvChipDataNameMap.put("9F26", "Application Cryptogram");
		emvChipDataNameMap.put("9F27", "Cryptogram Information Data");
		emvChipDataNameMap.put("9F33", "Terminal Capability Profile");
		emvChipDataNameMap.put("9F34", "Cardholder Verification Method Results");
		emvChipDataNameMap.put("9F35", "Terminal Type");
		emvChipDataNameMap.put("9F36", "Application Transaction Counter");
		emvChipDataNameMap.put("9F37", "Unpredictable Number");
		emvChipDataNameMap.put("9F41", "Transaction sequence counter");
		emvChipDataNameMap.put("9F5B", "Issuer Script Results");
		emvChipDataNameMap.put("9F63", "Card Product Label Information(卡產品標識信息)");
		emvChipDataNameMap.put("9F6E", "Form Factor Indicator");
		emvChipDataNameMap.put("9F7C", "Partner Discretionary Data");
		emvChipDataNameMap.put("DFED", "Chip Condition Code	00");
		emvChipDataNameMap.put("DFEE", "Terminal Entry Capability");
		emvChipDataNameMap.put("DFEF", "Reason Online Code (BASE24 use)");
		emvChipDataNameMap.put("S1", "Smart Pay Serial Number (Smart Pay卡片交易序號)");
		emvChipDataNameMap.put("S2", "Smart Pay TAC (Smart Pay交易驗證碼)");
		emvChipDataNameMap.put("S3", "MCC/BranchBank ID");
		emvChipDataNameMap.put("S4", "Terminal Check Code (端末設備檢核碼)");
		emvChipDataNameMap.put("S5", "Smart Pay RRN (Smart Pay調單編號)");
		emvChipDataNameMap.put("S6", "Original Transaction Date (原始交易日期)");
		emvChipDataNameMap.put("S7", "Transaction Date & Time (交易日期時間)");

		// Table ID
		tableDataNameMap.clear();
		tableDataNameMap.put("A", "TMS related data");
		tableDataNameMap.put("C", "CUP data information");
		tableDataNameMap.put("D", "經DCC詢價轉台幣交易");
		tableDataNameMap.put("O", "Pre-Auth Complete交易");
		tableDataNameMap.put("S", "持卡人自助交易");
		tableDataNameMap.put("Y", "取得主機的交易西元年");
		tableDataNameMap.put("N1", "CUP交易");
		tableDataNameMap.put("N4", "分期資料");
		tableDataNameMap.put("N5", "紅利資料");
		tableDataNameMap.put("N8", "AE card 4DBC");
		tableDataNameMap.put("L1", "Reward (優惠活動)");
		tableDataNameMap.put("L2", " Reward or Advertisement (優惠活動||累計訊息)及廣告資訊");
		tableDataNameMap.put("L3", "Redeem Information(優惠兌換資訊)");
		tableDataNameMap.put("L4", "Advice Information(通知資訊)");

		// sort
		sortMap.clear();
		sortMap.put("tran_date", "交易日期");
		sortMap.put("tran_time", "交易時間");
		sortMap.put("card_num", "卡號");
		sortMap.put("tran_amount", "交易金額");
		sortMap.put("bank_id", "金融機構名稱");
		sortMap.put("approve_code", "授權碼");
		sortMap.put("resp_code", "回應碼");
		sortMap.put("merchant_id", "特店代號");
		sortMap.put("term_id", "端末代號");

		refreshParamData();
	}

	private void refreshParamData() {
		ParameterQSImpl qs = (ParameterQSImpl) ProjServices.getParameterQS();
		List<Parameter> objs = qs.queryAll();

		this.authorizeOptions.clear();
		this.authorizeNameMap.clear();
		this.cardNameMap.clear();
		this.eWalletNameMap.clear();

		for (Iterator<Parameter> iter = objs.iterator(); iter.hasNext();) {
			Parameter obj = iter.next();
			// 授權來源
			if (Parameter.TYPE_AUTH_SOURCE.equals(obj.getType())) {
				this.authorizeOptions.add(new LabelValueBean(obj.getValue(), obj.getCode()));
				this.authorizeNameMap.put(obj.getCode(), obj.getValue());
			}
			// 卡別
			if (Parameter.TYPE_CARD_SOURCE.equals(obj.getType())) {
				this.cardNameMap.put(obj.getCode(), obj.getValue());
				if (Parameter.TYPE_CARD_eWallet_SOURCE.equals(obj.getValueExt1())) {
					this.eWalletNameMap.put(obj.getCode(), obj.getValue());
				}
			}
			// M2025074_R114117_CHESG 電子簽單
			if (Parameter.TYPE_CHESG_URI.equals(obj.getType())) {
				this.chesgUri = obj.getValue();
			}
		}
	}

	/**
	 * @return the bankMap
	 */
	public Map<String, String> getBankNameMap() {

		if (bankNameMap == null)
			bankNameMap = new HashMap<String, String>();
		return bankNameMap;
	}

	/**
	 * @return the cardMap
	 */
	public Map<String, String> getCardNameMap() {
		return cardNameMap;
	}

	/**
	 * @return the transTypeNameMap
	 */
	public Map<String, String> getTransTypeNameMap() {
		return transTypeNameMap;
	}

	/**
	 * @return the responseCodeNameMap
	 */
	public Map<String, String> getResponseCodeNameMap() {
		return responseCodeNameMap;
	}

	/**
	 * @return the emvChipDataNameMap
	 */
	public Map<String, String> getEmvChipDataNameMap() {
		return emvChipDataNameMap;
	}

	/**
	 * @return the tableDataNameMap
	 */
	public Map<String, String> getTableDataNameMap() {
		return tableDataNameMap;
	}

	/**
	 * @return the logReasonCodeNameMap
	 */
	public Map<String, String> getLogReasonCodeNameMap() {
		return logReasonCodeNameMap;
	}

	/**
	 * @return the logRespCodeNameMap
	 */
	public Map<String, String> getLogRespCodeNameMap() {
		return logRespCodeNameMap;
	}

	public List getEmvCardTypeList() {
		return emvCardTypeList;
	}

	/**
	 * @return the emvCardTypeNameMap
	 */
	public Map<String, String> getEmvCardTypeNameMap() {
		return emvCardTypeNameMap;
	}

	public List getEmvCardTypeOptions() {
		return emvCardTypeOptions;
	}

	public void setEmvCardTypeOptions(List emvCardTypeOptions) {
		this.emvCardTypeOptions = emvCardTypeOptions;
	}

	public List getLdssAccordOptions() {
		return ldssAccordOptions;
	}

	public void setLdssAccordOptions(List ldssAccordOptions) {
		this.ldssAccordOptions = ldssAccordOptions;
	}

	public List getLdssTransTypeNameList() {
		return ldssTransTypeNameList;
	}

	public void setLdssTransTypeNameList(List ldssTransTypeNameList) {
		this.ldssTransTypeNameList = ldssTransTypeNameList;
	}

	public Map<String, String> getLdssTransTypeNameMap() {
		return ldssTransTypeNameMap;
	}

	public void setLdssTransTypeNameMap(Map<String, String> ldssTransTypeNameMap) {
		this.ldssTransTypeNameMap = ldssTransTypeNameMap;
	}

	public Map<String, String> getIfesTransTypeNameMap() {
		return ifesTransTypeNameMap;
	}

	public void setIfesTransTypeNameMap(Map<String, String> ifesTransTypeNameMap) {
		this.ifesTransTypeNameMap = ifesTransTypeNameMap;
	}

	public List getIfesTransTypeNameList() {
		return ifesTransTypeNameList;
	}

	public void setIfesTransTypeNameList(List ifesTransTypeNameList) {
		this.ifesTransTypeNameList = ifesTransTypeNameList;
	}

	public Map<String, String> getSortMap() {
		return sortMap;
	}

	public Map<String, String> geteWalletNameMap() {
		return eWalletNameMap;
	}

	public List<LabelValueBean> getAuthorizeOptions() {
		try {
			this.refreshParamData();
		} catch (Exception x) {
			throw new ProcessException(x);
		}
		return authorizeOptions;
	}

	public Map<String, String> getAuthorizeNameMap() {
		try {
			this.refreshParamData();
		} catch (Exception x) {
			throw new ProcessException(x);
		}
		return authorizeNameMap;
	}

	public String getChesgUri() {
		return chesgUri;
	}

	public void setChesgUri(String chesgUri) {
		this.chesgUri = chesgUri;
	}
}
