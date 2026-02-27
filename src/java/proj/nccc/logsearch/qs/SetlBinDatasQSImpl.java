
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: SetlBinDatasQSImpl.java,v 1.3 2019/10/21 01:11:01 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.sql.ResultSetCallback;
import com.edstw.sql.ResultSetTool;
import com.edstw.util.NotImplementedException;

import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.CupBinData;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.SetlBinDatas;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author APAC\czrm4t $
 * @version $Revision: 1.3 $
 */
public class SetlBinDatasQSImpl extends AbstractJdbcPersistableQueryService implements SetlBinDatasQS {
	public String getServiceName() {
		return "Setl_BIN_Datas Query Service";
	}

	public void setServiceParams(Map arg0) throws ServiceException {
		// do nothing
	}

	public SetlBinDatas queryByPrimaryKey(String binNo) throws QueryServiceException {
		List<String> params = new LinkedList<String>();
		params.add(binNo);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A, a.token_flag,");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                a.FID as FID_B,'Y' as EMB_KIND_ATM_B, ");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE     a.FID=c.FID");
		cmd.append(" AND         ? between a.bin_no_l and a.bin_no_h ");
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));

				obj.setBussType(rst.getString("BUSS_TYPE_C"));

				obj.setBinNob(rst.getString("BIN_NO_A"));
				obj.setFidb(rst.getString("FID_B"));
				obj.setEmbKindAtmb(rst.getString("EMB_KIND_ATM_B"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public List<SetlBinDatas> queryByPrimaryKeys(List ids) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<SetlBinDatas> queryByExample(ProjPersistableArg example) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<SetlBinDatas> queryAll() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public SetlBinDatas queryByCardType(String binNo, String yyyymmdd) throws QueryServiceException {
		List<String> params = new LinkedList<String>();
		params.add(binNo);
		params.add(yyyymmdd);
		params.add(yyyymmdd);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A, a.token_flag, ");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE    a.FID=c.FID");
		cmd.append(" AND         ? between a.bin_no_l and a.bin_no_h ");
		cmd.append(" AND         (a.IN_DATE <= ? ");
		cmd.append(" AND         (a.OUT_DATE is null ");
		cmd.append(" OR           a.OUT_DATE >= ? ))");
		// System.out.println("cmd="+ cmd.toString());
		// System.out.println("binNo="+binNo);
		// System.out.println("systemYYMM="+systemYYMM);
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));
				obj.setBussType(rst.getString("BUSS_TYPE_C"));

				// obj.setBinNob(rst.getString("BIN_NO_B"));
				// obj.setFidb(rst.getString("FID_B"));
				// obj.setEmbKindAtmb(rst.getString("EMB_KIND_ATM_B"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public SetlBinDatas queryByAcqIssByMchtNo6(String mchtNo6, String yyyymmdd) throws QueryServiceException {
		// 以mchtNo 的4碼或6碼來判斷收單bin , ACQ_ISS = 'A'
		List<String> params = new LinkedList<String>();
		params.add(mchtNo6);
		params.add(yyyymmdd);
		params.add(yyyymmdd);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A,a.token_flag, ");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                c.MAIN_ID as MAIN_ID_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a  ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE    a.FID=c.FID");
		cmd.append(" AND         ? between a.bin_no_l and a.bin_no_h ");
		cmd.append(" AND         a.ACQ_ISS = 'A' ");
		cmd.append(" AND         (a.IN_DATE <= ? ");
		cmd.append(" AND           (a.OUT_DATE is null ");
		cmd.append(" OR           a.OUT_DATE >= ? ))");
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));
				obj.setBussType(rst.getString("BUSS_TYPE_C"));
				obj.setMainId(rst.getString("MAIN_ID_C"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public SetlBinDatas queryByAcqIssByMchtNo4(String mchtNo4, String yyyymmdd) throws QueryServiceException {
		// 以mchtNo 的4碼或6碼來判斷收單bin , ACQ_ISS = 'A'
		List<String> params = new LinkedList<String>();
		params.add(mchtNo4);
		params.add(yyyymmdd);
		params.add(yyyymmdd);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A, a.token_flag, ");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                c.MAIN_ID as MAIN_ID_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a  ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE    a.FID=c.FID");
		cmd.append(" AND         a.ICA = ? ");
		cmd.append(" AND         a.ACQ_ISS = 'A' ");
		cmd.append(" AND         (a.IN_DATE <= ? ");
		cmd.append(" AND           (a.OUT_DATE is null ");
		cmd.append(" OR           a.OUT_DATE >= ? ))");
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));
				obj.setBussType(rst.getString("BUSS_TYPE_C"));
				obj.setMainId(rst.getString("MAIN_ID_C"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public SetlBinDatas queryByAcqIssByBin(String binNo, String yyyymmdd) throws QueryServiceException {
		// 以卡號Bin 來判斷 , ACQ_ISS = 'I'
		List<String> params = new LinkedList<String>();
		params.add(binNo);
		params.add(yyyymmdd);
		params.add(yyyymmdd);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A, a.token_flag, ");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                a.FID as FID_B,'Y' as EMB_KIND_ATM_B, ");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE       a.FID=c.FID");
		cmd.append(" AND         ? between a.bin_no_l and a.bin_no_h ");
		cmd.append(" AND         a.ACQ_ISS = 'I' ");
		cmd.append(" AND         (a.IN_DATE <= ? ");
		cmd.append(" AND           (a.OUT_DATE is null ");
		cmd.append(" OR           a.OUT_DATE >= ? ))");
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));

				obj.setBussType(rst.getString("BUSS_TYPE_C"));

				obj.setBinNob(rst.getString("BIN_NO_A"));
				obj.setFidb(rst.getString("FID_B"));
				obj.setEmbKindAtmb(rst.getString("EMB_KIND_ATM_B"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public SetlBinDatas queryByOutDateFlg(String binNo, String yyyymmdd) throws QueryServiceException {
		// 以特店7 碼, (清算代碼)
		List<String> params = new LinkedList<String>();
		params.add(binNo);
		params.add(yyyymmdd);
		params.add(yyyymmdd);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  substr(a.bin_no_l, 1, a.bin_len) as BIN_NO_A,a.FID as FID_A,a.ACQ_ISS as ACQ_ISS_A,");
		cmd.append("                a.CARD_TYPE as CARD_TYPE_A,a.token_flag, ");
		cmd.append("                c.BUSS_TYPE as BUSS_TYPE_C ,");
		cmd.append("                a.IN_DATE as IN_DATE_A,a.OUT_DATE as OUT_DATE_A ,");
		cmd.append("                a.FID as FID_B,'Y' as EMB_KIND_ATM_B, ");
		cmd.append("                c.FID as FID_C,c.ABBR_NAME as ABBR_NAME_C,c.ADDR as ADDR_C,");
		cmd.append("                c.IN_DATE as IN_DATE_C,c.OUT_DATE as OUT_DATE_C,");
		cmd.append("                c.OUT_DATE_FLAG as OUT_DATE_FLAG_C,c.A_BUSS_TEL as A_BUSS_TEL_C");
		cmd.append(" FROM      setl_bin_new_bino_parm a ,SETL_BIN_BANK_PARM c");
		cmd.append(" WHERE      a.FID=c.FID");
		cmd.append(" AND         ? between a.bin_no_l and a.bin_no_h ");
		cmd.append(" AND         c.OUT_DATE_FLAG = 'N' ");
		cmd.append(" AND         (a.IN_DATE <= ? ");
		cmd.append(" AND           (a.OUT_DATE is null ");
		cmd.append(" OR           a.OUT_DATE >= ? ))");
		final SetlBinDatas obj = new SetlBinDatas();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setBinNoA(rst.getString("BIN_NO_A"));
				obj.setFidA(rst.getString("FID_A"));
				obj.setAcqIssA(rst.getString("ACQ_ISS_A"));
				obj.setCardTypeA(rst.getString("CARD_TYPE_A"));
				obj.setTokenFlag(rst.getString("token_flag"));
				obj.setInDateA(rst.getString("IN_DATE_A"));
				obj.setOutDateA(rst.getString("OUT_DATE_A"));

				obj.setBussType(rst.getString("BUSS_TYPE_C"));

				obj.setBinNob(rst.getString("BIN_NO_A"));
				obj.setFidb(rst.getString("FID_B"));
				obj.setEmbKindAtmb(rst.getString("EMB_KIND_ATM_B"));

				obj.setFidC(rst.getString("FID_C"));
				obj.setAbbrNameC(rst.getString("ABBR_NAME_C"));
				obj.setAddrC(rst.getString("ADDR_C"));
				obj.setInDateC(rst.getString("IN_DATE_C"));
				obj.setOutDateC(rst.getString("OUT_DATE_C"));
				obj.setOutDateFlagC(rst.getString("OUT_DATE_FLAG_C"));
				obj.setABussTelC(rst.getString("A_BUSS_TEL_C"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getBinNoA() != null)
			return obj;
		else
			return null;
	}

	public BinBankParm getAcqBkALLBy7Mcht(String mchtNo, String today) throws QueryServiceException {
		// SELECT main_id FROM SETL_BIN_BANK_PARM where fid='8221217'
		List params = new LinkedList();
		params.add(mchtNo);
		params.add(today);
		params.add(today);
		StringBuffer cmd = new StringBuffer();
		cmd.append(
				"SELECT * FROM SETL_BIN_BANK_PARM WHERE FID=? AND IN_DATE<=? AND ( OUT_DATE IS NULL OR OUT_DATE > ? ) ");
		final BinBankParm obj = new BinBankParm();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
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
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getFid() != null)
			return obj;
		else
			return null;
	}

	public String getAcqBkBy7Mcht(String mchtNo, String today) throws QueryServiceException {
		BinBankParm bbp = this.getAcqBkALLBy7Mcht(mchtNo, today);
		if (bbp != null)
			return bbp.getMainId();
		else
			return null;

	}

	public CupBinData getCupBin(String bin) throws QueryServiceException {
		List params = new LinkedList();
		params.add(bin);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT  * ");
		cmd.append(" FROM   AUTH_CUP_BIN_DATA ");
		cmd.append(" WHERE  CUP_BIN = ? ");
		final CupBinData obj = new CupBinData();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
				ResultSetTool rst = new ResultSetTool(rs);
				obj.setCutBin(rst.getString("CUP_BIN"));
				obj.setFileDate(rst.getDateString("FILE_DATE"));
				obj.setModProgramId(rst.getString("MOD_PROGRAM_ID"));
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getCutBin() != null)
			return obj;
		else
			return null;
	}

	public boolean isCupCard(String cardNo) {
		for (int i = 4; i < 14; i++) {
			CupBinData cup = getCupBin(cardNo.substring(0, i));
			if (cup != null)
				return true;
		}
		return false;
	}

	public boolean isCupDualBrand(String cardNo) {
		if (cardNo.startsWith("37") || cardNo.startsWith("35") || cardNo.startsWith("4") || cardNo.startsWith("5"))
			return true;

		return false;
	}

	public BinBankParm getBinBankParmByBinNo(String bin) throws QueryServiceException {
		List params = new LinkedList();
		params.add(bin);
		StringBuffer cmd = new StringBuffer();
		cmd.append("SELECT B.* FROM setl_bin_new_bino_parm A, SETL_BIN_BANK_PARM B ");
		cmd.append("WHERE A.FID=B.FID ");
		cmd.append("AND ( A.OUT_DATE IS NULL OR A.OUT_DATE > TO_CHAR(SYSDATE, 'YYYYMMDD') ) ");
		cmd.append("AND A.IN_DATE <= TO_CHAR(SYSDATE, 'YYYYMMDD') ");
		cmd.append("AND ? between a.bin_no_l and a.bin_no_h ");
		final BinBankParm obj = new BinBankParm();
		super.query(cmd.toString(), params, new ResultSetCallback() {
			public Object processResultSet(ResultSet rs) throws SQLException {
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
				return null;
			}

			public boolean isStopProcess() {
				return false;
			}
		});
		if (obj != null && obj.getFid() != null)
			return obj;
		else
			return null;
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		String idObj = (String) id;
		return queryByPrimaryKey(idObj);
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

}
