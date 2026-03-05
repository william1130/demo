package proj.nccc.logsearch.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.ISO8583VO;

import proj.nccc.logsearch.parse.BuildIsoVo;
import proj.nccc.logsearch.vo.NcccParm;

/**
 * 
 * @author Stephen Lin
 * @versionType $Revision: 1.2 $
 */
public class Active0200Util {
	private static final Logger log = LogManager.getLogger(Active0200Util.class);

	private String atsHost1 = "ATSsvr1";
	private String atsHost2 = "ATSsvr2";
	private int serverPort = 33022;

	private int timeOutSec;

	public Active0200Util() {
		this.setTimeOutSec(Integer.parseInt("120"));
	}
//0101062628
	// Active0200Util a=new Active0200Util();
	// NcccParm inp = new NcccParm();
	// inp.cardNum = "4563010012345678"; /* 卡號 */
	// inp.cardExpireDate = "0812"; /* 有效日期 */
	// inp.transAmount = "12300"; /* 交易金額 */
	// inp.cvv2 = "789"; /* CVV2 */
	// inp.merchantId = "0101001240"; /* 特店代號 */
	// inp.terminalId = "5678"; /* 端末機代號 */
	// inp.acquirerId = "493817"; /* 收單行代號 */
	// inp.merchantName = "SOGO DEPARTMANT"; /* 特店英文名稱 */
	// inp.mccCode = "1234"; /* 行業類別碼 */
	// inp.city = "TAIPEI"; /* 程式英文名稱 */
	// a.sednB24(inp);

	public NcccParm sendAts(NcccParm inp, String bankNo) {
		NcccParm rtnValue = null;
		Socket userSd = null;
		DataOutputStream dOut = null;
		OutputStream socketOutputStream = null;
		DataInputStream dIn = null;
		InputStream socketInputStream = null;
		try {
			BuildIsoVo bVo = new BuildIsoVo();
			ISO8583VO request = null;
			if (inp.getCancelFlag().equals("Y")) {
				request = bVo.buildRevRequest(inp.getRequestObj());
			} else {
				request = bVo.buildRequest(inp, bankNo);
				inp.setRequestObj(request);
			}

			byte[] reqArr = bVo.getByteArray(request);
			log.info("iso:[" + CommonFunction.bytesToHex(reqArr) + "]");
			try {
				userSd = new Socket(atsHost1, serverPort);
				userSd.setSoTimeout(timeOutSec * 1000);
			} catch (Exception x) {
				log.info("Try to connect to 2rd Server IP :" + atsHost2 + ":" + serverPort);
				userSd = new Socket(atsHost2, serverPort);
				userSd.setSoTimeout(timeOutSec * 1000);
			}

			socketOutputStream = userSd.getOutputStream();
			socketInputStream = userSd.getInputStream();

			// ** 傳送授權資料至 ATS *//*
//			socketOutputStream.write(reqArr);
//			socketOutputStream.flush();
			dOut = new DataOutputStream(socketOutputStream);
			String dataHexLenStr = "0000" + Integer.toHexString(reqArr.length);
			dataHexLenStr = dataHexLenStr.substring(dataHexLenStr.length() - 4, dataHexLenStr.length());
			log.info(dataHexLenStr);
			dOut.write(CommonFunction.hexDecode(dataHexLenStr));
			dOut.write(reqArr); // write the reqArr
			dOut.flush();
			// ** 接收 ATS之授權回覆資料 *//*
			dIn = new DataInputStream(socketInputStream);

			byte[] lengthByteArr = new byte[2];
			dIn.read(lengthByteArr);

			String lengthRtn = CommonFunction.bytesToHex(lengthByteArr);
//			String lengthRtn=""+lengthByteArr[0]+lengthByteArr[1];
			int intLength = Integer.parseInt(lengthRtn, 16);
			ISO8583VO response = null;
			if (intLength > 0) {
				byte[] message = new byte[intLength];
				dIn.readFully(message, 0, message.length); // read the message
				log.info("iso:[" + CommonFunction.bytesToHex(message) + "]");
				response = bVo.buildResponse(message);
				if (response != null && response.hasField(39)) {
					rtnValue = new NcccParm();
					BeanUtils.copyProperties(rtnValue, inp);
					log.info("response code[" + response.getField(39).toString() + "]");
					rtnValue.setRespCode(response.getField(39).toString());
					if (response.hasField(38)) {
						log.info("auth code[" + response.getField(38).toString() + "]");
						rtnValue.setAuthCode(response.getField(38).toString());

					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("Connect ATS Exception Error " + ex.getMessage());
			return null;
		} finally {
			if (dIn != null) {
				try {
					dIn.close();
				} catch (IOException e) {
				}
				dIn = null;
			}
			if (dOut != null) {
				try {
					dOut.close();
				} catch (IOException e) {
				}
				dOut = null;
			}

			if (socketOutputStream != null) {
				try {
					socketOutputStream.close();
				} catch (IOException e) {
				}
				socketOutputStream = null;
			}
			if (socketInputStream != null) {
				try {
					socketInputStream.close();
				} catch (IOException e) {
				}
				socketInputStream = null;
			}
			if (userSd != null) {
				try {
					userSd.close();
				} catch (IOException e) {
				}
				userSd = null;
			}
		}
		return rtnValue;
	}

	public int getTimeOutSec() {
		return timeOutSec;
	}

	public void setTimeOutSec(int timeOutSec) {
		this.timeOutSec = timeOutSec;
	}

}
