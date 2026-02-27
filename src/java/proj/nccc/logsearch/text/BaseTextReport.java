package proj.nccc.logsearch.text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.edstw.report.ReportException;
import com.edstw.report.text.AbstractTextReport;

import proj.nccc.logsearch.beans.MyDateUtil;
import proj.nccc.logsearch.user.ProjUserInfo;
import proj.nccc.logsearch.user.ProjUserProfile;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseTextReport extends AbstractTextReport {
	private final static String SP = "                                                  ";
	private final static String HL = "--------------------------------------------------";
	public final static DecimalFormat longFormater = new DecimalFormat("#,##0");
	public final static DecimalFormat doubleFormater = new DecimalFormat("#,##0.00");

	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private OutputStreamWriter writer;
	private int columnIndex = 0;
	private StringBuffer columnBuffer = new StringBuffer();

	private List[] headerInfosArray;
	private String reportTitle = "";
	private List conStrings = null;

	private String reportName = "";

	public BaseTextReport() {

	}

	public void export(OutputStream stream) throws ReportException {
		try {
			stream.write(getByteArray());
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public byte[] getByteArray() throws ReportException {
		return out.toByteArray();
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void buildReport(Object dataSrc) throws ReportException {
		try {
			writer = new OutputStreamWriter(out, "big5");
		} catch (UnsupportedEncodingException e) {

		}

		createReport(dataSrc);

		flushAndClose();
	}

	protected abstract void createReport(Object dataSrc) throws ReportException;

	protected void writeReportTitle(String title, List headerInfos, List conStrings) throws ReportException {
		writeReportTitle(title, new List[] { headerInfos }, conStrings);
	}

	protected void writeReportTitle(String title, List[] headerInfosArray, List conStrings) throws ReportException {
		this.headerInfosArray = new List[headerInfosArray.length];
		for (int i = 0; i < headerInfosArray.length; i++) {
			this.headerInfosArray[i] = new LinkedList();
			this.headerInfosArray[i].addAll(headerInfosArray[i]);
		}

		this.reportTitle = title;
		this.conStrings = conStrings;

		writeReportTitle();
	}

	protected void writeReportTitle() throws ReportException {
		if (headerInfosArray == null)
			throw new java.lang.IllegalStateException("Column header has not been initialized yet.");

		ProjUserInfo ui = (ProjUserInfo) ProjUserProfile.getCurrentUserProfile().getUserInfo();
		String date = MyDateUtil.getSysDateTime(MyDateUtil.FULL_DATETIME_2);
		String dateLen = String.valueOf(date.length());
		String userName = String.format("%1$-" + dateLen + "s", ui.getUserName());
		
		int totalLength = 0;
		List headerInfos = headerInfosArray[headerInfosArray.length - 1];
		for (Iterator iter = headerInfos.iterator(); iter.hasNext();) {
			HeaderInfo h = (HeaderInfo) iter.next();
			totalLength = totalLength + h.getWidth();
		}

		totalLength = totalLength + (headerInfos.size() - 1);

		writeLine(adjustString(reportTitle, totalLength, HeaderInfo.Align.center));
		writeLine(adjustString("列印人員: " + userName, totalLength, HeaderInfo.Align.right));
		writeLine(adjustString("列印日期: " + date, totalLength, HeaderInfo.Align.right));

		if (conStrings != null && conStrings.size() > 0) {
			for (Iterator iter = conStrings.iterator(); iter.hasNext();) {
				String s = (String) iter.next();
				writeLine(s == null ? "" : s);
			}
		}

		writeColumnHeader();
	}

	protected void writeColumnHeader() throws ReportException {
		StringBuffer sLine = new StringBuffer();

		for (int i = 0; i < this.headerInfosArray.length; i++) {
			StringBuffer hLine = new StringBuffer();

			List headerInfos = this.headerInfosArray[i];
			boolean lastHeader = i == (this.headerInfosArray.length - 1);

			for (Iterator iter = headerInfos.iterator(); iter.hasNext();) {
				HeaderInfo h = (HeaderInfo) iter.next();

				hLine.append(" ").append(adjustString(h.getName(), h.getWidth(), h.getAlign()));
				if (lastHeader)
					paddingRight(sLine.append(" "), h.getWidth(), HL);
			}

			writeLine(hLine.length() > 0 ? hLine.substring(1) : "");
		}

		writeLine(sLine.length() > 0 ? sLine.substring(1) : "");
	}

	protected void writeColumn(Integer value) throws ReportException {
		if (value == null)
			writeColumn("");
		else
			writeColumn(value.longValue());
	}

	protected void writeColumn(Long value) throws ReportException {
		if (value == null)
			writeColumn("");
		else
			writeColumn(value.longValue());
	}

	protected void writeColumn(int value) throws ReportException {
		writeColumn((long) value);
	}

	protected void writeColumn(long value) throws ReportException {
		writeColumn(longFormater.format(value));
	}

	protected void writeColumn(Double value) throws ReportException {
		if (value == null)
			writeColumn("");
		else
			writeColumn(value.doubleValue());
	}

	protected void writeColumn(double value) throws ReportException {
		writeColumn(doubleFormater.format(value));
	}

	protected void writeColumn(String value) throws ReportException {
		List headerInfos = this.headerInfosArray[this.headerInfosArray.length - 1];

		if (columnIndex > 0)
			columnBuffer.append(" ");
		HeaderInfo h = (HeaderInfo) headerInfos.get(columnIndex);

		value = value == null ? "" : value.replaceAll("[\\r\\n]", "");

		columnBuffer.append(adjustString(value, h.getWidth(), h.getAlign()));

		columnIndex++;
		if (columnIndex >= headerInfos.size()) {
			writeColumnBuffer();
		}
	}

	protected void writeColumn(String value, String sepal) throws ReportException {
		List headerInfos = this.headerInfosArray[this.headerInfosArray.length - 1];

		if (columnIndex > 0)
			columnBuffer.append(sepal);
		HeaderInfo h = (HeaderInfo) headerInfos.get(columnIndex);

		value = value == null ? "" : value.replaceAll("[\\r\\n]", "");

		columnBuffer.append(adjustString(value, h.getWidth(), h.getAlign()));

		columnIndex++;
		if (columnIndex >= headerInfos.size()) {
			writeColumnBuffer();
		}
	}

	protected void writeLine(String l) throws ReportException {
		try {
			writer.write(l);
			writer.write("\r\n");
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	protected String adjustString(String s, int width, HeaderInfo.Align align) {
		StringBuffer buf = new StringBuffer();

		int byteLen = 0;
		if (align == HeaderInfo.Align.right) {
			int i;
			for (i = s.length() - 1; i >= 0; i--) {
				int cw = s.charAt(i) > 127 ? 2 : 1;
				if (byteLen + cw > width) {
					i++;
					break;
				}
				byteLen += cw;
			}

			s = i > 0 ? s.substring(i) : s;
		} else {
			int i;
			for (i = 0; i < s.length(); i++) {
				int cw = s.charAt(i) > 127 ? 2 : 1;
				if (byteLen + cw > width) {
					break;
				}
				byteLen += cw;
			}

			s = i >= s.length() ? s : s.substring(0, i);
		}

		buf.append(s);

		if (byteLen < width) {
			switch (align) {
			case right:
				paddingLeft(buf, width - byteLen, SP);
				break;
			case center:
				int tl = (width - byteLen) / 2;
				paddingLeft(buf, tl, SP);
				paddingRight(buf, width - byteLen - tl, SP);
				break;
			case left:
				paddingRight(buf, width - byteLen, SP);
				break;
			}
		}

		return buf.toString();
	}

	private void flushAndClose() throws ReportException {
		try {
			if (columnBuffer.length() > 0) {
				writer.write(columnBuffer.toString());
				columnBuffer.delete(0, columnBuffer.length());
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	private void writeColumnBuffer() throws ReportException {
		writeLine(columnBuffer.toString());

		columnBuffer.delete(0, columnBuffer.length());
		columnIndex = 0;
	}

	private void paddingLeft(StringBuffer buf, int n, String pattern) {
		for (int i = 0; i < (n / pattern.length()); i++)
			buf.insert(0, pattern);
		buf.insert(0, pattern.substring(0, n % pattern.length()));
	}

	private void paddingRight(StringBuffer buf, int n, String pattern) {
		for (int i = 0; i < (n / pattern.length()); i++)
			buf.append(pattern);
		buf.append(pattern.substring(0, n % pattern.length()));
	}
	
	protected void setHeaderInfos(List[] headerInfosArray) {		
		this.headerInfosArray = new List[headerInfosArray.length];
		for (int i = 0; i < headerInfosArray.length; i++) {
			this.headerInfosArray[i] = new LinkedList();
			this.headerInfosArray[i].addAll(headerInfosArray[i]);
		}
	}

}
