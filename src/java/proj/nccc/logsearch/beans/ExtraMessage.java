/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2009/1/8, 下午 02:19:25, By 許欽程(Shiu Vincent, sz12tk)
 * 
 * ==============================================================================================
 * $Id: ExtraMessage.java,v 1.1 2010/06/14 09:42:45 xz04wy Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.util.LinkedList;
import java.util.List;

import com.edstw.util.ValidateUtil;

/**
 * 用於將額外訊息(可以含format)顯示至Web畫面的物件.
 * @author 許欽程(Shiu Vincent, sz12tk)
 * @version $Revision: 1.1 $
 */
public class ExtraMessage 
{
	/** messageType常數, 其值為0, 代表一般訊息 */
	public static final int TYPE_MESSAGE = 0;
	/** messageType常數, 其值為1, 代表錯誤訊息 */
	public static final int TYPE_ERROR = 1;
	/** messageType常數, 其值為2, 代表警告訊息 */
	public static final int TYPE_WARNING = 2;

	private int messageType;
	private String message;
	private List<String> messageDetails;

	public ExtraMessage()
	{
		messageDetails = new LinkedList<String>();
	}

	/**
	 * 判斷本物件中是否含有訊息.
	 * @return true or false
	 */
	public boolean isEmpty()
	{
		return ValidateUtil.isBlank(message) && messageDetails.isEmpty();
	}

	/**
	 * 判斷本訊息是否為錯誤訊息
	 */
	public boolean isErrorMessage()
	{
		return messageType == TYPE_ERROR;
	}

	/**
	 * 判斷本訊息是否為一般訊息
	 */
	public boolean isNormalMessage()
	{
		return messageType == TYPE_MESSAGE;
	}

	/**
	 * 判斷本訊息是否為警告訊息
	 */
	public boolean isWarningMessage()
	{
		return messageType == TYPE_WARNING;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public List<String> getMessageDetails()
	{
		return messageDetails;
	}

	/**
	 * 與addMessageDetails( List messageDetails )相同.
	 * @param messageDetails
	 */
	public void setMessageDetails(List<String> messageDetails)
	{
		if( messageDetails != null && messageDetails.size() > 0 )
			this.messageDetails.addAll( messageDetails );
	}

	public void addMessageDetails( String detail )
	{
		this.messageDetails.add( detail );
	}

	public void addMessageDetails( List<String> messageDetails )
	{
		if( messageDetails != null && messageDetails.size() > 0 )
			this.messageDetails.addAll( messageDetails );
	}

	/**
	 * @return the messageType
	 */
	public int getMessageType()
	{
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(int messageType)
	{
		this.messageType = messageType;
	}
}
