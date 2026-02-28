package proj.nccc.atsLog.batch.dao.entity;

import java.sql.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysEventMaster extends ProjEntity {
	private static final long serialVersionUID = 4182032023289617814L;

	private String eventCode;
	private String eventDesc;
	private String eventLogicalRule;
	private String eventRuleValue;
	private String eventNotice;
	private String status;
	private int nextEventTime;
	private String createUser;
	private Date createDate;
	private String lastUpdateUser;
	private Date lastUploadDate;
	private int eventCount;
	private String nsshmsglLevel;
	private boolean toSms;
	private boolean toNssm;

}
