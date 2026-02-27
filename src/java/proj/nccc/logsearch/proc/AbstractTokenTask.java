package proj.nccc.logsearch.proc;

import com.dxc.nccc.aplog.edstw.persist.LogFunctionState;
import com.edstw.task.AbstractTask;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvRefSpec;
import proj.nccc.logsearch.qs.EmvRefSpecQS;

/**
 * @author 
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTokenTask extends AbstractTask
{
	protected static Long token;
	
	private boolean ignoreToken;
	
	public static void setToken( Long taskToken )
	{
		token = taskToken;
	}

	public boolean validToRun()
	{
		try
		{
			if( ignoreToken )
			{
				log.info( "IgnoreToken is set. Skip check Task Token : " + token );
				return true;
			}
//			if( !ProjConfig.getInstance().isCheckTaskToken() )
//			{
//				log.info( "CheckTaskToken set to false in proj-config.xml, skip task token checking." );
//				return true;
//			}
			log.info( "Check Task Token : " + token );
			LogFunctionState para = ProjServices.getLogFunctionStateQS().queryByPrimaryKey("SYS", "TOKEN", "TASK_TOKEN");
			
			if (para == null) {
				return false;
			}
			long savedToken = Long.parseLong( para.getFunctionName());
			
			if(savedToken != token) {
				log.info("runtaskValue:[" + token + "] vs ["+savedToken+"] not match!");
				return false;
			} 
			return true;
		}
		catch(Exception e)
		{
			log.error( e.getMessage(), e );
			throw new RuntimeException(e.getMessage(), e );
		}
	}

	public boolean isIgnoreToken()
	{
		return ignoreToken;
	}

	public void setIgnoreToken(boolean ignoreToken)
	{
		this.ignoreToken = ignoreToken;
	}
	
}
