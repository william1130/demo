package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

public class EmvRefSpecTemp extends AbstractProjPersistable {

	/**
	 * table=emvRefSpecTemp
	 * @author Gail Lee
	 * @version 1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private String specID;
	private String specName;
	private String activeCode;
	
	@Override
	public Object getId() {
		return this.getSpecID();
	}
	

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((specID == null) ? 0 : specID.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EmvRefSpecTemp other = (EmvRefSpecTemp) obj;
		if (specID == null) {
			if (other.specID != null)
				return false;
		} else if (!specID.equals(other.specID))
			return false;
		return true;
	}
	
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(specID);
		vi.addValue(specName);
		vi.addValue(activeCode);
		return vi;
	}
	
	public String getSpecID() {
		return specID;
	}

	public void setSpecID(String specID) {
		this.specID = specID;
	}
	
	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}



}
