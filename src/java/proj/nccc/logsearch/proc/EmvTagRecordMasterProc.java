package proj.nccc.logsearch.proc;

import java.util.List;

import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.vo.EmvTagRecordMasterArg;
import proj.nccc.logsearch.vo.ProjPersistableArg;

import com.edstw.process.ProcessException;

/**
 *
 * @author 
 * @version
 */
public interface EmvTagRecordMasterProc extends BaseCRUDProc
{
	public EmvProjPersistable create(List<?> arg) throws ProcessException;
	//public EmvProjPersistable modify(List<?> arg) throws ProcessException; 

	public void deleteEmv(ProjPersistableArg entity);

}
