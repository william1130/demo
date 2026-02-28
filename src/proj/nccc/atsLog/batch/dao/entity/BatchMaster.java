package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BatchMaster extends ProjEntity {

	private static final long serialVersionUID = -26992746755170244L;
	private String batchId;
	private String batchName;
	private String batchCycleType;
	private int batchCycleTime;
	private long execWarningOverTime;
	private String noticeType;
	private String status;
	private int noAlertMinus;
	private String nsshmsglLevel;

}
