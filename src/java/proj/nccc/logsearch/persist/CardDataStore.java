/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id:
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.edstw.persist.Persistable;
import com.edstw.persist.jdbc.FieldInfo;

/**
 * 
 * @author APAC\czrm4t $
 * @version $Revision: 1.2 $
 */
public class CardDataStore extends ProjStore {

	private static List<FieldInfo> fieldInfos;
	static {
		// 依序定義欄位名稱及是否為primary key
		fieldInfos = new LinkedList();
		fieldInfos.add(new FieldInfo("CARD_NO", true));
		fieldInfos.add(new FieldInfo("BANK_NO", false));
		fieldInfos.add(new FieldInfo("LAST_PURCHASE_DATE", false));
		fieldInfos.add(new FieldInfo("LAST_APPROVE_NO", false));
		fieldInfos.add(new FieldInfo("BILL_AMT", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_AMT_TIME", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_LIMIT_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("NORMAL_AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("NORMAL_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("NORMAL_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("AUTH_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("AUTH_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("AUTH_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("AUTH_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("AUTH_REJECT_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("FOREIGN_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("FOREIGN_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("FOREIGN_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("FOREIGN_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_AMT_TIME", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_LIMIT_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("CASH_BILL_AMT", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_LAST_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("CASH_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("CASH_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_AMT_MONTH", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_AMT_DAY", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_AMT_TIME", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_CNT_MONTH", false));
		fieldInfos.add(new FieldInfo("TOTAL_LIMIT_CNT_DAY", false));
		fieldInfos.add(new FieldInfo("PRE_AUTH_AMT", false));
	}

	/** Creates a new instance of CardDataStore */
	public CardDataStore() {

	}

	/**
	 * 提供本store物件對應之table name.
	 */
	public String getTableName() {

		return "AUTH_CARD_DATA";
	}

	/**
	 * 提供table欄位的相關資訊, 以便組出PreparedStatement所需的SQL command.
	 */
	public List getFieldInfos() {

		return fieldInfos;
	}

	protected void insert(Persistable persistable, PreparedStatement pstmt) throws SQLException {

		int i = 1;
		CardData o = (CardData) persistable;

		if (o.getCardNo() != null)
			pstmt.setString(i++, o.getCardNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);
		if (o.getBankNo() != null)
			pstmt.setString(i++, o.getBankNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLastPurchaseDate() != null)
			pstmt.setString(i++, o.getLastPurchaseDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLastApproveNo() != null)
			pstmt.setString(i++, o.getLastApproveNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getBillAmt() != null)
			pstmt.setDouble(i++, o.getBillAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitCntMonth() != null)
			pstmt.setInt(i++, o.getNormalLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitCntDay() != null)
			pstmt.setInt(i++, o.getNormalLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtLastMonth() != null)
			pstmt.setDouble(i++, o.getNormalAmtLastMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtMonth() != null)
			pstmt.setDouble(i++, o.getNormalAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtDay() != null)
			pstmt.setDouble(i++, o.getNormalAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalCntMonth() != null)
			pstmt.setInt(i++, o.getNormalCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalCntDay() != null)
			pstmt.setInt(i++, o.getNormalCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthAmtMonth() != null)
			pstmt.setDouble(i++, o.getAuthAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthAmtDay() != null)
			pstmt.setDouble(i++, o.getAuthAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthCntMonth() != null)
			pstmt.setInt(i++, o.getAuthCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthCntDay() != null)
			pstmt.setInt(i++, o.getAuthCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthRejectCntMonth() != null)
			pstmt.setInt(i++, o.getAuthRejectCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignAmtMonth() != null)
			pstmt.setDouble(i++, o.getForeignAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignAmtDay() != null)
			pstmt.setDouble(i++, o.getForeignAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignCntMonth() != null)
			pstmt.setInt(i++, o.getForeignCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignCntDay() != null)
			pstmt.setInt(i++, o.getForeignCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitCntMonth() != null)
			pstmt.setInt(i++, o.getCashLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitCntDay() != null)
			pstmt.setInt(i++, o.getCashLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashBillAmt() != null)
			pstmt.setDouble(i++, o.getCashBillAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtLastMonth() != null)
			pstmt.setDouble(i++, o.getCashAmtLastMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtMonth() != null)
			pstmt.setDouble(i++, o.getCashAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtDay() != null)
			pstmt.setDouble(i++, o.getCashAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashCntMonth() != null)
			pstmt.setInt(i++, o.getCashCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashCntDay() != null)
			pstmt.setInt(i++, o.getCashCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitCntMonth() != null)
			pstmt.setInt(i++, o.getTotalLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitCntDay() != null)
			pstmt.setInt(i++, o.getTotalLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getPreAuthAmt() != null)
			pstmt.setDouble(i++, o.getPreAuthAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

	}

	protected void update(Persistable persistable, PreparedStatement pstmt) throws SQLException {

		int i = 1;
		CardData o = (CardData) persistable;
		if (o.getBankNo() != null)
			pstmt.setString(i++, o.getBankNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLastPurchaseDate() != null)
			pstmt.setString(i++, o.getLastPurchaseDate());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getLastApproveNo() != null)
			pstmt.setString(i++, o.getLastApproveNo());
		else
			pstmt.setNull(i++, java.sql.Types.VARCHAR);

		if (o.getBillAmt() != null)
			pstmt.setDouble(i++, o.getBillAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getNormalLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitCntMonth() != null)
			pstmt.setInt(i++, o.getNormalLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalLimitCntDay() != null)
			pstmt.setInt(i++, o.getNormalLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtLastMonth() != null)
			pstmt.setDouble(i++, o.getNormalAmtLastMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtMonth() != null)
			pstmt.setDouble(i++, o.getNormalAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalAmtDay() != null)
			pstmt.setDouble(i++, o.getNormalAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalCntMonth() != null)
			pstmt.setInt(i++, o.getNormalCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getNormalCntDay() != null)
			pstmt.setInt(i++, o.getNormalCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthAmtMonth() != null)
			pstmt.setDouble(i++, o.getAuthAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthAmtDay() != null)
			pstmt.setDouble(i++, o.getAuthAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthCntMonth() != null)
			pstmt.setInt(i++, o.getAuthCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthCntDay() != null)
			pstmt.setInt(i++, o.getAuthCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getAuthRejectCntMonth() != null)
			pstmt.setInt(i++, o.getAuthRejectCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignAmtMonth() != null)
			pstmt.setDouble(i++, o.getForeignAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignAmtDay() != null)
			pstmt.setDouble(i++, o.getForeignAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignCntMonth() != null)
			pstmt.setInt(i++, o.getForeignCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getForeignCntDay() != null)
			pstmt.setInt(i++, o.getForeignCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getCashLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitCntMonth() != null)
			pstmt.setInt(i++, o.getCashLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashLimitCntDay() != null)
			pstmt.setInt(i++, o.getCashLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashBillAmt() != null)
			pstmt.setDouble(i++, o.getCashBillAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtLastMonth() != null)
			pstmt.setDouble(i++, o.getCashAmtLastMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtMonth() != null)
			pstmt.setDouble(i++, o.getCashAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashAmtDay() != null)
			pstmt.setDouble(i++, o.getCashAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashCntMonth() != null)
			pstmt.setInt(i++, o.getCashCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getCashCntDay() != null)
			pstmt.setInt(i++, o.getCashCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtMonth() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtMonth().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtDay() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtDay().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitAmtTime() != null)
			pstmt.setDouble(i++, o.getTotalLimitAmtTime().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitCntMonth() != null)
			pstmt.setInt(i++, o.getTotalLimitCntMonth().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getTotalLimitCntDay() != null)
			pstmt.setInt(i++, o.getTotalLimitCntDay().intValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		if (o.getPreAuthAmt() != null)
			pstmt.setDouble(i++, o.getPreAuthAmt().doubleValue());
		else
			pstmt.setNull(i++, java.sql.Types.NUMERIC);

		pstmt.setString(i++, o.getCardNo());
	}

	protected void delete(Persistable persistable, PreparedStatement pstmt) throws SQLException {

		int i = 1;
		CardData o = (CardData) persistable;
		pstmt.setString(i++, o.getCardNo());
	}

	protected String getPreparedUpdateCmd() {

		return new StringBuffer("UPDATE AUTH_CARD_DATA       " + "SET    BANK_NO                = ? ,"
				+ "       LAST_PURCHASE_DATE     = ? ," + "       LAST_APPROVE_NO        = ? ,"
				+ "       BILL_AMT               = ? ," + "       NORMAL_LIMIT_AMT_MONTH = ? ,"
				+ "       NORMAL_LIMIT_AMT_DAY   = ? ," + "       NORMAL_LIMIT_AMT_TIME  = ? ,"
				+ "       NORMAL_LIMIT_CNT_MONTH = ? ," + "       NORMAL_LIMIT_CNT_DAY   = ? ,"
				+ "       NORMAL_AMT_LAST_MONTH  = ? ," + "       NORMAL_AMT_MONTH       = ? ,"
				+ "       NORMAL_AMT_DAY         = ? ," + "       NORMAL_CNT_MONTH       = ? ,"
				+ "       NORMAL_CNT_DAY         = ? ," + "       AUTH_AMT_MONTH         = ? ,"
				+ "       AUTH_AMT_DAY           = ? ," + "       AUTH_CNT_MONTH         = ? ,"
				+ "       AUTH_CNT_DAY           = ? ," + "       AUTH_REJECT_CNT_MONTH  = ? ,"
				+ "       FOREIGN_AMT_MONTH      = ? ," + "       FOREIGN_AMT_DAY        = ? ,"
				+ "       FOREIGN_CNT_MONTH      = ? ," + "       FOREIGN_CNT_DAY        = ? ,"
				+ "       CASH_LIMIT_AMT_MONTH   = ? ," + "       CASH_LIMIT_AMT_DAY     = ? ,"
				+ "       CASH_LIMIT_AMT_TIME    = ? ," + "       CASH_LIMIT_CNT_MONTH   = ? ,"
				+ "       CASH_LIMIT_CNT_DAY     = ? ," + "       CASH_BILL_AMT          = ? ,"
				+ "       CASH_AMT_LAST_MONTH    = ? ," + "       CASH_AMT_MONTH         = ? ,"
				+ "       CASH_AMT_DAY           = ? ," + "       CASH_CNT_MONTH         = ? ,"
				+ "       CASH_CNT_DAY           = ? ," + "       TOTAL_LIMIT_AMT_MONTH  = ? ,"
				+ "       TOTAL_LIMIT_AMT_DAY    = ? ," + "       TOTAL_LIMIT_AMT_TIME   = ? ,"
				+ "       TOTAL_LIMIT_CNT_MONTH  = ? ," + "       TOTAL_LIMIT_CNT_DAY    = ? ,"
				+ "       PRE_AUTH_AMT           = ? ," + "       FILE_DATE              = ? ,"
				+ "       MOD_PROGRAM_ID         = ? ," + "       UPDATE_DATE            = ? ,"
				+ "       UPDATE_ID              = ? ," + "       UPDATE_NAME            = ? ,"
				+ "       UPDATE_DEPT_NAME       = ? ," + "       CONFIRM_DATE           = ? ,"
				+ "       CONFIRM_ID             = ? ," + "       CONFIRM_NAME           = ? ,"
				+ "       CONFIRM_DEPT_NAME      = ? ," + "       CONFIRM_FLAG           = ? ,"
				+ "       REMARK                 = ?  " + "WHERE  CARD_NO                = ENCRYPT_3KEY_MODE(?) ")
						.toString();
	}

}
