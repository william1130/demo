package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SetlBinNewBinoParm extends ProjEntity {

	private static final long serialVersionUID = -26912746115170244L;
	private String fid;
	private String acqIss;
	private String cardtype;
	private String binNoL;
	private String binNoH;
	private String binLen;
	private String inDate;
	private String outDate;
	private String ica;
	private String cardLen;
	private String tokenFlag;
	private String authOutDate;
	private String jcbAcqId;
	private String iin;
	private String dualCurFlag;
	private String curCode;
	private String debitFlag;
	private String updateDate;
	private String modPgm;
	private String modTime;
	
	private String updateBatchNum;

}
