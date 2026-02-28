package proj.nccc.atsLog.batch.dao.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysEventLog extends ProjEntity {
	private static final long serialVersionUID = 4680701350030058503L;

	private String uuid;
	private Date createDate;
	private String host;
	private String eventCode;
	private String logDesc;
	private String reqSendNssMsg;
	private String reqSendEmail;
	private Date sendDate;
	private String eventItem;
	private String nsshmsglLevel;
	


}
