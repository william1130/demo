package proj.nccc.logsearch.vo;

import org.apache.commons.beanutils.BeanUtils;
import proj.nccc.logsearch.persist.EmvProjPersistable;
import proj.nccc.logsearch.persist.EmvTagRecordDetail;

public class EmvTagRecordDetailArg extends EmvTagRecordDetail
{
	private static final long serialVersionUID = 1L;

	public void buildFromProjPersistable(EmvProjPersistable c) throws Exception
	{
		BeanUtils.copyProperties(this, c);
	}
}
