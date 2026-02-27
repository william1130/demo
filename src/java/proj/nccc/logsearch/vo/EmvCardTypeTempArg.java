package proj.nccc.logsearch.vo;

import com.edstw.sql.DisplayTagPagingInfo;
import org.apache.commons.beanutils.BeanUtils;
import proj.nccc.logsearch.persist.EmvCardTypeTemp;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.qs.DisplayTagPagingInfos;

public class EmvCardTypeTempArg extends EmvCardTypeTemp implements ProjPersistableArg, PagingArg, ApproveParaArg
{
	private static final long serialVersionUID = 1L;
	private DisplayTagPagingInfos pagingInfo = new DisplayTagPagingInfos();
	private String uiLogAction = null;
	private String uiLogFunctionName = null;
	private String uiLogOther = null;

	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception
	{
		BeanUtils.copyProperties(this, c);
	}

	public DisplayTagPagingInfos getPagingInfo()
	{
		return pagingInfo;
	}

	/**
	 * @return the uiLogAction
	 */
	public String getUiLogAction()
	{
		return uiLogAction;
	}

	/**
	 * @param uiLogAction the uiLogAction to set
	 */
	public void setUiLogAction(String uiLogAction)
	{
		this.uiLogAction = uiLogAction;
	}

	/**
	 * @return the uiLogFunctionName
	 */
	public String getUiLogFunctionName()
	{
		return uiLogFunctionName;
	}

	/**
	 * @param uiLogFunctionName the uiLogFunctionName to set
	 */
	public void setUiLogFunctionName(String uiLogFunctionName)
	{
		this.uiLogFunctionName = uiLogFunctionName;
	}

	/**
	 * @return the uiLogOther
	 */
	public String getUiLogOther()
	{
		return uiLogOther;
	}

	/**
	 * @param uiLogOther the uiLogOther to set
	 */
	public void setUiLogOther(String uiLogOther)
	{
		this.uiLogOther = uiLogOther;
	}
}
