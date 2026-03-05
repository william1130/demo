package proj.nccc.logsearch.proc;

import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.process.ProcessException;

/**
 *
 * @author 
 * @version
 */
public interface EmvTagRecordMasterTempProc extends BaseCRUDProc
{
	public void approve(ProjPersistableArg arg) throws ProcessException; 
	public void reject(ProjPersistableArg arg) throws ProcessException;
	
}
