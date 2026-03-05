package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import proj.nccc.logsearch.vo.MFESCheckoutLogArg;

import com.edstw.persist.jdbc.AbstractJdbcPersistableBuilder;
import com.edstw.persist.jdbc.JdbcPersistable;
import com.edstw.persist.jdbc.JdbcPersistableBuilder;
import com.edstw.service.QueryServiceException;
import com.edstw.util.ValidateUtil;

public class MFESCheckoutLogQSImpl extends ProjQS {

	private static JdbcPersistableBuilder builder = new AbstractJdbcPersistableBuilder() {
		protected JdbcPersistable build(ResultSet rs) throws SQLException {
			MFESCheckoutLogArg obj = new MFESCheckoutLogArg();

			obj.setMerchantId(rs.getString("MCHT_NO"));

			return obj;
		}
	};

	public int queryCount(MFESCheckoutLogArg arg) throws QueryServiceException {
		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append(
				"select count(*) cnt from EMS_MFES A, EMS_MFES_PARAM B where A.APPLY_CODE = B.APPLY_CODE and B.MFES_AUTO_DOWNLOAD_FLAG = 'Y' and B.CLOUD_FES = 'Y' and A.FES_TYPE= 'Y' and A.apply_code = ? and A.MCHT_TYPE IN ('11','12','13') and A.START_DATE <= to_char(sysdate,'yyyymmdd') and (A.END_DATE IS NULL or A.END_DATE >= to_char(sysdate,'yyyymmdd'))");
//		System.out.println("queryCount " + cmd);
		prepareArg(arg, params, cmd);

		String value = this.queryString(cmd.toString(), params, "cnt");

		return Integer.parseInt(value);
	}

	private void prepareArg(MFESCheckoutLogArg arg, List<Object> params, StringBuffer cmd) {

		// 交易代碼
		if (ValidateUtil.isNotBlank(arg.getApplyCode())) {
			params.add(arg.getApplyCode());
		}
	}

	/**
	 * @return
	 * @throws QueryServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<MFESCheckoutLogArg> query(MFESCheckoutLogArg arg) throws QueryServiceException {

		List<Object> params = new ArrayList<Object>();

		StringBuffer cmd = new StringBuffer().append(
				"select distinct a.MCHT_NO from EMS_MFES A, EMS_MFES_PARAM B where A.APPLY_CODE = B.APPLY_CODE and B.MFES_AUTO_DOWNLOAD_FLAG = 'Y' and B.CLOUD_FES = 'Y' and A.FES_TYPE= 'Y' and A.apply_code = ? and A.MCHT_TYPE IN ('11','12','13') and A.START_DATE <= to_char(sysdate,'yyyymmdd') and (A.END_DATE IS NULL or A.END_DATE >= to_char(sysdate,'yyyymmdd')) order by a.MCHT_NO");

		prepareArg(arg, params, cmd);

		List<MFESCheckoutLogArg> list = this.queryObjectList(cmd.toString(), params, builder);

		return list;
	}

}
