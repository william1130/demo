package proj.nccc.logsearch.text;

public class HeaderInfo {
	public enum Align {left, center, right };
	
	private String name;
	private int width;
	private Align align;

	public HeaderInfo(String name, int width, Align align) {
		super();
		this.name = name;
		this.width = width;
		this.align = align;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}
}
