package proj.nccc.atsLog.batch.service.logAlert;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.EventLogDao;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.dao.other.NssDao;

@Log4j2
abstract class AbstractLogAlertService {
	public NssDao nssDao;
	public EventLogDao eventLogDao;
	
	public boolean processEventLog(SysEventLog eventLog, String jobId) throws Exception {
		log.info("eventCode : {}, {}, {}", eventLog.getEventCode()
				, eventLog.getReqSendEmail(), eventLog.getReqSendNssMsg());
		return sendAlert(eventLog, jobId);
	}
	
	/**
	 * - 實作此介面, 傳回實際的alert動作
	 * - 發送 Alert
	 */
	public abstract boolean sendAlert(SysEventLog eventLog, String jobId) throws Exception;
	
	
}
