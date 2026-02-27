package proj.nccc.logsearch.struts;

import com.dxc.nccc.aplog.LogMaster;
import com.edstw.web.WebConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import proj.nccc.logsearch.ProjConstants;
import proj.nccc.logsearch.ProjServices;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetail;
import proj.nccc.logsearch.persist.EmvTagRecordMaster;
import proj.nccc.logsearch.proc.EmvTagRecordDetailProc;
import proj.nccc.logsearch.proc.EmvTagRecordMasterProc;
import proj.nccc.logsearch.qs.EmvTagRecordMasterQS;
import proj.nccc.logsearch.vo.EmvTagRecordDetailArg;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
import proj.nccc.logsearch.vo.PagingArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

public class EmvTagRecordActions extends BaseCRUDActions
{
	private final String DEL_NAME = "DELETE";
	private final String FUNC_NAME = "EMV_PARA";
	private static final long serialVersionUID = 1L;
	private List<?> resultTempList = null;
	private EmvTagRecordMasterArg entity;
	private LogMaster master;
	
	protected EmvTagRecordMasterProc getBaseCRUDProc()
	{
		return ProjServices.getEmvTagRecordMasterProc();
	}
	protected EmvTagRecordDetailProc getBaseCRUDProc(String type)
	{
		return ProjServices.getEmvTagRecordDetailProc();
	}
	
	public EmvTagRecordMasterArg getEntity() {
		return entity;
	}

	public void setEntity(EmvTagRecordMasterArg entity) {
		this.entity = entity;
	}

	public EmvTagRecordActions() {
		this.setEntity(new EmvTagRecordMasterArg());
		this.master = super.currentApLogManager().getLogMaster();
		String funcName = master.getFunctionName();
		master.setFunctionName(funcName.concat("-EMV查詢"));
	}
	
	protected ProjPersistableArg createEmptyProjPersistableArg()
	{
		return new EmvTagRecordMasterArg();
	}

	public String toModify()
	{
		//LogMaster master = super.currentApLogManager().getLogMaster();
		try
		{
			EmvTagRecordMasterArg arg = (EmvTagRecordMasterArg) this.getEntity();
			EmvProjPersistable obj = getBaseCRUDProc().queryById(arg.getId());
			arg.buildFromProjPersistable(obj);
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	@Override
	public String create()
	{
		//LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_A);
		try
		{
			String[] byteValue = super.getParameters("byteValue");
			String[] bitValue8 = super.getParameters("bitValue8");
			String[] bitValue7 = super.getParameters("bitValue7");
			String[] bitValue6 = super.getParameters("bitValue6");
			String[] bitValue5 = super.getParameters("bitValue5");
			String[] bitValue4 = super.getParameters("bitValue4");
			String[] bitValue3 = super.getParameters("bitValue3");
			String[] bitValue2 = super.getParameters("bitValue2");
			String[] bitValue1 = super.getParameters("bitValue1");
			String[] cardType = super.getParameters("cardType");
			String[][] bitValue = { bitValue1, bitValue2, bitValue3, bitValue4, bitValue5, bitValue6, bitValue7, bitValue8 };
			EmvTagRecordMasterArg emvTag = (EmvTagRecordMasterArg) this.getEntity();
			boolean isSameValue = true;
			if (emvTag.getSameValueFlag() != null)
			{
				isSameValue = false;
			}
			String allCardType = "";
			for (int i = 0; i < cardType.length; i++)
			{
				allCardType = allCardType + cardType[i] + ",";
			}
	
			List<EmvTagRecordMasterArg> newArg = new ArrayList<EmvTagRecordMasterArg>();
			List<EmvTagRecordDetailArg> detailArg = new ArrayList<EmvTagRecordDetailArg>();
			if (isSameValue)
			{
				EmvTagRecordMasterArg emv = new EmvTagRecordMasterArg();
				emv.setUiLogAction(emvTag.getUiLogAction());
				emv.setUiLogOther(emvTag.getUiLogOther());
				emv.setUiLogFunctionName(emvTag.getUiLogFunctionName());
				emv.setCardType(allCardType.substring(0, allCardType.length() - 1));
				emv.setEmvAbbr(emvTag.getEmvAbbr());
				emv.setEmvTag(emvTag.getEmvTag());
				emv.setEmvDesc(emvTag.getEmvDesc());
				emv.setEmvName(emvTag.getEmvName());
				emv.setEmvLen(emvTag.getEmvLen());
				emv.setSameValueFlag("Y");
				newArg.add(emv);
				for (int a = 0; a < emvTag.getEmvLen(); a++)
				{
					for (int b = 8; b > 0; b--)
					{
						EmvTagRecordDetailArg detailEmv = new EmvTagRecordDetailArg();
						detailEmv.setEmvTag(emvTag.getEmvTag());
						detailEmv.setCardType(allCardType.substring(0, allCardType.length() - 1));
						detailEmv.setPosByte(a + 1);
						detailEmv.setValueByte(byteValue[a]);
						detailEmv.setPosBit(b);
						detailEmv.setValueBit(bitValue[b - 1][a]);
						detailEmv.setSameValueFlag("Y");
						detailArg.add(detailEmv);
					}
				}
			}
			else
			{
				for (int i = 0; i < cardType.length; i++)
				{
					EmvTagRecordMasterArg emv = new EmvTagRecordMasterArg();
					emv.setUiLogAction(emvTag.getUiLogAction());
					emv.setUiLogOther(emvTag.getUiLogOther());
					emv.setUiLogFunctionName(emvTag.getUiLogFunctionName());
					emv.setCardType(cardType[i]);
					emv.setEmvAbbr(emvTag.getEmvAbbr());
					emv.setEmvTag(emvTag.getEmvTag());
					emv.setEmvDesc(emvTag.getEmvDesc());
					emv.setEmvName(emvTag.getEmvName());
					emv.setEmvLen(emvTag.getEmvLen());
					emv.setSameValueFlag(cardType[i]);
					newArg.add(emv);
					for (int a = 0; a < emvTag.getEmvLen(); a++)
					{
						for (int b = 8; b > 0; b--)
						{
							EmvTagRecordDetailArg detailEmv = new EmvTagRecordDetailArg();
							detailEmv.setEmvTag(emvTag.getEmvTag());
							detailEmv.setCardType(cardType[i]);
							detailEmv.setPosByte(a + 1);
							detailEmv.setValueByte(byteValue[a]);
							detailEmv.setPosBit(b);
							detailEmv.setValueBit(bitValue[b - 1][a]);
							detailEmv.setSameValueFlag(cardType[i]);
							detailArg.add(detailEmv);
						}
					}
				}
			}
			getBaseCRUDProc().create(newArg);
			getBaseCRUDProc("detail").create(detailArg);
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	public String queryMod()
	{
		//LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_Q);
		try
		{
			EmvTagRecordMasterArg entity = (EmvTagRecordMasterArg) this.getEntity();
			if (entity instanceof PagingArg)
			{
				super.processPagingInfo((PagingArg) entity);
			}
			this.setResultList(getBaseCRUDProc().query(entity));
			master.setFunctionCount(this.getResultList().size());
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	@Override
	public String modify()
	{
		//LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_U);
		try
		{
			String[] byteValue = super.getParameters("valueByte");
			String[] bitValue8 = super.getParameters("valueBit8");
			String[] bitValue7 = super.getParameters("valueBit7");
			String[] bitValue6 = super.getParameters("valueBit6");
			String[] bitValue5 = super.getParameters("valueBit5");
			String[] bitValue4 = super.getParameters("valueBit4");
			String[] bitValue3 = super.getParameters("valueBit3");
			String[] bitValue2 = super.getParameters("valueBit2");
			String[] bitValue1 = super.getParameters("valueBit1");
			String[] cardType = super.getParameters("cardType");
			String[][] bitValue = { bitValue1, bitValue2, bitValue3, bitValue4, bitValue5, bitValue6, bitValue7, bitValue8 };
			EmvTagRecordMasterArg emvTag = (EmvTagRecordMasterArg) this.getEntity();
			boolean isSameValue = true;
			if (emvTag.getNowSameValueFlag() != null)
			{
				isSameValue = false;
			}
			String allCardType = "";
			for (int i = 0; i < cardType.length; i++)
			{
				allCardType = allCardType + cardType[i] + ",";
				if(emvTag.getOriCardType().indexOf(cardType[i]) == -1){
					EmvTagRecordMasterQS qs = ProjServices.getEmvTagRecordMasterQS();
					EmvTagRecordMaster.EmvTagRecordMasterId uid = new EmvTagRecordMaster().new EmvTagRecordMasterId(emvTag.getEmvTag(), cardType[i]);
					EmvTagRecordMaster emvTagRecord = (EmvTagRecordMaster) qs.queryById(uid);
					if(emvTagRecord == null){
						uid = new EmvTagRecordMaster().new EmvTagRecordMasterId(emvTag.getEmvTag(), "Y");
						emvTagRecord = (EmvTagRecordMaster) qs.queryById(uid);
						if(emvTagRecord != null && emvTagRecord.getCardType().indexOf(cardType[i])>= 0){
							super.saveMessage("action.warn", new Object[] { "已存在卡別["+cardType[i]+"]的TAG["+emvTag.getEmvTag()+"]資料" });
							return WebConstant.FAIL_KEY;
						}
					}else{
						super.saveMessage("action.warn", new Object[] { "已存在卡別["+cardType[i]+"]的TAG["+emvTag.getEmvTag()+"]資料" });
						return WebConstant.FAIL_KEY;
					}
				}
			}
		
			EmvTagRecordMasterArg emv = new EmvTagRecordMasterArg();
			List<EmvTagRecordDetailArg> detailArg = new ArrayList<EmvTagRecordDetailArg>();
			if (isSameValue)
			{
				String[] cardT = emvTag.getOriCardType().split(",");
				boolean re = true;
				for(String s : cardT){
					if(allCardType.indexOf(s) == -1){
						re = false;
						break;
					}
				}
				if( !re ) {
					super.saveMessage("action.warn", new Object[] { "同值關係需包含原本的卡別:"+emvTag.getOriCardType() });
					return WebConstant.FAIL_KEY;
				}
				emv.setUiLogAction(emvTag.getUiLogAction());
				emv.setUiLogOther(emvTag.getUiLogOther());
				emv.setUiLogFunctionName(emvTag.getUiLogFunctionName());
				emv.setCardType(allCardType.substring(0, allCardType.length() - 1));
				emv.setEmvAbbr(emvTag.getEmvAbbr());
				emv.setEmvTag(emvTag.getEmvTag());
				emv.setEmvDesc(emvTag.getEmvDesc());
				emv.setEmvName(emvTag.getEmvName());
				emv.setEmvLen(emvTag.getEmvLen());
				if(emvTag.getSameValueFlag().equals("Y")){
					emv.setSameValueFlag("Y");
				}else{
				emv.setSameValueFlag(emvTag.getOriSameValueFlag());
				}
				emv.setOriCardType(emvTag.getOriCardType());
				emv.setNowSameValueFlag(emvTag.getOriSameValueFlag());
				for (int a = 0; a < emvTag.getEmvLen(); a++)
				{
					for (int b = 8; b > 0; b--)
					{
						EmvTagRecordDetailArg detailEmv = new EmvTagRecordDetailArg();
						detailEmv.setEmvTag(emvTag.getEmvTag());
						detailEmv.setCardType(allCardType.substring(0, allCardType.length() - 1));
						detailEmv.setPosByte(a + 1);
						detailEmv.setValueByte(byteValue[a]);
						detailEmv.setPosBit(b);
						detailEmv.setValueBit(bitValue[b - 1][a]);
						if(emvTag.getSameValueFlag().equals("Y")){
							detailEmv.setSameValueFlag("Y");
						}else{
							detailEmv.setSameValueFlag(emvTag.getOriSameValueFlag());
						}
						detailArg.add(detailEmv);
					}
				}
			}
//			}else{
//				for (int i = 0; i < cardType.length; i++)
//				{
//					emv = new EmvTagRecordMasterArg();
//					emv.setUiLogAction(emvTag.getUiLogAction());
//					emv.setUiLogOther(emvTag.getUiLogOther());
//					emv.setUiLogFunctionName(emvTag.getUiLogFunctionName());
//					emv.setCardType(cardType[i]);
//					emv.setEmvAbbr(emvTag.getEmvAbbr());
//					emv.setEmvTag(emvTag.getEmvTag());
//					emv.setEmvDesc(emvTag.getEmvDesc());
//					emv.setEmvName(emvTag.getEmvName());
//					emv.setEmvLen(emvTag.getEmvLen());
//					emv.setSameValueFlag(cardType[i]);
//					newArg.add(emv);
//					for (int a = 0; a < emvTag.getEmvLen(); a++)
//					{
//						for (int b = 8; b > 0; b--)
//						{
//							EmvTagRecordDetailArg detailEmv = new EmvTagRecordDetailArg();
//							detailEmv.setEmvTag(emvTag.getEmvTag());
//							detailEmv.setCardType(cardType[i]);
//							detailEmv.setPosByte(a + 1);
//							detailEmv.setValueByte(byteValue[a]);
//							detailEmv.setPosBit(b);
//							detailEmv.setValueBit(bitValue[b - 1][a]);
//							detailEmv.setSameValueFlag(cardType[i]);
//							detailArg.add(detailEmv);
//						}
//					}
//				}
//			}
			else
			{
				boolean re = false;
				if(emvTag.getOriCardType().indexOf(cardType[0]) != -1){
					re = true;
				}else{
					re = false;
				}
				
				if( !re ) {
					super.saveMessage("action.warn", new Object[] { "非同值關係需選擇原本的卡別中的其中一個:"+emvTag.getOriCardType() });
					return WebConstant.FAIL_KEY;
				}
				
				emv.setUiLogAction(emvTag.getUiLogAction());
				emv.setUiLogOther(emvTag.getUiLogOther());
				emv.setUiLogFunctionName(emvTag.getUiLogFunctionName());
				emv.setCardType(cardType[0]);
				emv.setEmvAbbr(emvTag.getEmvAbbr());
				emv.setEmvTag(emvTag.getEmvTag());
				emv.setEmvDesc(emvTag.getEmvDesc());
				emv.setEmvName(emvTag.getEmvName());
				emv.setEmvLen(emvTag.getEmvLen());
				emv.setSameValueFlag(cardType[0]);
				emv.setOriCardType(emvTag.getOriCardType());
				emv.setNowSameValueFlag(emvTag.getOriSameValueFlag());
				for (int a = 0; a < emvTag.getEmvLen(); a++)
				{
					for (int b = 8; b > 0; b--)
					{
						EmvTagRecordDetailArg detailEmv = new EmvTagRecordDetailArg();
						detailEmv.setEmvTag(emvTag.getEmvTag());
						detailEmv.setCardType(cardType[0]);
						detailEmv.setPosByte(a + 1);
						detailEmv.setValueByte(byteValue[a]);
						detailEmv.setPosBit(b);
						detailEmv.setValueBit(bitValue[b - 1][a]);
						detailEmv.setSameValueFlag(cardType[0]);
						detailArg.add(detailEmv);
					}
				}
			}
			getBaseCRUDProc().modify(emv);
			getBaseCRUDProc("detail").create(detailArg);
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		}
		catch (Exception e)
		{
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}

	/**
	 * @return the resultTempList
	 */
	public List getResultTempList()
	{
		if (resultTempList == null && this.getEntity() != null)
		{
			List l = new ArrayList();
			EmvTagRecordMaster emvTagRecord = (EmvTagRecordMaster) this.getEntity();
			String emvTag = emvTagRecord.getEmvTag();
			String sameValueFlag = emvTagRecord.getSameValueFlag();
			Integer tempBytePos = 0;
			Integer tempBitPos = 0;
			List emvTagRecordDetail = ProjServices.getEmvTagRecordDetailQS().queryByEmvTagAndSameValueFlag(emvTag, sameValueFlag);
			for (Iterator<EmvTagRecordDetail> itr = emvTagRecordDetail.iterator(); itr.hasNext(); )
			{
				EmvTagRecordDetail detail = itr.next();
				if (tempBytePos != detail.getPosByte() || tempBitPos != detail.getPosBit())
				{
					tempBytePos = detail.getPosByte();
					tempBitPos = detail.getPosBit();
					l.add(detail);
				}
			}
			resultTempList = l;
		}
		return resultTempList;
	}
	
	public String delete() {
		//LogMaster master = super.currentApLogManager().getLogMaster();
		master.setAccessType(ProjConstants.ACCESS_TYPE_D);
		try {
			EmvTagRecordMasterArg entity = (EmvTagRecordMasterArg) this.getEntity();
			entity.setUiLogAction(DEL_NAME);
			entity.setUiLogFunctionName(FUNC_NAME);
			if(entity.getSameValueFlag()!=null && !entity.getSameValueFlag().equals("Y")){
				entity.setUiLogOther("TAG名稱["+entity.getEmvTag()+"],卡別["+entity.getSameValueFlag()+"]");
			}else{
				entity.setUiLogOther("TAG名稱["+entity.getEmvTag()+"]");
			}
			getBaseCRUDProc().deleteEmv(entity);
			super.saveUpdatedMessage();
			return WebConstant.SUCCESS_KEY;
		} catch (Exception e) {
			super.handleException(e);
			master.setSuccessFlag("N");
			return WebConstant.FAIL_KEY;
		}
	}
	
}
