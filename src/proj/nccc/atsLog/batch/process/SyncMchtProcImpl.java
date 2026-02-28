package proj.nccc.atsLog.batch.process;

import java.util.Date;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.ProjDao;
import proj.nccc.atsLog.batch.dao.other.MchtUtilDao;
import proj.nccc.atsLog.batch.service.SyncMerchantService;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.vo.ReturnVO;


@Log4j2
public class SyncMchtProcImpl {
	private String jobId;
	private String startDate;
	
	private boolean runMerchantMaster;
	private boolean runMerchantCard;
	private boolean runMerchantBusiness;
	private boolean runMerchantChinese;
	
	
	public SyncMchtProcImpl(String jobId, boolean runMerchantMaster, boolean runMerchantCard, 
			boolean runMerchantBusiness, boolean runMerchantChinese) {
		this.jobId = jobId;
		this.runMerchantMaster = runMerchantMaster;
		this.runMerchantCard = runMerchantCard;
		this.runMerchantBusiness = runMerchantBusiness;
		this.runMerchantChinese = runMerchantChinese;
		
		log.info("runScope : merchantMaster({}), mercvhantCard({}), "
				+ "merchantBusiness({}), merchantChinese({})", 
				runMerchantMaster, runMerchantCard, runMerchantBusiness, runMerchantChinese);
		
	}

	public ReturnVO process(String startDate) throws Exception {
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {}, process Date: {} ", jobId, startDate);
		this.startDate = startDate;
				
		String updateBatchNum = MyDateUtil.toDateString(new Date());
		MchtUtilDao mchtDao = new MchtUtilDao();
		ProjDao dao = new ProjDao();
		SyncMerchantService syncMerch = new SyncMerchantService(mchtDao, dao, this.startDate, updateBatchNum);
		
		int totalMaster = 0;
		int totalBusiness = 0;
		int totalCard = 0;
		int totalChinese = 0;
		
		try {
			log.info("updateBatchNum : " + updateBatchNum);
			if (this.runMerchantMaster) {
				log.info("start to sync Merchant");
				totalMaster = syncMerch.syncMerchantMaster();
			}
			
			if (this.runMerchantBusiness) {
				log.info("start to sync Business Data");
				totalBusiness = syncMerch.syncBusinessData();
			}
			
			if (this.runMerchantCard) {
				log.info("start to sync Card Date");
				totalCard = syncMerch.syncCardData();
			}
			
			if (this.runMerchantChinese) {
				log.info("start to sync Chinese Date");
				totalChinese = syncMerch.syncChineseData();
			}
			
			log.info("Total record : {}, {}, {}, {}", totalMaster, totalBusiness, totalCard, totalChinese);
			log.info("Job : {} finish", jobId);
			
			returnVo.setSuccess(true);
			returnVo.setMessage("Total file record : " + totalMaster 
					+ ", " + totalBusiness
					+ ", " + totalCard
					+ ", " + totalChinese);
			return returnVo;
		}finally {
			dao.close();
			mchtDao.close();
		}
	}
	
}
