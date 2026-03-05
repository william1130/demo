package proj.nccc.logsearch.proc;

import com.edstw.lang.DateString;
import com.edstw.task.TaskException;

import proj.nccc.logsearch.ProjServices;


public class LogFunctionStateTask extends AbstractTokenTask
{
	@Override
	public void doTask() throws TaskException
	{
		log.info("LogFunctionStateTask started...");
		log.info("runtaskValue:[" + token + "]");
		try
		{
			if(validToRun()) {
				DateString newMonth = DateString.add( new DateString(), DateString.FIELD_MONTH, 1);
				ProjServices.getLogFunctionStateProc().synchronizeSysMonthData( newMonth.getString("yyyyMM") );
			} 			
			log.info("LogFunctionStateTask done.");
		}
		catch (Exception e)
		{
			log.error( e.getMessage() , e );
			throw new TaskException( e.getMessage() , e );
		}
	}
}
