package proj.nccc.atsLog.batch.service.logAlert;

import proj.nccc.atsLog.batch.dao.EventLogDao;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.dao.other.NssDao;

public class LogAlertNssmsgService extends AbstractLogAlertService 
									implements AtsLogAlertManager {

	@Override
	public boolean sendAlert(SysEventLog eventLog, String jobId) {
		EventLogDao logDao = new EventLogDao();
		try {
			logDao.writeR6NssMsg(eventLog.getHost() + eventLog.getLogDesc(), jobId, eventLog.getNsshmsglLevel());
			return true;
		}catch(Exception x) {
			return false;
		}finally {
			logDao.close();
		}
	}

	@Override
	public void setNssDao(NssDao nssDao) {
		super.nssDao = nssDao;
	}

}
