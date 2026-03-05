package proj.nccc.logsearch;

import javax.servlet.ServletContext;

import com.dxc.nccc.aplog.ApLogConfig;
import com.edstw.web.WebApplication;


public class ProjApplication4Estate extends WebApplication {
	public ProjApplication4Estate(ServletContext serlvetContext) {
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
		
	}

}
