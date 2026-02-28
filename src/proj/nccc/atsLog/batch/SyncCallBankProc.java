package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.SyncCallBankProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - 風控系統DB-國外卡callbank參數 (SYNC_CALLBANK)
 *
 */
@Log4j2
public class SyncCallBankProc extends BatchBaseMain {
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		
		try {
			if(super.init(jobId)) {
				
				SyncCallBankProcImpl impl = new SyncCallBankProcImpl(jobId);
				
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
		SyncCallBankProc proc = new SyncCallBankProc();
		
		String jobId="SYNC_CALLBANK";
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
