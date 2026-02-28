package proj.nccc.atsLog.batch.vo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.util.DecodeUtil;

@Data
@ToString
@Log4j2
public class FtpSourceInfo extends FtpServerInfo implements Serializable {

	private static final long serialVersionUID = 4880265102812383859L;
	public static final String ENCRYPT_SH = "encrypt.sh";
	public static final String DECRYPT_SH = "decrypt.sh";
	
	/**
	 * - 本地端的目錄
	 */
	private String localOutPath;
	
	
	/**
	 * @param cfgFolder
	 * @param propertiesFile
	 * @throws Exception 
	 */
	public FtpSourceInfo(String cfgFolder, String propertiesFile) throws Exception {
		this.init(cfgFolder + propertiesFile);
	}
	
	/**
	 * - 初始並透過ini file 解密取得  ftp資訊
	 * @param filePath : properties file
	 * @throws Exception 
	 */
	private void init(String filePath) throws Exception {
		DecodeUtil decodeUtil=new DecodeUtil();
		InputStream in = null;
		try {
			String spFile = filePath;
			
			log.info("properties file : " + spFile);
			spFile = decodeUtil.execDecode(filePath + ".Z", filePath);
			log.info("decode properties file : " + spFile);
			
			in = new FileInputStream(spFile);
			Properties props = new Properties();
			props.load(in);
			
			this.setServerIp(props.getProperty("server.ip"));
			this.setServerPort(Integer.parseInt(props.getProperty("server.port")));
			this.setServerUser(props.getProperty("server.user"));
			this.setServerMima(props.getProperty("server.mima"));
			
			this.setRemoteRootFolder(props.getProperty("server.rootFolder"));
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}finally{
			try{
				if (in != null){ in.close(); in = null; }
			}catch(Exception x){
				log.error(x); 
			}
			try{
				decodeUtil.deleteFile(); 
			}catch(Exception x){
				log.error(x);
			}
		}
	}

}
