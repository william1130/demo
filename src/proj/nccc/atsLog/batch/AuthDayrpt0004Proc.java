package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.AuthDayrpt0004ProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - 執行auth_dayrpt_0004 (人工強制授權報表(MGPA004))
 *
 */
@Log4j2
public class AuthDayrpt0004Proc extends BatchBaseMain {
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		super.createDateRange(args);
		try {
			if(super.init(jobId)) {
				// -----------------------------------------
				// -- 預設為系統日-1
				String processDay = super.getStartDate();
				
				AuthDayrpt0004ProcImpl impl = new AuthDayrpt0004ProcImpl(jobId);
				
				ReturnVO r = impl.process(processDay);
				if(r.isSuccess()){
					super.close("CLOSE", r.getMessage());
				}else {
					super.close("ERROR", r.getMessage());
				}
			}else {
				throw new Exception("Init fail");
			}
		}catch(Exception x) {
			log.error(x.getMessage(), x);
			EventLogService.sendLog(jobId, EventType.PROG_EXE_FAIL, jobId + ":" + x.getMessage());
			super.close("ERROR", x.getMessage());
		}
		log.info( "["+jobId+"], Close ");
	}


	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		AuthDayrpt0004Proc proc = new AuthDayrpt0004Proc();
		
		String jobId="MGPA004";
		String user="SYSTEM";
		
		// 取得指定日期, 否則預設為系統日 - 1
		try{
			proc.process(jobId, user, args);
			System.out.println("["+jobId+"] End ..");
		}catch(Exception x)
		{
			x.printStackTrace();
			System.out.println("["+jobId+"] Exception :"+x);
		}
	}

}
