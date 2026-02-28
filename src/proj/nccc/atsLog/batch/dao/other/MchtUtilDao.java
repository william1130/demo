package proj.nccc.atsLog.batch.dao.other;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import proj.nccc.atsLog.batch.dao.connection.MchtConn;
import proj.nccc.atsLog.batch.vo.ValueVO;


/**
 * Mcht系統util Dao
 * 
 */

@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})
public class MchtUtilDao extends MchtConn {
	public int count(String table, String where) throws Exception {
		int i = 0;
		StringBuffer cmd = new StringBuffer("");
		try {
			con = super.getConnection();
			cmd.append("SELECT count(*) FROM " + table);
			cmd.append(" WHERE " + where);

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

	public List<String> getMiniPosEnable(String today) {
		String sqlValidateBizType = "select MERCHANT_NO From MERCHANT_BUSINESS_DATA "
				+ " WHERE BUSINESS_TYPE = '039' "
				+ " AND start_date <= ? " + " AND ( end_date is null OR end_date >= ?) ";
		List<String> retList = new LinkedList<String>();
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(sqlValidateBizType);
			ps.clearParameters();
			ps.setString(1, today);
			ps.setString(2, today);
			rs = ps.executeQuery();
			while (rs.next()) {
				String s = rs.getString(1);
				log.debug("MERCHANT_BUSINESS_DATA.BUSINESS_TYPE = '039' :" + s);
				retList.add(s);
			}
			return retList;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return retList;
		} finally {
			try {
				// 不close connect , for performance
				// this.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("MchtUtilDao.getLoyaltyEnable.finally exception:" + e);
			}
		}
	}

	public Map<String, String> getAreaMasterNameMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			cmd.append("SELECT AREA_CODE, AREA_CHIN_NAME FROM AREA_MASTER order by AREA_CODE");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("AREA_CODE"), rs.getString("AREA_CHIN_NAME"));
			}
			return map;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				// 不close connect , for performance
				// this.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("MchtUtilDao.getAreaMap.finally exception:" + e);
			}
		}
	}

	public Map<String, String> getInduMasterNameMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer cmd = new StringBuffer("");
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			cmd.append("SELECT INDU_CODE, CHIN_NAME FROM INDUSTRY_MASTER order by INDU_CODE");
			ps = con.prepareStatement(cmd.toString());
			ps.clearParameters();
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("INDU_CODE"), rs.getString("CHIN_NAME"));
			}
			return map;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				// 不close connect , for performance
				// this.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				rs = null;
				ps = null;
			} catch (Exception e) {
				log.error("MchtUtilDao.getIndustryMap.finally exception:" + e);
			}
		}
	}

	public List<ValueVO> getInstallFile() throws Exception {
		List<ValueVO> dataList = new LinkedList();
		final String selectSQL= "select MERCHANT_NO, BANK_NO, INSTALLMENT_NO, BIN_NO_START, BIN_NO_END, EFFECTIVE_DATE, NONEFFECTIVE_DATE, CARD_TYPE from INSTALLMENT_FILE";
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(selectSQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				ValueVO vo = new ValueVO();
				vo.setCode(rs.getString("MERCHANT_NO"));
				vo.setValue1(rs.getString("BANK_NO"));
				vo.setValue2(rs.getString("INSTALLMENT_NO"));
				vo.setValue3(rs.getString("BIN_NO_START"));
				vo.setValue4(rs.getString("BIN_NO_END"));
				vo.setValue5(rs.getString("EFFECTIVE_DATE"));
				vo.setValue6(rs.getString("NONEFFECTIVE_DATE"));
				vo.setValue7(rs.getString("CARD_TYPE"));
				dataList.add(vo);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				log.error("MchtUtilDao.getInstallFile.finally exception:" + e);
			}
		}
		return dataList;
	}
	
	//M2016173 add function
	public boolean isSubStMerchant(String merchantNo) throws SQLException, Exception {
		boolean finalResult = false;
		int resultCount = 0;
		final String selectSQL= "select count(*) from merchant_sub_st where upper_merchant_no=?";
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(selectSQL);
			ps.setString(1, merchantNo);
			rs = ps.executeQuery();
			while (rs.next()) {
				resultCount = rs.getInt(1);
			}
			if (resultCount > 0)
				return true;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				log.error("MchtUtilDao.isSubStMerchant.finally exception:" + e);
			}
		}
		return finalResult;
	}
	
	//M2016173 add function //M2017185 modify return String[], [0]=CHIN, [1]=ENG
	// 20190807 add return value : [2]=MCC_CODE
	public String[] getSubStMerchantName(String terminalNo) throws SQLException, Exception {
		String[] resultValue = {"", "", ""};
		final String selectSQL= "select ST_CHIN_NAME, ST_ENG_NAME, MCC_CODE from merchant_sub_st where terminal_no=?";
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(selectSQL);
			ps.setString(1, terminalNo);
			rs = ps.executeQuery();
			while (rs.next()) {
				resultValue[0] = rs.getString("ST_CHIN_NAME") == null ? "" : rs.getString("ST_CHIN_NAME");
				resultValue[1] = rs.getString("ST_ENG_NAME") == null ? "" : rs.getString("ST_ENG_NAME");
				resultValue[2] = rs.getString("MCC_CODE") == null ? "" : rs.getString("MCC_CODE");
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				log.error("MchtUtilDao.getSubStMerchantName.finally exception:" + e);
			}
		}
		return resultValue;
	}
	
	/**
	 * 取得特店資訊
	 * @param mchtNo
	 * @param infoCode
	 * @return Map<String, String>
	 * INFO_MOBILE_PHONE, INFO_EMAIL
	 * @throws Exception 
	 */
	public Map<String, String> getMchtInfo(String mchtNo, String infoCode) throws Exception{
		String sql = "SELECT MERCHANT_NO, INFO_MOBILE_PHONE, INFO_EMAIL "
				+ " FROM MERCHANT_INFO_DATA "
				+ " WHERE MERCHANT_NO =? AND INFO_CODE = ? ";
		Map<String, String> result = null;
		try {
			if (con == null || con.isClosed())
				con = super.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, mchtNo);
			ps.setString(2, infoCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = new HashMap<String, String>();
				result.put("INFO_MOBILE_PHONE", rs.getString("INFO_MOBILE_PHONE"));
				result.put("INFO_EMAIL", rs.getString("INFO_EMAIL"));
			}
			return result;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				log.error("ERROR", e);
			}
		}
	}
	
}
