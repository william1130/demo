package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.SkmMonrptProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - 執行SKM_monrpt (新光三越統計月報)
 *
 */
@Log4j2
public class SkmMonrptProc extends BatchBaseMain {

	public void process(String jobId, String user, String[] args) throws Exception {

		System.out.println("[" + jobId + "] Start ..");

		try {
			if (super.init(jobId)) {
				// -----------------------------------------
				// -- 若未指定月份區間則預設為系統日的上個月
				String processMonth = super.getProcessMonth(args);

				SkmMonrptProcImpl impl = new SkmMonrptProcImpl(jobId);

				ReturnVO r = impl.process(processMonth);
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
		log.info("[" + jobId + "], Close ");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SkmMonrptProc proc = new SkmMonrptProc();

		String jobId = "SKM_RPT";
		String user = "SYSTEM";

		// 取得指定日期, 否則預設為上個月
		try {
			proc.process(jobId, user, args);
			System.out.println("[" + jobId + "] End ..");
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("[" + jobId + "] Exception :" + x);
		}
	}

}
