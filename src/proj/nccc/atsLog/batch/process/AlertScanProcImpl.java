package proj.nccc.atsLog.batch.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.EventLogDao;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.service.logAlert.LogAlertMailService;
import proj.nccc.atsLog.batch.service.logAlert.LogAlertNssmsgService;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AlertScanProcImpl {
	private String jobId;
	private EventLogDao eventLogDao;
	private NssDao nssDao;
	public AlertScanProcImpl(String jobId) {
		this.jobId = jobId;
		this.eventLogDao = new EventLogDao();
		this.nssDao = new NssDao();
	}

	public ReturnVO process() throws Exception {
		ReturnVO returnVo = new ReturnVO();
		
		// ---------------------------------------------
		try {
//			int count = 0;
			Map<String, List<SysEventLog>> alertMap = new HashMap<String, List<SysEventLog>>();
			List<SysEventLog> list = eventLogDao.queryByNotProcess();
			String lastMapKey = "";
			List<SysEventLog> nssList = null;
			for(SysEventLog eventLog : list) {
				try {
					// --------------------------------------
					// -- 利用Map 處理, 確保同一個 Event
					// -- 同一個 event 只發送一個
					String mapKey = eventLog.getHost() + eventLog.getEventCode();
					if (!lastMapKey.equals(mapKey)) {
						if (nssList != null) {
							log.debug("map key {}, size {}", lastMapKey, nssList.size());
						}
						nssList = new LinkedList<SysEventLog>();
						nssList.add(eventLog);
						alertMap.put(mapKey, nssList);
						lastMapKey = mapKey;
					}else {
						nssList.add(eventLog);
					}
				}catch(Exception e) {
					log.error(e.getMessage(), e);
					returnVo.setSuccess(false);
					returnVo.setMessage(e.getMessage());
				}
				// -----------------------------------------
				// -- 同訊息成功也需更新
				eventLogDao.updateEventLog(eventLog.getUuid());
				log.info("update event Log : " + eventLog.getUuid());
			}
			if (alertMap.size() > 0) {
				this.sendAlert(alertMap);
			}else {
				log.info("Alert message is empty.");
			}
			
			returnVo.setSuccess(true);
			returnVo.setMessage("Total alert item : " + list.size() + " / " + alertMap.size());
		}finally {
			eventLogDao.close();
			nssDao.close();
		}
		return returnVo;
	}
	
	private void sendAlert(Map<String, List<SysEventLog>> alertMap) {
		// --------------------------------------------------------
		// -- 根據不同的去向,進行發送
		try {
			LogAlertNssmsgService nssmsgService = new LogAlertNssmsgService();
			LogAlertMailService alertMailService = new LogAlertMailService();
			
			for (Entry<String, List<SysEventLog>> entry : alertMap.entrySet()) {
				String eventCodeKey = entry.getKey();
				List<SysEventLog> eventLogList = entry.getValue();
				log.info("eventCodeKey : " + eventCodeKey +" , size : " + eventLogList.size());
				
				String lastLogDesc = "";
				boolean toUpdateEventLog = false;
				for(SysEventLog eventLog: eventLogList) {
					if (!lastLogDesc.equals(eventLog.getLogDesc())){
						// -----------------------------------------
						// -- 訊息相同只處理一個
						try {
							if ("Y".equals(eventLog.getReqSendNssMsg())) {
								nssmsgService.setNssDao(nssDao);
								nssmsgService.processEventLog(eventLog, jobId);
								toUpdateEventLog = true;
							}
						}catch(Exception x) {
							EventLogService.sendLog(jobId, EventType.NSSM_DB_FAIL);
						}
						try {
							if ("Y".equals(eventLog.getReqSendEmail())) {
								alertMailService.processEventLog(eventLog, jobId);
								toUpdateEventLog = true;
							}
						}catch(Exception x) {
							log.error(x.getMessage(), x);
							EventLogService.sendLog(jobId, EventType.EMAIL_FAIL);
						}
//						eventLogDao.updateEventLog(eventLog.getUuid());
						log.info("update event Log : " + eventLog.getUuid());
						lastLogDesc = eventLog.getLogDesc();
					}
				}
				if (toUpdateEventLog) {
					log.info("UpdateEventLog" + toUpdateEventLog);
				}
			}
		}catch(Exception x) {
			log.error(x.getMessage(), x);
		}
		
	}

}
