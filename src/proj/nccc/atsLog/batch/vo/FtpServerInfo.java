package proj.nccc.atsLog.batch.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FtpServerInfo implements Serializable {

	private static final long serialVersionUID = -5103277122091314800L;
	
	private String serverIp;
	private int serverPort;
	private String serverUser;
	private String serverMima;
	/**
	 * Remote root folder should include slash '/'
	 */
	private String remoteRootFolder;
	

}
