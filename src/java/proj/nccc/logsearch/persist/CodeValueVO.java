package proj.nccc.logsearch.persist;

import com.edstw.nccc.sql.log.ValueImage;

/**
 * 通用 Code Value 物件
 *
 */
public class CodeValueVO extends AbstractProjPersistable{
	private static final long serialVersionUID = 1L;
	private String itemCode;
	private String itemValue;
	
	public Object getId() {
		return itemCode;
	}
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	@Override
	public ValueImage createValueImage() {
		ValueImage vi = new ValueImage();
		vi.addValue(itemCode);
		vi.addValue(itemValue);
		return vi;
	}

}
