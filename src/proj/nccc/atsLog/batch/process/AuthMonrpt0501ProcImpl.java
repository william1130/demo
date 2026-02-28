package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.dao.AuthLogDataDao;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthMonrpt0501ProcImpl extends AbstractRptAtslogProc{
	private String jobId;
	private String processMonth;
	/**
	 * 月份 : MM
	 */
	private String processMm;
	
	public AuthMonrpt0501ProcImpl(String jobId) {
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
		AuthLogDataDao authLogDataDao = new AuthLogDataDao();
		try {
			// ----------------------------------------------------------------
			// -- 刪除既有資料(待上傳的資料)
			String itemCategory = "FTP_CSD";
			String itemType = this.getReportId();
			fileUploadDao.delete(itemCategory, itemType, processMonth);
			
			// ----------------------------------------------------------------
			// -- 取得該月份的起訖日期
			super.createDateRange(processMonth);
			
			log.info("基準日期: {} - {}", super.getStartDate(), super.getEndDate());
			
			List<Map<String, Object>> listData = authLogDataDao.queryMonrtp501Data(super.getStartDate(), super.getEndDate());
			List<Map<String, Object>> listSummary = authLogDataDao.queryMonrtp501Summary(super.getStartDate(), super.getEndDate());
			int records = listData.size();
			// ----------------------------------------------------------------
			List<String> reportContent = new LinkedList<String>();
			reportContent.addAll(this.reportTitle());
			reportContent.addAll(this.writeLine(listData, false));
			reportContent.addAll(this.writeLine(listSummary, true));
			
			this.writeTo(reportContent, itemCategory, itemType, fileUploadDao, false);
			log.info("Job : {} finish, Total file line count {}", jobId, records);
			returnVo.setSuccess(true);
			returnVo.setMessage("Total " + records + " records");
			return returnVo;
		}finally {
			fileUploadDao.close();
			dao.close();
			authLogDataDao.close();
		}
	}

	
	/**
	 * 寫入csv format
	 * @param list
	 * @param summary
	 * @return
	 * @throws Exception
	 */
	private List<String> writeLine(List<Map<String, Object>> list, boolean summary) throws Exception {
		List<String> reportLines = new LinkedList<String>();
		
		for(Map<String, Object> map : list) {
			
			String purchaseDate = MapUtils.getString(map, "PURCHASE_DATE", "");
			if (summary) {
				purchaseDate = "筆數總計";
			}
			String cardType = MapUtils.getString(map, "CARD_TYPE", "");
			String conditionCode = MapUtils.getString(map, "CONDITION_CODE", "");
			long count = MapUtils.getLongValue(map, "COUNT", 0);
			
			reportLines.add(
					purchaseDate
					+ ","
					+ cardType
					+ ","
					+ conditionCode
					+ ","
					+ count
			);
		}
		return reportLines;
	}
	
	private List<String> reportTitle(){
		List<String> title = new LinkedList<String>();
		title.add("交易日期,卡別,授權結果,筆數,");
		return title;
	}

	@Override
	String getReportId() {
		return "MGAUTH501";
	}
	@Override
	String getReportName() {
		return "非會員銀行之每日人工授權交易分析月報";
	}
	@Override
	String getFileName() {
		return this.processMm + "MGAUTH501.csv";
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
