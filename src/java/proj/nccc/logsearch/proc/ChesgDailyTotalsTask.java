package proj.nccc.logsearch.proc;

import com.edstw.task.TaskException;

import proj.nccc.logsearch.ProjServices;

public class ChesgDailyTotalsTask extends AbstractTokenTask {
	@Override
	public void doTask() throws TaskException {
		log.info("ChesgDailyTotalsTask started...");
		log.info("runtaskValue:[" + token + "]");
		try {
			if (validToRun()) {
				ProjServices.getChesgDailyTotalsProc().ChesgDailyTotalsTask();
			}
			log.info("ChesgDailyTotalsTask done.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TaskException(e.getMessage(), e);
		}
	}
}
