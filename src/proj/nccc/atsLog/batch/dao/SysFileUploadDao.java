package proj.nccc.atsLog.batch.dao;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.entity.SysFileUpload;
import proj.nccc.atsLog.batch.util.ProjInit;

@Log4j2
public class SysFileUploadDao extends ProjDao {
	
	@Override
	public SysFileUpload buildObj(ResultSet resultSet) throws Exception {
		SysFileUpload o = new SysFileUpload();
		o.setUuid(rs.getString("UUID"));
		o.setItemCategory(rs.getString("ITEM_CATEGORY"));
		o.setItemType(rs.getString("ITEM_TYPE"));
		o.setFileMonth(rs.getString("FILE_MONTH"));
		o.setFileName(rs.getString("FILE_NAME"));
		o.setPlanUploadDate(rs.getString("PLAN_UPLOAD_DATE"));
		o.setStatus(rs.getString("STATUS"));
		
		o.setResendRetryCount(rs.getInt("RESEND_RETRY_COUNT"));
		Blob blob1 = rs.getBlob("FILE_BLOB");
		if (blob1 != null) {
			o.setFileBlob(blob1.getBytes(1, (int)blob1.length()));
		}
		if (rs.getTimestamp("CREATE_DATE") != null) {
			o.setCreateDate(rs.getTimestamp("CREATE_DATE"));
		}
		if (rs.getTimestamp("UPLOAD_DATE") != null) {
			o.setUploadDate(rs.getTimestamp("UPLOAD_DATE"));
		}
		
		return o;
	}
	
	/**
	 * - 新增
	 * @param o
	 * @throws Exception
	 */
	public void insert(SysFileUpload o) throws Exception {
		String sql = "Insert into SYS_FILE_UPLOAD "
				+ " (UUID, ITEM_CATEGORY, ITEM_TYPE, FILE_MONTH, FILE_BLOB, FILE_NAME, "
				+ " PLAN_UPLOAD_DATE, STATUS, RESEND_RETRY_COUNT, CREATE_DATE, UPLOAD_DATE) "
				+ "  Values "
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		int i = 1;
		try {
			if (con == null || con.isClosed()) {
				con = super.getConnection();
			}
			ps = con.prepareStatement(sql);
			ps.setString(i++, o.getUuid());
			ps.setString(i++, o.getItemCategory());
			ps.setString(i++, o.getItemType());
			ps.setString(i++, o.getFileMonth());
			ps.setBytes(i++, o.getFileBlob());
			ps.setString(i++, o.getFileName());
			
			ps.setString(i++, o.getPlanUploadDate());
			ps.setString(i++, o.getStatus());
			ps.setInt(i++, o.getResendRetryCount());
			ps.setTimestamp(i++, new Timestamp(o.getCreateDate().getTime()));
			
			ps.setNull(i++, java.sql.Types.DATE);
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
	 * - ??
	 * @param uuid
	 * @param status
	 * @param uploadDate
	 * @throws Exception
	 */
	public void update(String uuid, String status, String fileName, Date uploadDate) throws Exception {
		String sql = "update SYS_FILE_UPLOAD set STATUS = ? , FILE_NAME = ? ";
		
		List<Object> params = new LinkedList<Object>();
		params.add(status);
		params.add(fileName);
		if (ProjInit.FTP_SEND_SUCCESS.equals(status)) {
			sql += ", UPLOAD_DATE = ? ";
			params.add(uploadDate);
		}
		sql += " where UUID = ? ";
		params.add(uuid);
		
		super.execUpdate(sql, params);
		
	}
	
	/**
	 * - 更新
	 * @param uuid
	 * @param status
	 * @param uploadDate
	 * @throws Exception
	 */
	public void updateCount(String uuid) throws Exception {
		String sql = "update SYS_FILE_UPLOAD set RESEND_RETRY_COUNT = RESEND_RETRY_COUNT + 1 "
				+ " where UUID = ? ";
		List<Object> params = new LinkedList<Object>();
		params.add(uuid);
		
		super.execUpdate(sql, params);
	}
	
	
	/**
	 * Delete
	 * @param itemType
	 * @param fileMonth : YYYYMM or YYYYMMDD
	 * @throws Exception
	 */
	public void delete(String itemCategory, String itemType, String fileMonth) throws Exception{
		
		String sql = "Delete SYS_FILE_UPLOAD where "
				+ " ITEM_CATEGORY = ? AND ITEM_TYPE = ? AND FILE_MONTH = ?";
		List<Object> params = new LinkedList<Object>();
		params.add(itemCategory);
		params.add(itemType);
		params.add(fileMonth);
		super.execUpdate(sql, params);
		return;
	}
	
	
	/**
	 * - 依狀態碼取得資料
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<SysFileUpload> queryByStatus(String status) throws Exception{
		String sql = "select * from SYS_FILE_UPLOAD where STATUS = ? ";
		List<String> params = new LinkedList<String>();
		params.add(status);
		
		@SuppressWarnings("unchecked")
		List<SysFileUpload> list = (List<SysFileUpload>) queryList(sql, params);
		
		return list;
	}
	
	
}
