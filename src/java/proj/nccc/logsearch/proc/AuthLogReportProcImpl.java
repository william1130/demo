/*
 * AuthLogReportProcImpl.java
 *
 * Created on 2008年7月14日, 上午 9:35
 * ==============================================================================================
 * $Id: AuthLogReportProcImpl.java,v 1.1 2017/04/24 01:31:25 asiapacific\jih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.proc;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.lang.DateString;
import com.edstw.lang.DoubleString;
import com.edstw.process.ProcessException;
import com.edstw.report.Report;
import com.edstw.service.ServiceException;
import com.edstw.user.UserLogger;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.JasperReportXType;
import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.persist.AuthLogData;
import proj.nccc.logsearch.qs.AuthLogDataQS;
import proj.nccc.logsearch.vo.AuthLogDataArg;
import proj.nccc.logsearch.vo.AuthTransReportData;

/**
 *
 * @author lzltp3
 * @version $Revision: 1.1 $
 */
public class AuthLogReportProcImpl extends ProjProc implements AuthLogReportProc {
	/**
	 * Creates a new instance of AuthLogReportProcImpl
	 */
	public AuthLogReportProcImpl() {
	}

	public String getServiceName() {
		return "授權報表";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	public Report authLogAuthTransReport(AuthLogDataArg arg) throws ProcessException {
		try {
			boolean bTPage = arg.getPagingInfo().isEnablePaging();
			arg.getPagingInfo().setEnablePaging(false);

			AuthLogDataQS qs = ProjServices.getAuthLogDataQS();
			List list = qs.queryByExample(arg);
			arg.getPagingInfo().setEnablePaging(bTPage);

			AuthTransReportData data = new AuthTransReportData();
			// 將 Report header 的資料塞進去
			data.setFileDate1(arg.getProcessDate1());
			data.setFileDate2(arg.getProcessDate2());
			data.setBankNo(arg.getBankNo());
			String bankName = "";
			if (arg.getBankNo() != null)
				bankName = ParamBean.getInstance().getBankNameMap().get(arg.getBankNo());
			data.setBankName(bankName);
			data.setDataSource(arg.getDataSource());
			data.setCardType(arg.getCardType());
			data.setCardKind(arg.getCardKind());
			// 將Detail的資料塞進去
			List records = new LinkedList();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				AuthLogData obj = (AuthLogData) iter.next();
				AuthTransReportData.Record rec = data.new Record();
				rec.setBankNo(obj.getBankNo());
				rec.setCardNo(obj.getCardNo());
				if (obj.getLoyaltyFlag() != null && obj.getLoyaltyFlag().equals("Y"))
					rec.setTransName("紅利");
				else if (obj.getInstallmentFlag() != null && obj.getInstallmentFlag().equals("Y"))
					rec.setTransName("分期");
				else
					rec.setTransName("一般");
				rec.setMerchantNo(obj.getMerchantNo());
				rec.setTerminalNo(obj.getTerminalNo());
				if (obj.getPurchaseAmt() == null)
					rec.setPurchaseAmt("0");
				else
					rec.setPurchaseAmt(obj.getPurchaseAmt().toString());
				if (obj.getSurchgAmt() == null)
					rec.setSurchgAmt("0");
				else
					rec.setSurchgAmt(obj.getSurchgAmt().toString());

				// 20090211 fallback資料授權不成功時,不顯示授權碼
				// if(obj.getManualEntryResult().matches("[YC]") ||
				// obj.getTransType().equals("IP") || obj.getApprovalNo().equals("AT0000"))
				if (obj.getDisplayApproveNo().equals("Y") || obj.getManualEntryResult().matches("[YC]")
						|| obj.getApprovalNo().equals("AT0000"))
					rec.setApprovalNo(obj.getApprovalNo());
//				rec.setApprovalNo(obj.getApprovalNo());
				rec.setPurchaseDate(obj.getPurchaseDate());
				rec.setPurchaseTime(obj.getPurchaseTime());
				rec.setTransType(obj.getTransType());
				rec.setConditionCode(obj.getConditionCode());
				rec.setReturnHostCode(obj.getReturnHostCode());
				rec.setEntryMode(obj.getEntryMode());
				rec.setMccCode(obj.getMccCode());
				rec.setAcquireId(obj.getAcquireId());
				rec.setCountryName(obj.getCountryName());

				// 紅利
				// 原紅利點數
				if (obj.getLoyaltyOriginalPoint() != null)
					rec.setLoyaltyOriginalPoint(obj.getLoyaltyOriginalPoint().getDouble());
				// 扣抵點數
				if (obj.getLoyaltyRedemptionPoint() != null)
					rec.setLoyaltyRedemptionPoint(obj.getLoyaltyRedemptionPoint().getDouble());
				// 扣抵金額
				if (obj.getLoyaltyRedemptionAmt() != null)
					rec.setLoyaltyRedemptionAmt(obj.getLoyaltyRedemptionAmt().getDouble());
				// 扣抵後金額
				if (obj.getLoyaltyPaidCreditAmt() != null)
					rec.setLoyaltyPaidCreditAmt(obj.getLoyaltyPaidCreditAmt().getDouble());
				// 分期
				// 分期期數
				if (obj.getInstallPeriodNum() != null)
					rec.setInstallPeriodNum(obj.getInstallPeriodNum().getDouble());
				// 首期金額
				if (obj.getInstallFirstPayment() != null)
					rec.setInstallFirstPayment(obj.getInstallFirstPayment().getDouble());
				// 每期金額
				if (obj.getInstallStagesPayment() != null)
					rec.setInstallStagesPayment(obj.getInstallStagesPayment().getDouble());
				// 手續費
				if (obj.getInstallFormalityFee() != null)
					rec.setInstallFormalityFee(obj.getInstallFormalityFee().getDouble());

				// 行業別次限額
				if (obj.getMccAmtLimit() != null)
					rec.setMccAmtLimit(obj.getMccAmtLimit().getDouble());

				rec.setCardClass(obj.getCardClass());
				rec.setLimitType(obj.getLimitType());

				// // 信用餘額 = 最大總月限額 - (本月+上月)累積 - 帳務金額
				try {
					double c = obj.getMaxLimitAmtMonth().doubleValue();
					double c1 = obj.getAmtCurrentMonth().doubleValue();
					double c2 = obj.getAmtLastMonth().doubleValue();
					double c3 = obj.getIdBillAmt().doubleValue();
					rec.setCreditLimit(new DoubleString(c - c1 - c2 - c3).getDouble());
				} catch (Exception xx) {
					rec.setCreditLimit(new Double(0));
				}
				// M98085國內臨時額度第二階段增加臨時餘額欄位
				// 臨時餘額,tempCreditLimit
				if (obj.getTempCreditLimit() != null)
					rec.setTempcreditLimit(obj.getTempCreditLimit().getDouble());
				// ID本月累積總消費金額, amtCurrentMonth
				if (obj.getAmtCurrentMonth() != null)
					rec.setAmtCurrentMonth(obj.getAmtCurrentMonth().getDouble());
				// ID上月累積總消費金額, amtLastMonth
				if (obj.getAmtLastMonth() != null)
					rec.setAmtLastMonth(obj.getAmtLastMonth().getDouble());
				// ID帳務金額, idBillAmt
				if (obj.getIdBillAmt() != null)
					rec.setIdBillAmt(obj.getIdBillAmt().getDouble());
				// ID最大總月限額, maxLimitAmtMonth
				if (obj.getMaxLimitAmtMonth() != null)
					rec.setMaxLimitAmtMonth(obj.getMaxLimitAmtMonth().getDouble());
				// ID本月累積預借現金金額, cashAmtCurrentMonth
				if (obj.getCashAmtCurrentMonth() != null)
					rec.setCashAmtCurrentMonth(obj.getCashAmtCurrentMonth().getDouble());
				// ID上月 累積預借現金金額, idCashAmtLastMonth
				if (obj.getIdCashAmtLastMonth() != null)
					rec.setIdCashAmtLastMonth(obj.getIdCashAmtLastMonth().getDouble());
				// ID預借現金帳務金額, idCashBillAmt
				if (obj.getIdCashBillAmt() != null)
					rec.setIdCashBillAmt(obj.getIdCashBillAmt().getDouble());
				// ID 信用餘額, idCreditLimit
				if (obj.getIdCreditLimit() != null)
					rec.setIdCreditLimit(obj.getIdCreditLimit().getDouble());
				// 一般消費月限額, normalLimitAmtMonth
				if (obj.getNormalLimitAmtMonth() != null)
					rec.setNormalLimitAmtMonth(obj.getNormalLimitAmtMonth().getDouble());
				// 一般消費月累積金額, normalAmtMonth
				if (obj.getNormalAmtMonth() != null)
					rec.setNormalAmtMonth(obj.getNormalAmtMonth().getDouble());
				// 一般消費上月累積金額, normalAmtLastMonth
				if (obj.getNormalAmtLastMonth() != null)
					rec.setNormalAmtLastMonth(obj.getNormalAmtLastMonth().getDouble());
				// 總消費月限額, totalLimitAmtMonth
				if (obj.getTotalLimitAmtMonth() != null)
					rec.setTotalLimitAmtMonth(obj.getTotalLimitAmtMonth().getDouble());
				// 預借現金月限額, cashLimitAmtMonth
				if (obj.getCashLimitAmtMonth() != null)
					rec.setCashLimitAmtMonth(obj.getCashLimitAmtMonth().getDouble());
				// 預借現金月累積消費金額, cashAmtMonth
				if (obj.getCashAmtMonth() != null)
					rec.setCashAmtMonth(obj.getCashAmtMonth().getDouble());
				// 預借現金上月累積消費金額, cashAmtLastMonth
				if (obj.getCashAmtLastMonth() != null)
					rec.setCashAmtLastMonth(obj.getCashAmtLastMonth().getDouble());
				// 預借現金帳務金額, cashBillAmt
				if (obj.getCashBillAmt() != null)
					rec.setCashBillAmt(obj.getCashBillAmt().getDouble());
				records.add(rec);
			}
			data.setRecords(records);
			Map param = new HashMap();
			param.put("REPORT_DATE", new DateString(new Date()).toString());
			// 資料月份
			param.put("FILE_DATE1", arg.getProcessDate1());
			param.put("FILE_DATE2", arg.getProcessDate2());
			List datas = new LinkedList();
			datas.addAll(data.getRecords());

			JasperReportXType jX = new JasperReportXType();
			String fileName = "AuthTransReport";
			Report rep = jX.getReport("proj/nccc/logsearch/report/AuthTransReport.jasper", true, datas, param,
					arg.getReportType(), fileName);

			if (datas != null) {
				datas.clear();
				datas = null;
			}
			if (data != null) {
				data = null;
			}
			if (param != null) {
				param.clear();
				param = null;
			}
			return rep;

		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}

}
