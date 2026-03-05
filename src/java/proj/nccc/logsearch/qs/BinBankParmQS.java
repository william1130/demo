
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBankParmQS.java,v 1.1 2017/04/24 01:31:14 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.sql.ResultSetTool;

import proj.nccc.logsearch.persist.BinBankParm;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.1 $
 */
public interface BinBankParmQS extends BaseCRUDQS {
	public static final JdbcPersistableBuilder BinBankParmBuilder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {

			BinBankParm obj = new BinBankParm();
			ResultSetTool rst = new ResultSetTool(rs);
			obj.setFid(rst.getString("FID"));
			obj.setBankId(rst.getString("BANK_ID"));
			obj.setManIdNccc(rst.getString("MAN_ID_NCCC"));
			obj.setMainId(rst.getString("MAIN_ID"));
			obj.setFullName(rst.getString("FULL_NAME"));
			obj.setAbbrName(rst.getString("ABBR_NAME"));
			obj.setEngName(rst.getString("ENG_NAME"));
			obj.setAbbrEng(rst.getString("ABBR_ENG"));
			obj.setAddr(rst.getString("ADDR"));
			obj.setZipCode(rst.getString("ZIP_CODE"));
			obj.setAddrBuss(rst.getString("ADDR_BUSS"));
			obj.setBussZip(rst.getString("BUSS_ZIP"));
			obj.setUniformNoBank(rst.getString("UNIFORM_NO_BANK"));
			obj.setInDate(rst.getString("IN_DATE"));
			obj.setOutDate(rst.getString("OUT_DATE"));
			obj.setOutDateFlag(rst.getString("OUT_DATE_FLAG"));
			obj.setProcId(rst.getString("PROC_ID"));
			obj.setProcName(rst.getString("PROC_NAME"));
			obj.setMedia(rst.getString("MEDIA"));
			obj.setChnCode(rst.getString("CHN_CODE"));
			obj.setEngCode(rst.getString("ENG_CODE"));
			obj.setMemType(rst.getString("MEM_TYPE"));
			obj.setRptWay(rst.getString("RPT_WAY"));
			obj.setSettle(rst.getString("SETTLE"));
			obj.setSettleFlag(rst.getString("SETTLE_FLAG"));
			obj.setBussType(rst.getString("BUSS_TYPE"));
			obj.setFilePrd(rst.getString("FILE_PRD"));
			obj.setNmipSvrId(rst.getString("NMIP_SVR_ID"));
			obj.setABussMan(rst.getString("A_BUSS_MAN"));
			obj.setABussTel(rst.getString("A_BUSS_TEL"));
			obj.setIBussMan(rst.getString("I_BUSS_MAN"));
			obj.setIBussTel(rst.getString("I_BUSS_TEL"));
			obj.setAct(rst.getString("ACT"));
			obj.setAprFlag(rst.getString("APR_FLAG"));
			obj.setUsrId(rst.getString("USR_ID"));
			obj.setRptPageNum(rst.getString("RPT_PAGE_NUM"));
			obj.setTapeFid(rst.getString("TAPE_FID"));
			obj.setLnkCtr(rst.getString("LNK_CTR"));
			obj.setSpecBuss(rst.getString("SPEC_BUSS"));
			obj.setNtcAcqFlag(rst.getString("NTC_ACQ_FLAG"));
			obj.setNtcIssFlag(rst.getString("NTC_ISS_FLAG"));
			obj.setDailyChk(rst.getString("DAILY_CHK"));
			obj.setDailyChkFlag(rst.getString("DAILY_CHK_FLAG"));
			obj.setStandIn(rst.getString("STAND_IN"));
			obj.setStandInFlag(rst.getString("STAND_IN_FLAG"));
			obj.setInvoName(rst.getString("INVO_NAME"));
			obj.setProcFeeMode(rst.getString("PROC_FEE_MODE"));
			obj.setFeeModeFlag(rst.getString("FEE_MODE_FLAG"));
			obj.setNmipFid(rst.getString("NMIP_FID"));
			obj.setModPgm(rst.getString("MOD_PGM"));
			obj.setModTime(rst.getDateString("MOD_TIME"));
			obj.setAuthTel1(rst.getString("AUTH_TEL1"));
			obj.setAuthTel2(rst.getString("AUTH_TEL2"));
			return obj;
		}
	};

	public BinBankParm queryByPrimaryKey(String rowid) throws QueryServiceException;

	public BinBankParm getBinBankByFid(String fid) throws QueryServiceException;

	public String getBankNo2(String bin) throws QueryServiceException;

	public boolean isOnUsCard(String bankNo, String bin) throws QueryServiceException;

	public boolean isMemberBank(String bin) throws QueryServiceException;
//	public String getAcqNameByICA(String ica) throws QueryServiceException;
}
