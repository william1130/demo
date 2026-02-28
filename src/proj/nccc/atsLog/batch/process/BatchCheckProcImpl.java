package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.BatchLogDao;
import proj.nccc.atsLog.batch.dao.BatchMasterDao;
import proj.nccc.atsLog.batch.dao.EventLogDao;
import proj.nccc.atsLog.batch.dao.entity.BatchLog;
import proj.nccc.atsLog.batch.dao.entity.BatchMaster;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class BatchCheckProcImpl  {
	private String jobId;
	public BatchCheckProcImpl(String jobId) {
		this.jobId = jobId;
	}
	
	public ReturnVO process() throws Exception {
		BatchMasterDao batchMasterDao = new BatchMasterDao();
		BatchLogDao batchLogDao = new BatchLogDao();
		EventLogDao eventLogDao = new EventLogDao();
		
		List<BatchMaster> batchMasterList = batchMasterDao.getBatchMasterList();
		log.info("JobId : " + this.jobId);
		ReturnVO returnVo = new ReturnVO();
		try {
			for (BatchMaster batchMaster : batchMasterList) {
				if (StringUtils.isBlank(batchMaster.getBatchCycleType()) 
						|| batchMaster.getBatchCycleTime() <= 0) {
					log.error("Not define CycleType or CycleTime = 0 in SYS_BATCH_MASTER {}", batchMaster.getBatchId());
				}else {
					log.info("batch Id : {}, {}, {}", batchMaster.getBatchId(), batchMaster.getBatchCycleType(), 
							batchMaster.getBatchCycleTime());
					// -------------------------------------------------
					// -- 最低單位為"分鐘", 一律為整數
					// -- M : 月
					// -- D : 天
					// -- H : 小時
					// -- m : 分鐘
					int cycleTime = batchMaster.getBatchCycleTime();
					Date currentDate = new Date();
					Date cutDate = null;
					if ("M".equals(batchMaster.getBatchCycleType())) {
						cutDate = MyDateUtil.nextTime(currentDate, cycleTime, "BEFORE", "MONTH");
					}else if ("D".equals(batchMaster.getBatchCycleType())) {
						cutDate = MyDateUtil.nextTime(currentDate, cycleTime, "BEFORE", "DAY");
					}else if ("H".equals(batchMaster.getBatchCycleType())) {
						cutDate = MyDateUtil.nextTime(currentDate, cycleTime, "BEFORE", "HOUR");
					}else if ("m".equals(batchMaster.getBatchCycleType())) {
						cutDate = MyDateUtil.nextTime(currentDate, cycleTime, "BEFORE", "MINUTE");
					}
					
					// ----------------------------------------------------------------------
					// -- 檢查 batchLog 取得最後一筆
					// -- 1. 規定時間是否有執行
					// -- 2. 執行時間是否超時
					log.info("validating batch job : {} ", batchMaster);
					BatchLog bl = batchLogDao.queryLastItem(batchMaster.getBatchId());
					log.info("batch log : {} ", bl);
					if (bl == null) {
						log.info("Batch {} no execute log", batchMaster.getBatchId());
						this.writeEventLog(eventLogDao, batchMaster, ProjInit.PROG_NO_RUN, null);
					}else {
						Date baseTime = MyDateUtil.nextTime(currentDate, 1, "BEFORE", "HOUR");
						
						if (bl.getBatchStartDate().compareTo(baseTime) > 0) {
							// -----------------------------------
							// -- 僅 1小時內的 log 作為超時判斷
							// -----------------------------------
							// -- 判斷是否在規定時間內
							if (cutDate.compareTo(bl.getBatchStartDate()) > 0) {
								// -------------------------------
								// -- 規定時間比執行時間大 : 表示尚未執行
								log.info("Batch {} last execute date {} less {}", 
										batchMaster.getBatchId(), bl.getBatchStartDate(), cutDate);
								this.writeEventLog(eventLogDao, batchMaster, ProjInit.PROG_NO_RUN, bl);
							}else {
								// -------------------------------------
								// -- 判斷該筆是否超時PROG_RUN_OVER_TIME
								// -- 若該Job 尚未結束則以現在時間判斷
								Date d = new Date();
								if (bl.getBatchEndDate() != null) {
									d = bl.getBatchEndDate();
								}
								long runtime = MyDateUtil.dateDiff(bl.getBatchStartDate(), d, "MINUTES");
								if (runtime > batchMaster.getExecWarningOverTime()) {
									log.info("Batch {} run overtime {} greater than {}", 
											batchMaster.getBatchId(), runtime, batchMaster.getExecWarningOverTime());
									this.writeEventLog(eventLogDao, batchMaster, ProjInit.PROG_RUN_OVER_TIME, bl);
								}
							}
						}
					}
				}
			}
			log.info("finish batch check total {} Job", batchMasterList.size());
			returnVo.setMessage("[CHECK_BATCH] Complete!");
			returnVo.setSuccess(true);
		} catch(Exception x) {
			x.printStackTrace();
			log.error(x);
			returnVo.setSuccess(false);
			returnVo.setMessage(x.getMessage());
			throw new Exception( x.getMessage(),x );
		} finally {
			batchMasterDao.close();
			batchLogDao.close();
			eventLogDao.close();
		}
		return returnVo;
	}
	
	/**
	 * 寫入EventLog 
	 * @param batchId
	 * @param eventCode : 未執行(ProgExeTimeAlert)或超時(ProgExeLimitAlert)
	 * @throws Exception 
	 */
	private void writeEventLog(EventLogDao eventLogDao, BatchMaster batchMaster, 
			String eventCode, BatchLog batchLog) throws Exception {
		// -- TODO
		// -- include eventLog was insert
		int noAlertMinus = batchMaster.getNoAlertMinus();
		if (noAlertMinus > 0) {
			// ------------------------------------------
			//-- n分鐘內若已有發送Alert則 ignore this
			Date cutDate = MyDateUtil.nextTime(new Date(), noAlertMinus, "BEFORE", "MINUTE");
			List<SysEventLog> list = eventLogDao.getEventLogAfter(
					cutDate, batchMaster.getBatchId(), eventCode, ProjInit.HOST_NAME);
			if (list != null && list.size() > 0) {
				SysEventLog o = list.get(0);
				log.info("Event {}/{} have been alert : {} on {}[{}/{}] and sendDate [ {} ]", 
						eventCode, batchMaster.getBatchId(), 
						ProjInit.HOST_NAME, o.getCreateDate(),cutDate , 
						o.getSendDate());
				return;
			}else {
				// --------------------------------------
				// -- insert eventLog
				SysEventLog o = new SysEventLog();
				o.setEventCode(eventCode);
				o.setEventItem(batchMaster.getBatchId());
				o.setHost(ProjInit.HOST_NAME);
				if (eventCode.equalsIgnoreCase(ProjInit.PROG_NO_RUN)) {
					o.setLogDesc("批次程式(" + batchMaster.getBatchId() + ")區間內未執行");
				}else if (eventCode.equalsIgnoreCase(ProjInit.PROG_RUN_OVER_TIME)) {
					String batchSatrt = "";
					String batchEnd = "";
					if (batchLog != null && batchLog.getBatchStartDate() != null) {
						batchSatrt = DateUtil.dateToString(batchLog.getBatchStartDate(), "yyyyMMdd HHmmss");
					}
					if (batchLog != null && batchLog.getBatchEndDate() != null) {
						batchEnd = DateUtil.dateToString(batchLog.getBatchEndDate(), "yyyyMMdd HHmmss");
					}
					o.setLogDesc("批次程式(" + batchMaster.getBatchId() + ")執行超時 "
							+ " " + batchSatrt + " - " + batchEnd);
				}else {
					o.setLogDesc(eventCode);
				}
				o.setReqSendNssMsg(batchMaster.getNoticeType().substring(0, 1));
				o.setReqSendEmail(batchMaster.getNoticeType().substring(1, 2));
				o.setNsshmsglLevel(batchMaster.getNsshmsglLevel());
				eventLogDao.insertEventLog(o);
				log.info("Event {}/{} insert eventLog success. {}", 
						eventCode, batchMaster.getBatchId(), o);
			}
		}else {
			log.info("With no alert cause atsLogBatchMaster.noAlertMinus not greater than 0");
		}
	}


}
