package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.AuthMonrpt0003Dao;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.util.CommUtil;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.MyStringUtils;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthMonrpt0003ProcImpl extends AbstractRptAtslogProc{
	private String jobId;
	private String processMonth;
	private int pageDataCnt = 25;
	private int pageTotalCnt = 0;
	private int pageCnt = 0;
	private int lineCnt = 0;
	private int totPageCnt = 0;
	private boolean headFlg;
	private long totalCrdCnt = 0;
	
	private int a003WriteCnt = 0;
	private String runDate;
	public AuthMonrpt0003ProcImpl(String jobId) {
		this.jobId = jobId;
		this.runDate = MyDateUtil.yyyymmdd(new Date());
	}

	/**
	 * @param processMonth
	 * @return
	 * @throws Exception
	 */
	public ReturnVO process(String processMonth) throws Exception {
		this.processMonth = processMonth;
		
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {}, process Month : {}", jobId, processMonth);
		SysFileUploadDao fileUploadDao = new SysFileUploadDao();
		ProjDao dao = new ProjDao();
		AuthMonrpt0003Dao authMonrpt0003Dao = new AuthMonrpt0003Dao();
		try {
			// ----------------------------------------------------------------
			// -- 刪除既有資料(待上傳的資料)
			String itemCategory = "EREPORT";
			String itemType = this.getReportId();
			fileUploadDao.delete(itemCategory, itemType, processMonth);
			
			// ----------------------------------------------------------------
			// -- 取得該月份的起訖日期
			super.createDateRange(processMonth);
			
			log.info("基準日期: {} - {}", super.getStartDate(), super.getEndDate());
			
			// ----------------------------------------------------------------
			// -- 資料歸類, 將大資料變成小資料
			temporaryTable0003(dao, super.getStartDate(), super.getEndDate());
			
			List<String> main1 = mainProcess1(authMonrpt0003Dao);
			
			List<String> main2 = mainProcess2(authMonrpt0003Dao);
			
			// ----------------------------------------------------------------
			List<String> reportContent = new LinkedList<String>();
			reportContent.addAll(main1);
			reportContent.addAll(main2);
			
			this.writeTo(reportContent, itemCategory, itemType, fileUploadDao, false);
			log.info("Total file line count : " + a003WriteCnt + " , " + lineCnt);
			log.info("Job : {} finish, total process {} pages ", jobId, totPageCnt);
			returnVo.setSuccess(true);
			returnVo.setMessage("Total " + totPageCnt + " pages");
			return returnVo;
		}finally {
			fileUploadDao.close();
			dao.close();
			authMonrpt0003Dao.close();
		}
	}

	
	private List<String> mainProcess1(AuthMonrpt0003Dao authMonrpt0003Dao) throws Exception {
		List<String> reportLines = new LinkedList<String>();
		
		List<Map<String, Object>> list = authMonrpt0003Dao.queryManCountByCardTypeName();
		headFlg = true; 
		
		for(Map<String, Object> map : list) {
			
			long manCount = MapUtils.getLongValue(map, "MAN_COUNT", 0);
			String cardTypeName = MapUtils.getString(map, "CARD_TYPE_NAME", "");
			if (pageTotalCnt > pageDataCnt){
				headFlg = true; 
			}
			if (headFlg){
				reportLines.addAll(reportTitle(1));
				reportLines.addAll(reportHeading(1));
			}
			reportLines.add(
					" "
					+ "    "
					+ MyStringUtils.rightPad(cardTypeName, 16, " ")
					+ MyStringUtils.leftPad( "" + CommUtil.numberFormat(manCount, null), 15, " ")
			);
			totalCrdCnt += manCount;
			a003WriteCnt ++;
		}
		if (headFlg){
			reportLines.addAll(reportTitle(1));
			reportLines.addAll(reportHeading(1));
		}else{
			reportLines.addAll(reportGrandTotalCrd());
		}
		
		return reportLines;
	}
	
	private List<String> mainProcess2(AuthMonrpt0003Dao authMonrpt0003Dao) throws Exception {
		List<String> reportLines = new LinkedList<String>();
		
		List<Map<String, Object>> list = authMonrpt0003Dao.queryManCount();
		headFlg = true;
		for(Map<String, Object> map : list) {
			
			long manCount = MapUtils.getLongValue(map, "MAN_COUNT", 0);
			String updateId = MapUtils.getString(map, "UPDATE_ID", "");
			if (pageTotalCnt > pageDataCnt){
				headFlg = true; 
			}
			if (headFlg){
				reportLines.addAll(reportTitle(2));
				reportLines.add(fileLine());
				reportLines.addAll(reportHeading(2));
			}
			reportLines.add(
					" "
					+ "    "
					+ MyStringUtils.rightPad(updateId, 16, " ")
					+ MyStringUtils.leftPad( "" + CommUtil.numberFormat(manCount, null), 15, " ")
			);
			totalCrdCnt += manCount;
			a003WriteCnt ++;
		}
		if (headFlg){
			reportLines.addAll(reportTitle(2));
			reportLines.addAll(reportHeading(2));
		}
		return reportLines;
	}

	
	/**
	 * 資料歸類, 將大資料變成小資料
	 * @param dao
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	private void temporaryTable0003(ProjDao dao, String startDate, String endDate) throws Exception {
		String delSql = "DELETE auth_monrpt_0003";
		dao.execSql(delSql, null);
		
		List<Object> params = new LinkedList<Object>();
		params.add(startDate);
		params.add(endDate);
		dao.execUpdate(INSERT_AUTH_MONRPT_0003, params);
	}
	
	private final static String INSERT_AUTH_MONRPT_0003 = 
			"INSERT INTO auth_monrpt_0003 "
			+ " SELECT A.UPDATE_ID AS BF_UPDATE_ID "
			+ "   ,( CASE WHEN B.MAN_ID_NCCC LIKE '50%' AND B.BANK_NO_2 NOT IN ('04', 'AE') "
			+ "         THEN "
			+ "             (CASE WHEN TRIM(A.ACQUIRE_BK) = '99'  AND (LENGTH(A.MERCHANT_NO) = 10 AND A.MERCHANT_NO <> '0000000000') "
			+ "                THEN "
			+ "                   '0' " //-- 會員銀行於中心特店交易 
			+ "                ELSE "
			+ "                   '9'" // -- 會員銀行於非中心特店交易
			+ "                END"
			+ "             )"
			+ "         ELSE "
			+ "             A.CARD_TYPE "
			+ "         END "
			+ "    ) AS CARD_TYPE"
			+ "   ,COUNT(*) AS BF_MAN_COUNT "
			+ ""
			+ " FROM AUTH_LOG_DATA A, "
			+ "      ( SELECT DISTINCT(MAIN_ID) AS BANK_NO_2, MAN_ID_NCCC "
			+ "        FROM SETL_BIN_BANK_PARM c, "
			+ "          SETL_BIN_NEW_BINO_PARM d "
			+ "        WHERE c.fid = d.fid "
			+ "        AND d.acq_iss = 'I' ) B "
			+ ""
			+ " WHERE A.PURCHASE_DATE BETWEEN ? AND ? "
			+ "   AND DATA_SOURCE = '4' "
			+ "   AND TRANS_TYPE LIKE 'A%' "
			+ "   AND DECODE(A.BANK_NO_2,'77','99',A.BANK_NO_2) = B.BANK_NO_2 "
			+ "   AND UPPER(A.DEPT_ID) LIKE 'CSD%' "
			+ ""
			+ " GROUP BY A.UPDATE_ID, B.MAN_ID_NCCC, B.BANK_NO_2, "
			+ "          A.acquire_bk, A.CARD_TYPE,length(A.merchant_no), "
			+ "          A.merchant_no  ";

	private List<String> reportTitle(int rptType) {
		List<String> title = new LinkedList<String>();
		if (!(pageCnt == 0)){
			// -- end_line
			title.add(" ");
			lineCnt ++;
		}
		pageCnt ++;
		totPageCnt++;
		
		// -- header 1-1
		title.add("NCCCBK77MGPA003A    MG");
		lineCnt ++;
		
		if (rptType == 1){
			// header 1-2
			title.add(MyStringUtils.rightPad(
					"      PROGRAM-ID : auth_monrpt_0003 *****               人工授權交易數量分析月報表                   *****" 
					,106, " "
					));
			lineCnt ++;
			// header 1-3
			title.add(MyStringUtils.rightPad(
					"      REPORT-ID  : MGPA003A         ***                                                                ***"
					,106, " "
					)
					+ MyStringUtils.rightPad("     PAGE-CNT : ", 16, " ")
					+ MyStringUtils.leftPad("" + pageCnt , 8 , " ")
					);
			lineCnt ++;
			// ----------------------------------
			// header 1-4
			
			//// -- 取得民國年月
			//String chineseYYYMM = DateUtil.chineseYearMonth(processMonth, true);
			String processMon = processMonth.substring(0,4) + "/" +  processMonth.substring(4);
			
			title.add(
				MyStringUtils.rightPad("                                    *****                  ACCEPT DATE :", 72, " ")
				+ processMon
				+ "                      *****     RUN-DATE : "
				+ DateUtil.dateToString(new Date(), "yyyy/MM/dd")
					);
			lineCnt ++;
			title.add(fileLine());
			lineCnt ++;
		}
		return title;
	}
	
	private List<String> reportHeading(int rptType) {
		List<String> head = new LinkedList<String>();
		
		String reportHead = "    ";
		if (rptType == 1) {
			reportHead += MyStringUtils.rightPad("卡別", 26, " ") + MyStringUtils.leftPad("筆數", 4, " ");
			
		}else {
			reportHead += MyStringUtils.rightPad("USER ID", 26, " ") + MyStringUtils.leftPad("筆數", 4, " ");
		}
		head.add(reportHead);
		
		lineCnt ++;
		head.add(fileLine());
		lineCnt ++;
		pageTotalCnt = 0;
		headFlg = false;
		return head;	
	}
	
	private List<String> reportGrandTotalCrd() {
		List<String> grandTotal = new LinkedList<String>();
		grandTotal.add(fileLine());
		lineCnt ++;
		grandTotal.add("     " 
				+ MyStringUtils.rightPad("TOTAL", 16, " ") 
				+ MyStringUtils.leftPad( "" + CommUtil.numberFormat(totalCrdCnt, null), 15, " "));
		//grandTotal.add(" ");
		lineCnt ++;
		return grandTotal;
	}
	
	
	/**
	 * as separate line
	 * @return
	 * @throws Exception 
	 */
	private String fileLine() {
		return " " + MyStringUtils.rightPad("-", 139, "-");
	}
	
	@Override
	String getReportId() {
		return "MGPA003A";
	}
	@Override
	String getReportName() {
		return "人工授權交易數量分析月報表";
	}
	@Override
	String getFileName() {
		return this.runDate + "mamgpa003a.r";
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
