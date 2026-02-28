package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.dao.SysParaDao;
import proj.nccc.atsLog.batch.dao.TransLogDao;
import proj.nccc.atsLog.batch.dao.entity.SkmData;
import proj.nccc.atsLog.batch.dao.entity.SysPara;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class SkmMonrptProcImpl extends AbstractRptAtslogProc {
	private String jobId;
	private String processMonth;
	/**
	 * 月份 : MM
	 */
	private String processMm;
	private String rptId;

	public SkmMonrptProcImpl(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @param processMonth
	 * @return
	 * @throws Exception
	 */
	public ReturnVO process(String processMonth) throws Exception {
		this.processMonth = processMonth;
		this.processMm = StringUtils.substring(this.processMonth, 2);

		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {}, process Month : {}", jobId, processMonth);
		SysFileUploadDao fileUploadDao = new SysFileUploadDao();
		ProjDao dao = new ProjDao();
		SysParaDao sysParaDao = new SysParaDao();
		TransLogDao transLogDao = new TransLogDao();
		try {
			// ----------------------------------------------------------------
			// -- 刪除既有資料(待上傳的資料)
			String itemCategory = "FTP_SKM";
			String itemType1 = this.getReportId();
			String itemType2 = this.getReportId() + "_Dtl";
			fileUploadDao.delete(itemCategory, itemType1, processMonth);
			fileUploadDao.delete(itemCategory, itemType2, processMonth);

			// ----------------------------------------------------------------
			// -- 取得該月份的起訖日期
			super.createDateRange(processMonth);

			log.info("Date range: {} - {}", super.getStartDate(), super.getEndDate());

			// tID
			SysPara paraData = sysParaDao.getSysParameters("RPT", "SKM");
			if (paraData == null) {
				returnVo.setSuccess(true);
				returnVo.setMessage("SKM para settings is null.");
				return returnVo;
			}
			log.info("Trem ID: {} - {}", paraData.getValue(), paraData.getValueExt1());

			List<SkmData> listData = transLogDao.queryTransLogByTid(super.getStartDate(), super.getEndDate(),
					paraData.getValue(), paraData.getValueExt1());

			int records = listData.size();
			// ----------------------------------------------------------------

			Map<Integer, List<String>> map = this.writeLine(listData);

			// 總表
			List<String> reportContent = new LinkedList<String>();
			reportContent.addAll(this.reportTitle());
			reportContent.addAll(map.get(1));
			rptId = itemType1;

			this.writeTo(reportContent, itemCategory, itemType1, fileUploadDao, false);

			// 明細
			List<String> detailContent = new LinkedList<String>();
			detailContent.addAll(this.detailTitle());
			detailContent.addAll(map.get(2));
			rptId = itemType2;

			this.writeTo(detailContent, itemCategory, itemType2, fileUploadDao, false);

			log.info("Job : {} finish, Total file line count {}", jobId, records);
			returnVo.setSuccess(true);
			returnVo.setMessage("Total " + records + " records");
			return returnVo;
		} finally {
			fileUploadDao.close();
			dao.close();
			transLogDao.close();
		}
	}

	/**
	 * update data
	 * 
	 * @param list
	 * @return Map1:reportLines, Map2:detailLines
	 * @throws Exception
	 */
	private Map<Integer, List<String>> writeLine(List<SkmData> list) throws Exception {
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> reportMap = new HashedMap();
		List<String> detailLines = new LinkedList<String>();
		List<String> reportLines = new LinkedList<String>();
		int detailCount = 0;
		int reportCount = 0;
		String upMertId = "";
		String mertId = "";
		String mertName = "";
		String tid = "";
		int tidCount = 0;
		Double tidSum = 0.0;

		for (SkmData data : list) {

			// 總表
			if (tid != "" && !tid.equals(data.getTermId())) {
				reportLines.add(++reportCount + "," + upMertId + "," + mertId + "," + mertName + "," + formatbycsv(tid)
						+ "," + tidCount + "," + tidSum.intValue());
				tidCount = 0;
				tidSum = 0.0;
			}
			upMertId = formatbycsv(data.getUpperMerchantNo());
			mertId = formatbycsv(data.getMerchantNo());
			mertName = data.getMerchantName();
			tid = data.getTermId();
			++tidCount;
			tidSum = Double.sum(tidSum, data.getAmount());

			// 明細
			detailLines.add(++detailCount + "," + upMertId + "," + mertId + "," + mertName + "," + formatbycsv(tid)
					+ "," + data.getLogDate() + "," + data.getBatchNum() + "," + data.getAmount().intValue());

		}
		if (tid != "") {
			reportLines.add(++reportCount + "," + upMertId + "," + mertId + "," + mertName + "," + formatbycsv(tid)
					+ "," + tidCount + "," + tidSum);
			tidCount = 0;
			tidSum = 0.0;
		}
		reportMap.put(1, reportLines);
		reportMap.put(2, detailLines);
		return reportMap;
	}

	private String formatbycsv(String val) {
		if (StringUtils.isBlank(val)) {
			val = "";
		} else {
			val = "\"=T(\"\"" + val + "\"\")\"";
		}
		return val;
	}

	private List<String> detailTitle() {
		List<String> title = new LinkedList<String>();
		title.add("序號,母店代號,子店代號,特店中文名稱,端末機代號(39開頭),結帳日期(YYYYMMDD),批次號碼,結帳金額");
		return title;
	}

	private List<String> reportTitle() {
		List<String> title = new LinkedList<String>();
		title.add("序號,母店代號,子店代號,特店中文名稱,端末機代號,當月結帳次數累計(含0元結帳),當月結帳金額總計(正負相抵)");
		return title;
	}

	@Override
	String getReportId() {
		return jobId;
	}

	@Override
	String getReportName() {
		return "新光三越統計月報";
	}

	@Override
	String getFileName() {
		return this.processMm + rptId + ".csv";
	}

	@Override
	String getPlanUploadDate() {
		// ----------------------------------------
		// -- 預期上傳 target 的日期.
		return DateUtil.dateToString(new Date(), "yyyyMMdd");
	}

	@Override
	String getProcessMonth() {
		return this.processMonth;
	}

}
