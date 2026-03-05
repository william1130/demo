package proj.nccc.logsearch.proc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dxc.nccc.aplog.edstw.persist.LogDetailImpl;
import com.dxc.nccc.aplog.edstw.persist.LogMasterImpl;
import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.persist.StatefulPersistableUtil;
import com.edstw.process.ProcessException;
import com.edstw.service.ServiceException;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.BaseCRUDQS;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EchoMessageProcImpl extends AbstractBaseCRUDProc implements EchoMessageProc {
	private static final Logger log = LogManager.getLogger(EchoMessageProcImpl.class);

	public EmvProjPersistable createEmptyProjPersistable() {
		return null;
	}

	public String getServiceName() {
		return "EchoMessage Process";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
	}

	protected void mergeOnModify(ProjPersistableArg arg, EmvProjPersistable entity) throws Exception {
		StatefulPersistableUtil.copyPropertiesWithoutState(entity, arg);
	}

	@Override
	public void saveEchoMessage() throws Exception {
		PersistableManager pm = super.currentPersistableManager();
		PersistableTransaction pt = pm.getTransaction();
		try {
			pt = pm.getTransaction();
			pt.begin();
			final String guid = UUID.randomUUID().toString();
			LogMasterImpl master = new LogMasterImpl();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setLenient(false);
			master.setGuid(guid);
			final String nowDateTime = sdf.format(new Date());
			master.setWriteDate(nowDateTime.substring(0, 8));
			master.setWriteTime(nowDateTime.substring(8));
			master.setAccessDate(nowDateTime.substring(0, 8));
			master.setAccessTime(nowDateTime.substring(8));
			final String sysId = "AP004";
			master.setSystemId(sysId);
			final String userId = "QRadar";
			master.setUserId(userId);
			final String userName = "AP系統正常運作_M";
			master.setUserName(userName);

			LogDetailImpl detail = new LogDetailImpl();
			detail.setGuid(guid);
			detail.setWriteDate(nowDateTime.substring(0, 8));
			detail.setWriteTime(nowDateTime.substring(8));
			final String sqlCode = "AP系統正常運作_D";
			detail.setSqlCode(sqlCode);
			log.info("EchoMessage SysID:" + sysId + " UserID:" + userId);

			pm.persist(master);
			pm.persist(detail);

			pt.commit();
			return;
		} catch (Exception e) {
			try {
				pt.rollback();
			} catch (Exception e1) {
				getLog().error(e1.getMessage(), e);
			}
			getLog().error(e.getMessage(), e);
			throw new ProcessException(e.getMessage(), e);
		}
	}

	@Override
	public BaseCRUDQS getBaseCRUDQS() {
		return null;
	}

}
