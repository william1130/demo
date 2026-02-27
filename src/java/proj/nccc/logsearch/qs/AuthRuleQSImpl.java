package proj.nccc.logsearch.qs;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.edstw.lang.DateString;
import com.edstw.persist.jdbc.AbstractJdbcPersistableQueryService;
import com.edstw.service.QueryServiceException;
import com.edstw.service.ServiceException;
import com.edstw.util.NotImplementedException;

import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.beans.CardNumLogicCheckBean;
import proj.nccc.logsearch.persist.BinBankParm;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.SetlBinDatas;
import proj.nccc.logsearch.vo.MerchantVO;
import proj.nccc.logsearch.vo.ProjPersistableArg;

/**
 *
 * @author Stephen Lin $
 * @version $Revision: 1.3 $
 */
public class AuthRuleQSImpl extends AbstractJdbcPersistableQueryService implements AuthRuleQS {
	private static final Logger log = LogManager.getLogger(AuthRuleQSImpl.class);

	/** Creates a new instance of AuthRuleQSImpl */
	public AuthRuleQSImpl() {
	}

	public String getServiceName() {
		return "AuthRuleQSImpl Query Service";
	}

	public void setServiceParams(Map map) throws ServiceException {
		// do nothing
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

	@Override
	public List queryAll() throws QueryServiceException {
		throw new NotImplementedException();
	}

	public boolean isCorrectCardNumber(String cardNoArg) throws AuthRuleException {
		if (cardNoArg.trim().length() == 0)
			throw (AuthRuleException) new WrongCardNumberException(cardNoArg);
		CardNumLogicCheckBean ck = new CardNumLogicCheckBean();
		SetlBinDatas sbdObj = ProjServices.getSetlBinDatasQS().queryByCardType(cardNoArg.trim(),
				new DateString().toString());
		if (sbdObj != null) {
			if (sbdObj != null && ck.CreditCard(sbdObj.getCardTypeA(), cardNoArg.trim()))
				return true;
			else
				throw (AuthRuleException) new WrongCardNumberException(cardNoArg);
		} else {

			log.info("1.sbdObj is null");
			String cardType = CardNumLogicCheckBean.simpleCheckCardType(cardNoArg);

			if (cardType == null)
				throw (AuthRuleException) new WrongCardNumberException(cardNoArg);

			log.info("2.cardType = " + cardType);

			if (ck.CreditCard(cardType, cardNoArg.trim()))
				return true;
			else
				throw (AuthRuleException) new WrongCardNumberException(cardNoArg);
		}
	}

//    
//    public boolean validManualAuthRule(AuthPersistableArg arg,boolean isCheckCVC2) throws AuthRuleException 
//    {
//        
//        ManualAuthArg argObj = (ManualAuthArg) arg;
//        String binNo=argObj.getCardNo().substring(0,6);
//        String abbrName="";
//        //1.
//        this.toValidCardMaster(argObj.getBankNo(),argObj.getCardNo(),argObj.getExpireDate());
//        //2.
//        log.info("isCheckCVC2.."+isCheckCVC2);
//        if (isCheckCVC2)
//        {
//    		HSM7000 hsm = null;
//    		try
//			{
//				KeyData keyData = ProjServices.getKeyDataQS().queryByPrimaryKey(argObj.getCardNo().substring(0,6));
//				if (keyData!=null)
//				{
//		    		String hsmIp = SysDefaultBean.getInstance().getHSMIp();
//		    		int hsmPort = new Integer(SysDefaultBean.getInstance().getHSMPort()).intValue();
//		    		String hsmCharSet = SysDefaultBean.getInstance().getHSMCharSet();
//		    		log.info("******hsmcharset=["+hsmCharSet+"]******");
//	    			hsm = new HSM7000(hsmIp, hsmPort, hsmCharSet);
//					String serviceCode = null;
//					String cvc2 = null;
//					log.info("******CVC2 keyExist!!!!!******");
//					if ( argObj.getCardType().equals("U"))
//						serviceCode = "";
//					else
//						serviceCode = "000";
//					log.info("******CVC2 serviceCode=["+serviceCode+"]******");
//					cvc2 = hsm.getCvc2(argObj.getCardNo(), argObj.getExpireDate(), keyData, serviceCode);
//					log.info("******CVC2 cvc2="+cvc2+"*******");
//					if ( ValidateUtil.isNotEqual(argObj.getCvc2(), cvc2) )
//						throw (AuthRuleException) new Cvc2VerificationException(argObj.getCardNo());
//				}
//				else
//					throw (AuthRuleException) new KeyDateNotFoundException(argObj.getCardNo());
//			}
//			catch ( Cvc2VerificationException e )
//			{
//				throw new Cvc2VerificationException(argObj.getCardNo());
//			}
//			catch ( KeyDateNotFoundException e )
//			{
//				throw new KeyDateNotFoundException(argObj.getCardNo());
//			}
//			finally
//			{
//				log.info("close hsm socket");
//				if (hsm!=null)
//					hsm.close();
//			}        	
//        }
//        //3.
//        // log.info("argObj.getAcquirerType()..["+argObj.getAcquirerType()+"]");
//        if(argObj.getAcquirerType()!=null && argObj.getAcquirerType().equals("D"))
//        {
//	        // if ((argObj.getMerchantNo().trim().length() == 4 || argObj.getMerchantNo().trim().length() == 6) &&
//	        //    (Integer.parseInt(argObj.getMerchantNo()) > 1 && Integer.parseInt(argObj.getMerchantNo()) < 999999))
//        	abbrName=this.toValidSetlBinDataByBinNo(argObj.getTransType(),argObj.getMerchantNo());
//        	argObj.setAcqName(abbrName);
//        	
//	        //4.
//	        if (argObj.getMerchantNo().trim().length() == 10)
//	            this.toValidMerchantData(argObj.getMerchantNo(),argObj.getTransType(),argObj.getCardType());
//        }
//        else
//        {
//        	// U Card 不得國外消費
//        	if(argObj.getCardType().equals("U"))
//        		throw (AuthRuleException) new UCardNotForFException("");
//        	
//        }
//        // 檢查卡bin
//        SetlBinDatas sbdObj = ProjServices.getSetlBinDatasQS().queryByAcqIssByBin(argObj.getCardNo(),new DateString().toString());
//        if(sbdObj!=null)
//        {
//	        if ((argObj.getTransType().equals("AC") || argObj.getTransType().equals("EC")))
//	        {
//	        	if(!sbdObj.getEmbKindAtmb().equals("Y"))
//	                throw (AuthRuleException) new CardNotAuthPayCashException("");
//	        	/* 20080919 , disable by Sam.chen no necessary so far
//	        	if(!(sbdObj.getBussType()!=null && (
//	        			sbdObj.getBussType().equals("1") ||  sbdObj.getBussType().equals("0"))))
//	        		throw (AuthRuleException) new NoneSupportBussTypeException(binNo);
//        		*/
//	        }
//        }
//        else
//        {
//        	 throw (AuthRuleException) new RecordNotFoundException("SetlBinDatas");
//        }
//        log.info("===============> validManualAuthRule .. PASS");
//        return true;
//    }
//    
//    public boolean validCreditAuthRule(AuthPersistableArg arg) throws AuthRuleException 
//    {
//        CardMasterArg argObj = (CardMasterArg) arg;
//        String cardNo =argObj.getCardNo();
//        boolean bolCheck =false;
//        if (cardNo != null) 
//        {
//            this.toValidCardMaster(argObj.getBankNo(),argObj.getCardNo(),argObj.getNewExpireDate());
//            bolCheck = true;
//        }
//        return bolCheck;
//    }
//
//    
//    /** 卡片資料檢核 */
//    public void toValidCardMaster(String bankNo,String cardNoArg,String expireDateArg) throws AuthRuleException 
//    {
//        
//        CardMaster cmObj = ProjServices.getCardMasterQS().queryByPrimaryKey(cardNoArg);
//        if (cmObj == null)
//            throw (AuthRuleException) new RecordNotFoundException("卡片主檔");
//        if (!cmObj.getBankNo().equals(bankNo))
//            throw (AuthRuleException) new RecordNotFoundException("非貴行卡號!");
//        if (cmObj.getCurrentCode() != null) 
//        {
//            if (cmObj.getCurrentCode().equals("2") || cmObj.getCurrentCode().equals("3"))
//                throw (AuthRuleException) new CardSuspendException(cardNoArg);
//            if(cmObj.getCurrentCode().equals("4"))
//            	throw (AuthRuleException) new CardAbandonException(cardNoArg);
//            
//            /** 卡片為附卡時,須追加判斷正卡資料是否有效 20081018*/
//            if ( cmObj.getMajorSubCode().equals("1") )
//            {
//            	CardMaster majorCard = ProjServices.getCardMasterQS().queryByPrimaryKey( cmObj.getMajorCardNo() );
//            	if ( majorCard == null )
//            		throw (AuthRuleException) new RecordNotFoundException("正卡不存在");
//                if (majorCard.getCurrentCode().equals("2") || majorCard.getCurrentCode().equals("3"))
//                    throw (AuthRuleException) new MajorCardSuspendException(cardNoArg);
//                if(majorCard.getCurrentCode().equals("4"))
//                	throw (AuthRuleException) new MajorCardAbandonException(cardNoArg);
//            }
//            
//            if (expireDateArg != null) 
//            {
//            	String yyyymm=new DateString().toString().substring(0, 6);
//            	String eyyyymm="";
//            	int iExpire=Integer.parseInt(expireDateArg);
//            	if(expireDateArg.substring(0, 1).equals("9"))
//            		eyyyymm="19"+expireDateArg;
//            	else
//            		eyyyymm="20"+expireDateArg;
//            	
////                if ( Integer.parseInt(eyyyymm) < Integer.parseInt(yyyymm))
////                    throw (AuthRuleException) new MajorCardAbandonException(cardNoArg);
//                
//                if (Integer.parseInt(eyyyymm) < Integer.parseInt(yyyymm) || 
//                   (Integer.parseInt(expireDateArg) != Integer.parseInt(cmObj.getOldExpireDate()) &&
//                	Integer.parseInt(expireDateArg) != Integer.parseInt(cmObj.getNewExpireDate()) ) )
//                    throw (AuthRuleException) new CardExpireDateErrorException(cardNoArg,expireDateArg);
//
//                String activeStatus="";
//                if(expireDateArg.equals(cmObj.getNewExpireDate()))
//                	activeStatus=cmObj.getNewActivateStatus();
//                else if(expireDateArg.equals(cmObj.getOldExpireDate()))
//                	activeStatus=cmObj.getOldActivateStatus();
//                else
//                    throw (AuthRuleException) new CardExpireDateErrorException(cardNoArg,expireDateArg);
//                if (activeStatus.equals("1"))
//                    throw (AuthRuleException) new CardNotOpenException(cardNoArg);
//            }
////            else
////            {
////            	throw (AuthRuleException) new CardNotOpenException(cardNoArg);
////            }
//        }
//    }
//    
//    public boolean toValidActiveCard(String bankNo,String cardNoArg,String newOldFlag) throws AuthRuleException
//    {
//        CardMaster cmObj = ProjServices.getCardMasterQS().queryByPrimaryKey(cardNoArg);
//        if (cmObj == null)
//            throw (AuthRuleException) new RecordNotFoundException("卡片主檔");
//        if (!cmObj.getBankNo().equals(bankNo))
//            throw (AuthRuleException) new RecordNotFoundException("非貴行卡號!");
//        if (cmObj.getCurrentCode() != null) 
//        {
//            //
//            if (cmObj.getCurrentCode().equals("2") || cmObj.getCurrentCode().equals("3"))
//                throw (AuthRuleException) new CardSuspendException(cardNoArg);
//            if(cmObj.getCurrentCode().equals("4"))
//            	throw (AuthRuleException) new CardAbandonException(cardNoArg);
//            
//            if ( cmObj.getMajorSubCode().equals("1") )
//            {
//            	CardMaster majorCard = ProjServices.getCardMasterQS().queryByPrimaryKey( cmObj.getMajorCardNo() );
//            	if ( majorCard == null )
//            		throw (AuthRuleException) new RecordNotFoundException("正卡不存在");
//                if (majorCard.getCurrentCode().equals("2") || majorCard.getCurrentCode().equals("3"))
//                    throw (AuthRuleException) new MajorCardSuspendException(cardNoArg);
//                if(majorCard.getCurrentCode().equals("4"))
//                	throw (AuthRuleException) new MajorCardAbandonException(cardNoArg);
//            }
//            
//           	String yyyymm=new DateString().toString().substring(0, 6);
//        	String eyyyymm="";
//        	String expireDate=null;
//            if(newOldFlag.equals("Y"))
//            	expireDate=cmObj.getNewExpireDate();
//            else 
//            	expireDate=cmObj.getOldExpireDate();
//
//        	if(expireDate.substring(0, 1).equals("9"))
//        		eyyyymm="19"+expireDate;
//        	else
//        		eyyyymm="20"+expireDate;
//            if ( Integer.parseInt(eyyyymm) < Integer.parseInt(yyyymm))
//                throw (AuthRuleException) new CardExpiredException(cardNoArg);
//            
//            String activeStatus="";
//            if(newOldFlag.equals("Y"))
//            	activeStatus=cmObj.getNewActivateStatus();
//            else 
//              	activeStatus=cmObj.getOldActivateStatus();
//            if (activeStatus.equals("1"))
//                throw (AuthRuleException) new CardNotOpenException(cardNoArg);
//         }
//         else
//         {
//         	throw (AuthRuleException) new CardNotOpenException(cardNoArg);
//         }    	
//    	
//    	return true;
//    }
//    
//    public boolean toValidCVC2Number(String cardNoArg,String expireDateArg,String serviceCodeArg,String cvkAArg,String cvkBArg,String cvc2Arg) throws AuthRuleException 
//    {
//        // TODO UnImplement CVC2 = 以卡號,有校年月,SERVICE CODE=000,CVKA,CVKB,
//        String retCvc2Arg="";
//        
//        if (!(cvc2Arg.equals(retCvc2Arg) ||cvc2Arg.equals("000")))
//            throw (AuthRuleException) new CardCVC2NotMatchException(cardNoArg);
//        return true;
//    }
//    
//    public String toValidSetlBinDataByBinNo(String transTypeArg ,String mchtNo) throws AuthRuleException 
//    {
//        if (mchtNo.trim().length() <= 2 || mchtNo.trim().length() == 5 ||
//        	mchtNo.trim().length() == 8 || mchtNo.trim().length() == 9 ||
//        	mchtNo.trim().equals("0000") || mchtNo.trim().equals("000000") ||
//        	mchtNo.trim().equals("0000000"))
//            throw (AuthRuleException) new MerchantAcquireCodeErrorException("");
//        
//        return this.getAcqAbbrName( mchtNo );
//    }
//    
//    public String getAcqAbbrName(String mchtNo) throws AuthRuleException
//    {
//        if (mchtNo.trim().length() == 7)
//        {
//        	BinBankParm bbp=ProjServices.getSetlBinDatasQS().getAcqBkALLBy7Mcht(mchtNo, new DateString().toString());
//            if(bbp==null )
//            	throw (AuthRuleException) new RecordNotFoundException("無法取得7碼的SetlBin");
//            else
//            {
//            	return bbp.getAbbrName();
//            }
//        }
//        
//        if (mchtNo.trim().length() == 6 )
//        {
//        	SetlBinDatas ss=ProjServices.getSetlBinDatasQS().queryByAcqIssByMchtNo6(mchtNo,new DateString().toString());
//        	if(ss==null)
//        		throw (AuthRuleException) new RecordNotFoundException("特店6碼的Acq SetlBin");
//        	else
//        		return ss.getAbbrNameC();
//        }
//        
//        if (mchtNo.trim().length() == 4 )
//        {        	
//        	SetlBinDatas ss=ProjServices.getSetlBinDatasQS().queryByAcqIssByMchtNo4("00"+mchtNo,new DateString().toString());
//        	if(ss==null)
//        		throw (AuthRuleException) new RecordNotFoundException("特店4碼的Acq SetlBin");
//        	else
//        		return ss.getAbbrNameC();
//        }
//        return "";
//    }
//    
//    //20090113 補登不查核bin是否有效
	public String getAcqAbbrNamePF8(String mchtNo) throws AuthRuleException {
		if (mchtNo.trim().length() == 7) {
			BinBankParm bbp = ProjServices.getSetlBinDatasQS().getAcqBkALLBy7Mcht(mchtNo, new DateString().toString());
			if (bbp != null)
				return bbp.getAbbrName();
		}

		if (mchtNo.trim().length() == 6) {
			SetlBinDatas ss = ProjServices.getSetlBinDatasQS().queryByAcqIssByMchtNo6(mchtNo,
					new DateString().toString());
			if (ss != null)
				return ss.getAbbrNameC();
		}

		if (mchtNo.trim().length() == 4) {
			SetlBinDatas ss = ProjServices.getSetlBinDatasQS().queryByAcqIssByMchtNo4("00" + mchtNo,
					new DateString().toString());
			if (ss != null)
				return ss.getAbbrNameC();
		}
		return "";
	}

//    
//    public void toValidMerchantData(String merchantNoArg,String tranType,String cardType) throws AuthRuleException 
//    {
//    	MerchantVO mvoObj= null;
//    	if (merchantNoArg.length()==16){
//        	mvoObj= ProjServices.getMerchantQS().queryQrMerchantBase(merchantNoArg);
//        }
//        else{
//    	    mvoObj= ProjServices.getMerchantQS().queryMerchantBase(merchantNoArg);
//        }
//        
//        if (mvoObj == null)
//            throw (AuthRuleException) new RecordNotFoundException(" 特店代號﹔"+merchantNoArg+"無此特店!!");
//        /** 分期only需求先暫緩 - 2008/12/03 */
////        if (ValidateUtil.isEqual( mvoObj.getInstallmentOnly() , "Y" ))
////        	throw (AuthRuleException) new MerchantDataInstallmentOnlyException(merchantNoArg);
//        if(mvoObj.getCurrentCode()==null || mvoObj.getCurrentCode().equals("3"))
//        	throw (AuthRuleException) new MerchantDataStoreDeContractException(merchantNoArg);
//        if((merchantNoArg.substring(2, 5).equals("999") && ! tranType.equals("AC")))
//        	throw (AuthRuleException) new MerchantTransactionTypeErrorException(merchantNoArg);
//        if(tranType.equals("AC") || tranType.equals("EC"))
//        {
//        	if(! ProjServices.getMerchantQS().isAcEcMcht(merchantNoArg))
//        		throw (AuthRuleException) new MerchantNotAuthPayCashException(merchantNoArg);
//        }
//    	if(! ProjServices.getMerchantQS().isSuitableCardForMcht(merchantNoArg, cardType))
//    		throw (AuthRuleException) new MerchantCardConsumeErrorException(merchantNoArg);
//        
//        	
//    }
//    
	public void toValidMerchantDataPF8(String merchantNoArg, String tranType, String cardType)
			throws AuthRuleException {
		MerchantVO mvoObj = ProjServices.getMerchantQS().queryMerchantBase(merchantNoArg);
		if (mvoObj == null)
			throw (AuthRuleException) new RecordNotFoundException(" 特店代號﹔" + merchantNoArg + "無此特店!!");
		/** 分期only需求先暫緩 - 2008/12/03 */
//        if (ValidateUtil.isEqual( mvoObj.getInstallmentOnly() , "Y" ))
//        	throw (AuthRuleException) new MerchantDataInstallmentOnlyException(merchantNoArg);
		if (mvoObj.getCurrentCode() == null || mvoObj.getCurrentCode().equals("3"))
			throw (AuthRuleException) new MerchantDataStoreDeContractException(merchantNoArg);
//        if((merchantNoArg.substring(2, 5).equals("999") && ! tranType.equals("AC")))
//        	throw (AuthRuleException) new MerchantTransactionTypeErrorException(merchantNoArg);
		if (tranType.equals("AC") || tranType.equals("EC")) {
			if (!ProjServices.getMerchantQS().isAcEcMcht(merchantNoArg))
				throw (AuthRuleException) new MerchantNotAuthPayCashException(merchantNoArg);
		}
		if (!ProjServices.getMerchantQS().isSuitableCardForMcht(merchantNoArg, cardType))
			throw (AuthRuleException) new MerchantCardConsumeErrorException(merchantNoArg);

	}

//    
	/** AUTH_RULE 的應用檢核例外訊息 */
	public abstract class AuthRuleException extends Exception {
	}

	// 代表 AUTH 共用例外訊息
	public class RecordNotFoundException extends AuthRuleException {
		/** 檔案資料不存在例外訊息 */
		private String tableNameArg;

		public RecordNotFoundException(String tableName) {
			tableNameArg = tableName;
		}

		public RecordNotFoundException(String arg[]) {
		}

		public String getMessage() {
			return String.format("%s_資料不存在＊請重來！", tableNameArg);
		}
	}

	// 代表 AUTH_CARD_MASTER Rule 例外訊息
	public class WrongCardNumberException extends AuthRuleException {
		// 卡號檢核錯誤
		private String cardNoArg;

		public WrongCardNumberException(String cardNo) {
			cardNoArg = cardNo;
		}

		public String getMessage() {
			return String.format("卡號： %s 檢核錯誤！", cardNoArg);
		}
	}

//    public class MajorCardSuspendException extends AuthRuleException 
//    {
//        // 正卡已被停掛例外訊息*/
//        private String cardNoArg;
//        public MajorCardSuspendException(String cardNo) 
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("系統錯誤：卡號：%s該卡之正卡已被停掛：請注意！",cardNoArg);
//        }
//    }
//    public class MajorCardAbandonException extends AuthRuleException 
//    {
//        // 正卡已無效例外訊息*/
//        private String cardNoArg;
//        public MajorCardAbandonException(String cardNo) 
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("系統警告：卡號：%s該卡之正卡已無效：請注意！",cardNoArg);
//        }
//    }
//    public class CardExpiredException extends AuthRuleException
//    {
//    	// 卡號有效期已過訊息 */
//    	private String cardNoArg;
//    	public CardExpiredException(String cardNo)
//    	{
//    		cardNoArg = cardNo;
//    	}
//        public String getMessage() 
//        {
//            return String.format("系統警告：卡號：%s該卡之卡號有效期已過：請注意！",cardNoArg);
//        }
//    }
//    public class CardSuspendException extends AuthRuleException 
//    {
//        // 正卡已被停掛例外訊息*/
//        private String cardNoArg;
//        public CardSuspendException(String cardNo) 
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("系統錯誤：卡號：%s該卡已被停掛：請注意！",cardNoArg);
//        }
//    }
//    public class CardAbandonException extends AuthRuleException 
//    {
//        // 正卡已無效例外訊息*/
//        private String cardNoArg;
//        public CardAbandonException(String cardNo) 
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("系統警告：卡號：%s該卡已無效：請注意！",cardNoArg);
//        }
//    }
//    public class CardExpireDateErrorException extends AuthRuleException 
//    {
//        // 查核有效日期錯誤例外訊息*/
//        private String cardNoArg;
//        private String expireDateArg;
//        public CardExpireDateErrorException(String cardNo,String expireDate) 
//        {
//            cardNoArg = cardNo;
//            expireDateArg=expireDate;
//        }
//        public String getMessage() 
//        {
//            return String.format("系統警告：卡號：%s查核有效日期錯誤！",cardNoArg);
//        }
//    }
//    public class CardNotOpenException extends AuthRuleException 
//    {
//        // 信用卡未開卡例外訊息*/
//        private String cardNoArg;
//        public CardNotOpenException(String cardNo)
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("開卡作業，卡號：%s請執行信用卡開卡作業！",  cardNoArg);
//        }
//    }
//    public class CardCVC2NotMatchException extends AuthRuleException 
//    {
//        // 查核CVC2錯誤例外訊息*/
//        private String cardNoArg;
//        public CardCVC2NotMatchException(String cardNo)
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("卡號：%s , CVC2錯誤！",  cardNoArg);
//        }
//    }
//    public class KeyDateNotFoundException extends AuthRuleException 
//    {
//        // 查無Key Data*/
//        private String cardNoArg;
//        public KeyDateNotFoundException(String cardNo)
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format(" 卡號: %s 查無Key Data ！",  cardNoArg);
//        }
//    }
//    public class Cvc2VerificationException extends AuthRuleException 
//    {
//        // 查無Key Data*/
//        private String cardNoArg;
//        public Cvc2VerificationException(String cardNo)
//        {
//            cardNoArg = cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format(" 卡號: %s CVC2 檢核錯誤 ！",  cardNoArg);
//        }
//    }
//    //  代表 AUTH_KEY_DATA  Rule 例外訊息
//    //  代表 AUTH_ID_DATA  Rule 例外訊息
//    //  代表 AUTH_CARD_DATA  Rule 例外訊息
//    //  代表 AUTH_CORP_DATA  Rule 例外訊息
//    //  代表 SETL_BIN_NEW_BINO_PARM  Rule 例外訊息
//    public class NoneSupportBussTypeException extends AuthRuleException
//    {
//        private String merchantNoArg;
//        public NoneSupportBussTypeException(String MerchantNo)
//        {
//            merchantNoArg=MerchantNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s 清算代碼無收單業務 ！",merchantNoArg);
//        }
//    }
//    public class MerchantAcquireCodeErrorException extends AuthRuleException
//    {
//        // 查核特店代號／區域清算代碼錯誤
//        private String merchantNoArg;
//        public MerchantAcquireCodeErrorException(String MerchantNo)
//        {
//            merchantNoArg=MerchantNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s查核特店代號／區域清算代碼錯誤！",merchantNoArg);
//        }
//    }
//    public class MerchantAcquireCodeCloseException extends AuthRuleException
//    {
//        // 查核特店代號／區域清算代碼已終止
//        private String merchantNoArg;
//        public MerchantAcquireCodeCloseException(String MerchantNo)
//        {
//            merchantNoArg=MerchantNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s查核特店代號／區域清算代碼已終止！",merchantNoArg);
//        }
//    }
//    //  代表 SETL_BIN_BUSINESS_PARM  Rule 例外訊息
//    public class CardNotAuthPayCashException extends AuthRuleException
//    {
//        // 該卡不能預借現金
//        private String cardNoArg;
//        public CardNotAuthPayCashException(String cardNo)
//        {
//            cardNoArg=cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format( "%s 該卡不能預借現金！",cardNoArg);
//        }
//    }
//    public class UCardNotForFException extends AuthRuleException
//    {
//        // U卡不得國外消費
//        private String cardNoArg;
//        public UCardNotForFException(String cardNo)
//        {
//            cardNoArg=cardNo;
//        }
//        public String getMessage() 
//        {
//            return String.format( "U 卡不得國外消費 ！");
//        }
//    }
//    //  代表 SETL_BIN_BANK_PARM  Rule 例外訊息
//    //  代表 MERCHANT_DATA  Rule 例外訊息
	public class MerchantDataStoreDeContractException extends AuthRuleException {
		// 查核特店代號／區域清算代碼已終止
		private String mchtNoArg;

		public MerchantDataStoreDeContractException(String mchtNo) {
			mchtNoArg = mchtNo;
		}

		public String getMessage() {
			return String.format("%s特店已解約！", mchtNoArg);
		}
	}

//    public class MerchantDataInstallmentOnlyException extends AuthRuleException
//    {
//        // 查核特店代號／區域清算代碼已終止
//        private String mchtNoArg;
//        public MerchantDataInstallmentOnlyException(String mchtNo)
//        {
//        	mchtNoArg=mchtNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s為分期Only特店,不可執行此項作業！",mchtNoArg);
//        }
//    }
//    public class MerchantDataAmountOverException extends AuthRuleException
//    {
//        // 該行業別單筆交易金額不得超過十萬元
//        private String noarg;
//        public MerchantDataAmountOverException(String no)
//        {
//        	noarg=no;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s該行業別單筆交易金額不得超過十萬元！",noarg);
//        }
//    }
	// 代表 MERCHANT_BUSINESS_DATA Rule 例外訊息
	public class MerchantNotAuthPayCashException extends AuthRuleException {
		// 特店不能預借現金
		private String merchantNoArg;

		public MerchantNotAuthPayCashException(String merchantNo) {
			merchantNoArg = merchantNo;
		}

		public String getMessage() {
			return String.format("%s該特店不能預借現金！", merchantNoArg);
		}
	}

//    public class MerchantTransactionTypeErrorException extends AuthRuleException
//    {
//        // 預借現金特店交易類別應為AC
//        private String merchantNoArg;
//        public MerchantTransactionTypeErrorException(String merchantNo)
//        {
//            merchantNoArg=merchantNo;
//        }
//        public String getMessage() 
//        {
//            return String.format("%s預借現金特店交易類別應為AC！",merchantNoArg);
//        }
//    }
	// 代表 MERCHANT_CARD_DATA Rule 例外訊息
	public class MerchantCardConsumeErrorException extends AuthRuleException {
		// 該卡別不可在此特店消費;特店未簽該卡別
		private String merchantNoArg;

		public MerchantCardConsumeErrorException(String merchantNo) {
			merchantNoArg = merchantNo;
		}

		public String getMessage() {
			return String.format("%s該卡別不可在此特店消費;特店未簽該卡別！", merchantNoArg);
		}
	}
}
