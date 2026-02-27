
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBankParmQSImpl.java,v 1.2 2019/10/21 01:11:02 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.edstw.lang.DateString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;
import com.edstw.util.ValidateUtil;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.BinBankParmArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class BinBankParmQSImpl extends AbstractJdbcPersistableQueryService implements BinBankParmQS {
	public String getServiceName() {
		return "BinBankParm Query Service";
	}

	public void setServiceParams(Map arg0) throws ServiceException {
		// do nothing
	}

	public BinBankParm queryByPrimaryKey() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public BinBankParm queryByPrimaryKey(String id) throws QueryServiceException {
		List params = new LinkedList();
		params.add(id);
		StringBuffer cmd = new StringBuffer(
				"SELECT rowid as X , SETL_BIN_BANK_PARM.* FROM SETL_BIN_BANK_PARM WHERE ROWID=? ");

		List<BinBankParm> list = query(cmd.toString(), params, BinBankParmBuilder);
		return list.size() > 0 ? list.get(0) : null;

	}

	public List<BinBankParm> queryByPrimaryKeys(List<String> ids) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<BinBankParm> queryByExample(BinBankParmArg obj) throws QueryServiceException {

		List<String> params = new LinkedList<String>();
		StringBuffer criteria = new StringBuffer();
		if (ValidateUtil.isNotBlank(obj.getFid())) {
			criteria.append("FID=? AND ");
			params.add(obj.getFid());
		}

		if (ValidateUtil.isNotBlank(obj.getBankId())) {
			criteria.append("BANK_ID=? AND ");
			params.add(obj.getBankId());
		}

		StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BANK_PARM ");
		if (criteria.length() > 0) {
			cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));
		}
		if (obj.getPagingInfo().isEnablePaging())
			return queryObjectList(cmd.toString(), params, BinBankParmBuilder, obj.getPagingInfo());
		else
			return queryObjectList(cmd.toString(), params, BinBankParmBuilder);
	}

	public List<BinBankParm> queryAll() throws QueryServiceException {
		StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BANK_PARM ");
		return queryObjectList(cmd.toString(), BinBankParmBuilder);
	}

	public BinBankParm getBinBankByFid(String fid) throws QueryServiceException {
		List<String> params = new LinkedList<String>();

		StringBuffer criteria = new StringBuffer();
		criteria.append("FID=? AND ");
		params.add(fid);
		StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BANK_PARM ");

		cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));

		List<BinBankParm> list = super.query(cmd.toString(), params, BinBankParmBuilder);
		return list.size() > 0 ? list.get(0) : null;
	}

	public String getBankNo2(String bin) throws QueryServiceException {
		List<String> params = new LinkedList<String>();
		String sToday = MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD);
		params.add(bin);
		params.add(sToday);
		params.add(sToday);
		StringBuffer cmd = new StringBuffer();
		cmd.append(" SELECT  b.MAIN_ID  FROM SETL_BIN_NEW_BINO_PARM a ,SETL_BIN_BANK_PARM b      ");
		cmd.append(" WHERE     a.FID = b.FID                                                 ");
		cmd.append(" AND       ? between a.bin_no_l and a.bin_no_h                           ");
		cmd.append(" AND      a.IN_DATE <= ?                                                 ");
		cmd.append(" AND     (a.OUT_DATE is null  OR   a.OUT_DATE >= ? )                     ");
		List list = super.queryValueList(cmd.toString(), params, "MAIN_ID");
		if (list != null && list.size() > 0) {
			return (String) list.get(0);
		} else
			return null;
	}

	public boolean isOnUsCard(String bankNo, String bin) throws QueryServiceException {
		boolean bRet = false;
		List<String> params = new LinkedList<String>();
		String sToday = MyDateUtil.getSysDateTime(MyDateUtil.YYYYMMDD);
		params.add(bin);
		params.add(bankNo);
		params.add(sToday);
		params.add(sToday);
		StringBuffer cmd = new StringBuffer();
		cmd.append(" SELECT count(*) as cnt FROM SETL_BIN_NEW_BINO_PARM a ,SETL_BIN_BANK_PARM b  ");
		cmd.append(" WHERE     a.FID = b.FID                                                 ");
		cmd.append(" AND       ? between a.bin_no_l and a.bin_no_h                           ");
		cmd.append(" AND       b.BANK_ID= ?                                                  ");
		cmd.append(" AND      a.IN_DATE <= ?                                                 ");
		cmd.append(" AND     (a.OUT_DATE is null  OR   a.OUT_DATE >= ? )                     ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) >= 1)
				bRet = true;
		}
		return bRet;
	}

	public boolean isMemberBank(String bin) throws QueryServiceException {
		boolean bRet = false;
		List<String> params = new LinkedList<String>();
		String sToday = new DateString(new java.util.Date()).toString();
		params.add(bin);
		params.add(sToday);
		params.add(sToday);
		StringBuffer cmd = new StringBuffer();
		cmd.append(" SELECT count(*) as cnt FROM SETL_BIN_NEW_BINO_PARM a ,SETL_BIN_BANK_PARM b  ");
		cmd.append(" WHERE     a.FID = b.FID  ");
		cmd.append(" AND       ? between a.bin_no_l and a.bin_no_h                           ");
		cmd.append(" AND       b.MAN_ID_NCCC like '50%'    ");
		cmd.append(" AND      a.IN_DATE <= ?               ");
		cmd.append(" AND     (a.OUT_DATE is null  OR   a.OUT_DATE >= ? )  ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) >= 1)
				bRet = true;
		}
		return bRet;
	}

	public boolean isTwBank(String bin) throws QueryServiceException {
		boolean bRet = false;
		List<String> params = new LinkedList<String>();
		String sToday = new DateString(new java.util.Date()).toString();
		params.add(bin);
		params.add(sToday);
		params.add(sToday);
		StringBuffer cmd = new StringBuffer();
		cmd.append(" SELECT count(*) as cnt FROM SETL_BIN_NEW_BINO_PARM a ,SETL_BIN_BANK_PARM b  ");
		cmd.append(" WHERE     a.FID = b.FID  ");
		cmd.append(" AND       ? between a.bin_no_l and a.bin_no_h                           ");
		cmd.append(" AND      a.IN_DATE <= ?               ");
		cmd.append(" AND     (a.OUT_DATE is null  OR   a.OUT_DATE >= ? )  ");
		List list = super.queryValueList(cmd.toString(), params, "cnt");
		if (list != null) {
			if (Integer.parseInt(list.get(0).toString()) >= 1)
				bRet = true;
		}
		return bRet;
	}

	@Override
	public EmvProjPersistable queryById(Object id) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByIds(List ids) throws QueryServiceException {
		throw new NotImplementedException();
	}

	@Override
	public List queryByExample(ProjPersistableArg example) throws QueryServiceException {
		throw new NotImplementedException();
	}
}
