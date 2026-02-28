package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.connection.ProjConn;
import proj.nccc.atsLog.batch.dao.entity.ProjEntity;
import proj.nccc.atsLog.batch.dao.entity.SetlBinBankParm;
import proj.nccc.atsLog.batch.dao.entity.SetlBinNewBinoParm;

/**
 *  -  連接 atsLogdb
 *
 */
@Log4j2
public class ProjDao extends ProjConn {

	protected ProjEntity buildObj(ResultSet rs) throws Exception {
		return null;
	}
	
	public List<?> queryList(String sql, List<String> params) throws Exception {
		
		List<ProjEntity> list = new LinkedList<ProjEntity>();
		
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
			}
			
			ps = con.prepareStatement(sql);
			ps.clearParameters();
			if (params != null) {
				int i = 1;
				for(String p : params) {
					ps.setString(i++, p);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(buildObj(rs));
			}
			return list;
		} finally {
			try {
				// close connect , for performance
				// this.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * insert CallBank
	 * @param paraType
	 * @param paraCode
	 * @param paraAmt
	 * @param apraRemark
	 * @param fileDate
	 * @param modProgramId
	 * @param updateBatchNum
	 * @throws Exception 
	 */
	public void insertCallBankParm(String paraType, String paraCode, String paraAmt, 
			String paraRemark, String fileDate, 
			String modProgramId, String updateBatchNum) throws Exception {
		String sql = "INSERT into AUTH_CALLBANK_PARM "
				+ " ( PARM_TYPE ,PARM_CODE, PARM_AMT, PARM_MARK, FILE_DATE, MOD_PROGRAM_ID, UPDATE_BATCHNUM )"
				+ " Values ( ?, ?, ?, ?, ?, ?, ? ) ";
		List<Object> params = new LinkedList<Object>();
		params.add(paraType);
		params.add(paraCode);
		params.add(paraAmt);
		params.add(paraRemark);
		params.add(fileDate);
		params.add(modProgramId);
		params.add(updateBatchNum);
		super.execUpdate(sql, params);
	}
	
	/**
	 * 刪除舊資料
	 * @param updateBatchNum
	 * @return
	 * @throws Exception
	 */
	public int deleteCallBankDataNotBatchNum(String updateBatchNum) throws Exception {
		String sql = "delete AUTH_CALLBANK_PARM where UPDATE_BATCHNUM != ? ";
		List<Object> params = new LinkedList<Object>();
		params.add(updateBatchNum);
		return super.execUpdate(sql, params);
	}
	
	/**
	 * insert cupBin
	 * @param cupBin
	 * @param fileDate
	 * @param modProgramId
	 * @param updateBatchNum
	 * @throws Exception
	 */
	public void insertCupBin(String cupBin, String fileDate, String modProgramId, String updateBatchNum) throws Exception {
		String sql = "INSERT into AUTH_CUP_BIN_DATA "
				+ " ( CUP_BIN ,FILE_DATE, MOD_PROGRAM_ID, UPDATE_BATCHNUM )"
				+ " Values ( ?, ?, ?, ? ) ";
		List<Object> params = new LinkedList<Object>();
		params.add(cupBin);
		params.add(fileDate);
		params.add(modProgramId);
		params.add(updateBatchNum);
		super.execUpdate(sql, params);
	}
	
	/**
	 * 刪除舊資料
	 * @param updateBatchNum
	 * @return
	 * @throws Exception
	 */
	public int deleteCupBinNotBatchNum(String updateBatchNum) throws Exception {
		String sql = "delete AUTH_CUP_BIN_DATA where UPDATE_BATCHNUM != ? ";
		List<Object> params = new LinkedList<Object>();
		params.add(updateBatchNum);
		return super.execUpdate(sql, params);
	}
	
	/**
	 * insert BinBank
	 * @param o
	 * @param jobId
	 * @param updateBatchNum
	 * @throws Exception
	 */
	public void insertSetlBinBank(SetlBinBankParm o, String jobId, String updateBatchNum) throws Exception {
		
		String sql = "INSERT into SETL_BIN_BANK_PARM "
				+ " ( FID, BANK_ID, MAN_ID_NCCC, MAIN_ID, FULL_NAME, ABBR_NAME, "
				+ " ENG_NAME, ABBR_ENG, ADDR, ZIP_CODE, ADDR_BUSS, BUSS_ZIP, "
				+ " UNIFORM_NO_BANK, IN_DATE, OUT_DATE, OUT_DATE_FLAG, PROC_ID, PROC_NAME, "
				+ " MEDIA, CHN_CODE, ENG_CODE, MEM_TYPE, RPT_WAY, SETTLE, "
				+ " SETTLE_FLAG, BUSS_TYPE, FILE_PRD, NMIP_SVR_ID, A_BUSS_MAN, A_BUSS_TEL, "
				+ " I_BUSS_MAN, I_BUSS_TEL, ACT, APR_FLAG, USR_ID, RPT_PAGE_NUM, "
				+ " TAPE_FID, LNK_CTR, SPEC_BUSS, NTC_ACQ_FLAG, NTC_ISS_FLAG, DAILY_CHK, "
				+ " DAILY_CHK_FLAG, STAND_IN, STAND_IN_FLAG, INVO_NAME, PROC_FEE_MODE, FEE_MODE_FLAG, "
				+ " NMIP_FID, MOD_PGM, MOD_TIME, AUTH_TEL1, AUTH_TEL2, UPDATE_BATCHNUM )"
				+ " Values ( "
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ? "
				+ " ) ";
		List<Object> params = new LinkedList<Object>();
		params.add(o.getFid());
		params.add(o.getBankId());
		params.add(o.getManIdNccc());
		params.add(o.getMainId());
		params.add(o.getFullName());
		params.add(o.getAbbrName());
		
		params.add(o.getEngName());
		params.add(o.getAbbrEng());
		params.add(o.getAddr());
		params.add(o.getZipCode());
		params.add(o.getAddrBuss());
		params.add(o.getBussZip());
		
		params.add(o.getUniformNoBank());
		params.add(o.getInDate());
		params.add(o.getOutDate());
		params.add(o.getOutDateFlag());
		params.add(o.getProcId());
		params.add(o.getProcName());
		
		params.add(o.getMedia());
		params.add(o.getChnCode());
		params.add(o.getEngCode());
		params.add(o.getMemType());
		params.add(o.getRptWay());
		params.add(o.getSettle());
		
		params.add(o.getSettleFlag());
		params.add(o.getBussType());
		params.add(o.getFilePrd());
		params.add(o.getNmipSvrId());
		params.add(o.getAbussMan());
		params.add(o.getAbussTel());
		
		params.add(o.getIbussMan());
		params.add(o.getIbussTel());
		params.add(o.getAct());
		params.add(o.getAprFlag());
		params.add(o.getUsrId());
		params.add(o.getRptPageNum());
		
		params.add(o.getTapeFid());
		params.add(o.getLnkCtr());
		params.add(o.getSpecBuss());
		params.add(o.getNtcAcqFlag());
		params.add(o.getNtcIssFlag());
		params.add(o.getDailyChk());
		
		params.add(o.getDailyChkFlag());
		params.add(o.getStandIn());
		params.add(o.getStandInFlag());
		params.add(o.getInvoName());
		params.add(o.getProcFeeMode());
		params.add(o.getFeeModeFlag());
		
		params.add(o.getNmipFid());
		params.add(jobId);
		params.add(new Timestamp(new Date().getTime()));
		params.add(o.getAuthTel1());
		params.add(o.getAuthTel2());
		params.add(updateBatchNum);
		
		super.execUpdate(sql, params);
	}
	
	/**
	 * 刪除舊資料
	 * @param updateBatchNum
	 * @return
	 * @throws Exception
	 */
	public int deleteSetlBinBankNotBatchNum(String updateBatchNum) throws Exception {
		String sql = "delete SETL_BIN_BANK_PARM where UPDATE_BATCHNUM != ? and FID != 'ZZZZZZZ' ";
		List<Object> params = new LinkedList<Object>();
		params.add(updateBatchNum);
		return super.execUpdate(sql, params);
	}
	
	/**
	 * insert BinBank
	 * @param o
	 * @param jobId
	 * @param updateBatchNum
	 * @throws Exception
	 */
	public void insertSetlBinoParm(SetlBinNewBinoParm o, String jobId, String updateBatchNum) throws Exception {
		
		String sql = "INSERT into SETL_BIN_NEW_BINO_PARM "
				+ " ( FID, ACQ_ISS, CARD_TYPE, BIN_NO_L, BIN_NO_H, BIN_LEN, "
				+ " IN_DATE, OUT_DATE, ICA, CARD_LEN, TOKEN_FLAG, AUTH_OUT_DATE, "
				+ " JCB_ACQ_ID, IIN, DUAL_CUR_FLAG, CUR_CODE, DEBIT_FLAG, UPDATE_DATE, "
				+ " MOD_PGM, MOD_TIME, UPDATE_BATCHNUM "
				+ " ) Values ( "
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?  "
				+ " ) ";
		List<Object> params = new LinkedList<Object>();
		params.add(o.getFid()); 
		params.add(o.getAcqIss()); 
		params.add(o.getCardtype()); 
		params.add(o.getBinNoL()); 
		params.add(o.getBinNoH()); 
		params.add(o.getBinLen()); 
		
		params.add(o.getInDate()); 
		params.add(o.getOutDate()); 
		params.add(o.getIca()); 
		params.add(o.getCardLen()); 
		params.add(o.getTokenFlag()); 
		params.add(o.getAuthOutDate()); 
		
		params.add(o.getJcbAcqId()); 
		params.add(o.getIin()); 
		params.add(o.getDualCurFlag()); 
		params.add(o.getCurCode()); 
		params.add(o.getDebitFlag()); 
		params.add(o.getUpdateDate()); 
		
		params.add(jobId);
		params.add(new Timestamp(new Date().getTime()));
		params.add(updateBatchNum);
		
		super.execSqlByObj(sql, params);
	}	
	
	/**
	 * 刪除舊資料
	 * @param updateBatchNum
	 * @return
	 * @throws Exception
	 */
	public int deleteSetlBinoParmNotBatchNum(String updateBatchNum) throws Exception {
		String sql = "delete SETL_BIN_NEW_BINO_PARM where UPDATE_BATCHNUM != ? and FID != 'ZZZZZZZ' ";
		List<Object> params = new LinkedList<Object>();
		params.add(updateBatchNum);
		return super.execUpdate(sql, params);
	}
	
}
