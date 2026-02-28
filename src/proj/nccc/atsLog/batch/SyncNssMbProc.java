package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.SyncNssMbProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - Sync 國內清算DB (NSSMB*
 *
 */
@Log4j2
public class SyncNssMbProc extends BatchBaseMain {
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		
		try {
			if(super.init(jobId)) {
				
				SyncNssMbProcImpl impl = new SyncNssMbProcImpl(jobId);
				
				ReturnVO r = impl.process();
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
		SyncNssMbProc proc = new SyncNssMbProc();
		
		String jobId="SYNC_NSSMB";
		String user="SYSTEM";
		
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
