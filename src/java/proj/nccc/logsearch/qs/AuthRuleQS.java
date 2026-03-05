/*
 * AuthRuleQS.java
 *
 * Created on 2008年5月21日, 下午 3:45
 * ==============================================================================================
 * $Id: AuthRuleQS.java,v 1.1 2017/04/24 01:31:14 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.qs;

import proj.nccc.logsearch.qs.AuthRuleQSImpl.AuthRuleException;

/**
 *
 * @author Stephen Lin $
 * @version $Revision: 1.1 $
 */
public interface AuthRuleQS extends BaseCRUDQS {
	// 卡號邏輯驗算
	boolean isCorrectCardNumber(String cardNo) throws AuthRuleException;

//
//    // 人工授權檢核邏輯
//    boolean validManualAuthRule(ProjPersistableArg arg,boolean isCheckCVC2) throws AuthRuleException;
//    // 信用卡開卡檢核邏輯
//    boolean validCreditAuthRule(ProjPersistableArg arg) throws AuthRuleException;
//    
//    public void toValidMerchantData(String merchantNoArg,String tranType,String cardType) throws AuthRuleException;
//    
	public void toValidMerchantDataPF8(String merchantNoArg, String tranType, String cardType) throws AuthRuleException;

//    
//    public String getAcqAbbrName(String mchtNo) throws AuthRuleException;
//    
	public String getAcqAbbrNamePF8(String mchtNo) throws AuthRuleException;
}
