package proj.nccc.atsLog.batch.util;

import java.io.FileInputStream;
import java.util.Properties;

public class ProjInit {
	
	
	public static String dbProperty = "cfg/db.properties";
	public static String propertyFolder = "cfg/";
	
	public static final String PROG_NO_RUN = "ProgExeTimeAlert";
	public static final String PROG_RUN_OVER_TIME = "ProgExeLimitAlert";
	
	public static final String SYS_NAME = "ATSLOG";
	public static final String HOST_NAME = "BatchSvr";
	
	public static String nmipLocalInPath = "";
	public static String nmipLocalTempPath = "";
	public static String nmipLocalOutPath = "";
	
	
	/**
	 * - FTP 允許最大重送次數, 若超過則 Call OP
	 */
	public static int FTP_MaxRetry = 999;

	/**
	 * N:Wait for FTP
	 */
	public static String FTP_WAIT_TO = "N";
	/**
	 * Y:Send completed
	 */
	public static String FTP_SEND_SUCCESS = "Y";
	
	/**
	 * FAIL
	 */
	public static String FTP_SEND_FAIL = "F";
	
	/**
	 * PARA 參數代碼 : Mail 名單
	 */
	public static String PARA_ALERT_MAIL = "ALERT_MAIL";
	
	/**
	 * 是否需要呼叫 nmip sh
	 */
	public static boolean nmipNeedNmip;
	
	/**
	 * nmip Encrypt sh name
	 */
	public static String nmipEncryptSh = "encrypt.sh";
	
	/**
	 * nmip Decrypt sh name
	 */
	public static String nmipDecryptSh = "decrypt.sh";
	
	public void getInitParm() throws Exception {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream("./cfg/proj.init"));
			
//			dbProperty = props.getProperty("db.properties");
//			propertyFolder = props.getProperty("properties.folder");
			
			nmipLocalInPath = props.getProperty("nmip.local.in.path");
			nmipLocalTempPath = props.getProperty("nmip.local.temp.path");
			nmipLocalOutPath = props.getProperty("nmip.local.out.path");
			
			String s1 = props.getProperty( "nmip.NeedNmip" );
			if(s1==null || s1.equalsIgnoreCase("TRUE")){
				nmipNeedNmip=true;
			}
			else{
				nmipNeedNmip=false;
			}
			
		} catch (Exception e) {
			throw e;
		}
	}

}