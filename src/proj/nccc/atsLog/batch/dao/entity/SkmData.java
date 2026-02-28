package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SkmData extends ProjEntity{
	
	private static final long serialVersionUID = -2896339047722544188L;
	private String upperMerchantNo;
	private String MerchantNo;
	private String MerchantName;
	private String termId;
	private String logDate;
	private String batchNum;
	private Double amount;
}
