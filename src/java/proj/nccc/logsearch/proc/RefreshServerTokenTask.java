package proj.nccc.logsearch.proc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.dxc.nccc.aplog.edstw.persist.LogFunctionState;
import com.edstw.persist.PersistUtils;
import com.edstw.persist.PersistableConfig;
import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.task.TaskException;

import proj.nccc.logsearch.ProjServices;


/**
 *
 * @author Hank
 * @version $Revision: 1.0 $
 */
public class RefreshServerTokenTask extends AbstractTokenTask {

	public void doTask() throws TaskException {
		log.info("RefreshTokenTask started...");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		PersistableTransaction pt = null;
		try {
			LogFunctionState para = ProjServices.getLogFunctionStateQS().queryByPrimaryKey("SYS", "TOKEN", "TASK_TOKEN");
			if(para==null) {
				para=new LogFunctionState();
				para.setSysMonth("SYS");
				para.setSystemId("TOKEN");
				para.setFunctionId("TASK_TOKEN");
			}
			para.setFunctionUseCount(Integer.parseInt(sdf.format(today)));

			Random rd = new Random();
			long token = rd.nextLong();
			para.setFunctionName(String.valueOf(token));

			PersistableManager pm = PersistableConfig.getInstance().getPersistableManagerFactory()
					.currentPersistableManager();
			pt = pm.getTransaction();
			pt.begin();
			pm.persist(para);
			pt.commit();
			AbstractTokenTask.setToken(token);
			log.info("runtaskValue:[" + token + "]");
			log.info("RefreshTokenTask done.");

		} catch (Exception e) {
			PersistUtils.handleRollback(pt);
			log.error(e.getMessage(), e);
			throw new TaskException(e.getMessage(), e);
		} finally {
			PersistableConfig.getInstance().getPersistableManagerFactory().checkAndClearCurrentPersistableManager();
		}

	}
}
