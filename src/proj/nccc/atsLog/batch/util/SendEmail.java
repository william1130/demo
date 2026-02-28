package proj.nccc.atsLog.batch.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.service.EventLogService;
import proj.nccc.atsLog.batch.service.EventLogService.EventType;

@Log4j2
public class SendEmail {

	private String mailHost;
	private int mailPort;
	private String mailFrom;
	MultiPartEmail email = new MultiPartEmail();
	public SendEmail() throws Exception {
		Properties props = new Properties();
		InputStream in = null;
		try
		{
			String sPFile= ProjInit.propertyFolder+"mail.properties";
			in = new FileInputStream(sPFile);
			props.load( in );
			this.mailHost=props.getProperty("mail.host");
			try{
				String s=props.getProperty("mail.port");
				this.mailPort=Integer.parseInt(s);
			}catch(Exception x){
				this.mailPort=25;
			}
			this.mailFrom=props.getProperty("mail.from");
			
		}catch(Exception x){
			x.printStackTrace();
			log.error(x);
			throw new Exception("Mail Properties fail:"+x.getMessage());
		}
	}
	
	/**
	 * 發送Email
	 * @param emailAddress : 多比以分號隔開
	 * @param subject
	 * @param message
	 * @throws Exception
	 */
	public void send(String emailAddress, String subject, String message)
			throws Exception {
		String[] stringArray  = emailAddress.split(";");
		List<String> toList = new ArrayList<String>(Arrays.asList(stringArray ));
		this.send(toList, subject, message);
	}
	
	
	public void send(List<String> toList, String subject,String message)throws Exception{
		MultiPartEmail email = new HtmlEmail();
		log.info("Send Mail >>>"+subject);
		try {
			email.setCharset("Big5");
			email.setHostName(this.mailHost);
			email.setSmtpPort(this.mailPort);
			// email.setAuthenticator(new DefaultAuthenticator(this.userName, this.userPass));
			// email.setSSLOnConnect(false);
			email.setFrom(this.mailFrom);
			email.setSubject(subject);
			email.setMsg(message);
			for (String to : toList) {
				email.addTo(to.trim());
			}
			
//			email.send();
			log.info("Send Mail -> OK ");
		} catch (Exception e) {
			
			EventLogService.sendLog(this.mailHost, EventType.EMAIL_FAIL, "SendMail:"+e.getMessage());
			log.error("Send Mail -> Fail [" + e.getMessage() + "] ", e);
			throw new Exception(e);
		}
	}
	
}
