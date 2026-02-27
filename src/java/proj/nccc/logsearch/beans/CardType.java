/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2008/2/13, 下午 04:58:41, By 許欽程(Shiu Vincent, sz12tk)
 * 
 * ==============================================================================================
 * $Id: CardType.java,v 1.1 2017/04/24 01:31:26 asiapacific\jih Exp $
 * ==============================================================================================
 */

package proj.nccc.logsearch.beans;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CardType的enum type.
 * @author 許欽程(Shiu Vincent, sz12tk)
 * @version $Revision: 1.1 $
 */
public enum CardType 
{
	VISA_CARD("Visa一般卡","VC"),
	VISA_GOLD("Visa金卡","VG"),
	VISA_PLATIUM("Visa白金卡","VP"),
	VISA_INFINITY("Visa無限卡","VI"),
	VISA_SIGNATURE("Visa Signature Card","VS"),
	MASTERCARD_CARD("M/C一般卡","MC"),
	MASTERCARD_GOLD("M/C金卡","MG"),
	MASTERCARD_PLATIUM("M/C白金卡","MP"),
	MASTERCARD_INFINITY("M/Ca無限卡","MI"),
	MASTERCARD_SIGNATURE("M/C Signature Card","MS"),
	JCB_CARD("JCB一般卡","JC"),
	JCB_GOLD("JCB金卡","JG"),
	JCB_PLATIUM("JCB白金卡","JP"),
	JCB_INFINITY("JCB無限卡","JI"),
	JCB_SIGNATURE("JCB Signature Card","JS"),
	/*M2012119銀聯晶片卡需求*/
	CUP_CARD("CUP一般卡","CC"),
	CUP_GOLD("CUP金卡","CG"),
	CUP_PLATIUM("CUP白金卡","CP"),
	CUP_INFINITY("CUP無限卡","CI"),
	CUP_SIGNATURE("CUP Signature Card","CS");
	
	static
	{
		ConvertUtils.register( new CardTypeConverter( null ) , CardType.class );
	}
	
	private String value;
	private String label;
	
	CardType( String label, String value )
	{
		this.label = label;
		this.value = value;
	}
	
	public static CardType findByValue( String value )
	{
		for( CardType tp : CardType.values() )
		{
			if( tp.getValue().equals( value ) )
				return tp;
		}
		return null;
	}
	public static List getAllCardType( )
	{
		List list=new LinkedList();
		for( CardType tp : CardType.values() )
		{
			list.add(tp.getValue());
		}
		return list;
	}

	@Override
	public String toString()
	{
		return value;
	}
	
	public String getValue()
	{
		return value;
	}
	public String getLabel()
	{
		return label;
	}
	
	/**
	 * 定義使用BeanUtil在copy property時, 傳入之物件轉換成EmployeeType的規則.
	 */
	private static class CardTypeConverter implements Converter
	{
		private static final Log log = LogFactory.getLog( CardTypeConverter.class );
		private Object defaultValue;

		/** Creates a new instance of EnumConverter */
		public CardTypeConverter()
		{
		}

		public CardTypeConverter( Object defaultValue )
		{
			this.defaultValue = defaultValue;
		}

		public Object convert(Class aClass, Object object)
		{
			if( object != null && object.toString().trim().length() > 0 )
			{
				try
				{
					return CardType.findByValue( object.toString() );
				}
				catch( IllegalArgumentException e )
				{
					log.error( e.getMessage(), e );
				}
			}
			return defaultValue;
		}
	}
}
