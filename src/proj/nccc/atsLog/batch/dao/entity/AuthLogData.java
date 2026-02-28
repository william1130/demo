package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthLogData extends ProjEntity {

	private static final long serialVersionUID = -7561564517672843365L;
	private String merchantNo;
	private String cardNo;
	private String purchaseDate;
	private String purchaseTime;
	private String approvalNo;
	private String cancelApprovalNo;
	private String dataSource;
	private String icFlag;
	private long purchaseAmt;
	private String conditionCode;
	private String bankNo2;
	private String expireDate;
	
	// for output file
	private String mauthNo;
	private String mtxType;
	private String mtxAmt;
	private String mfiller1;
	private String mfiller2;
	private String mcancelFlag;
	private String mfiller3;
	private String mauthBank;
	private String mfiller4;
	private String carriage;
	private String nullFlg;
	
}
