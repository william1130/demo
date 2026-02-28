package proj.nccc.atsLog.batch;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.dao.SysParaDao;
import proj.nccc.atsLog.batch.dao.entity.SysFileUpload;
import proj.nccc.atsLog.batch.dao.entity.SysPara;
import proj.nccc.atsLog.batch.exception.PendingException;
import proj.nccc.atsLog.batch.process.FileUploadProcImpl;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.util.ProjInit;

@Log4j2
public class FileUploadProc extends BatchBaseMain {
	// ------------------------------------------------------------
	// -- 檔案上傳 (FTP)
	// -- 1. -取得 TABLE ACS_FILE_UPLOAD
	// -- 2. -根據 acsFileUpload.itemCategory = acsSysPara.lookupCode 取得FTP properties
	
	public void process(String jobId,String user,String[] args) throws Exception {
		
		System.out.println("["+jobId+"] Start ..");
		// --------------------------------------------------
		// -- arg : -dyyyyMMdd 指定日期, 未輸入表示為系統日
		String procDate = super.getProcessDate(args);
		if(super.init(jobId)) {
			// ----------------------------------------------
			// -- 取得待處理的資料
			SysFileUploadDao dao = new SysFileUploadDao();
			SysParaDao paraDao = new SysParaDao();
			try {
				int successCount = 0;
				int failCount = 0;
				List<SysFileUpload> list = dao.queryByStatus(ProjInit.FTP_WAIT_TO);
				if (list != null && list.size() > 0) {
					Map<String, SysPara> paraMap = paraDao.getSysParaMap("FTP_SERVER");
					
					FileUploadProcImpl imp = new FileUploadProcImpl();
					for(SysFileUpload o : list) {
						log.info("process {} - {} plan upload Date {}", 
								o.getItemCategory(), o.getItemType(), o.getPlanUploadDate());
						if (o.getFileBlob() == null || o.getFileBlob().length <= 1) {
							log.info("Category {} type {} with no Blbo file, pass thus record.", 
									o.getItemCategory(), o.getItemType());
							continue;
						}
						if (procDate.compareTo(o.getPlanUploadDate()) < 0) {
							log.info("Category {} type {} suspend cause {} / {} ", 
									o.getItemCategory(), o.getItemType(), procDate, o.getPlanUploadDate());
							continue;
						}
						SysPara para = paraMap.get(o.getItemCategory());
						if (para == null) {
							throw new Exception("Not Define Item Category on sysPara : " + o.getItemCategory());
						}
						try {
							String fileRename = imp.process(o, para.getValue());
							
							dao.update(o.getUuid(), ProjInit.FTP_SEND_SUCCESS, fileRename, new Date());
							successCount ++;
						}catch(PendingException p) {
							failCount ++;
							// 非事務性錯誤, 待下次執行
							log.error(p.getMessage(), p);
							dao.updateCount(o.getUuid());
							if (o.getResendRetryCount() > ProjInit.FTP_MaxRetry) {
								String msg = "檔案  " + o.getItemCategory() + ", " +o.getItemType() 
								+ ", " + o.getFileName() + " 嘗試上傳FTP次數已達上限(" 
								+ ProjInit.FTP_MaxRetry + ")";
								log.info(msg);
								EventLogService.sendLog(jobId, EventType.PROG_EXE_LIMIT_ALERT, msg);
							}
						}catch(Exception x) {
							log.error(x.getMessage(), x);
							failCount ++ ;
							o.setStatus(ProjInit.FTP_SEND_FAIL);
							dao.update(o.getUuid(), ProjInit.FTP_SEND_FAIL, o.getFileName(), null);
						}
					}
				}
				super.close("CLOSE", "Total " + failCount + "/" + successCount + "/" + list.size());
			}catch(Exception x) {
				
				log.error(x.getMessage(), x);
			}finally {
				dao.close();
			}
		}
		log.info( "["+jobId+"], Close ");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		FileUploadProc proc = new FileUploadProc();
		
		String jobId="fileUpload";
		String user="SYSTEM";
		
		// 取得指定日期, 否則預設為系統日
		try{
			proc.process(jobId,user,args);
			System.out.println("["+jobId+"] End ..");
		}catch(Exception x)
		{
			x.printStackTrace();
			System.out.println("["+jobId+"] Exception :"+x);
		}
	}

}
