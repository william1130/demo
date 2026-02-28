package proj.nccc.atsLog.batch.dao.other;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.connection.NssConn;
import proj.nccc.atsLog.batch.dao.entity.Nsshmsgl;
import proj.nccc.atsLog.batch.dao.entity.SetlBinBankParm;
import proj.nccc.atsLog.batch.dao.entity.SetlBinNewBinoParm;

@Log4j2
public class NssDao extends NssConn {
	private final String TBL_NAME = "NSSHMSGL";
	private final String FLD_NSSHMSGL_SYS_DATE = "NSSHMSGL_SYS_DATE";
	private final String FLD_NSSHMSGL_PID = "NSSHMSGL_PID";
	private final String FLD_NSSHMSGL_ERR_MSG = "NSSHMSGL_ERR_MSG";
	private final String FLD_NSSHMSGL_FILE = "NSSHMSGL_FILE";
	private final String FLD_NSSHMSGL_CODE = "NSSHMSGL_CODE";
	private final String FLD_NSSHMSGL_SYS_MSG = "NSSHMSGL_SYS_MSG";
	private final String FLD_NSSHMSGL_LEVEL = "NSSHMSGL_LEVEL";
	private final String FLD_NSSHMSGL_TYPE = "NSSHMSGL_TYPE";
	private final String FLD_NSSHMSGL_AP_MSG = "NSSHMSGL_AP_MSG";
	
	public void insertR6Log(Nsshmsgl nsshmsgl) throws Exception {
		final String sql = String.format("insert into %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?,?,?,?) ",
				TBL_NAME, FLD_NSSHMSGL_SYS_DATE, FLD_NSSHMSGL_PID, FLD_NSSHMSGL_ERR_MSG, FLD_NSSHMSGL_FILE,
				FLD_NSSHMSGL_CODE, FLD_NSSHMSGL_SYS_MSG, FLD_NSSHMSGL_LEVEL, FLD_NSSHMSGL_TYPE, FLD_NSSHMSGL_AP_MSG);
		try {

			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(sql);
			ps.clearParameters();
			int i = 1;
			ps.setTimestamp(i++, new Timestamp(nsshmsgl.getSysDate().getTime()));
			ps.setString(i++, nsshmsgl.getPid());
			ps.setString(i++, nsshmsgl.getErrMsg().getBytes().length > 52
					? nsshmsgl.getErrMsg().substring(0,
							52 - (nsshmsgl.getErrMsg().getBytes().length - nsshmsgl.getErrMsg().length() > 52 ? 26
									: nsshmsgl.getErrMsg().getBytes().length - nsshmsgl.getErrMsg().length()))
					: nsshmsgl.getErrMsg());
			ps.setString(i++, nsshmsgl.getFileName());
			ps.setString(i++, nsshmsgl.getCode());
			ps.setString(i++, nsshmsgl.getSysMsg().getBytes().length > 36
					? nsshmsgl.getSysMsg().substring(0,
							36 - (nsshmsgl.getSysMsg().getBytes().length - nsshmsgl.getSysMsg().length() > 36 ? 18
									: nsshmsgl.getSysMsg().getBytes().length - nsshmsgl.getSysMsg().length()))
					: nsshmsgl.getSysMsg());
			ps.setString(i++, nsshmsgl.getLevel());
			ps.setString(i++, nsshmsgl.getType());
			ps.setString(i++, nsshmsgl.getApMsg());
			ps.executeUpdate();
			return;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
				log.error("R6NssmUtilDao.insertR6Log.finally exception:" + e);
			}
		}
	}

	public int count(String table, String where) throws Exception {
		int i = 0;
		StringBuffer cmd = new StringBuffer("");
		try {
			con = super.getConnection();
			cmd.append("SELECT count(*) FROM " + table);
			cmd.append(" WHERE " + where);
			log.debug(cmd.toString());
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			if (rs.next()) {
				String s = rs.getString(1);
				try {
					i = Integer.parseInt(s);
				} catch (Exception e) {
				}
			}
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
			}
		}
	}
	
	public void commit() {
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
			}
			con.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * - 取得CUP BIN 資料(group by), cause ignore data duplicate
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryCupBin() throws Exception{
		String sql = "SELECT ISSMCBIN_BIN "
				+ " FROM ISSMCBIN "
				+ " GROUP BY ISSMCBIN_BIN ";
		return super.queryListMap(sql, null);
	}
	
	
	/**
	 * query NSSMBINR
	 * @return
	 * @throws Exception
	 */
	public List<SetlBinNewBinoParm> queryNssmbino() throws Exception{
		String sql = "select NSSMBINR_FID, NSSMBINR_ACQ_ISS, NSSMBINR_CRD_TYP, NSSMBINR_BIN_NUM_L, NSSMBINR_BIN_NUM_H, "
				+ " NSSMBINR_BIN_LEN, TO_CHAR(to_date(NSSMBINR_IN_DATE,'mmddyyyy') ,'yyyymmdd') AS NSSMBINR_IN_DATE, "
				+ " TO_CHAR(to_date(NSSMBINR_OUT_DATE,'mmddyyyy') ,'yyyymmdd') AS NSSMBINR_OUT_DATE, NSSMBINR_ICA, NSSMBINR_CRD_LEN, "
				+ " NSSMBINR_TOKEN_FLG, NSSMBINR_AUTH_OUT_DATE, NSSMBINR_JCB_ACQ_ID, NSSMBINR_IIN, NSSMBINR_DUAL_CUR_FLG, "
				+ " NSSMBINR_CUR_CDE, NSSMBINR_DEBIT_FLG "
				+ " from NSSMBINR "
				+ " where TO_CHAR(to_date(NSSMBINR_IN_DATE,'mmddyyyy') ,'yyyymmdd') <= TO_CHAR(sysdate,'yyyymmdd') "
				+ " AND (TO_CHAR(to_date(NVL(trim(NSSMBINR_OUT_DATE) ,'12312999'),'mmddyyyy'),'yyyyymmdd') > TO_CHAR(sysdate,'yyyymmdd'))";
		
		List<SetlBinNewBinoParm> ret = new LinkedList<SetlBinNewBinoParm>();
		List<Map<String, Object>> list = super.queryListMap(sql, null);
		
		for(Map<String, Object> map : list) {
			SetlBinNewBinoParm o = new SetlBinNewBinoParm();
			o.setFid(MapUtils.getString(map, "NSSMBINR_FID"));
			o.setAcqIss(MapUtils.getString(map, "NSSMBINR_ACQ_ISS"));
			o.setCardtype(MapUtils.getString(map, "NSSMBINR_CRD_TYP"));
			o.setBinNoL(MapUtils.getString(map, "NSSMBINR_BIN_NUM_L"));
			o.setBinNoH(MapUtils.getString(map, "NSSMBINR_BIN_NUM_H"));
			o.setBinLen(MapUtils.getString(map, "NSSMBINR_BIN_LEN"));
			o.setInDate(MapUtils.getString(map, "NSSMBINR_IN_DATE"));
			o.setOutDate(MapUtils.getString(map, "NSSMBINR_OUT_DATE"));
			o.setIca(MapUtils.getString(map, "NSSMBINR_ICA"));
			o.setCardLen(MapUtils.getString(map, "NSSMBINR_CRD_LEN"));
			o.setTokenFlag(MapUtils.getString(map, "NSSMBINR_TOKEN_FLG"));
			o.setAuthOutDate(MapUtils.getString(map, "NSSMBINR_AUTH_OUT_DATE"));
			o.setJcbAcqId(MapUtils.getString(map, "NSSMBINR_JCB_ACQ_ID"));
			o.setIin(MapUtils.getString(map, "NSSMBINR_IIN"));
			o.setDualCurFlag(MapUtils.getString(map, "NSSMBINR_DUAL_CUR_FLG"));
			o.setCurCode(MapUtils.getString(map, "NSSMBINR_CUR_CDE"));
			o.setDebitFlag(MapUtils.getString(map, "NSSMBINR_DEBIT_FLG"));

			ret.add(o);
		}
		return ret;
	}
	
	/**
	 * query NSSMBANK
	 * @return
	 * @throws Exception
	 */
	public List<SetlBinBankParm> queryNssmbank() throws Exception{
		String sql = "select * from NSSMBANK a "
				+ " LEFT JOIN AUTHVOIC b ON a.NSSMBANK_BANK_ID = b.AUTHVOIC_BANK_ID "
				+ " where TO_CHAR(to_date(NSSMBANK_IN_DATE,'mmddyyyy') ,'yyyymmdd') <= TO_CHAR(sysdate,'yyyymmdd') "
				+ " AND (TO_CHAR(to_date(NVL(trim(NSSMBANK_OUT_DATE) ,'12312999'),'mmddyyyy'),'yyyyymmdd') > TO_CHAR(sysdate,'yyyymmdd'))";
		
		List<SetlBinBankParm> ret = new LinkedList<SetlBinBankParm>();
		List<Map<String, Object>> list = super.queryListMap(sql, null);
		
		for(Map<String, Object> map : list) {
			SetlBinBankParm o = new SetlBinBankParm();
			o.setFid(MapUtils.getString(map, "NSSMBANK_FID"));
			o.setBankId(MapUtils.getString(map, "NSSMBANK_BANK_ID"));
			
			String nssMBankMainId = MapUtils.getString(map, "NSSMBANK_MAN_ID");
			o.setManIdNccc(nssMBankMainId);
			o.setMainId(nssMBankMainId.substring(nssMBankMainId.length()-2));
			
			o.setFullName(MapUtils.getString(map, "NSSMBANK_FULL_NAME"));
			o.setAbbrName(MapUtils.getString(map, "NSSMBANK_ABBR_NAME"));
			o.setEngName(MapUtils.getString(map, "NSSMBANK_ENG_NAME"));
			o.setAbbrEng(MapUtils.getString(map, "NSSMBANK_ABBR_ENG"));
			o.setAddr(MapUtils.getString(map, "NSSMBANK_ADDR"));
			o.setZipCode(MapUtils.getString(map, "NSSMBANK_ZIP_CODE"));
			o.setAddrBuss(MapUtils.getString(map, "NSSMBANK_ADDR_BUSI"));
			o.setBussZip(MapUtils.getString(map, "NSSMBANK_BUSI_ZIP"));
			o.setInvoName(MapUtils.getString(map, "NSSMBANK_INVO_NUM"));
			o.setInDate(MapUtils.getString(map, "NSSMBANK_IN_DATE"));
			o.setOutDate(MapUtils.getString(map, "NSSMBANK_OUT_DATE"));
			o.setOutDateFlag(MapUtils.getString(map, "NSSMBANK_OUT_DATE_FLAG"));
			o.setProcId(MapUtils.getString(map, "NSSMBANK_PROC_ID"));
			o.setProcName(MapUtils.getString(map, "NSSMBANK_PROC_NAME"));
			o.setMedia(MapUtils.getString(map, "NSSMBANK_MEDIA"));
			o.setChnCode(MapUtils.getString(map, "NSSMBANK_CHN_CODE"));
			o.setEngCode(MapUtils.getString(map, "NSSMBANK_ENG_CODE"));
			o.setMemType(MapUtils.getString(map, "NSSMBANK_MEM_TYPE"));
			o.setRptWay(MapUtils.getString(map, "NSSMBANK_RPT_WAY"));
			o.setSettle(MapUtils.getString(map, "NSSMBANK_SETTLE"));
			o.setSettleFlag(MapUtils.getString(map, "NSSMBANK_SETTLE_FLAG"));
			o.setBussType(MapUtils.getString(map, "NSSMBANK_BUSI_TYPE"));
			o.setFilePrd(MapUtils.getString(map, "NSSMBANK_FILE_PRD"));
			o.setNmipSvrId(MapUtils.getString(map, "NSSMBANK_NMIP_SVR_ID"));
			o.setAbussMan(MapUtils.getString(map, "NSSMBANK_A_BUSI_MAN"));
			o.setAbussTel(MapUtils.getString(map, "NSSMBANK_A_BUSI_TEL"));
			o.setIbussMan(MapUtils.getString(map, "NSSMBANK_I_BUSI_MAN"));
			o.setIbussTel(MapUtils.getString(map, "NSSMBANK_I_BUSI_TEL"));
			o.setAct(MapUtils.getString(map, "NSSMBANK_ACT"));
			o.setAprFlag(MapUtils.getString(map, "NSSMBANK_APR_FLAG"));
			o.setUsrId(MapUtils.getString(map, "NSSMBANK_USR_ID"));
			o.setRptPageNum(MapUtils.getString(map, "NSSMBANK_RPT_PAGE_NUM"));
			o.setTapeFid(MapUtils.getString(map, "NSSMBANK_TAPE_FID"));
			o.setLnkCtr(MapUtils.getString(map, "NSSMBANK_LNK_CTR"));
			o.setSpecBuss(MapUtils.getString(map, "NSSMBANK_SPEC_BUSI"));
			o.setNtcAcqFlag(MapUtils.getString(map, "NSSMBANK_NTC_ACQ_FLG"));
			o.setNtcIssFlag(MapUtils.getString(map, "NSSMBANK_NTC_ISS_FLG"));
			o.setDailyChk(MapUtils.getString(map, "NSSMBANK_DAILY_CHK"));
			o.setDailyChkFlag(MapUtils.getString(map, "NSSMBANK_DAILY_CHK_FLG"));
			o.setStandIn(MapUtils.getString(map, "NSSMBANK_STAND_IN"));
			o.setStandInFlag(MapUtils.getString(map, "NSSMBANK_STAND_IN_FLG"));
			o.setInvoName(MapUtils.getString(map, "NSSMBANK_INVO_NAME"));
			o.setProcFeeMode(MapUtils.getString(map, "NSSMBANK_PROC_FEE_MODE"));
			o.setFeeModeFlag(MapUtils.getString(map, "NSSMBANK_FEE_MODE_FLG"));
			o.setNmipFid(MapUtils.getString(map, "NSSMBANK_NMIP_FID"));
			o.setAuthTel1(MapUtils.getString(map, "AUTHVOIC_TEL1"));
			o.setAuthTel2(MapUtils.getString(map, "AUTHVOIC_TEL2"));
			
			ret.add(o);
		}
		return ret;
	}	
}
