package proj.nccc.atsLog.batch;

import java.util.Date;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.Nsshmsgl;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.process.BatchCheckProcImpl;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class BatchCheckProc extends BatchBaseMain {

	public BatchCheckProc() {
		ThreadContext.put("LOG_CATEGORY", "SCAN_CHECK");
	}
	public void process(String jobId,String user,String[] args) throws Exception
	{
		System.out.println("["+jobId+"] Start ..");
		if (super.init(jobId, true)){
			log.debug( "["+jobId+"], Start");
			try{
				ReturnVO r = null;
				BatchCheckProcImpl b = new BatchCheckProcImpl(jobId);
				r = b.process();
				if (r.isSuccess()){
					super.close("CLOSE", r.getMessage());
				}else{
					super.close("ERROR", r.getMessage());
				}
			} catch(Exception f){
				f.printStackTrace();
				log.debug( "["+jobId+"] ,Exception= " + f, f);
				super.close("ERROR",f.getMessage());
			}
			log.debug( "["+jobId+"], Close ");
		}else{
			super.close();
		}
	}

	public static void main(String[] args)
	{
		BatchCheckProc proc = new BatchCheckProc();
			
		String jobId="BatchCheck";
		String user="SYSTEM";
		try{
			// ----------------------------------------
			// -- 偵測批次執行情形
			proc.process(jobId,user,args);
			
			System.out.println("["+jobId+"] End ..");
		} catch(Exception x) {
			try {
				Nsshmsgl nssLog = new Nsshmsgl();
				nssLog.setPid(ProjInit.SYS_NAME + "_" + jobId);
				nssLog.setCode("ERROR");
				nssLog.setErrMsg("系統連線錯誤。請確認DB連線是否正常！");
				nssLog.setSysMsg(x.getMessage());
				nssLog.setLevel("3");
				nssLog.setSysDate(new Date());
				nssLog.setType("A");
				new NssDao().insertR6Log(nssLog);
			} catch (Exception e) {}
			x.printStackTrace();
			System.out.println("["+jobId+"] Exception :"+x);
		}
    }
}
