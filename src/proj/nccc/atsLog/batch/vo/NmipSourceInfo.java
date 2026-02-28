package proj.nccc.atsLog.batch.vo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.util.DecodeUtil;


@ToString
@Log4j2
public class NmipSourceInfo extends FtpServerInfo implements Serializable {

	private static final long serialVersionUID = -5903277122011314800L;
	public static final String ENCRYPT_SH = "encrypt.sh";
	public static final String DECRYPT_SH = "decrypt.sh";
	
	/**
	 * ftp下來的本機檔案路徑
	 */
	private String localInPath;
	private String localTempPath;
	private String localOutPath;
	
	/**
	 * nmip 特有資訊 bankId
	 */
	private String bankId;
	/**
	 * nmip 特有資訊 fileCode 業務類型編碼
	 */
	private String fileCode;
	
	public NmipSourceInfo(String cfgFolder) {
		this.init(cfgFolder + "nmip.properties");
	}
	
	/**
	 * - 初始並透過ini file 解密取得 nmip ftp資訊
	 * @param configName : nmip.properties
	 */
	private void init(String filePath) {
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
