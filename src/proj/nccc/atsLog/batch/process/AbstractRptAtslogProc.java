package proj.nccc.atsLog.batch.process;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import proj.nccc.atsLog.batch.dao.SysFileUploadDao;
import proj.nccc.atsLog.batch.dao.entity.SysFileUpload;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.FileUtil;

abstract class AbstractRptAtslogProc {
	
	abstract String getReportId();
	abstract String getFileName();
	abstract String getReportName();
	abstract String getPlanUploadDate();
	abstract String getProcessMonth();
	
	private String startDate;
	private String endDate;
	
	public void createDateRange(String processMonth) {
		this.startDate = processMonth + "01";
		this.endDate = DateUtil.getMonthEnd(processMonth);
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * - 寫入資料庫
	 * @param reportHeader
	 * @param reportBody
	 * @param bank
	 * @param itemCategory
	 * @param itemType
	 * @param fileUploadDao
	 * @param oaOnly T/F : oxoa only
	 * @throws Exception
	 */
	public void writeTo(List<String> reportContent,
			String itemCategory, String itemType, 
			SysFileUploadDao fileUploadDao, boolean oaOnly) throws Exception {
		
		SysFileUpload o = new SysFileUpload();
		o.setUuid(UUID.randomUUID().toString());
		o.setItemCategory(itemCategory);
		o.setItemType(itemType);
		o.setFileMonth(this.getProcessMonth());
		String odoa = "\r\n";
		if (oaOnly) {
			odoa = "\r";
		}
		o.setFileBlob(FileUtil.listToBytes(reportContent, "Big5", odoa));
		o.setFileName(this.getFileName());
		o.setPlanUploadDate(this.getPlanUploadDate());
		o.setStatus("N");
		o.setCreateDate(new Date());
		fileUploadDao.insert(o);
	}

	/**
	 * - 寫入資料庫
	 * @param reportHeader
	 * @param reportBody
	 * @param bank
	 * @param itemCategory
	 * @param itemType
	 * @param fileUploadDao
	 * @throws Exception
	 */
	public void writeTo(byte[] reportContent,
			String itemCategory, String itemType, 
			SysFileUploadDao fileUploadDao) throws Exception {
		
		SysFileUpload o = new SysFileUpload();
		o.setUuid(UUID.randomUUID().toString());
		o.setItemCategory(itemCategory);
		o.setItemType(itemType);
		o.setFileMonth(this.getProcessMonth());
		o.setFileBlob(reportContent);
		o.setFileName(this.getFileName());
		o.setPlanUploadDate(this.getPlanUploadDate());
		o.setStatus("N");
		o.setCreateDate(new Date());
		fileUploadDao.insert(o);
	}
}
