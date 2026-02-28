package proj.nccc.atsLog.batch.process;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.SysFileUpload;
import proj.nccc.atsLog.batch.exception.BatchException;
import proj.nccc.atsLog.batch.exception.PendingException;
import proj.nccc.atsLog.batch.service.FtpService;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.FtpSourceInfo;

@Log4j2
public class FileUploadProcImpl {
	
	
	public FileUploadProcImpl() {
	}
	
	public String process(SysFileUpload o, String propertiesFile) throws Exception {
		
		FtpSourceInfo ftpSourceInfo = null;
		for(int i = 0 ; i <= 2 ; i++) {
			// -------------------------------
			// -- retry 3 times in case nmip decode fail
			log.info("Try to decode ftp infomation : " + i);
			try {
				ftpSourceInfo = new FtpSourceInfo( ProjInit.propertyFolder, propertiesFile);
				log.info("decode sucess, server ip : " 
						+ ftpSourceInfo.getServerIp() + ":" + ftpSourceInfo.getServerPort());
				break;
			}catch(Exception x) {
				log.error(x);
			}
		}
		
		log.info("Total file size = " + o.getFileBlob().length);
		
		FtpService ftpService = new FtpService(ftpSourceInfo);
		try {

			log.info("start to upload file " + o.getFileName());
			ftpService.upload("", o.getFileName(), o.getFileBlob());
		} catch (BatchException x) {
			throw new PendingException(x.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		log.info("upload success : " + o.getFileName());
		return o.getFileName();
	}

}
