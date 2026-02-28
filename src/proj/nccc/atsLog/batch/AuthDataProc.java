package proj.nccc.atsLog.batch;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.AuthDataProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthDataProc extends BatchBaseMain {
	// ------------------------------------------------------------
	// -- 匯入 Auth Log Data
	public static String fileName = "";
	public static String startDate = "";
	public static String endDate = "";

	public void process(String jobId, String user, String[] args) throws Exception {

		System.out.println("[" + jobId + "] Start ..");
		super.createDateRange(args);
		try {
			if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
				fileName = args[0];
			} else {
				fileName = "AUTHLOGDATA.TXT";
			}
			if (args.length >= 3 && StringUtils.isNotBlank(args[1])) {
				if (!MyDateUtil.isDate(args[1])) {
					throw new Exception("Start Date variable err.");
				}
				startDate = args[1];
			} else {
				// -- 預設為系統日-1
				startDate = super.getStartDate();
			}
			if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
				if (!MyDateUtil.isDate(args[2])) {
					throw new Exception("End Date variable err.");
				}
				endDate = args[2];
			} else {
				// -- 預設為系統日-1
				endDate = super.getStartDate();
			}
			
			if (super.init(jobId)) {

				AuthDataProcImpl impl = new AuthDataProcImpl(jobId);

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
		AuthDataProc proc = new AuthDataProc();

		String jobId = "uploadAuthData";
		String user = "SYSTEM";

		// 取得指定日期, 否則預設為系統日
		try {
			proc.process(jobId, user, args);
			System.out.println("[" + jobId + "] End ..");
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("[" + jobId + "] Exception :" + x);
		}
	}

}
