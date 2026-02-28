package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.entity.SetlBinBankParm;
import proj.nccc.atsLog.batch.dao.entity.SetlBinNewBinoParm;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class SyncNssMbProcImpl {
	private String jobId;
	
	public SyncNssMbProcImpl(String jobId) {
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
			
			// ----------------------------------------------------
			// -- Sync BIN_BANK
			log.info("Sync BIN_BANK : " + new Date());
			List<SetlBinBankParm> listBank = nssDao.queryNssmbank();
			int totalBank = 0;
			int deleteBank = 0;
			if (listBank != null && listBank.size() > 0) {
				dao.setAutoCommit(false);
				for(SetlBinBankParm o : listBank) {
					dao.insertSetlBinBank(o, jobId, updateBatchNum);
					totalBank ++;
				}
				// ----------------------------------------------------------------
				// -- 刪除舊資料 : 非該batchNum
				deleteBank = dao.deleteSetlBinBankNotBatchNum(updateBatchNum);
				dao.commit();
			}
			log.info("Job : {} finish, insert {}, del {} ", jobId, totalBank, deleteBank);
			
			// ----------------------------------------------------
			// -- Sync BIN_BINO
			log.info("Sync BIN_BINO : " + new Date());
			List<SetlBinNewBinoParm> listBino = nssDao.queryNssmbino();
			int totalBino = 0;
			int deleteBino = 0;
			if (listBino != null && listBino.size() > 0) {
				dao.setAutoCommit(false);
				for(SetlBinNewBinoParm o : listBino) {
					dao.insertSetlBinoParm(o, jobId, updateBatchNum);
					totalBino ++;
				}
				// ----------------------------------------------------------------
				// -- 刪除舊資料 : 非該batchNum
				deleteBino = dao.deleteSetlBinoParmNotBatchNum(updateBatchNum);
				dao.commit();
			}
			log.info("Job : {} finish, insert {}, del {} ", jobId, totalBino, deleteBino);
			
			
			returnVo.setSuccess(true);
			returnVo.setMessage("A:" + totalBank + "/" + totalBino 
					+ " , D:" + deleteBank + "/" + deleteBino);
			return returnVo;
		}finally {
			nssDao.close();
			dao.close();
		}
	}

}
