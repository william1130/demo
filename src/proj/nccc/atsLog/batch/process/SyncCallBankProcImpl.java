package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.other.AuthDao;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class SyncCallBankProcImpl {
	private String jobId;
	
	public SyncCallBankProcImpl(String jobId) {
		this.jobId = jobId;
	}
	
	public ReturnVO process() throws Exception {
		
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {} ", jobId);
		ProjDao dao = new ProjDao();
		AuthDao authDao = new AuthDao();
		try {
			// ----------------------------------------------------
			// -- 透過updateBatchNum來管控insert後刪除舊資料~
			String updateBatchNum = MyDateUtil.toDateString(new Date());
			
			String fileDate = MyDateUtil.yyyymmdd(new Date());
			List<Map<String, Object>> list = authDao.queryCallBank();
			int totalRecord = 0;
			int deleteRecord = 0;
			if (list != null && list.size() > 0) {
				dao.setAutoCommit(false);
				for(Map<String, Object> o : list) {
					String paraType = MapUtils.getString(o, "CM_PARA_TYPE");
					String paraCode = MapUtils.getString(o, "CM_PARA_CODE");
					String paraAmt = MapUtils.getString(o, "CM_PARA_AMT");
					dao.insertCallBankParm(paraType, paraCode, paraAmt, "N", fileDate, jobId, updateBatchNum);
					totalRecord ++;
				}
				// ----------------------------------------------------------------
				// -- 刪除舊資料 : 非該batchNum
				deleteRecord = dao.deleteCallBankDataNotBatchNum(updateBatchNum);
				dao.commit();
			}
			log.info("Job : {} finish, insert {}, del {} ", jobId, totalRecord, deleteRecord);
			returnVo.setSuccess(true);
			returnVo.setMessage("insert " + totalRecord + " , delete " + deleteRecord);
			return returnVo;
		}finally {
			authDao.close();
			dao.close();
		}
	}

}
