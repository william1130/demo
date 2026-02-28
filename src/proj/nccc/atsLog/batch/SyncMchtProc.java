package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.SyncMchtProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.ReturnVO;

/**
 * - Sync Mcht
 *
 */
@Log4j2
public class SyncMchtProc extends BatchBaseMain {

	public SyncMchtProc() {
	}

	public void process(String jobId, String user, String[] args) throws Exception {
		System.out.println("[" + jobId + "] Start .. or with -syyyyMMdd");
		try {
			if (super.init(jobId)) {
				// -----------------------------------------
				// -- 若未指定日期區間則預設為系統日 - 1
				super.createDateRange(args);
				boolean runMerchantMaster = false;
				boolean runMerchantCard = false;
				boolean runMerchantBusiness = false;
				boolean runMerchantChinese = false;
				String runScope = null;
				for (String arg : args)
				{
					if ('-' == arg.charAt(0))
					{
						switch (arg.charAt(1))
						{
							case 'r':
								if (arg.length() > 2) {
									runScope = arg.substring(2, arg.length());
									if (runScope.equalsIgnoreCase("MerchantMaster")) {
										runMerchantMaster = true;
									}else if (runScope.equalsIgnoreCase("MerchantCard")) {
										runMerchantCard = true;
									}else if (runScope.equalsIgnoreCase("MerchantBusiness")) {
										runMerchantBusiness = true;
									}else if (runScope.equalsIgnoreCase("MerchantChinese")) {
										runMerchantChinese = true;
									}
								}
								break;
						}
					}
				}
				if (runScope == null) {
					runMerchantMaster = true;
					runMerchantCard = true;
					runMerchantBusiness = true;
					runMerchantChinese = true;
				}
				
				SyncMchtProcImpl bth = new SyncMchtProcImpl(jobId, runMerchantMaster, runMerchantCard, runMerchantBusiness, runMerchantChinese);
				ReturnVO r = bth.process(super.getStartDate());
				if (r.isSuccess())
					super.close("CLOSE", r.getMessage());
				else {
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

	public static void main(String[] args) {
		SyncMchtProc proc = new SyncMchtProc();

		String jobId = "SYNC_MCHT";
		String user = "SYSTEM";
		// 取得指定日期, 否則預設為系統日 - 1
		try {
			proc.process(jobId, user, args);
			System.out.println("[" + jobId + "] End ..");
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("[" + jobId + "] Exception :" + x);
		}
	}
}
