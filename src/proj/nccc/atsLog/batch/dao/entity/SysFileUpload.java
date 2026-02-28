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
public class SysFileUpload extends ProjEntity {

	private static final long serialVersionUID = 448259816172915766L;
	private String uuid;
	private String itemCategory;
	private String itemType;
	private String fileMonth;
	private byte[] fileBlob;
	private String fileName;
	private String planUploadDate;
	private String status;
	private int resendRetryCount;
	private Date createDate;
	private Date uploadDate;
	
}
