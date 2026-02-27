package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * table=emvRefSpec
 * @author Gail Lee
 * @version 1.0
 */

public class EmvRefSpec extends AbstractProjPersistable {
	
	/**
	 * EMV_REF_SPEC
	 */
	private static final long serialVersionUID = 1L;
	
	private String specID; //SPEC_ID
	private String specName;  //SPEC_NAME
	private String status; 

	@Override
	public Object getId() {
		return this.getSpecID();
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;

		return true;
	}

	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
		return hash;
	}
	
	
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(specID);
		vi.addValue(specName);
		vi.addValue(status);

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	

}
