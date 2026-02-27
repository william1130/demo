package proj.nccc.logsearch.text;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.edstw.report.ReportException;
import com.edstw.user.UserLogger;

import proj.nccc.logsearch.beans.ParamBean;
import proj.nccc.logsearch.vo.AtslogTransLogArg;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AETransLogTextForAMEX extends BaseTextReport {
	@Override
	protected void createReport(Object dataSrc) throws ReportException {
		try {
			List[] headerInfosArray = new List[] { new LinkedList() };

			List headerInfos = headerInfosArray[0];
			headerInfos.add(new HeaderInfo("", 10, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 8, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 6, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 8, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 6, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 6, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 6, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 5, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 3, HeaderInfo.Align.center));
			headerInfos.add(new HeaderInfo("", 4, HeaderInfo.Align.center));

			setHeaderInfos(headerInfosArray);

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

			String tranDate = (rec.getTranYear() == null) ? ""
					: rec.getTranYear() + (rec.getTranDate() == null ? "" : rec.getTranDate());

			writeColumn(rec.getMerchantId(), ",");
			writeColumn(rec.getTermId(), ",");
			writeColumn(rec.getApproveCode(), ",");
			writeColumn(tranDate, ",");
			writeColumn(rec.getTranTime(), ",");
			writeColumn(rec.getBatchNum(), ",");
			writeColumn(rec.getSeqNum(), ",");
			writeColumn(paramBean.getResponseCodeNameMap().get(rec.getRespCode()), ",");
			writeColumn(rec.getB24PosEntryMode(), ",");
			writeColumn(rec.getEdcMti(), ",");
		}
	}
}
