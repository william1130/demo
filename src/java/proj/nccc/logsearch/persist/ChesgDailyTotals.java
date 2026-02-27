
package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

import proj.nccc.logsearch.vo.AtslogTransLogArg;

public class ChesgDailyTotals extends AbstractProjPersistable {

	private static final long serialVersionUID = 1L;
	// LOG_DATE Varchar2(8) LOG記錄日期
	private String logDate;

	// MERCHANT_ID Varchar2(15) 特店代號
	private String merchantId;

	// TERM_ID Varchar2(8) 端末機代號
	private String termId;

	// TOTAL_COUNT Varchar2(8) 總筆數
	private Integer totalCount;

	// CHESG_COUNT Varchar2(8) CHESG總筆數
	private Integer chesgCount;

	public ChesgDailyTotals() {
	}

	public class ChesgDailyTotalsId extends CompositeId {
		public ChesgDailyTotalsId(String logDate, String merchantId, String termId) {
			super(logDate, merchantId, termId);
		}

		public String getLogDate() {
			return (String) keys.get(0);
		}

		public String getMerchantId() {
			return (String) keys.get(1);
		}

		public String getTermId() {
			return (String) keys.get(2);
		}
	}

	public ChesgDailyTotals(AtslogTransLogArg log) {
		this.logDate = log.getLogDate();
		this.merchantId = log.getMerchantId();
		this.termId = log.getTermId();
		this.totalCount = log.getTotalCount();
		this.chesgCount = log.getChesgCount();
	}

	@Override
	public Object getId() {
		return new ChesgDailyTotalsId(logDate, merchantId, termId);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		final ChesgDailyTotals other = (ChesgDailyTotals) obj;
		if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId())))
			return false;
		else
			return true;
	}

	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
		return hash;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
		this.modified();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		this.modified();
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
		this.modified();
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
		this.modified();
	}

	public Integer getChesgCount() {
		return chesgCount;
	}

	public void setChesgCount(Integer chesgCount) {
		this.chesgCount = chesgCount;
		this.modified();
	}

	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(logDate);
		vi.addValue(merchantId);
		vi.addValue(termId);
		vi.addValue(totalCount);
		vi.addValue(chesgCount);
		return vi;
	}
}
