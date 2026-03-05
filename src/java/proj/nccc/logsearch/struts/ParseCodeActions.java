package proj.nccc.logsearch.struts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.user.UserLogger;
import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.exception.ISO8583ParseException;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.Util;
import proj.nccc.logsearch.parse.ChipUtil;
import proj.nccc.logsearch.parse.ISOUtil;
import proj.nccc.logsearch.parse.TrustUtil;
import proj.nccc.logsearch.parse.UnyUtil;
import proj.nccc.logsearch.parse.chip.ChipParse;
import proj.nccc.logsearch.parse.chip.ChipParseMulti;
import proj.nccc.logsearch.persist.AtslogRawLog;
import proj.nccc.logsearch.persist.ChipcardInfo;
import proj.nccc.logsearch.persist.LDSSRawLog;
import proj.nccc.logsearch.qs.AtslogRawLogQSImpl;
import proj.nccc.logsearch.qs.LDSSRawLogQSImpl;
import proj.nccc.logsearch.vo.ChipcardInfoValue;
import proj.nccc.logsearch.vo.IsoInfoValue;
import proj.nccc.logsearch.vo.TrustInfoValue;
import proj.nccc.logsearch.vo.UnyInfoValue;

public class ParseCodeActions extends BaseActions {

	private static final long serialVersionUID = 8913847241742044077L;
	private String fixedCodeType = "";
	private String code;
	private String tagName;
	private String cardType;
	private String chipType;
	private String edcSpec;
	private long seqId;
	private String rawType;
	private String maskCardNo;
	// M2016144電子票證需求
	private String etData;
	// M2020020 DCC/ESC
	private String logTime;
	private String hostAccord;
	private String escData;

	private static final String[] SUPPORT_EDC_SPEC = { "AE", "", "ATS", "RFES", "GEDC", "TRUST" };
	public static final Set<String> SUPPORT_EDC_SPEC_SET = new HashSet<String>(Arrays.asList(SUPPORT_EDC_SPEC));
	private static final String[] SUPPORT_RAW_TYPE = { "FROM ASM", "TO ASM", "FROM B24", "TO B24", "FROM EDC", "TO EDC",
			"FROM EDC CONFIRM", "TO EDC CONFIRM", "FROM MFES", "TO MFES", "FROM SP SWITCH", "TO SP SWITCH",
			"TO SP SWITCH CONFIRM", "FROM TMS", "TO TMS", "FROM UPLAN", "TO UPLAN", "FROM ESC", "TO ESC", "FROM DCC",
			"TO DCC", "FROM UNY", "TO UNY", "TO UNY NOTIFY", "FROM TRUST", "TO TRUST" };
	public static final Set<String> SUPPORT_RAW_TYPE_SET = new HashSet<String>(Arrays.asList(SUPPORT_RAW_TYPE));

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */

	public String parseFixedCode() {
		String fixedCodeType = this.getFixedCodeType();
		return fixedCodeType;
	}

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String parseISOCode() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String rawType = this.getRawType();
		String edcSpec = this.getEdcSpec();
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		if (!SUPPORT_EDC_SPEC_SET.contains(edcSpec)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}

		AtslogRawLog rawLog = null;
		try {
			AtslogRawLogQSImpl qs = (AtslogRawLogQSImpl) ProjServices.getAtslogRawLogQS();
			rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, rawType);
			if (rawLog == null) {
				if (rawType.indexOf("TO") != -1) {
					rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, "TO MFES");
				}
				if (rawType.indexOf("FROM") != -1) {
					rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, "FROM MFES");
				}
				if (rawLog == null) {
					super.saveError("action.fail", new Object[] { "無此資料" });
					master.setSuccessFlag("N");
					return ProjConstants.FAIL_KEY;
				}
			}
			// M2020218_UNY
			if (rawType.indexOf("UNY") != -1) {
				UnyUtil unyUtil = new UnyUtil();
				List<UnyInfoValue> resultList = unyUtil.parse(rawType, rawLog.getRawData());
				this.setResultList(resultList);
			} else if (rawType.indexOf("TRUST") != -1) {
				// M2024060_R113110_信託資訊平台
				TrustUtil trustUtil = new TrustUtil();
				List<TrustInfoValue> resultList = trustUtil.parse(rawType, rawLog.getRawData());
				this.setResultList(resultList);
			} else {
				List<IsoInfoValue> resultList = ISOUtil.getInstance().parse(rawType, edcSpec, rawLog.getRawData(),
						"Y".equalsIgnoreCase(this.getMaskCardNo()), this.getCardType());
				this.setResultList(resultList);
			}
			if (!"Y".equalsIgnoreCase(this.getMaskCardNo())) {
				this.setEtData(rawLog.getRawData());
			}
			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String parseAEISOCode() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String rawType = this.getRawType();
		String edcSpec = this.getEdcSpec();
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		if (!SUPPORT_EDC_SPEC_SET.contains(edcSpec)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}

		AtslogRawLog rawLog = null;
		try {
			AtslogRawLogQSImpl qs = (AtslogRawLogQSImpl) ProjServices.getAtslogRawLogQS();
			rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, rawType);
			if (rawLog == null) {
				if (rawType.indexOf("TO") != -1) {
					rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, "TO MFES");
				}
				if (rawType.indexOf("FROM") != -1) {
					rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, "FROM MFES");
				}
				if (rawLog == null) {
					super.saveError("action.fail", new Object[] { "無此資料" });
					return ProjConstants.FAIL_KEY;
				}
			}
			List<IsoInfoValue> resultList = ISOUtil.getInstance().parse(rawType, edcSpec, rawLog.getRawData(),
					"Y".equalsIgnoreCase(this.getMaskCardNo()), this.getCardType());
			this.setResultList(resultList);
			if (!"Y".equalsIgnoreCase(this.getMaskCardNo())) {
				this.setEtData(rawLog.getRawData());
			}

			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public String parseICCode() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try {
			String code = this.getCode();
			String tagName = this.getTagName();
			String cardType = this.getCardType();
			String chipType = this.getChipType();
			String id = ChipcardInfo.createId(tagName, cardType, chipType);
			ChipParse chipParse = ChipUtil.getInstance().getChipParse(id);
			if (chipParse == null) {
				super.saveError("action.fail", new Object[] { "未定義的類型 " + tagName + "," + cardType + "," + chipType });
				System.out.println("未定義的類型 " + tagName + "," + cardType + "," + chipType);
				master.setSuccessFlag("N");
				return ProjConstants.FAIL_KEY;
			}
			List<ChipcardInfoValue> resultList = chipParse.parse(code);
			this.setResultList(resultList);
			if (chipParse instanceof ChipParseMulti) {
				return "Mutil";
			} else {
				return "Normal";
			}
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			super.saveError("action.fail", new Object[] { e.getMessage() });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	// M2016144電子票證需求
	public String showETData() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String tagName = this.getTagName();
		String rawType = this.getRawType();
		String edcSpec = this.getEdcSpec();
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		if (!SUPPORT_EDC_SPEC_SET.contains(edcSpec)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		AtslogRawLog rawLog = null;
		try {
			AtslogRawLogQSImpl qs = (AtslogRawLogQSImpl) ProjServices.getAtslogRawLogQS();
			rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, rawType);
			if (rawLog == null) {
				super.saveError("action.fail", new Object[] { "無此資料" });
				return ProjConstants.FAIL_KEY;
			}
			String etRawData = ISOUtil.getInstance().showETData(rawType, edcSpec, rawLog.getRawData(), tagName);
			if (tagName.startsWith("EZ") || tagName.startsWith("IC") || tagName.startsWith("HC02")
					|| tagName.startsWith("LP01") || tagName.startsWith("EP01") || tagName.startsWith("IP01")
					|| tagName.startsWith("SW01") || tagName.startsWith("PX01") || tagName.startsWith("PP01")
					|| tagName.startsWith("PX00") || tagName.startsWith("EP00") || tagName.startsWith("PI01")
					|| tagName.startsWith("CP00") || tagName.startsWith("CP01")) {
				etRawData = new String(CommonFunction.hexDecode(etRawData), "UTF-8");
			}
			etRawData = etRawData.replace(" ", "$$");
			this.setEtData(etRawData);
			this.setResultList(null);
			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	/**
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	// M2016144電子票證需求
	public String praseETData() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String rawType = this.getRawType();
		String edcSpec = this.getEdcSpec();
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		if (!SUPPORT_EDC_SPEC_SET.contains(edcSpec)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
		AtslogRawLog rawLog = null;
		try {
			AtslogRawLogQSImpl qs = (AtslogRawLogQSImpl) ProjServices.getAtslogRawLogQS();
			rawLog = (AtslogRawLog) qs.queryByPrimaryKey(seqId, rawType);
			if (rawLog == null) {
				super.saveError("action.fail", new Object[] { "無此資料" });
				master.setSuccessFlag("N");
				return ProjConstants.FAIL_KEY;
			}
			List<IsoInfoValue> resultList = ISOUtil.getInstance().parseETData(rawType, edcSpec, rawLog.getRawData(),
					"TKS");
			this.setResultList(resultList);
			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	// M2020020 ESC_DCC電文詳細查詢
	public String parseLDSSData() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String type = this.getRawType();
		String logTime = this.getLogTime();
		String hostAccord = this.getHostAccord();
		String rawType = "";
		String strFrom = "FROM ";
		String strTo = "TO ";
		if (type.startsWith(strFrom)) {
			rawType += strFrom;
			type = type.replace(strFrom, "");
		}
		if (type.startsWith(strTo)) {
			rawType += strTo;
			type = type.replace(strTo, "");
		}
		if ("DCC".equals(hostAccord)) {
			rawType += "DCC";
		} else if ("ESC".equals(hostAccord)) {
			rawType += "ESC";
		}
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}

		LDSSRawLog rawLog = null;
		try {
			LDSSRawLogQSImpl qs = (LDSSRawLogQSImpl) ProjServices.getLDSSRawLogQS();
			rawLog = (LDSSRawLog) qs.queryByPKLogTime(seqId, this.getRawType(), logTime);
			if (rawLog == null) {
				super.saveError("action.fail", new Object[] { "無此資料" });
				master.setSuccessFlag("N");
				return ProjConstants.FAIL_KEY;
			}

			List<IsoInfoValue> resultList = ISOUtil.getInstance().parse(rawType, "", rawLog.getRawData(),
					"Y".equalsIgnoreCase(this.getMaskCardNo()), "");
			this.setResultList(resultList);
			if (!"Y".equalsIgnoreCase(this.getMaskCardNo())) {
				this.setEtData(rawLog.getRawData());
			}

			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	// M2020020 DCC Table ID "X" 解析
	public String parseDCCData() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String type = this.getRawType();
		String logTime = this.getLogTime();
		String rawType = "";
		String strFrom = "FROM ";
		String strTo = "TO ";
		if (type.startsWith(strFrom)) {
			rawType += strFrom + "DCC";
		}
		if (type.startsWith(strTo)) {
			rawType += strTo + "DCC";
		}
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}

		LDSSRawLog rawLog = null;
		try {
			LDSSRawLogQSImpl qs = (LDSSRawLogQSImpl) ProjServices.getLDSSRawLogQS();
			rawLog = (LDSSRawLog) qs.queryByPKLogTime(seqId, this.getRawType(), logTime);
			if (rawLog == null) {
				super.saveError("action.fail", new Object[] { "無此資料" });
				master.setSuccessFlag("N");
				return ProjConstants.FAIL_KEY;
			}

			List<IsoInfoValue> resultList = ISOUtil.getInstance().parseDCCData(rawType, rawLog.getRawData());
			this.setResultList(resultList);

			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	// M2020020 ESC Table ID "E1","E2" 解析
	public String parseE1E2Data() {
		LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		long seqId = this.getSeqId();
		String type = this.getRawType();
		String logTime = this.getLogTime();
		String rawType = "";
		String strFrom = "FROM ";
		String strTo = "TO ";
		if (type.startsWith(strFrom)) {
			rawType += strFrom + "ESC";
		}
		if (type.startsWith(strTo)) {
			rawType += strTo + "ESC";
		}
		if (!SUPPORT_RAW_TYPE_SET.contains(rawType)) {
			UserLogger.getLog(this.getClass()).warn("參數錯誤");
			super.saveError("action.fail", new Object[] { "參數錯誤" });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}

		LDSSRawLog rawLog = null;
		try {
			LDSSRawLogQSImpl qs = (LDSSRawLogQSImpl) ProjServices.getLDSSRawLogQS();
			rawLog = (LDSSRawLog) qs.queryByPKLogTime(seqId, this.getRawType(), logTime);
			if (rawLog == null) {
				super.saveError("action.fail", new Object[] { "無此資料" });
				master.setSuccessFlag("N");
				return ProjConstants.FAIL_KEY;
			}

			String result = ISOUtil.getInstance().parseE1E2Data(rawType, rawLog.getRawData());
			this.setEscData(result);
			this.setResultList(null);

			return ProjConstants.SUCCESS_KEY;
		} catch (ISO8583ParseException e) {
			super.saveError("action.fail", new Object[] { String.format("error message[%s]\ndetail message[%s]",
					e.getMessage(), e.getTraceInfo() == null ? "" : e.getTraceInfo().toString()) });
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		} catch (Exception e) {
			if (e.getCause() instanceof ISO8583ParseException && rawLog != null) {
				UserLogger.getLog(this.getClass()).warn(e.getMessage());
				super.saveError("action.fail", new Object[] { "電文解析錯誤 原始資料[" + rawLog.getRawData() + "]" });
			} else {
				UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
				super.saveError("action.fail", new Object[] { e.getMessage() });
			}
			master.setSuccessFlag("N");
			return ProjConstants.FAIL_KEY;
		}
	}

	public String getMaskCardNo() {
		return maskCardNo;
	}

	public void setMaskCardNo(String maskCardNo) {
		this.maskCardNo = maskCardNo;
	}

	/**
	 * @return the fixedCodeType
	 */
	public String getFixedCodeType() {
		return fixedCodeType;
	}

	/**
	 * @param fixedCodeType the fixedCodeType to set
	 */
	public void setFixedCodeType(String fixedCodeType) {
		this.fixedCodeType = fixedCodeType;
	}

	/**
	 * @return the seqId
	 */
	public long getSeqId() {
		return seqId;
	}

	/**
	 * @param seqId the seqId to set
	 */
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the chipType
	 */
	public String getChipType() {
		return chipType;
	}

	/**
	 * @param chipType the chipType to set
	 */
	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	/**
	 * @return the rawType
	 */
	public String getRawType() {
		return rawType;
	}

	/**
	 * @param rawType the rawType to set
	 */
	public void setRawType(String rawType) {
		rawType = StringEscapeUtils.escapeHtml4(rawType);
		this.rawType = rawType;
	}

	public String getEdcSpec() {
		return edcSpec;
	}

	public void setEdcSpec(String edcSpec) {
		edcSpec = StringEscapeUtils.escapeHtml4(edcSpec);
		this.edcSpec = edcSpec;
	}

	public String getEtData() {
		return etData;
	}

	public void setEtData(String etData) {
		this.etData = etData;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		logTime = StringEscapeUtils.escapeHtml4(logTime);
		if (Util.isNumeric(logTime) && (logTime.length() == 7 || logTime.length() == 9)) {
			this.logTime = logTime;
		} else {
			this.logTime = "";
		}
	}

	public String getHostAccord() {
		return hostAccord;
	}

	public void setHostAccord(String hostAccord) {
		this.hostAccord = hostAccord;
	}

	public String getEscData() {
		return escData;
	}

	public void setEscData(String escData) {
		this.escData = escData;
	}

}
