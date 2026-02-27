
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * ==============================================================================================
 * $Id: AuthBizOut0200Proc.java,v 1.1 2017/04/24 01:31:17 asiapacific\jih Exp $
 * ==============================================================================================
 */
package proj.nccc.logsearch.proc;

import proj.nccc.logsearch.vo.ManualAuthorizerArg;

/**
 *
 * @author APAC\czrm4t $
 * @version $Revision: 1.1 $
 */
public interface AuthBizOut0200Proc extends BaseCRUDProc {

	// 補登, insertAuthLog
	boolean insertAuthLogInAdd(ManualAuthorizerArg entity, String sAuthManuEntry);

	// 一般交易, insertAuthLog
	boolean insertAuthLogIn0200(ManualAuthorizerArg entity, String sAuthManuEntry);

	// 一般交易-取消0220, insertAuthLog
	boolean insertAuthLogIn0220(ManualAuthorizerArg entity);

	// IP
	boolean insertAuthLogWhenB24(ManualAuthorizerArg entity, String sAuthManuEntry);

}
