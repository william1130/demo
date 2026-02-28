package proj.nccc.atsLog.batch.service;

import java.io.File;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.exception.BatchException;
import proj.nccc.atsLog.batch.util.FtpsUtil;
import proj.nccc.atsLog.batch.vo.FtpSourceInfo;

@Log4j2
public class FtpService {

	private FtpSourceInfo ftpSourceInfo;

	public FtpService(FtpSourceInfo ftpSourceInfo) {
		this.ftpSourceInfo = ftpSourceInfo;
	}

	/**
	 * - 上傳檔案
	 * 
	 * @param targetFolder
	 * @param fileName
	 * @param byte[]       fileContent
	 * @throws BatchException
	 * @throws Exception
	 */
	public void upload(String targetFolder, String fileName, byte[] fileContent) throws BatchException, Exception {
		FtpsUtil ftpsUtil = new FtpsUtil(ftpSourceInfo, ftpSourceInfo.getLocalOutPath());
		try {
			if (!ftpsUtil.connect()) {
				log.error("FTP Login Fail");
				throw new BatchException("can not login to FTPS Server");
			} else {
				String remoteDir = ftpSourceInfo.getRemoteRootFolder() + targetFolder + "/";
				String remoteFileName = remoteDir + fileName;
				ftpsUtil.uploadFile(remoteFileName, fileContent);
			}
		} finally {
			if (ftpsUtil != null) {
				ftpsUtil.disconnect();
			}
		}
	}

	/**
	 * - 下載檔案
	 * 
	 * @param targetFolder
	 * @param fileName
	 * @throws BatchException
	 * @throws Exception
	 */
	public File download(String localFolder, String fileName) throws BatchException, Exception {
		FtpsUtil ftpsUtil = new FtpsUtil(ftpSourceInfo, ftpSourceInfo.getLocalOutPath());
		File file = null;
		try {
			if (!ftpsUtil.connect()) {
				log.error("FTP Login Fail");
				throw new BatchException("can not login to FTPS Server");
			} else {
				file = ftpsUtil.downloadFile(fileName, localFolder);
			}
		} finally {
			if (ftpsUtil != null) {
				ftpsUtil.disconnect();
			}
		}
		return file;
	}

	public void deleteFile(String targetFolder, String fileName) throws BatchException, Exception {
		FtpsUtil ftpsUtil = new FtpsUtil(ftpSourceInfo, ftpSourceInfo.getLocalOutPath());
		try {
			if (!ftpsUtil.connect()) {
				log.error("FTP Login Fail");
				throw new BatchException("can not login to FTPS Server");
			} else {
				String remoteDir = ftpSourceInfo.getRemoteRootFolder() + targetFolder + "/";
				String remoteFileName = remoteDir + fileName;
				log.info("FTP Delete File : " + remoteFileName);
				ftpsUtil.deleteFile(remoteFileName);
			}
		} finally {
			if (ftpsUtil != null) {
				ftpsUtil.disconnect();
			}
		}
	}

	public boolean fileExist(String targetFolder, String fileName) throws BatchException, Exception {
		FtpsUtil ftpsUtil = new FtpsUtil(ftpSourceInfo, ftpSourceInfo.getLocalOutPath());
		try {
			if (!ftpsUtil.connect()) {
				log.error("FTP Login Fail");
				throw new BatchException("can not login to FTPS Server");
			} else {
				String remoteDir = ftpSourceInfo.getRemoteRootFolder() + targetFolder ;
				String remoteFileName = remoteDir + fileName;
				log.info("FTP down File : " + remoteFileName);

				File file = ftpsUtil.downloadFile(fileName, targetFolder);
				if (file != null) {
					file.delete();
					return true;
				}
				return false;
			}
		} finally {
			if (ftpsUtil != null) {
				ftpsUtil.disconnect();
			}
		}
	}

}
