/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Created On : 2011/6/17, 下午 05:51:52, By 許欽程(Shiu Vincent)
 * 
 * ==============================================================================================
 * $Id: AlterInfo.java,v 1.1 2014/11/14 03:14:27 asiapacific\hsiehes Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.persist;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import proj.nccc.logsearch.user.ProjUserProfile;

import com.edstw.crud.AlterUserInfo;
import com.edstw.sql.ResultSetTool;
import com.edstw.util.ValidateUtil;

/**
 * 代表資料異動的資訊
 * 
 * @author 許欽程(Vincent Shiu)
 * @version $Revision: 1.1 $
 */
public class AlterInfo implements Serializable, AlterUserInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6805884113606390179L;
	private String name;
	private Date date;

	/** Creates a new instance of AlterInfo */
	public AlterInfo() {
	}

	/**
	 * 由傳入之ResultSetTool建立confirm info資料
	 */
	public static AlterInfo buildConfirmInfo(ResultSetTool rst)
			throws SQLException {
		AlterInfo ai = new AlterInfo();
		ai.setDate(rst.getDate("CONFIRM_DATE"));
		ai.setName(rst.getString("CONFIRM_USER"));
		// 若沒有日期及名字, 代表尚未覆核, 要傳回null.
		if (ValidateUtil.isBlank(ai.getName()) && ai.getDate() == null)
			return null;
		return ai;
	}

	/**
	 * 由傳入之ResultSetTool建立create info資料
	 */
	public static AlterInfo buildCreateInfo(ResultSetTool rst)
			throws SQLException {
		AlterInfo ai = new AlterInfo();
		ai.setDate(rst.getDate("CREATE_DATE"));
		ai.setName(rst.getString("CREATE_USER"));
		return ai;
	}

	/**
	 * 由傳入之ResultSetTool建立update info資料
	 */
	public static AlterInfo buildUpdateInfo(ResultSetTool rst)
			throws SQLException {
		AlterInfo ai = new AlterInfo();
		ai.setDate(rst.getDate("LAST_UPDATE_DATE"));
		ai.setName(rst.getString("LAST_UPDATE_USER"));
		return ai;
	}
	
	/**
	 * 由傳入之ResultSetTool建立update info資料
	 */
	public static AlterInfo buildRequestInfo(ResultSetTool rst)
			throws SQLException {
		AlterInfo ai = new AlterInfo();
		ai.setDate(rst.getDate("REQUEST_DATE"));
		ai.setName(rst.getString("REQUEST_USER"));
		return ai;
	}

	/**
	 * 依現行之UserProfile建立其AlterInfo物件.
	 */
	public static AlterInfo createAlterInfo() {
		AlterInfo ai = new AlterInfo();
		ai.setDate(new Date());
		ai.setName(ProjUserProfile.getCurrentUserProfile().getUserInfo()
				.getSignature());
		return ai;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
