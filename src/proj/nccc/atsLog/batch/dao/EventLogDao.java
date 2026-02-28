package proj.nccc.atsLog.batch.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.Nsshmsgl;
import proj.nccc.atsLog.batch.dao.entity.SysEventLog;
import proj.nccc.atsLog.batch.dao.entity.SysEventMaster;
import proj.nccc.atsLog.batch.dao.other.NssDao;
import proj.nccc.atsLog.batch.util.DateUtil;
import proj.nccc.atsLog.batch.util.ProjInit;

@Log4j2
public class EventLogDao extends ProjDao {

	public void insertEventLog(SysEventLog sysEventLog) throws Exception {
		String sql = "insert into SYS_EVENT_LOG ("
				+ " UUID, CREATE_DATE, TXN_SERVER, EVENT_CODE, LOG_DESC, "
				+ " REQ_SEND_NSSMSG, REQ_SEND_EMAIL, SEND_DATE, EVENT_ITEM, NSSHMSGL_LEVEL"
				+ " ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
		
		int i = 1;
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
			}
			ps = con.prepareStatement(sql);
			ps.setString(i++, UUID.randomUUID().toString());
			ps.setTimestamp(i++, new Timestamp(new Date().getTime()));
			ps.setString(i++, sysEventLog.getHost());
			ps.setString(i++, sysEventLog.getEventCode());
			ps.setString(i++, sysEventLog.getLogDesc());
			ps.setString(i++, sysEventLog.getReqSendNssMsg());
			ps.setString(i++, sysEventLog.getReqSendEmail());
			
			if (sysEventLog.getSendDate() == null) {
				ps.setNull(i++, java.sql.Types.DATE);
			}else {
				ps.setTimestamp(i++, new Timestamp(sysEventLog.getSendDate().getTime()));
			}
			
			ps.setString(i++, sysEventLog.getEventItem());
			ps.setString(i++, sysEventLog.getNsshmsglLevel());
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			ps = null;
		}
	}
	
	/**
	 * also insert R6 log if EventNotice().charAt(0) == 'Y'
	 * 
	 * @param sysEventMaster
	 * @param description
	 * @throws Exception
	 */
	public void insertEventLog(String jobId, SysEventMaster sysEventMaster, String description) throws Exception {
		this.insertEventLog(jobId, sysEventMaster, description, true);
	}
	
	public void insertEventLog(String jobId, SysEventMaster sysEventMaster, String description, 
			boolean writeR6Allert)
			throws Exception {
		this.insertEventLog(jobId, sysEventMaster, description, writeR6Allert, "3");
	}
	
	public void insertEventLog(String jobId, SysEventMaster sysEventMaster, String description, 
			boolean writeR6Allert, String alertLevel)
			throws Exception {
		SysEventLog sysEventLog = new SysEventLog();
		sysEventLog.setCreateDate(new Date());
		sysEventLog.setEventCode(sysEventMaster.getEventCode());
		sysEventLog.setHost(ProjInit.HOST_NAME);
		sysEventLog.setLogDesc(sysEventMaster.getEventDesc() + "[" + description + "]"
				+ sysEventMaster.getEventLogicalRule() + sysEventMaster.getEventRuleValue());
		sysEventLog.setReqSendNssMsg(sysEventMaster.getEventNotice().charAt(0) == 'Y' ? "Y" : "N");
		sysEventLog.setReqSendEmail(sysEventMaster.getEventNotice().charAt(1) == 'Y' ? "N" : "Y");
		if (writeR6Allert) {
			// ---------------------------------------
			// -- sendDate 有值表示已發送 R6 NSSMsg 或已處理
			sysEventLog.setSendDate(new Date());
		}
		sysEventLog.setNsshmsglLevel("" + alertLevel);
		sysEventLog.setEventItem(jobId);
		insertEventLog(sysEventLog);
		if (writeR6Allert) {
			if (sysEventMaster.getEventNotice().charAt(0) == 'Y') {
				this.writeR6NssMsg(sysEventLog.getLogDesc(), description, alertLevel);
			}
		}
	}
	
	
	public void writeR6NssMsg(String logDesc, String description, String alertLevel) throws Exception {
		if ("3".equals(alertLevel)) {
			this.writeR6NssMsg(logDesc, description, alertLevel, "ERROR");
		}else {
			this.writeR6NssMsg(logDesc, description, alertLevel, "WARN");
		}
	}
	
	/**
	 * - 撖怠OP Log NssMsg 
	 * @param logDesc
	 * @param description
	 * @param alertLevel : 1 - 5
	 * @throws Exception
	 */
	public void writeR6NssMsg(String logDesc, String description, String alertLevel, String code) throws Exception {
		Nsshmsgl nssLog = new Nsshmsgl();
		nssLog.setPid(ProjInit.SYS_NAME + "_" + ThreadContext.get("BATCH_ID"));
		nssLog.setCode(code);
		nssLog.setErrMsg(logDesc.getBytes().length > 52
				? logDesc.substring(0,
						52 - (logDesc.getBytes().length
								- logDesc.length() > 52
										? 26
										: logDesc.getBytes().length
												- logDesc.length()))
				: logDesc);
		
		log.info(String.format("errMsg[%s]", nssLog.getErrMsg()));
		
		nssLog.setSysMsg(
				description.getBytes().length > 36
						? description
								.substring(0,
										36 - (description.getBytes().length - description.length() > 36 ? 18
												: description.getBytes().length - description.length()))
						: description);
		
		log.info(String.format("sysMsg[%s]", nssLog.getSysMsg()));
		
		nssLog.setLevel("" + alertLevel);
		nssLog.setSysDate(new Date());
		nssLog.setType("A");
		
		NssDao nssDao = new NssDao();
		nssDao.insertR6Log(nssLog);
		nssDao.close();		
	}

	public long checkEventLogByDescription(SysEventMaster sysEventMaster, String keyWord) {
		final String selectSQL = "select max(CREATE_DATE) CREATE_DATE from SYS_EVENT_LOG where EVENT_CODE=? and LOG_DESC like '%["
				+ keyWord + "]%'";
		long occurTime = new Date().getTime();
		long prevOccurTime = 0L;
		try {
			if (con == null) {
				con = super.getConnection();
			}
			ps = con.prepareStatement(selectSQL);
			ps.setString(1, sysEventMaster.getEventCode());
			rs = ps.executeQuery();
			if (rs.next()) {
				prevOccurTime = rs.getTimestamp("CREATE_DATE").getTime();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("EventDao.checkEventLog.finally exception:" + e);
			}
		}
		return (occurTime - prevOccurTime) / 60000;
	}

	public long checkEventLog(SysEventMaster sysEventMaster) {
		final String selectSQL = "select max(CREATE_DATE) CREATE_DATE from SYS_EVENT_LOG where EVENT_CODE=?";
		long occurTime = new Date().getTime();
		long prevOccurTime = 0L;
		try {
			if (con == null) {
				con = super.getConnection();
			}
			ps = con.prepareStatement(selectSQL);
			ps.setString(1, sysEventMaster.getEventCode());
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getTimestamp("CREATE_DATE") != null) {
					Date d = rs.getTimestamp("CREATE_DATE");
					if (d != null) {
						prevOccurTime = d.getTime();
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("EventDao.checkEventLog.finally exception:" + e);
			}
		}
		return (occurTime - prevOccurTime) / 60000;
	}

	public SysEventLog queryEventLog(String eventCode) throws Exception {
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			cmd.append("select * from SYS_EVENT_LOG "
					+ " where EVENT_CODE=? order by CREATE_DATE desc ");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			ps.setString(1, eventCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				return this.buildObj(rs);
			}
			return null;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("EventDao.getEventWaitList.finally exception:" + e);
			}
		}
	}

	public List<SysEventLog> getEventWaitList() throws Exception {
		List<SysEventLog> list = new LinkedList<SysEventLog>();
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			cmd.append("select * from SYS_EVENT_LOG "
					+ " where (REQ_SEND_EMAIL='Y' or REQ_SEND_NSSMSG ='Y') "
					+ " AND SEND_DATE is null "
					+ " order by CREATE_DATE ");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			while (rs.next()) {
				
				list.add(this.buildObj(rs));
			}

			return list;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("EventDao.getEventWaitList.finally exception:" + e);
			}
		}
	}

	public void updateEventLog(String uuid) throws Exception {
		PreparedStatement ps = null;
		try {
			String sql = "update SYS_EVENT_LOG set SEND_DATE = ? where UUID = ? ";
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.setString(2, uuid);
			ps.executeUpdate();
			return;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			if (ps != null) {
				ps.close();
			}
			log.info("update event Log finish : " + uuid);
		}
	}
	
	/**
	 * 取得尚未處理的 Event 資料
	 * @return
	 * @throws Exception 
	 */
	public List<SysEventLog> queryByNotProcess() throws Exception{
		List<SysEventLog> list = new LinkedList<SysEventLog>();
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			// -------------------------------------------------------------
			// -- 配合 cron job 僅判斷過去 15 mins 的 eventLog
			// -- CREATE_DATE > SYSDATE - 15/24/60
			// --
			cmd.append("select * from SYS_EVENT_LOG "
					+ " where (REQ_SEND_EMAIL='Y' or REQ_SEND_NSSMSG ='Y') "
					+ " AND SEND_DATE is null "
					+ " AND CREATE_DATE > SYSDATE - 15/24/60 "
					+ " order by TXN_SERVER, EVENT_CODE, CREATE_DATE DESC ");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			while (rs.next()) {
				
				list.add(this.buildObj(rs));
			}

			return list;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("exception", e);
			}
		}
	}
	
	/**
	 * 取得該日期之後的資料
	 * @param date
	 * @param eventItem
	 * @param eventCode
	 * @param txnServer
	 * @return
	 * @throws Exception
	 */
	public List<SysEventLog> getEventLogAfter(Date date, String eventItem, 
			String eventCode, String txnServer) throws Exception {
		List<SysEventLog> list = new LinkedList<SysEventLog>();
		StringBuffer cmd = new StringBuffer("");
		
		// -- 20200125 205018
		String yyyymmddhhmmss = DateUtil.dateToString(date, "yyyyMMdd HHmmss");
		
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			cmd.append("select * from SYS_EVENT_LOG "
					+ " where CREATE_DATE >= to_date(?, 'YYYYMMDD HH24MISS')"
					+ " and EVENT_ITEM = ? "
					+ " and EVENT_CODE = ? "
					+ " and TXN_SERVER = ? "
					);
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			int i = 1;
			ps.setString(i++, yyyymmddhhmmss);
			ps.setString(i++, eventItem);
			ps.setString(i++, eventCode);
			ps.setString(i++, txnServer);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				list.add(this.buildObj(rs));
			}
			return list;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				super.closeRsPs();
			} catch (Exception e) {
				log.error("EventDao.getEventLog.finally exception:" + e);
			}
		}
	}
	
	protected SysEventLog buildObj(ResultSet rs) throws SQLException {
		SysEventLog obj = new SysEventLog();
		obj.setUuid(rs.getString("UUID"));
		obj.setCreateDate(rs.getTimestamp("CREATE_DATE"));
		obj.setHost(rs.getString("TXN_SERVER"));
		obj.setEventCode(rs.getString("EVENT_CODE"));
		obj.setLogDesc(rs.getString("LOG_DESC"));
		obj.setReqSendNssMsg(rs.getString("REQ_SEND_NSSMSG"));
		obj.setReqSendEmail(rs.getString("REQ_SEND_EMAIL"));
		obj.setSendDate(rs.getTimestamp("SEND_DATE"));
		obj.setEventItem(rs.getString("EVENT_ITEM"));
		obj.setNsshmsglLevel(rs.getString("NSSHMSGL_LEVEL"));
		return obj;
	}
}
