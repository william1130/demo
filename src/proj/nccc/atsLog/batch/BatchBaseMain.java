package proj.nccc.atsLog.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.BatchLogDao;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.ProjInit;

@Log4j2
public class BatchBaseMain {
	public BatchBaseMain() {
	}

	private String jobId;
	private String status;
	private String batchLogUuid;
	private ProjInit projInit;
	private boolean writeLog;
	
	private String startDate;
	private String endDate;
	
	private Date startDateTime;
	private Date endDateTime;

	public boolean init(String jobId) throws Exception {
		return this.init(jobId, true);
	}

	public boolean init(String jobId, boolean writeLog) throws Exception {
		boolean bRet = true;
		this.jobId = jobId;
		this.writeLog = writeLog;
		
		projInit = new ProjInit();
		try {
			projInit.getInitParm();
		} catch (Exception d) {
			d.printStackTrace();
			log.error("[" + jobId + "] get Init Parameter file FAIL :" + d.getMessage());
			bRet = false;
		}
		ThreadContext.put("BATCH_ID", jobId);
		ThreadContext.put("ACTIVE_HHMMSS", DateUtil.dateToString(new Date(), "HHmmss"));
		log.debug( "[" +jobId+"] Start Running:");
		log.debug( "["+jobId+"] init success : "+bRet);
		if (bRet) {
			this.status = "START";
		}else {
			this.status = "ERROR";
		}
		this.batchStartUp(jobId, status);
		
		return bRet;
	}
	
	public void close(String status, String description, String alertLevel) throws Exception {
		this.status = status;
		updateBatchLog(status, description);
		if ("ERROR".equals(status)) {
			EventLogService.sendLog(jobId, EventType.PROG_EXE_FAIL);
		}
	}
	public void close(String status, String description) throws Exception {
		this.status = status;
		updateBatchLog(status, description);
		if ("ERROR".equals(status)) {
			EventLogService.sendLog(jobId, EventType.PROG_EXE_FAIL);
		}
	}

	public void close() throws Exception {
		this.close(status, null);
	}

	public void batchStartUp(String jobId, String status) throws Exception {
		if (this.writeLog) {
			BatchLogDao dao = new BatchLogDao();
			this.batchLogUuid = dao.insertBatchLog(jobId, status);
			dao.close();
		}
		System.out.println("Start..." + new Date());
	}

	public void updateBatchLog(String status, String description) throws Exception {
		if (this.writeLog) {
			BatchLogDao dao = new BatchLogDao();
			dao.updateBatchLog(this.batchLogUuid, status, description);
			dao.close();
		}
	}
	
	
	/**
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public String getProcessDate(String[] args) throws Exception {
		Date date = new Date();
		String procDate = MyDateUtil.yyyymmdd(date);
		
		if (args != null)
		{
			for (String arg : args)
			{
				if ('-' == arg.charAt(0)){
					switch (arg.charAt(1)){
						case 'd':
							if (arg.length() > 2) {
								procDate = arg.substring(2, arg.length());
								// -- 日期格式是否合法
								new SimpleDateFormat("yyyyMMdd").parse(procDate);
							}
							break;
						default:
							throw new Exception("not support args : [" + arg.charAt(1) + "]");
					}
				}
			}
		}
		log.info("["+jobId+"] arg -dYYYYMMDD");
		return procDate;
	}
	
	public String getProcessMonth(String[] args) throws Exception {
		Date date = new Date();
		String procDate = MyDateUtil.yyyymmdd(date);
		
		if (args != null)
		{
			for (String arg : args)
			{
				if ('-' == arg.charAt(0)){
					switch (arg.charAt(1)){
						case 'd':
							if (arg.length() > 2) {
								procDate = arg.substring(2, arg.length()) + "01";
								// -- 日期格式是否合法
								new SimpleDateFormat("yyyyMMdd").parse(procDate);
								return arg.substring(2, arg.length());
							}
							break;
						default:
							throw new Exception("not support args : [" + arg.charAt(1) + "]");
					}
				}
			}
		}
		log.info("["+jobId+"] arg -dYYYYMM");
		
		return MyDateUtil.lastyyyymm(procDate);
	}	
	
	public void createDateRange(String[] args) {
		
		Date pdate = MyDateUtil.nextTime(new Date(), 1, "BEFORE", "DAY");
		
		this.startDate = DateUtil.dateToString(pdate, "yyyyMMdd");
		this.endDate = this.startDate;
		log.info("["+jobId+"] Date range : default : {},{}", this.startDate, this.endDate);
		if (args != null)
		{
			// -----------------------------------------
			// -- 指定日期(單天) -s20191201
			// -- 指定日期(區間) -s20191201 -e20191211
			// -- 指定月份(月份) -m201912
			// -- 指定日期時分(區間 yyyymmddHHMM) -H202007230900 -h202007231000
			for (String arg : args)
			{
				if ('-' == arg.charAt(0))
				{
					switch (arg.charAt(1))
					{
						case 's':
							if (arg.length() > 2) {
								startDate = arg.substring(2, arg.length());
								endDate = startDate;
							}
							break;
						case 'e':
							if (arg.length() > 2) {
								endDate = arg.substring(2, arg.length());
							}
							break;
						case 'm':
							if (arg.length() > 2) {
								String yyyymm = arg.substring(2, arg.length());
								startDate = yyyymm + "01";
								endDate = DateUtil.getMonthEnd(yyyymm);
							}
							break;
						case 'H':
							if (arg.length() > 2) {
								String date1 = arg.substring(2, arg.length());
								// yyyymmddHHMMSS
								startDateTime = DateUtil.stringToDate(date1 + "00", "yyyyMMddHHmmss");
							}
							break;
						case 'h':
							if (arg.length() > 2) {
								String date2 = arg.substring(2, arg.length());
								// yyyymmddHHMMSS
								endDateTime = DateUtil.stringToDate(date2 + "59", "yyyyMMddHHmmss");
							}
							break;
							
					}
				}
			}
			log.info("["+jobId+"] arg -sYYYYMMDD -eYYYYMMDD");
			log.info("["+jobId+"] arg -mYYYYMM");
			if (startDateTime != null) {
				if (endDateTime == null) {
					endDateTime = MyDateUtil.nextTime(
							new Date(), 1, "BEFORE", "MINUTE");
				}
				log.info("["+jobId+"] Date range have been modified : {},{}", this.startDateTime, this.endDateTime);
			}else {
				log.info("["+jobId+"] Date range have been modified : {},{}", this.startDate, this.endDate);
			}
		}
	}

	public ProjInit getProjInit() {
		return projInit;
	}

	public void setProjInit(ProjInit projInit) {
		this.projInit = projInit;
	}
	
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public static void main(String[] args)
	{
		System.out.println("Welcome to atsLogN2 batch");
	}	
}
