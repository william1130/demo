package proj.nccc.atsLog.batch;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.process.HouseKeepingProcImpl;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class HouseKeepingProc extends BatchBaseMain {

	public HouseKeepingProc() {
	}

	public void process(String jobId, String user, String[] args) throws Exception {
		System.out.println("[" + jobId + "] Start ..");
		if (super.init(jobId)) {
			log.debug("[" + jobId + "], Start");
			try {
				HouseKeepingProcImpl bth = new HouseKeepingProcImpl();
				ReturnVO r = bth.process();
				if (r.isSuccess())
					super.close("CLOSE", r.getMessage());
				else {

					super.close("ERROR", r.getMessage());
				}
			} catch (Exception f) {
				f.printStackTrace();
				log.debug("[" + jobId + "] ,Exception=" + f);
				System.out.println(f);
				super.close("ERROR", f.getMessage());
			}
		} else {
			super.close();
		}
		log.info("[" + jobId + "], Close ");
	}

	public static void main(String[] args) {
		HouseKeepingProc proc = new HouseKeepingProc();

		String jobId = "HouseKeeping";
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
