package proj.nccc.logsearch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletContext;

import com.dxc.nccc.aplog.ApLogConfig;
import com.dxc.nccc.aplog.edstw.persist.LogFunctionState;
import com.edstw.lang.DateString;
import com.edstw.persist.PersistUtils;
import com.edstw.persist.PersistableConfig;
import com.edstw.persist.PersistableManager;
import com.edstw.persist.PersistableTransaction;
import com.edstw.user.UserLogger;
import com.edstw.web.WebApplication;

import proj.nccc.logsearch.proc.AbstractTokenTask;

/**
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.1 $
 */
public class ProjApplication extends WebApplication {
	public ProjApplication(ServletContext serlvetContext) {
		super(serlvetContext);
	}

	@Override
	public void initialProject()
	{
		//若前面沒有出錯, 才做初始化
		if( this.getFailCause() == null )
		{
			try
			{
				ApLogConfig apLogConfig = ApLogConfig.getInstance();
				String apLogFile = super.getSerlvetContext().getInitParameter( "aplog-file");
				apLogConfig.initialize(super.getResourceAsStream( apLogFile ));
				ProjServices.getLogFunctionStateProc().synchronizeSysMonthData( new DateString().getString("yyyyMM") );
			}
			catch( Exception e )
			{
				this.failCause = e;
			}
			this.reloadTaskToken();
		}
		super.initialProject();
	}
	
	public void reloadTaskToken()
	{
		PersistableTransaction pt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		try
		{
			LogFunctionState para = ProjServices.getLogFunctionStateQS().queryByPrimaryKey("SYS", "TOKEN", "TASK_TOKEN");
			if(para==null) {
				para=new LogFunctionState();
				para.setSysMonth("SYS");
				para.setSystemId("TOKEN");
				para.setFunctionId("TASK_TOKEN");
			}
			para.setFunctionUseCount(Integer.parseInt(sdf.format(today)));
			
			Random rd=new Random();
			long token=rd.nextLong();
			para.setFunctionName(String.valueOf(token));
			
			PersistableManager pm = PersistableConfig.getInstance().getPersistableManagerFactory().currentPersistableManager();
			pt = pm.getTransaction();
			pt.begin();
			pm.persist( para );
			pt.commit();
			AbstractTokenTask.setToken( token );
			UserLogger.getLog(this.getClass()).info("Initial Task token:[" + token + "]");
		} catch( Exception e )
		{
			PersistUtils.handleRollback( pt );
			this.failCause = e;
		}
		finally
		{
			PersistableConfig.getInstance().getPersistableManagerFactory().checkAndClearCurrentPersistableManager();
		}
	}

}
