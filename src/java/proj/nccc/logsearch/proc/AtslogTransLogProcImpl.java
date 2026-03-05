package proj.nccc.logsearch.proc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.edstw.process.ProcessException;
import com.edstw.report.Report;
import com.edstw.report.jasper.PdfReportExporter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.beans.Util;
import proj.nccc.logsearch.qs.AtsRejectCodeSetupQSImpl;
import proj.nccc.logsearch.qs.AtslogTransLogQSImpl;
import proj.nccc.logsearch.qs.MerchantChinQSImpl;
import proj.nccc.logsearch.qs.TKSTransTypeMappingQSImpl;
import proj.nccc.logsearch.text.AETransLogTextForAMEX;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;
import proj.nccc.logsearch.vo.AtslogTransLogArg;

public class AtslogTransLogProcImpl extends ProjProc {

	public int queryCount(AtslogTransLogArg arg) {
		AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
		return qs.queryCount(arg);
	}

	/**
	 * @param arg
	 * @return
	 * @throws ProcessException
	 */
	public List<AtslogTransLogArg> getList(AtslogTransLogArg arg) throws ProcessException {

		List<AtslogTransLogArg> list = null;

		try {
			AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
			list = qs.query(arg);

			// get Merchant Chinese Name begin
			MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
			List<String> merchantIds = new ArrayList<String>();

			// 交易類別
			TKSTransTypeMappingQSImpl tksQS = (TKSTransTypeMappingQSImpl) ProjServices.getTKSTransTypeMappingQS();
			Map<String, String> tksTransTypeNamMap = tksQS.queryTKSNameMap();

			for (AtslogTransLogArg obj : list) {
				merchantIds.add(obj.getMerchantId());
			}

			Map<String, String> merchantNameMap = mcQs.queryNameMap(merchantIds);
			// get Merchant Chinese Name end

			// 授權碼 name begin
			AtsRejectCodeSetupQSImpl arcsQs = (AtsRejectCodeSetupQSImpl) ProjServices.getAtsRejectCodeSetupQS();
			Map<String, String> rejectCodeNamMap = arcsQs.queryNameMap();
			// 授權碼 name end

			// set
			for (AtslogTransLogArg obj : list) {
				String transTypeName = "";
				if (ProjConstants.TKS_CARD_SET.contains(obj.getCardType())) {
					transTypeName = (String) tksTransTypeNamMap.get(obj.getEdcMti() + obj.getEdcProcCode());
					obj.setTranType(transTypeName);
				}
				setArg(obj, arg.getMaskCardNo(), arg.getShowCardIso(),
						(String) merchantNameMap.get(obj.getMerchantId()),
						(String) rejectCodeNamMap.get(obj.getApproveCode()), transTypeName);
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}

		return list;
	}

	public void setArg(AtslogTransLogArg arg, String maskCardNo, String showCardIso, String merchantChinName,
			String approveCodeName, String tranTypeName) {
		if (ProjConstants.TKS_CARD_SET.contains(arg.getCardType())) {
			arg.setMaskCardNo(arg.getCardNum());
			if ("EZ".equals(arg.getCardType())) {
				arg.setExpDate(arg.getSwCardNum());
			}
		} else {
			arg.setMaskCardNo(maskCardNo);
		}
		arg.setShowCardIso(showCardIso);
		arg.setMerchantChinName(merchantChinName);
		arg.setTksTranTypeName(tranTypeName);
		String approveCode = arg.getApproveCode();
		if (approveCode != null && (approveCode.startsWith("REJ") || approveCode.startsWith("ID")
				|| approveCode.startsWith("API") || approveCode.startsWith("ATS") || approveCode.startsWith("RSK")
				|| approveCode.startsWith("NEG") || approveCode.startsWith("WEB"))) {
			arg.setApproveCodeName(approveCodeName);
		}

		// UPLAN-M2018187
		if (arg.getUpPreferAmt() != null && arg.getUpPreferAmt().length() > 0) {
			arg.setUpPreferAmtD(new BigDecimal(arg.getUpPreferAmt()).movePointLeft(2).doubleValue());
		}
		if (arg.getUpDiscountAmt() != null && arg.getUpDiscountAmt().length() > 0) {
			arg.setUpDiscountAmtD(new BigDecimal(arg.getUpDiscountAmt()).movePointLeft(2).doubleValue());
		}
		
		// M2024060_R113110_信託資訊平台
		if (StringUtils.equals("0957", arg.getNii())) {
			arg.setCardType("0957");
			arg.setCardNum(arg.getTrustNum()); 
			arg.setSeqNum(arg.getTrustSeqNum());
			arg.setTranType("TRUST" + arg.getTranType());
		}

		// M2025074_R114117_CHESG 電子簽單
		if (arg.isShowChesg()) {
			String uri = ParamBean.getInstance().getChesgUri();
			arg.setChesgUri(uri + Util.encodeShortUuid(arg.getChesgId()));
		}
	}

	/**
	 * @param seqIds
	 * @return
	 * @throws ProcessException
	 */
	public Report genReportById(String[] seqIds, String maskCardNo, String exFileType) throws ProcessException {

		try {
			AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
			List<AtslogTransLogArg> list = qs.queryByIds(seqIds);

			Map<Long, AtslogTransLogArg> map = new HashMap<Long, AtslogTransLogArg>();
			// 交易類別
			TKSTransTypeMappingQSImpl tksQS = (TKSTransTypeMappingQSImpl) ProjServices.getTKSTransTypeMappingQS();
			Map<String, String> tksTransTypeNamMap = tksQS.queryTKSNameMap();

			for (AtslogTransLogArg obj : list) {
				obj.setMaskCardNo(maskCardNo);
				String transTypeName = "";
				if (ProjConstants.TKS_CARD_SET.contains(obj.getCardType())) {
					transTypeName = (String) tksTransTypeNamMap.get(obj.getEdcMti() + obj.getEdcProcCode());
					obj.setMaskCardNo(obj.getCardNum());
					if ("EZ".equals(obj.getCardType())) {
						obj.setExpDate(obj.getSwCardNum());
					}
					obj.setTranType(transTypeName);
				}
				// M2024060_R113110_信託資訊平台
				if (StringUtils.equals("0957", obj.getNii())) {
					obj.setCardType("0957");
					obj.setSeqNum(obj.getTrustSeqNum());
					obj.setCardNo(obj.getTrustNum());
				}

				map.put(Long.valueOf(obj.getSeqId()), obj);
			}

			List<AtslogTransLogArg> resultList = new ArrayList<AtslogTransLogArg>();

			for (int i = 0; i < seqIds.length; i++) {

				if (seqIds[i] == null)
					continue;

				AtslogTransLogArg obj = (AtslogTransLogArg) map.get(Long.parseLong(seqIds[i]));

				if (obj != null) {

					resultList.add(obj);
				}
			}

			// M2020050 新增產出TXT報表
			if ("TXT".equals(exFileType)) {
				return genTxtReport(resultList);
			} else {
				return genReport(resultList); // PDF
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}
	}

	/**
	 * @param list
	 * @return
	 * @throws ProcessException
	 */
	public Report genReport(List<?> list) throws ProcessException {

		try {
			ProjUserInfo ui = (ProjUserInfo) ProjUserProfile.getCurrentUserProfile().getUserInfo();

			Map<String, Object> param = new HashMap<String, Object>();
			ParamBean paramBean = ParamBean.getInstance();
			param.put("REPORT_DATE", new Date());
			param.put("PRINT_USER", ui.getUserName());
			param.put("BANK_NAME_MAP", paramBean.getBankNameMap());
			param.put("TRANS_TYPE_NAME_MAP", paramBean.getTransTypeNameMap());
			param.put("RESPONSE_CODEN_AME_MAP", paramBean.getResponseCodeNameMap());
			param.put("CARD_NAME_MAP", paramBean.getCardNameMap());

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getClassLoader()
					.getResourceAsStream("proj/nccc/logsearch/report/AtslogTransLog.jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param,
					new JRBeanCollectionDataSource(list));

			PdfReportExporter rep = new PdfReportExporter();
			rep.setJasperPrint(jasperPrint);
			rep.setReportFileName("AtslogTransLog.pdf");
			return rep;
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}
	}

	// AE only
	public int queryAECount(AtslogTransLogArg arg) {
		AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
		return qs.queryAECount(arg);
	}

	public List<AtslogTransLogArg> getAEList(AtslogTransLogArg arg) throws ProcessException {

		List<AtslogTransLogArg> list = null;

		try {
			AtslogTransLogQSImpl qs = (AtslogTransLogQSImpl) ProjServices.getAtslogTransLogQS();
			list = qs.queryAE(arg);

			// get Merchant Chinese Name begin
			MerchantChinQSImpl mcQs = (MerchantChinQSImpl) ProjServices.getMerchantChinQS();
			List<String> merchantIds = new ArrayList<String>();

			for (AtslogTransLogArg obj : list) {
				merchantIds.add(obj.getMerchantId());
			}

			Map<String, String> merchantNameMap = mcQs.queryNameMap(merchantIds);
			// get Merchant Chinese Name end

			// 授權碼 name begin
			AtsRejectCodeSetupQSImpl arcsQs = (AtsRejectCodeSetupQSImpl) ProjServices.getAtsRejectCodeSetupQS();
			Map<String, String> rejectCodeNamMap = arcsQs.queryNameMap();
			// 授權碼 name end

			// set
			for (AtslogTransLogArg obj : list) {
				setArg(obj, arg.getMaskCardNo(), arg.getShowCardIso(),
						(String) merchantNameMap.get(obj.getMerchantId()),
						(String) rejectCodeNamMap.get(obj.getApproveCode()), "");
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}

		return list;
	}

	public Report genAEReport(List<?> list) throws ProcessException {

		try {
			ProjUserInfo ui = (ProjUserInfo) ProjUserProfile.getCurrentUserProfile().getUserInfo();

			Map<String, Object> param = new HashMap<String, Object>();
			ParamBean paramBean = ParamBean.getInstance();
			param.put("REPORT_DATE", new Date());
			param.put("PRINT_USER", ui.getUserName());
			param.put("BANK_NAME_MAP", paramBean.getBankNameMap());
			param.put("TRANS_TYPE_NAME_MAP", paramBean.getTransTypeNameMap());
			param.put("RESPONSE_CODEN_AME_MAP", paramBean.getResponseCodeNameMap());
			param.put("CARD_NAME_MAP", paramBean.getCardNameMap());

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getClassLoader()
					.getResourceAsStream("proj/nccc/logsearch/report/AtslogTransLog.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param,
					new JRBeanCollectionDataSource(list));

			PdfReportExporter rep = new PdfReportExporter();
			rep.setJasperPrint(jasperPrint);
			rep.setReportFileName("AtslogTransLog_AE.pdf");
			return rep;
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}
	}

	/**
	 * 產出TXT報表 (M2020050)
	 * 
	 * @param list
	 * @return
	 * @throws ProcessException
	 */
	public Report genTxtReport(List<?> list) throws ProcessException {

		try {
			AETransLogTextForAMEX rep = new AETransLogTextForAMEX();
			rep.setReportName("交易Log報表");
			rep.setReportFileName("AtslogTransLog.txt");
			rep.buildReport(list);
			return rep;
		} catch (Exception e) {

			e.printStackTrace();
			throw new ProcessException(e);
		}
	}
}
