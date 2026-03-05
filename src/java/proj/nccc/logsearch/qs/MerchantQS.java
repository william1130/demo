/*
 * MerchantQS.java
 *
 * Created on 2008年1月31日, 上午 11:03
 * ==============================================================================================
 * $Id: MerchantQS.java,v 1.2 2019/10/08 11:22:02 \jjih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.qs;

import proj.nccc.logsearch.vo.MerchantVO;

import com.edstw.service.QueryServiceException;

/**
 *
 * @author Stephen Lin
 * @version $Revision: 1.2 $
 */
public interface MerchantQS extends BaseCRUDQS {
	MerchantVO queryMerchantBase(String mchtNo, String cardType) throws QueryServiceException;

	MerchantVO queryMerchantBase(String mchtNo) throws QueryServiceException;

	MerchantVO queryQrMerchantBase(String mchtNo) throws QueryServiceException;/* M2018066-QR主掃 */

	MerchantVO queryMerchantInfo(String mchtNo) throws QueryServiceException;

	String queryMerchantName(String mchtNo) throws QueryServiceException;

	MerchantVO queryMerchantCardDataBase(String mchtNo, String cardType, String SystemYYYYmmdd)
			throws QueryServiceException;

	boolean isMerchantBusinessDataExitsLoyaltyBy(String mchtNo, String businessType, String systemDate)
			throws QueryServiceException;

	boolean isMerchantBusinessDataExitsInstallmentBy(String mchtNo, String businessType, String systemDate)
			throws QueryServiceException;

	boolean isFace2FaceTx(String mchtNo) throws QueryServiceException;

	boolean isSuitableCardForMcht(String mchtNo, String cardType) throws QueryServiceException;

	boolean isAcEcMcht(String mchtNo) throws QueryServiceException;

	boolean isAuthByCentralize(String mchtNo) throws QueryServiceException;

	boolean isRecurring(String mchtNo) throws QueryServiceException;

	boolean isCupMcht(String mchtNo);

	boolean isDfsMcht(String mchtNo);

	String merchantEngName(String mchtNo) throws QueryServiceException;
}
