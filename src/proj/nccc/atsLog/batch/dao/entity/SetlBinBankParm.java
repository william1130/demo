package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SetlBinBankParm extends ProjEntity {

	private static final long serialVersionUID = -26912226115170244L;
	private String fid; 
	private String bankId;
	private String manIdNccc;
	private String mainId;
	private String fullName;
	private String abbrName;
	private String engName;
	private String abbrEng;
	private String addr; 
	private String zipCode;
	private String addrBuss;
	private String bussZip;
	private String uniformNoBank;
	private String inDate;
	private String outDate;
	private String outDateFlag;
	private String procId;
	private String procName;
	private String media; 
	private String chnCode;
	private String engCode;
	private String memType;
	private String rptWay;
	private String settle; 
	private String settleFlag;
	private String bussType;
	private String filePrd;
	private String nmipSvrId;
	private String abussMan;
	private String abussTel;
	private String ibussMan;
	private String ibussTel;
	private String act; 
	private String aprFlag;
	private String usrId;
	private String rptPageNum;
	private String tapeFid;
	private String lnkCtr;
	private String specBuss;
	private String ntcAcqFlag;
	private String ntcIssFlag;
	private String dailyChk;
	private String dailyChkFlag;
	private String standIn;
	private String standInFlag;
	private String invoName;
	private String procFeeMode;
	private String feeModeFlag;
	private String nmipFid;
	private String modPgm;
	private String modTime;
	private String authTel1;
	private String authTel2;
	
	private String updateBatchNum;

}
