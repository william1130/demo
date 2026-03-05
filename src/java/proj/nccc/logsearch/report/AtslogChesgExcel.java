package proj.nccc.logsearch.report;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.edstw.report.ReportException;
import com.edstw.user.UserLogger;

import proj.nccc.logsearch.vo.ChesgDailyTotalsArg;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AtslogChesgExcel extends BaseExcelReport {

	private String queryRange;

	public AtslogChesgExcel() {
		super();
	}

	protected void insertIntoWorkbook(Object List) throws ReportException {
		try {
			createStyle();
			List dataSource = (List) List;
			this.createResultSheet(dataSource);
		} catch (Exception e) {
			UserLogger.getLog(this.getClass()).error(e.getMessage(), e);
			throw new ReportException(e.getMessage(), e);
		}
	}

	protected Object readFromWorkbook() throws ReportException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void createResultSheet(List dataSource) throws Exception {
		// 每6萬筆做成一張 sheet.
		int sheetSize = 60000;
		int sheetCount = (int) Math.ceil(dataSource.size() / (double) sheetSize);
		for (int i = 0; i < sheetCount; i++) {
			HSSFSheet sheet = this.book
					.createSheet("查詢結果" + (dataSource.size() > sheetSize ? "(第" + (i + 1) + "頁)" : ""));

			List headerInfos = new LinkedList();
			headerInfos.add(new HeaderInfo("序號", 8));
			headerInfos.add(new HeaderInfo("交易日期", 13));
			headerInfos.add(new HeaderInfo("端未機代號", 13));
			headerInfos.add(new HeaderInfo("特店代號", 14));
			headerInfos.add(new HeaderInfo("特店名稱", 50));
			headerInfos.add(new HeaderInfo("總交易筆數", 20));
			headerInfos.add(new HeaderInfo("CHESG Flag 交易筆數", 20));

			createTitleHeader(sheet, this.getReportName(), headerInfos.size() - 1, 0);
			this.createHeader(sheet, headerInfos, 2);

			int start = i * sheetSize;
			int end = Math.min(start + sheetSize, dataSource.size());
			fillResultData(dataSource.subList(start, end), sheet, start);
		}
	}

	protected void createTitleHeader(HSSFSheet sheet, String title, int cols, int rowIndex) throws Exception {
		HSSFRow titleRow = sheet.createRow(rowIndex);
		HSSFCell titleCell = this.createCell(titleRow, 0, titleStyle, CellType.STRING);
		titleCell.setCellValue(new HSSFRichTextString(title));
		this.mergeCell(sheet, BorderStyle.NONE, rowIndex, (short) 0, rowIndex, (short) cols);

		rowIndex++;
		HSSFRow info = sheet.createRow(rowIndex);
		HSSFCell dateCell = this.createCell(info, 0, rightStyle, CellType.STRING);
		dateCell.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);
		dateCell.setCellValue(new HSSFRichTextString("查詢區間：" + queryRange));
		this.mergeCell(sheet, BorderStyle.NONE, rowIndex, (short) 0, rowIndex, (short) cols);
	}

	private void fillResultData(List list, HSSFSheet sheet, int start) {
		int i = 3;
		for (Iterator iter = list.iterator(); iter.hasNext(); start++) {
			ChesgDailyTotalsArg rec = (ChesgDailyTotalsArg) iter.next();
			HSSFRow row = sheet.createRow(i++);

			int j = 0;

			this.addStringCell(row, j++, centerStyle, "" + (start + 1));
			this.addStringCell(row, j++, centerStyle, rec.getLogDate());
			this.addStringCell(row, j++, centerStyle, rec.getTermId());
			this.addStringCell(row, j++, centerStyle, rec.getMerchantId());
			this.addStringCell(row, j++, centerStyle, rec.getMerchantChinName());
			this.addNumberCell(row, j++, centerStyle, rec.getTotalCount());
			this.addNumberCell(row, j++, centerStyle, rec.getChesgCount());
		}
	}

	public String getQueryRange() {
		return queryRange;
	}

	public void setQueryRange(String queryRange) {
		this.queryRange = queryRange;
	}
}
