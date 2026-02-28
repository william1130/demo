package proj.nccc.atsLog.batch.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;
import proj.nccc.atsLog.batch.vo.FtpServerInfo;

@Log4j2
public class FtpsUtil {
	
	FTPSClient ftpsClient = null;
	FtpServerInfo serverInfo;
	
	String localFolder;
	
	List<String> ftpIpList = new LinkedList<String>();


	public FtpsUtil(FtpServerInfo serverInfo, String localFolder) {
		this.serverInfo = serverInfo;
		this.localFolder = localFolder;
	}
	
	public boolean connect() throws Exception {
		int returnCode = 0;
		try {
			ftpsClient = new FTPSClient(false);
			log.info("connect ftpes:port = " + 
					serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			ftpsClient.connect(serverInfo.getServerIp(), serverInfo.getServerPort());
			
			returnCode = ftpsClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(returnCode)) {
				if (ftpsClient.login(serverInfo.getServerUser(), serverInfo.getServerMima())) {
					ftpsClient.execPBSZ(0);
					ftpsClient.execPROT("P");
					ftpsClient.pasv();
					ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
					log.info("login success : " + serverInfo.getServerIp());
				} else {
					log.error("ftpes login fail. " + serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
					if (ftpsClient != null) {
						ftpsClient.logout();
						ftpsClient.disconnect();
					}
				}
			} else {
				log.error("ftpes login fail, " + serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
				if (ftpsClient != null) {
					ftpsClient.disconnect();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (ftpsClient != null) {
				try {
					ftpsClient.disconnect();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
				ftpsClient = null;
			}
		}
		if (ftpsClient != null && ftpsClient.isConnected())
			return true;
		else {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			return false;
		}
	}

	public boolean disconnect() throws Exception {
		try {
			if (ftpsClient != null && ftpsClient.isConnected()) {
				ftpsClient.logout();
				ftpsClient.disconnect();
			}
		} catch (Exception e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	public FTPFile[] listFiles() throws Exception {
		FTPFile[] ftpFiles = null;
		try {
			if (!ftpsClient.isConnected()) {
				connect();
			}
			// ------------------------------------------------------
			// -- 改寫 common-net 的 正則判斷
			// -- 使某些 unix server 會有 " . " 的在 ls -l 
			// -- ex:
			// -- -rw-r--r--   1 acsn2ap  acsn2ap      136 Dec 20 14:37 test.txt
			// -- -rw-r--r--.  1 acsn2ap  acsn2ap      136 Dec 20 14:37 test.txt
			ftpsClient.enterLocalPassiveMode();
			
			ftpsClient.configure(new FTPClientConfig("proj.nccc.atsLog.batch.util.UnixFTPEntryParser"));
			
			ftpFiles = ftpsClient.listFiles(".");
			log.info("ftps PassiveHost : " + ftpsClient.getPassiveHost());
			log.info("ftps PassivePort : " + ftpsClient.getPassivePort());
		} catch (IOException e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error("list ftp folder error.", e);
		}
		return ftpFiles;
	}

	public boolean changeFolder(String pathName) throws Exception {
		try {
			if (!ftpsClient.isConnected()) {
				connect();
			}
			if (ftpsClient.changeWorkingDirectory(pathName))
			{
				log.info(String.format("changeFolder sucessful! [%s]", pathName));
				return true;
			}
			else
			{
				log.info(String.format("changeFolder fail! [%s]", pathName));
				return false;
			}
		} catch (IOException e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error("change ftp folder error.", e);
			return false;
		}
	}
	
	/**
	 * @param srcFileName
	 * @param remoteFileName 
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(String srcFileName, String remoteFileName) throws Exception {
		boolean finalResult = false;
		FileInputStream fis;
		log.info("upload file \"" + srcFileName + "\" to "+ remoteFileName + ". ftp server [" + serverInfo.getServerIp() + "]");
		try {
			fis = new FileInputStream(srcFileName);
			
			ftpsClient.enterLocalPassiveMode();
			finalResult = ftpsClient.storeFile(remoteFileName, fis);
			fis.close();
		} catch (Exception e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return finalResult;
		}
		return finalResult;
	}
	
	
	/**
	 * @param targetFileName
	 * @param fileContent
	 * @return
	 * @throws Exception 
	 */
	public boolean uploadFile(String targetFileName, byte[] fileContent) throws Exception {
		boolean finalResult = false;
		ByteArrayInputStream bais;
		log.info("upload file  to "+ targetFileName + ". ftp server [" + serverInfo.getServerIp() + "]");
		try {
			bais = new ByteArrayInputStream(fileContent);
			
			ftpsClient.enterLocalPassiveMode();
			finalResult = ftpsClient.storeFile(targetFileName, bais);
			bais.close();
		} catch (Exception e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return finalResult;
		}
		return finalResult;
	}
	
	public File downloadFile(String fileName) throws Exception {
		return downloadFile(fileName, localFolder);
	}

	public File downloadFile(String fileName, String localFolder) throws Exception {
		File file = null;
		FileOutputStream fos = null;
		try {
			log.info("download File[" + fileName + "]");
			if (localFolder != null) {
				localFolder = localFolder + "/";
			}
			file = new File(localFolder + fileName);
			fos = new FileOutputStream(file, false);
			ftpsClient.enterLocalPassiveMode();
			ftpsClient.retrieveFile(fileName, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos = null;
			}
		}
		return file;
	}
	
	public boolean deleteFile(String fileName) throws Exception {
		boolean finalResult = false;
		try {
			log.info("delete File[" + fileName + "]");
			if (!ftpsClient.isConnected())
				connect();
			ftpsClient.enterLocalPassiveMode();
			finalResult = ftpsClient.deleteFile(fileName);
		} catch (IOException e) {
			EventLogService.sendLog(serverInfo.getServerIp(), 
					EventType.FTP_CONN_FAIL, serverInfo.getServerIp() + ":" + serverInfo.getServerPort());
			log.error("delete file error.", e);
			return finalResult;
		}
		return finalResult;
	}

	public String getLocalFolder() {
		return localFolder;
	}

	public void setLocalFolder(String localFolder) {
		this.localFolder = localFolder;
	}

}
