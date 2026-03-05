
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: CallBankParmQSImpl.java,v 1.2 2019/10/21 01:11:01 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.CallBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.CallBankParmArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.lang.DoubleString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class CallBankParmQSImpl extends AbstractJdbcPersistableQueryService implements CallBankParmQS {
	public String getServiceName() {
		return "CallBankParm Query Service";
	}

	public List<CallBankParm> queryByExample(CallBankParmArg obj) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BANK_PARM ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));
		}
		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, CallBankParmBuilder, obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, CallBankParmBuilder);
	}

	public List<CallBankParm> queryAll() throws QueryServiceException {
		StringBuffer cmd = new StringBuffer("SELECT * FROM AUTH_CALLBANK_PARM ");
		return super.queryObjectList(cmd.toString(), CallBankParmBuilder);
	}

	public void setServiceParams(Map arg0) throws ServiceException {
		// do nothing
	}

	public CallBankParm queryByPrimaryKey() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public CallBankParm queryByPrimaryKey(String id) throws QueryServiceException {
		List params = new LinkedList();
		params.add(id);
		StringBuffer cmd = new StringBuffer(
				"SELECT rowid as X , AUTH_CALLBANK_PARM.PARM_AMT FROM AUTH_CALLBANK_PARM WHERE ROWID=? ");

		List<CallBankParm> list = super.queryObjectList(cmd.toString(), params, CallBankParmBuilder);
		return list.size() > 0 ? list.get(0) : null;

	}

	public List<CallBankParm> queryByPrimaryKeys(List<String> ids) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public DoubleString getCallBankParmAmt(String parmcode) throws QueryServiceException {
		// 先以特店代號為主，若找得到特店代號的參數便以其限額為參考依據
		DoubleString parmamt = null;
		List<String> params = new LinkedList<String>();
		params.add(parmcode);
		StringBuffer cmd = new StringBuffer();
		cmd.append(" SELECT parm_amt  FROM AUTH_CALLBANK_PARM  ");
		cmd.append(" WHERE     parm_type = '2'                                                 ");
		cmd.append(" AND       parm_code = ?                   ");

		List list = super.queryValueList(cmd.toString(), params, "parm_amt");

		if (list.size() != 0) {
//			System.out.print("merchant is found in callbank parm :" + list);
			String test = (String) list.get(0);
			parmamt = new DoubleString(test);
		} else {

			// 若無by特店代號之參數，便尋找行業別的參數便以其限額為參考依據
			StringBuffer cmd2 = new StringBuffer();
			cmd2.append(" SELECT parm_amt  FROM AUTH_CALLBANK_PARM  ");
			cmd2.append(" WHERE     parm_type = '1'                 ");
			cmd2.append(" AND       parm_code = substr(?,3,3)");
			List list2 = super.queryValueList(cmd2.toString(), params, "parm_amt");
//			System.out.print("industry  in callbank parm :" + list2);
			if (list2.size() != 0) {
				String test2 = (String) list2.get(0);
				parmamt = new DoubleString(test2);
			}

		}
		return parmamt;
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List queryByExample(ProjPersistableArg arg) throws QueryServiceException {
		// TODO Auto-generated method stub
		return queryByExample((CallBankParmArg) arg);
	}

}