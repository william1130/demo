
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * ==============================================================================================
 * $Id: SetlBinDatasQS.java,v 1.1 2017/04/24 01:31:13 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import com.edstw.service.QueryServiceException;

import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.CupBinData;
import proj.nccc.logsearch.persist.SetlBinDatas;

/**
 *
 * @author APAC\czrm4t $
 * @version $Revision: 1.1 $
 */
public interface SetlBinDatasQS extends BaseCRUDQS {
	SetlBinDatas queryByCardType(String binNo, String yyyymmdd) throws QueryServiceException;

	SetlBinDatas queryByAcqIssByBin(String binNo, String yyyymmdd) throws QueryServiceException;

	SetlBinDatas queryByAcqIssByMchtNo6(String mchtNo6, String yyyymmdd) throws QueryServiceException;

	SetlBinDatas queryByAcqIssByMchtNo4(String mchtNo4, String yyyymmdd) throws QueryServiceException;

	SetlBinDatas queryByOutDateFlg(String binNo, String yyyymmdd) throws QueryServiceException;

	String getAcqBkBy7Mcht(String mchtNo, String today) throws QueryServiceException;

	BinBankParm getAcqBkALLBy7Mcht(String mchtNo, String today) throws QueryServiceException;

	CupBinData getCupBin(String bin) throws QueryServiceException;

	boolean isCupCard(String cardNo);

	boolean isCupDualBrand(String cardNo);

	BinBankParm getBinBankParmByBinNo(String bin) throws QueryServiceException;
}
