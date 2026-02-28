package proj.nccc.atsLog.batch.process;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.AuthDataFtpProc;
import proj.nccc.atsLog.batch.dao.SysParaDao;
import proj.nccc.atsLog.batch.dao.entity.SysPara;
import proj.nccc.atsLog.batch.exception.BatchException;
import proj.nccc.atsLog.batch.exception.PendingException;
import proj.nccc.atsLog.batch.service.FtpService;
import proj.nccc.atsLog.batch.util.DecodeUtil;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.FtpSourceInfo;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthDataFtpProcImpl {

	private String jobId;

	public AuthDataFtpProcImpl(String jobId) {
		this.jobId = jobId;
	}

	public ReturnVO process() throws Exception {
		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {} ", jobId);

		SysParaDao paraDao = new SysParaDao();
		Map<String, SysPara> paraMap = paraDao.getSysParaMap("FTP_SERVER");
		SysPara para = paraMap.get(AuthDataFtpProc.ftpPara);
		if (para == null) {
			throw new Exception("Not Define Item Category on sysPara : " + AuthDataFtpProc.ftpPara);
		}

		FtpSourceInfo ftpSourceInfo = null;
		for (int i = 0; i <= 2; i++) {
			// -------------------------------
			// -- retry 3 times in case nmip decode fail
			log.info("Try to decode ftp infomation : " + i);
			try {
				ftpSourceInfo = new FtpSourceInfo(ProjInit.propertyFolder, para.getValue());
				log.info("decode sucess, server ip : " + ftpSourceInfo.getServerIp() + ":"
						+ ftpSourceInfo.getServerPort());
				break;
			} catch (Exception x) {
				log.error(x);
			}
		}

		FtpService ftpService = new FtpService(ftpSourceInfo);
		String msg = "download fail : " + AuthDataFtpProc.fileName;
		boolean procStatus = false;
		try {
			boolean fileExist = ftpService.fileExist(ProjInit.nmipLocalOutPath, AuthDataFtpProc.fileName);

			if (fileExist) {
				log.info("start to download file " + AuthDataFtpProc.fileName);
				File f = ftpService.download(ProjInit.nmipLocalOutPath, AuthDataFtpProc.fileName);
				
				procStatus = true;
				msg = "file size : " + Files.size(f.toPath());
				// if (Files.size(f.toPath()) > 0) {
					// 加密
	//				DecodeUtil decodeUtil = new DecodeUtil();
	//				decodeUtil.execEncode(ProjInit.nmipLocalOutPath + "/" + AuthDataFtpProc.fileName);

					// 刪除原始檔
	//				decodeUtil.deleteFile();
					
				// 	procStatus = true;
				// 	msg = "download success : " + AuthDataFtpProc.fileName;
				// } else {
				// 	f.delete();
				// 	msg = "file size : 0 ";
				// }
			}

		} catch (BatchException x) {
			throw new PendingException(x.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		log.info(msg);
		returnVo.setSuccess(procStatus);
		returnVo.setMessage(msg);
		return returnVo;

	}
}
