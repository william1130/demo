package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.AuthLogDataDao;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.util.CommUtil;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.MyStringUtils;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthDayrpt0004ProcImpl extends AbstractRptAtslogProc {
	private String jobId;
	private String processDate;
	private String runDate;

	public AuthDayrpt0004ProcImpl(String jobId) {
		this.jobId = jobId;
		this.runDate = MyDateUtil.yyyymmdd(new Date());

	}

	/**
	 * @param processMonth
	 * @return
	 * @throws Exception
	 */
	public ReturnVO process(String processDate) throws Exception {
		this.processDate = processDate;

		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {}, process Date : {}", jobId, processDate);
		SysFileUploadDao fileUploadDao = new SysFileUploadDao();
		ProjDao dao = new ProjDao();
		AuthLogDataDao authLogDataDao = new AuthLogDataDao();
		try {
			// ----------------------------------------------------------------
			// -- 刪除既有資料(待上傳的資料)
			String itemCategory = "EREPORT";
			String itemType = this.getReportId();
			fileUploadDao.delete(itemCategory, itemType, processDate);
			super.setStartDate(processDate);
			super.setEndDate(processDate);

			log.info("基準日期: {} ", processDate);

			// ----------------------------------------------------------------
			// -- 主要處理區段
			List<String> reportContent = this.mainProcess(authLogDataDao, processDate, processDate);

			this.writeTo(reportContent, itemCategory, itemType, fileUploadDao, false);
			log.info("Total file record : " + (reportContent.size() - 7));
			returnVo.setSuccess(true);
			returnVo.setMessage("Total file record : " + (reportContent.size() - 7));
			return returnVo;
		} finally {
			fileUploadDao.close();
			dao.close();
			authLogDataDao.close();
		}
	}

	private List<String> mainProcess(AuthLogDataDao authLogDataDao, String date1, String date2) throws Exception {
		List<String> reportLines = new LinkedList<String>();

		int writeCnt = 0;
		int pageNo = 1;

		// -----------------------------------------------
		// -- report header
		reportLines.addAll(this.reportTitle(pageNo));
		writeCnt = 6;

		List<Map<String, Object>> list = authLogDataDao.queryDayrpt004(date1, date2);

		int selDataCnt = 0;
		for (Map<String, Object> map : list) {
			String purchaseDate = MapUtils.getString(map, "PURCHASE_DATE", "");
			String purchaseTime = MapUtils.getString(map, "PURCHASE_TIME", "");
			String merchantNo = MapUtils.getString(map, "MERCHANT_NO", "");
			String cardNo = MapUtils.getString(map, "CARD_NO", "");
			long purchaseAmt = MapUtils.getLongValue(map, "PURCHASE_AMT", 0);
			String approvalNo = MapUtils.getString(map, "APPROVAL_NO", "");
			String updateId = MapUtils.getString(map, "UPDATE_ID", "");
			String remark = MapUtils.getString(map, "REMARK", "");

			selDataCnt++;

			if (writeCnt >= 30) {
				pageNo++;
				reportLines.addAll(this.reportTitle(pageNo));
				writeCnt = 6;
			}

			// --------------------------------------------------------------
			// -- write body line
			String ptime = CommUtil.formatWithSymbol(purchaseTime, 2, ":");
			String purchaseAmtStr = CommUtil.numberFormat(purchaseAmt, null);
			reportLines.add("     " + MyStringUtils.rightPad(ptime, 16, " ") + " "
					+ MyStringUtils.rightPad(merchantNo, 15, " ") + " " + MyStringUtils.rightPad(cardNo, 20, " ") + " "
					+ MyStringUtils.leftPad(purchaseAmtStr, 11, " ") + "      "
					+ MyStringUtils.rightPad(approvalNo, 12, " ") + " " + MyStringUtils.rightPad(updateId, 10, " ")
					+ " " + MyStringUtils.rightPad(remark, 40, " "));
			writeCnt++;
		}
		log.info("auth_dayrpt_0004 finished with {} pages, total {} records", pageNo, selDataCnt);
		return reportLines;
	}

	private List<String> reportTitle(int pageNo) {
		List<String> title = new LinkedList<String>();

		title.add("      ");
		title.add("      PROGRAM-ID : auth_dayrpt_0004 "
				+ MyStringUtils.rightPad("*****                      人工強制授權報表                      *****", 75, " ")
				+ MyStringUtils.rightPad(" ", 29, " "));
		title.add("      " + MyStringUtils.rightPad("REPORT-ID  : MGPA004A", 30, " ")
				+ MyStringUtils.rightPad("***                                                                ***", 75,
						" ")
				+ MyStringUtils.rightPad("PAGE-CNT :", 11, " ") + MyStringUtils.leftPad("" + pageNo, 8, " ")
				+ "          ");

		String rptProcessDate = "*****                       " + processDate.substring(0, 4) + "年"
				+ processDate.substring(4, 6) + "月" + processDate.substring(6) + "日" + "                       *****";
		String rptRunDate = runDate.substring(0, 4) + "/" + runDate.substring(4, 6) + "/" + runDate.substring(6);

		title.add(MyStringUtils.rightPad(" ", 36, " ") + MyStringUtils.rightPad(rptProcessDate, 75, " ")
				+ MyStringUtils.rightPad("RUN-DATE :", 11, " ") + MyStringUtils.rightPad(rptRunDate, 18, " "));
		title.add(MyStringUtils.rightPad("-", 133, "-"));

		title.add("    " + MyStringUtils.rightPad("登錄時間", 19, " ") + MyStringUtils.rightPad("特店代號", 21, " ")
				+ MyStringUtils.rightPad("卡號", 17, " ") + MyStringUtils.rightPad("消費金額", 15, " ")
				+ MyStringUtils.rightPad("授權碼", 12, " ") + MyStringUtils.rightPad("操作者ID", 21, " ")
				+ MyStringUtils.rightPad("強  制  授  權  理  由", 31, " "));
		title.add(MyStringUtils.rightPad("-", 133, "-"));
		return title;
	}

	@Override
	String getReportId() {
		return "MGPA004A";
	}

	@Override
	String getReportName() {
		return "人工強制授權報表";
	}

	@Override
	String getFileName() {
		return this.runDate + "mamgpa004a.r";
	}

	@Override
	String getPlanUploadDate() {
		// ----------------------------------------
		// -- 預期上傳 target 的日期.
		return DateUtil.dateToString(new Date(), "yyyyMMdd");
	}

	@Override
	String getProcessMonth() {
		return this.processDate;
	}

}
