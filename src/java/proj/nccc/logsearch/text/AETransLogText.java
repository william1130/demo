package proj.nccc.logsearch.text;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.edstw.report.ReportException;
import com.edstw.user.UserLogger;

import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.vo.AtslogTransLogArg;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AETransLogText extends BaseTextReport {
	@Override
	protected void createReport(Object dataSrc) throws ReportException {
		try {
			List[] headerInfosArray = new List[] { new LinkedList(), new LinkedList() };

			List headerInfos = headerInfosArray[0];
			headerInfos.add(new HeaderInfo("卡號", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("金融機構名稱", 22, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("商店代碼", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("金額", 10, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("交易日期", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("交易時間", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("交易類別", 22, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("批號", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("收單行", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("回應碼", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("狀況", 10, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("B24 MTI", 10, HeaderInfo.Align.center));

			headerInfos = headerInfosArray[1];
			headerInfos.add(new HeaderInfo("有效期", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("卡別", 22, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("端末機代碼", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("授權碼", 10, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("分期紅利", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("IC", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("驗證結果", 22, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("序號", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("銀聯卡 Hot Key", 18, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("Entry Mode", 12, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("代行", 10, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("EDC MTI", 10, HeaderInfo.Align.center));

			writeReportTitle(getReportName(), headerInfosArray, null);

			fillResultData((List) dataSrc);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ReportException(e.getMessage(), e);
		}
	}

	private void fillResultData(List dataList) throws ReportException {
		ParamBean paramBean = ParamBean.getInstance();

		for (Iterator iter = dataList.iterator(); iter.hasNext();) {
			AtslogTransLogArg rec = (AtslogTransLogArg) iter.next();

			String bankName = "";
			String tranType = "";
			if ("IP".equals(rec.getCardType()) || "IC".equals(rec.getCardType()) || "HC".equals(rec.getCardType())
					|| "EZ".equals(rec.getCardType())) {
				bankName = "";
				tranType = rec.getTranType();
			} else {
				bankName = paramBean.getBankNameMap().get(rec.getBankId());
				String ats = "Y".equals(rec.getAtsReversal()) ? "(人工沖銷)" : "";
				tranType = paramBean.getTransTypeNameMap().get(rec.getTranType()) + ats;
			}

			String tranDate = (rec.getTranYear() == null) ? ""
					: rec.getTranYear() + (rec.getTranDate() == null ? "" : rec.getTranDate());

			String payType = "";
			if ("1".equals(rec.getPayTypeInd()) || "2".equals(rec.getPayTypeInd())) {
				payType = "紅利";
			} else if ("I".equals(rec.getPayTypeInd()) || "E".equals(rec.getPayTypeInd())) {
				payType = "分期";
			}

			writeColumn(rec.getCardNumMask());
			writeColumn(bankName);
			writeColumn(rec.getMerchantId());
			writeColumn(longFormater.format(rec.getTranAmount()));
			writeColumn(tranDate);
			writeColumn(rec.getTranTime());
			writeColumn(tranType);
			writeColumn(rec.getBatchNum());
			writeColumn(paramBean.getBankNameMap().get(rec.getAcqId()));
			writeColumn(paramBean.getResponseCodeNameMap().get(rec.getRespCode()));
			writeColumn(rec.getMtiStatus());
			writeColumn(rec.getB24Mti());

			writeColumn(rec.getExpDate());
			writeColumn(paramBean.getCardNameMap().get(rec.getCardType()));
			writeColumn(rec.getTermId());
			writeColumn(rec.getApproveCode());
			writeColumn(payType);
			writeColumn(rec.getIcTran());
			writeColumn(rec.getCardVeryFlag());
			writeColumn(rec.getSeqNum());
			writeColumn(rec.getCupHotKeyInd());
			writeColumn(rec.getB24PosEntryMode());
			writeColumn(rec.getStandInFlag());
			writeColumn(rec.getEdcMti());
		}
	}
}
