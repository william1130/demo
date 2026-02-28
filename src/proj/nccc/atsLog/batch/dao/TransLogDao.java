package proj.nccc.atsLog.batch.dao;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import proj.nccc.atsLog.batch.dao.entity.SkmData;

public class TransLogDao extends ProjDao {

	@Override
	public SkmData buildObj(ResultSet resultSet) throws Exception {
		SkmData o = new SkmData();
		o.setUpperMerchantNo(resultSet.getString("UPPER_MERCHANT_NO"));
		o.setMerchantNo(resultSet.getString("MERCHANT_ID"));
		o.setMerchantName(resultSet.getString("CHIN_NAME"));
		o.setTermId(resultSet.getString("TERM_ID"));
		o.setLogDate(resultSet.getString("LOG_DATE"));
		o.setBatchNum(resultSet.getString("BATCH_NUM"));
		o.setAmount(resultSet.getDouble("TRAN_AMOUNT"));
		return o;
	}
	
	/**
	 * -- queryTransLogByTid
	 * @return : SkmData
	 * @throws Exception
	 */
	public List<SkmData> queryTransLogByTid(String date1, String date2, String tId1, String tId2) throws Exception{
		
		String sql = " SELECT "
				+ " MD.UPPER_MERCHANT_NO, MERCHANT_ID, MC.CHIN_NAME, TERM_ID, LOG_DATE, BATCH_NUM, TRAN_AMOUNT "
				+ " FROM ATSLOG_TRANS_LOG A, MERCHANT_DATA MD, MERCHANT_CHINESE_DATA MC "
				+ " WHERE A.MERCHANT_ID = MD.MERCHANT_NO"
				+ " AND MD.MERCHANT_NO = MC.MERCHANT_NO "
				+ " AND MC.CARD_CODE = '03' "
				+ " AND EDC_MTI = '0500' " // --結帳
				+ " AND RESP_CODE = '00' " // --結帳成功
				+ " AND LOG_DATE BETWEEN ? AND ? "
				+ " AND TERM_ID BETWEEN ? AND ? "
				+ " ORDER BY MERCHANT_ID, TERM_ID ";
		List<String> params = new LinkedList<String>();
		params.add(date1);
		params.add(date2);
		params.add(tId1);
		params.add(tId2);

		@SuppressWarnings("unchecked")
		List<SkmData> list = (List<SkmData>) queryList(sql, params);
		return list;
	}
	
}
