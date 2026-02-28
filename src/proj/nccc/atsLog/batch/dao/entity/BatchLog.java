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
public class BatchLog extends ProjEntity {

	private static final long serialVersionUID = 5998880994235192887L;
	private String uuid;
	private String batchId;
	private Date batchStartDate;
	private Date batchEndDate;
	private String description;
	private String batchStatus;

}
