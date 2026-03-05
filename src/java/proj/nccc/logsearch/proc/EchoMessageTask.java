package proj.nccc.logsearch.proc;

import com.edstw.task.TaskException;

import proj.nccc.logsearch.ProjServices;

public class EchoMessageTask extends AbstractTokenTask {
	@Override
	public void doTask() throws TaskException {
		log.info("EchoMessageTask started...");
		try {
			ProjServices.getEchoMessageProc().saveEchoMessage();
			log.info("EchoMessageTask done.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TaskException(e.getMessage(), e);
		}
	}
}
