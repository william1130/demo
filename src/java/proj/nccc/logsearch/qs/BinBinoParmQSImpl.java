
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: BinBinoParmQSImpl.java,v 1.2 2019/10/21 01:11:01 redlee Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proj.nccc.logsearch.persist.BinBinoParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.BinBinoParmArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.lang.DateString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;

/**
 *
 * @author APAC\czrm4t
 * @version $Revision: 1.2 $
 */
public class BinBinoParmQSImpl extends AbstractJdbcPersistableQueryService implements BinBinoParmQS {
	public String getServiceName() {
		return "BinBinoParm Query Service";
	}

	public void setServiceParams(Map arg0) throws ServiceException {
		// do nothing
	}

	public BinBinoParm queryByPrimaryKey() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public BinBinoParm queryByPrimaryKey(String id) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
		/*
		 * List params = new LinkedList(); params.add(id); StringBuffer cmd = new
		 * StringBuffer("SELECT rowid as X , SETL_BIN_BINO_PARM.* FROM SETL_BIN_BINO_PARM WHERE ROWID=? "
		 * ); List<BinBinoParm> list = super.query( cmd.toString(), params,
		 * BinBinoParmBuilder ); return list.size() > 0 ? list.get(0) : null;
		 */
	}

	public List<BinBinoParm> queryByPrimaryKeys(List<String> ids) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<BinBinoParm> queryByExample(BinBinoParmArg obj) throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
		/*
		 * List<String> params = new LinkedList<String>(); StringBuffer criteria = new
		 * StringBuffer(); if( ValidateUtil.isNotBlank( obj.getBinNo() ) ) {
		 * criteria.append("BIN_NO=? AND "); params.add( obj.getBinNo() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getFid() ) ) {
		 * criteria.append("FID=? AND "); params.add( obj.getFid() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getAcqIss() ) ) {
		 * criteria.append("ACQ_ISS=? AND "); params.add( obj.getAcqIss() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getCardType() ) ) {
		 * criteria.append("CARD_TYPE=? AND "); params.add( obj.getCardType() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getCardKind() ) ) {
		 * criteria.append("CARD_KIND=? AND "); params.add( obj.getCardKind() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getInDate() ) ) {
		 * criteria.append("IN_DATE=? AND "); params.add( obj.getInDate() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getOutDate() ) ) {
		 * criteria.append("OUT_DATE=? AND "); params.add( obj.getOutDate() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getIca() ) ) {
		 * criteria.append("ICA=? AND "); params.add( obj.getIca() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getPurFlag() ) ) {
		 * criteria.append("PUR_FLAG=? AND "); params.add( obj.getPurFlag() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getBpFlag() ) ) {
		 * criteria.append("BP_FLAG=? AND "); params.add( obj.getBpFlag() ); }
		 * 
		 * if( ValidateUtil.isNotBlank( obj.getAuthOutDate() ) ) {
		 * criteria.append("AUTH_OUT_DATE=? AND "); params.add( obj.getAuthOutDate() );
		 * }
		 * 
		 * StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BINO_PARM "); if(
		 * criteria.length() > 0 ) { cmd.append("WHERE ").append( criteria.substring( 0,
		 * criteria.length()-4 ) ); } if( obj.getPagingInfo().isEnablePaging() ) return
		 * query( cmd.toString(), params, BinBinoParmBuilder, obj.getPagingInfo() );
		 * else return query( cmd.toString(), params, BinBinoParmBuilder );
		 */
	}

	public List<BinBinoParm> queryAll() throws QueryServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
		/*
		 * StringBuffer cmd = new StringBuffer("SELECT * FROM SETL_BIN_BINO_PARM ");
		 * return super.query( cmd.toString(), BinBinoParmBuilder );
		 */
	}

	public BinBinoParm getBinParm(String bin, Date today) throws QueryServiceException {
		List<String> params = new LinkedList<String>();

		StringBuffer criteria = new StringBuffer();
		String sToday = new DateString(today).toString();

		/*
		 * Jennifer for M2016073
		 * criteria.append("? between bin_no_l and bin_no_h AND ");
		 */
		criteria.append("Rpad(? ,11, '0')  between bin_no_l and bin_no_h AND ");
		params.add(bin);
		/*
		 * Jennifer
		 * 20090921修改bin有效之判斷criteria.append("( IN_DATE<=? OR IN_DATE is null ) AND ");
		 */
		criteria.append("( IN_DATE<=? ) AND ACQ_ISS = 'I' AND ");
		params.add(sToday);
		/*
		 * Jennifer
		 * 20090921修改bin有效之判斷criteria.append("( OUT_DATE>=? OR OUT_DATE is null ) AND "
		 * );
		 */
		criteria.append("( OUT_DATE>? OR OUT_DATE is null ) AND ");
		params.add(sToday);
		StringBuffer cmd = new StringBuffer("SELECT * FROM setl_bin_new_bino_parm ");

		cmd.append("WHERE ").append(criteria.substring(0, criteria.length() - 4));

		List<BinBinoParm> list = super.query(cmd.toString(), params, BinBinoParmBuilder);
		return list.size() > 0 ? list.get(0) : null;
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
