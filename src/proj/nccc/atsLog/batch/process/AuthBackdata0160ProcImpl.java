package proj.nccc.atsLog.batch.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.AuthLogDataDao;
import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.dao.entity.AuthLogData;
import proj.nccc.atsLog.batch.dao.other.NmipDao;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.DecodeUtil;
import proj.nccc.atsLog.batch.util.FileUtil;
import proj.nccc.atsLog.batch.util.MyDateUtil;
import proj.nccc.atsLog.batch.util.MyStringUtils;
import proj.nccc.atsLog.batch.util.ProjInit;
import proj.nccc.atsLog.batch.vo.ReturnVO;

@Log4j2
public class AuthBackdata0160ProcImpl extends AbstractRptAtslogProc {
	private String jobId;
	private String startDate;
	private String startTime;
	private String keyId;
	private String keyLabel;
	private String keyType;
	private String rptFileName;

	public AuthBackdata0160ProcImpl(String jobId) {
		this.jobId = jobId;
	}

	public ReturnVO process(String startDate, boolean idFlag) throws Exception {

		ReturnVO returnVo = new ReturnVO();
		log.info("JobId {}, process Date: {} ", jobId, startDate);
		this.startDate = startDate;
		startTime = DateUtil.dateToString(new Date(), "HHmmss");
		SysFileUploadDao fileUploadDao = new SysFileUploadDao();
		AuthLogDataDao dao = new AuthLogDataDao();
		NmipDao nmipDao = new NmipDao();

		try {
			// ----------------------------------------------------------------
			// -- 刪除既有資料(待上傳的資料)
			String itemCategory = "FTP_NMIP";
			String itemType = "MGBA160";
			fileUploadDao.delete(itemCategory, itemType, startDate);

			List<String> fileContent = new LinkedList<String>();
			List<AuthLogData> list = dao.queryAuthData(startDate, idFlag);
			Map<String, String> nmipKeyMap = nmipDao.getNmipKeyMap();
			if (nmipKeyMap != null) {
				keyId = nmipKeyMap.get("KEY_ID");
				keyLabel = nmipKeyMap.get("KEY_LABEL");
				keyType = nmipKeyMap.get("KEY_TYPE");
			}

			// header
			fileContent.add(getFileInfo(null));
			for (AuthLogData o : list) {
				// init
				o.setMauthNo(o.getMauthNo() != null ? o.getMauthNo() : "");
				o.setMtxType(o.getMtxType() != null ? o.getMtxType() : "");
				o.setMtxAmt(o.getMtxAmt() != null ? o.getMtxAmt() : "");
				o.setMcancelFlag(o.getMcancelFlag() != null ? o.getMcancelFlag() : "");

				if ("3".equals(o.getDataSource())) { // 無授權碼請款資料
					o.setMauthNo(MyStringUtils.rightPad(o.getApprovalNo(), 6, " "));

					if (o.getIcFlag().startsWith("Y")) {
						o.setMtxType("CP");
					} else {
						o.setMtxType("DP");
					}
					o.setMtxAmt(MyStringUtils.leftPad("" + o.getPurchaseAmt(), 8, "0"));
				} else if ("4".equals(o.getDataSource())) {
					o.setMtxType("AP");
					if ("0122900410".equals(o.getMerchantNo())) { // 開卡交易
						o.setMauthNo(MyStringUtils.rightPad(o.getApprovalNo(), 6, " "));
						o.setMtxAmt(MyStringUtils.leftPad("1", 8, "0"));
						if (!"AT0000".equals(o.getApprovalNo())) {
							o.setMcancelFlag("R");
						}
					} else {
						o.setMtxAmt(MyStringUtils.leftPad("" + o.getPurchaseAmt(), 8, "0"));
						if (o.getConditionCode().startsWith("C")) {
							o.setMauthNo(MyStringUtils.rightPad(o.getApprovalNo(), 6, " "));
							o.setMcancelFlag("R");
						} else {
							if (!o.getConditionCode().startsWith("N") || !o.getConditionCode().startsWith("A")
									|| !o.getConditionCode().startsWith("D")) {
								o.setMauthNo(MyStringUtils.rightPad(o.getApprovalNo(), 6, " "));
							}
						}
					}
				}
				String line = MyStringUtils.rightPad(o.getMerchantNo(), 10, " ");
				if (o.getCardNo() != null && o.getCardNo().length() > 16) {
					// 依原後端系統設計
					line += MyStringUtils.rightPad(o.getCardNo().substring(0, 16), 16, " ");
				} else {
					line += MyStringUtils.rightPad(o.getCardNo(), 16, " ");
				}
				if (StringUtils.isNotBlank(o.getPurchaseDate())) {
					// -------------------------------------
					// -- 01234567
					// -- yyyyMMdd
					line += o.getPurchaseDate().substring(2);
				} else {
					line += "      ";
				}
				line += MyStringUtils.rightPad(o.getPurchaseTime(), 6, " ");
				line += o.getMauthNo();
				line += o.getMtxType();
				line += o.getMtxAmt();
				line += "00";
				line += "          ";
				line += MyStringUtils.rightPad(o.getMcancelFlag(), 1, " ");
				line += "BK";
				line += MyStringUtils.rightPad(o.getBankNo2(), 2, " ");
				line += o.getExpireDate();
				line += "     ";

				fileContent.add(line);
			}
			// trailer
			fileContent.add(getFileInfo(list.size()));

			byte[] rptFile = FileUtil.listToBytes(fileContent, "Big5", "\r\n");

			// 壓縮檔案(zip)
			Date strToDate = MyDateUtil.toDateYYYYMMDD(startDate);
			Date addDate = MyDateUtil.getDateBeforeAfterByDay(strToDate, 1, "AFTER");
			rptFileName = "O702956" + MyDateUtil.yyyymmdd(addDate) + "01" + keyId;
			byte[] zipFile = FileUtil.zipFile(rptFile, rptFileName);

			// 加密(NMIP)
			String sourceName = rptFileName + ".zip";
			byte[] nmipFile = genNmipFile(zipFile, sourceName);

			// 寫入table
			this.writeTo(nmipFile, itemCategory, itemType, fileUploadDao);
			log.info("Total file record : " + list.size());
			log.info("Job : {} finish", jobId);
			returnVo.setSuccess(true);
			returnVo.setMessage("Total file record : " + list.size());
			return returnVo;
		} finally {
			dao.close();
			fileUploadDao.close();
		}

	}

	private byte[] genNmipFile(byte[] zipFile, String sourceName) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(ProjInit.nmipLocalTempPath + "/" + sourceName));
			stream.write(zipFile);

		} catch (IOException e) {
			log.error(sourceName + " write source file error", e);
			return null;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				log.error(sourceName + " stream closed error", e);
			}
		}
		log.info(String.format("Reject Report process >>> create and write source file[%s]. ", sourceName));

		DecodeUtil decodeUtil = new DecodeUtil();
		String targetName = sourceName + ".Z";
		try {
			// M2023089 支援AES演算法
			log.debug(String.format("tempFolder[%s],sourceName[%s],targetName[%s],keyLabel[%s],keyType[%s]",
					ProjInit.nmipLocalTempPath, sourceName, targetName, keyLabel, keyType));
			decodeUtil.encrypt(ProjInit.nmipLocalTempPath + sourceName, ProjInit.nmipLocalTempPath + targetName,
					keyLabel, keyType);
		} catch (Exception e) {
			log.error(targetName + " encrypt error", e);
			return null;
		} finally {
			decodeUtil.removeFile(ProjInit.nmipLocalTempPath + "/" + sourceName);
		}
		log.info(String.format("Reject Report process >>> encrypt file[%s]. ", targetName));

		RandomAccessFile f = null;
		byte[] fileData = null;
		File tempFile = new File(ProjInit.nmipLocalTempPath + "/" + targetName);
		try {
			if (tempFile.exists()) {
				f = new RandomAccessFile(tempFile, "r");
				fileData = new byte[(int) f.length()];
				f.read(fileData);
			}
		} catch (Exception e) {
			log.error(targetName + " read file error", e);
			return null;
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException e) {
					log.error(ProjInit.nmipLocalTempPath + "/" + targetName + " closed failed.", e);
				}
				f = null;
				decodeUtil.removeFile(ProjInit.nmipLocalTempPath + "/" + targetName);
			}
		}

		return fileData;
	}

	private String getFileInfo(Integer count) {
		String line = "956"; // Bank ID,3
		line += "702"; // File no,3
		line += "AUTHLOG "; // File name,8
		line += startDate.substring(2); // File date,6
		line += startTime; // File time,6
		line += "000000"; // Filler,6
		line += "000080"; // Record length,6
		line += "000000"; // Filler,6
		line += (count == null) ? "000000" : String.format("%06d", count); // Record count,6
		return String.format("%-80s", line); // total,80
	}

	@Override
	String getReportId() {
		return "MGBA160";
	}

	@Override
	String getFileName() {
		// Out bound、檔案種類、金融代碼、日期、序號、金鑰代碼
		return rptFileName + ".zip.Z";
	}

	@Override
	String getReportName() {
		return "交易回灌AUTH LOG";
	}

	@Override
	String getPlanUploadDate() {
		// ----------------------------------------
		// -- 預期上傳 target 的日期.
		return DateUtil.dateToString(new Date(), "yyyyMMdd");
	}

	@Override
	String getProcessMonth() {
		return this.startDate;
	}

}
