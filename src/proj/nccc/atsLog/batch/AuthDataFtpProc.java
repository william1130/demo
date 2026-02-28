package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.AuthDataFtpProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthDataFtpProc extends BatchBaseMain {

	public static String ftpPara = "FTP_TC40";
	public static String fileName = "";
	// ------------------------------------------------------------
	// -- 由FTP取得 Auth Log Data

	public void process(String jobId, String user, String[] args) throws Exception {

		System.out.println("[" + jobId + "] Start ..");

		try {
			if (args.length < 1) {
				throw new Exception("No variable.");
			}
			
			if (super.init(jobId)) {

				AuthDataFtpProcImpl impl = new AuthDataFtpProcImpl(jobId);
				fileName = args[0];
				ReturnVO r = impl.process();
				if (r.isSuccess()) {
					super.close("CLOSE", r.getMessage());
				} else {
					super.close("ERROR", r.getMessage());
				}
			} else {
				throw new Exception("Init fail");
			}
		} catch (Exception x) {
			log.error(x.getMessage(), x);
			EventLogService.sendLog(jobId, EventType.PROG_EXE_FAIL, jobId + ":" + x.getMessage());
			super.close("ERROR", x.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AuthDataFtpProc proc = new AuthDataFtpProc();

		String jobId = "getFtpAuthData";
		String user = "SYSTEM";

		try {
			proc.process(jobId, user, args);
			System.out.println("[" + jobId + "] End ..");
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("[" + jobId + "] Exception :" + x);
		}
	}

}
