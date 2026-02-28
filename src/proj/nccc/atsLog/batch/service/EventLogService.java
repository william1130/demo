package proj.nccc.atsLog.batch.service;

import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.EventLogDao;
import proj.nccc.atsLog.batch.dao.EventMasterDao;
import proj.nccc.atsLog.batch.dao.entity.SysEventMaster;

@Log4j2
public class EventLogService {
	public static int errorCount = 0;
	
	public static void sendLog(String jobId, EventType eventType) throws Exception {
		sendLog(jobId, eventType, "");
	}
	
	public static void sendLog(String jobId, EventType eventType, String description) throws Exception {
		EventMasterDao eventMasterDao = new EventMasterDao();
		Map<String, SysEventMaster> eventMasterMap = eventMasterDao.getSysEventMaster();
		if (description != null && description.length() > 250){
			description = description.substring(0, 250);
		}
		EventLogDao eventLogDao = new EventLogDao();
		synchronized (EventLogService.class) {
			try {
				log.info(String.format("EventLog.sendLog[%s]", eventType.name()));
				SysEventMaster eventMaster = eventMasterMap.get(eventType.getEventCode());
				if (eventType == EventType.PROG_EXE_FAIL) {
					eventLogDao.insertEventLog(jobId, eventMaster, description);
				}else if (eventType == EventType.DB_ALERT) {
					if (eventLogDao.checkEventLogByDescription(eventMaster, description) 
							> eventMaster.getNextEventTime()) {
						eventLogDao.insertEventLog(jobId, eventMaster, description);
					}
				}else {
					if (eventLogDao.checkEventLog(eventMaster) >= eventMaster.getNextEventTime()) {
						eventLogDao.insertEventLog(jobId, eventMaster, description);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				e.printStackTrace();
				throw new Exception(e);
			}finally {
				try {
					eventLogDao.close();
				}catch(Exception x) {}
				try {
					eventMasterDao.close();
				}catch(Exception x) {}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public enum EventType {
		
		UNKNOWN("Unknown"),
		CPU_ALERT("CpuAlert"),
		DB_ALERT("DbAlert"),
		DISK_ALERT("DiskAlert"),
		EMAIL_FAIL("EmailFail"),
		FTP_CONN_FAIL("FtpConnFail"),
		MEMORY_ALERT("MemoryAlert"),
		NSSM_DB_FAIL("NssmDbFail"),
		PROG_ACTIVE("ProgActive"),
		PROG_EXE_FAIL("ProgExeFail"),
		PROG_EXE_LIMIT_ALERT("ProgExeLimitAlert"),
		PROG_EXE_TIME_ALERT("ProgExeTimeAlert");
		
		private String eventCode;
		private String eventName;

		EventType(String eventCode) {
			this.eventCode = eventCode;
		}
		
		public String getEventCode() {
			return eventCode;
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

	}
	
	
}
