package proj.nccc.atsLog.batch.service.logAlert;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.SysParaDao;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.dao.entity.SysPara;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.util.SendEmail;
import proj.nccc.atsLog.batch.util.Util;

@Log4j2
public class LogAlertMailService extends AbstractLogAlertService 
									implements AtsLogAlertManager {

	@Override
	public boolean sendAlert(SysEventLog eventLog, String jobId) throws Exception {
		// ------------------------------------------
		// -- Send Mail alert
		List<String> mailList = new LinkedList<String>();
		SysParaDao dao = new SysParaDao();
		Map<String, SysPara> mailMap = dao.getSysParaMap(ProjInit.PARA_ALERT_MAIL);
		for (Entry<String, SysPara> entry : mailMap.entrySet()) {
			SysPara para = entry.getValue();
			if (Util.isValidEmailAddress(para.getValue())) {
				mailList.add(para.getValue());
			}
		}
		if (mailList.size() <= 0) {
			log.info("No mail address in SysPara for System alert.");
			return true;
		}
		SendEmail sendMail = new SendEmail();
		
		String subject = "[atsLogN2] System Alert : [" + eventLog.getEventCode() + "] " 
				+ DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		StringBuffer message = new StringBuffer();
		message.append("Date   : " + DateUtil.dateToString(
				eventLog.getCreateDate(), "yyyy-MM-dd HH:mm:ss") + "<BR>");
		message.append("Server : " + eventLog.getHost() + "<BR>");
		message.append("Event  : " + eventLog.getEventCode() + "<BR>");
		message.append("Alert Level : " + eventLog.getNsshmsglLevel() + "<BR>");
		message.append("Description : " + eventLog.getLogDesc() + "<BR><BR>");		
		sendMail.send(mailList, subject, message.toString());
		
		
		return false;
	}

	@Override
	public void setNssDao(NssDao nssDao) {
		super.nssDao = nssDao;
	}

}
