package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class SyncCupBinProcImpl {
	private String jobId;
	
	public SyncCupBinProcImpl(String jobId) {
		this.jobId = jobId;
	}
	
	public ReturnVO process() throws Exception {
		
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {} ", jobId);
		ProjDao dao = new ProjDao();
		NssDao nssDao = new NssDao();
		try {
			// ----------------------------------------------------
			// -- 透過updateBatchNum來管控insert後刪除舊資料~
			String updateBatchNum = MyDateUtil.toDateString(new Date());
			
			String fileDate = MyDateUtil.yyyymmdd(new Date());
			List<Map<String, Object>> list = nssDao.queryCupBin();
			int totalRecord = 0;
			int deleteRecord = 0;
			if (list != null && list.size() > 0) {
				dao.setAutoCommit(false);
				for(Map<String, Object> o : list) {
					String cupBin = MapUtils.getString(o, "ISSMCBIN_BIN");
					if (cupBin != null) {
						dao.insertCupBin(cupBin, fileDate, jobId, updateBatchNum);
						totalRecord ++;
					}
				}
				// ----------------------------------------------------------------
				// -- 刪除舊資料 : 非該batchNum
				deleteRecord = dao.deleteCupBinNotBatchNum(updateBatchNum);
				dao.commit();
			}
			log.info("Job : {} finish, insert {}, del {} ", jobId, totalRecord, deleteRecord);
			returnVo.setSuccess(true);
			returnVo.setMessage("insert " + totalRecord + " , delete " + deleteRecord);
			return returnVo;
		}finally {
			nssDao.close();
			dao.close();
		}
	}

}
