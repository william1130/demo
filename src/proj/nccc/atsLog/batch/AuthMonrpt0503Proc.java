package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.AuthMonrpt0503ProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - 執行auth_monrpt_0503 (AE每日人工授權交易分析月報表(MGAUTH503))
 *
 */
@Log4j2
public class AuthMonrpt0503Proc extends BatchBaseMain {
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		
		try {
			if(super.init(jobId)) {
				// -----------------------------------------
				// -- 若未指定月份區間則預設為系統日的上個月
				String processMonth = super.getProcessMonth(args);
				
				AuthMonrpt0503ProcImpl impl = new AuthMonrpt0503ProcImpl(jobId);
				
				ReturnVO r = impl.process(processMonth);
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
		AuthMonrpt0503Proc proc = new AuthMonrpt0503Proc();
		
		String jobId="MGAUTH503";
		String user="SYSTEM";
		
		// 取得指定日期, 否則預設為上個月
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
