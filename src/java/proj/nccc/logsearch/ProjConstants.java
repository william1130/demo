/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/16, 上午 11:54:55, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: ProjConstants.java,v 1.2 2017/05/24 07:39:30 linsteph2\cvsuser Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.edstw.web.WebConstant;

/**
 * 用來定義本系統使用到之各類常數
 * 
 * @author 許欽程(Shiu Vincent)
 * @version $Revision: 1.2 $
 */
public interface ProjConstants extends WebConstant {
	/** 其值為'ajaxResult', 用在struts action中用來指定 forward 的target. */
	static final String AJAX_RESULT_KEY = "Result";
	
	/** 資料狀態常數, 其值為'A', 代表新增 */
	public static final String ADD = "A";
	/** 資料狀態常數, 其值為'U', 代表修改 */
	public static final String MODIFY = "U";
	/** 資料狀態常數, 其值為'D', 代表刪除 */
	public static final String DELETE = "D";
	
	public static final String[] ALL_CARD =  { "IP","EZ","IC","HC"};
	public static final Set<String> TKS_CARD_SET = new HashSet<String>(Arrays.asList(ALL_CARD));
	
	public static final String ACCESS_TYPE_A = "A"; //新增
	public static final String ACCESS_TYPE_D = "D"; //刪除
	public static final String ACCESS_TYPE_U = "U"; //異動
	public static final String ACCESS_TYPE_Q = "Q"; //查詢
	public static final String ACCESS_TYPE_C = "C"; //放行/覆核
	public static final String ACCESS_TYPE_PR = "PR"; //列印報表
	public static final String ACCESS_TYPE_PF = "PF"; //產生報表檔
	public static final String ACCESS_TYPE_O = "O"; //匯出下載
	
	/**
	 * 定義 參數覆核 active_code
	 * @author 
	 *
	 */
	public enum Active_Code {
		ADD("A"), UPDATE("U"), DELETE("D");
		
		String code;
		
		private Active_Code(String s) {
			this.code = s;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	/**
	 * 定義 參數狀態常數值
	 * @author changwal
	 *
	 */
	public enum Para_Status {
		ON("Y"), OFF("N"), INAPPROVE("A");
		
		String code;
		
		private Para_Status(String s) {
			this.code = s;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	
	/** UI動作 */
	public enum UiLog_Action {
		QUERY("查詢"), CREATE("新增"), MODIDY("修改"), DELETE("刪除"), DETAIL("明細"), 
		APPROVE("核准"), REJECT("拒絕"), REPORT("報表");
		
		String name;
		
		private UiLog_Action(String s) {
			this.name = s;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
	}
	
	/** UI作業*/
	public enum UiLog_Function {
		EMV_CARD_TYPE("卡別"), EMV_PARA("EMV晶片"),EMV_SPEC("參考規格"), EMV_TAG_RECORD("EMV晶片");
		
		String name;
		
		private UiLog_Function(String s) {
			this.name = s;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
