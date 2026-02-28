package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.AuthBackdata0160ProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * 產錄AP、DP交易回灌AUTH LOG_MGBA160
 *
 */
@Log4j2
public class AuthBackdata0160Proc extends BatchBaseMain {
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		
		try {
			if(super.init(jobId)) {
				// -----------------------------------------
				// -- 若未指定日期區間則預設為系統日 - 1
				super.createDateRange(args);
				AuthBackdata0160ProcImpl impl = new AuthBackdata0160ProcImpl(jobId);
				
				boolean idFlag = false;
				if (args != null && args.length >= 2) {
					idFlag = Boolean.valueOf(args[1]);
				}
				ReturnVO r = impl.process(super.getStartDate(), idFlag);
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
		AuthBackdata0160Proc proc = new AuthBackdata0160Proc();
		
		String jobId="MGBA160";
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
