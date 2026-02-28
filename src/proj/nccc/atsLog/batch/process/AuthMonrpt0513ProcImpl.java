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
public class AuthMonrpt0513ProcImpl extends AbstractRptAtslogProc{
	private String jobId;
	private String processMonth;
	/**
	 * 月份 : MM
	 */
	private String processMm;
	
	public AuthMonrpt0513ProcImpl(String jobId) {
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
			
			List<Map<String, Object>> listData = authLogDataDao.queryMonrtp513Data(super.getStartDate(), super.getEndDate());
			int records = listData.size();
			// ----------------------------------------------------------------
			List<String> reportContent = new LinkedList<String>();
			reportContent.addAll(this.reportTitle());
			reportContent.addAll(this.writeLine(listData));
			
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
	private List<String> writeLine(List<Map<String, Object>> list) throws Exception {
		List<String> reportLines = new LinkedList<String>();
		
		for(Map<String, Object> map : list) {
			
			String purchaseDate = MapUtils.getString(map, "PURCHASE_DATE", "");
			String purchaseTime = MapUtils.getString(map, "PURCHASE_TIME", "");
			String merchantNo = MapUtils.getString(map, "MERCHANT_NO", "");
			String mccCode = MapUtils.getString(map, "MCC_CODE", "");
			long purchaseAmt = MapUtils.getLongValue(map, "PURCHASE_AMT", 0);
			String bankNo2 = MapUtils.getString(map, "BANK_NO_2", "");
			
			String conditionCode = MapUtils.getString(map, "CONDITION_CODE", "");
			
			reportLines.add(
					purchaseDate
					+ ","
					+ purchaseTime
					+ ","
					+ merchantNo
					+ ","
					+ mccCode
					+ ","
					+ purchaseAmt
					+ ","
					+ bankNo2
					+ ","
					+ conditionCode
					+ ","
			);
		}
		return reportLines;
	}
	
	private List<String> reportTitle(){
		List<String> title = new LinkedList<String>();
		title.add("交易日期,交易時間,商店代號,行業別,金額,銀行別,授權結果,");
		return title;
	}

	@Override
	String getReportId() {
		return "MGAUTH513";
	}
	@Override
	String getReportName() {
		return "AE每日人工授權交易檔";
	}
	@Override
	String getFileName() {
		return this.processMm + "MGAUTH513.csv";
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
